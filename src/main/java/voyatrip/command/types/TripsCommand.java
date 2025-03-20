package voyatrip.command.types;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import voyatrip.command.exceptions.InvalidCommand;

public class TripsCommand extends Command {
    static final String[] INVALID_NAMES = {"home", "all"};

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer numDay;
    private Integer totalBudget;
    private Integer index;

    public TripsCommand(CommandAction commandAction,
                        CommandTarget commandTarget,
                        String rawArgument) throws InvalidCommand {
        super(commandAction, commandTarget);
        name = null;
        startDate = null;
        endDate = null;
        numDay = null;
        totalBudget = null;
        index = null;

        processRawArgument(rawArgument);
    }

    @Override
    protected void processRawArgument(String rawArgument) throws InvalidCommand {
        super.processRawArgument(rawArgument);

        if (startDate != null) {
            numDay = Math.toIntExact(ChronoUnit.DAYS.between(startDate, endDate) + 1);
        }

        if (commandAction == CommandAction.DELETE_BY_INDEX && name != null) {
            super.setCommandAction(CommandAction.DELETE_BY_NAME);
        }
    }

    @Override
    protected void matchArgument(String argument) throws InvalidCommand {
        String argumentKeyword = argument.split("\\s+")[0];
        String argumentValue = argument.replaceFirst(argumentKeyword, "").strip();
        argumentKeyword = argumentKeyword.toLowerCase();

        try {
            switch (argumentKeyword) {
            case "name", "n" -> name = argumentValue;
            case "start", "s" -> startDate = parseDate(argumentValue);
            case "end", "e" -> endDate = parseDate(argumentValue);
            case "day", "d" -> numDay = Integer.parseInt(argumentValue);
            case "budget", "b" -> totalBudget = Integer.parseInt(argumentValue);
            case "index", "i" -> index = Integer.parseInt(argumentValue);
            default -> throw new InvalidCommand();
            }
        } catch (NumberFormatException e) {
            throw new InvalidCommand();
        }
    }

    private LocalDate parseDate(String date) throws InvalidCommand {
        try {
            if (date.split("-").length == 2) {
                date = date + "-" + LocalDate.now().getYear();
            }
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("d-M-yyyy"));
        } catch (DateTimeParseException e) {
            throw new InvalidCommand();
        }
    }

    @Override
    protected boolean isInvalidCommand() {
        boolean isInvalidName = name == null || Arrays.asList(INVALID_NAMES).contains(name);
        boolean isInvalidDate = startDate == null || endDate == null || startDate.isAfter(endDate);
        boolean isInvalidAdd = isInvalidName || isInvalidDate || totalBudget == null;
        boolean isInvalidDelete = isInvalidName && index == null;

        return switch (commandAction) {
        case ADD -> isInvalidAdd;
        case DELETE_BY_INDEX, DELETE_BY_NAME -> isInvalidDelete;
        case LIST, CHANGE_DIRECTORY -> isInvalidName;
        case EXIT -> false;
        default -> true;
        };
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public String getName() {
        return name;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Integer getNumDay() {
        return numDay;
    }

    public Integer getTotalBudget() {
        return totalBudget;
    }

    public Integer getIndex() {
        return index;
    }
}
