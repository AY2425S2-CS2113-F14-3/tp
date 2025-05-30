package voyatrip.logic.command.types;

import java.util.ArrayList;
import java.util.Arrays;

import voyatrip.logic.command.exceptions.ExtraArgument;
import voyatrip.logic.command.exceptions.InvalidArgumentKeyword;
import voyatrip.logic.command.exceptions.InvalidArgumentValue;
import voyatrip.logic.command.exceptions.InvalidBudget;
import voyatrip.logic.command.exceptions.InvalidDate;
import voyatrip.logic.command.exceptions.InvalidName;
import voyatrip.logic.command.exceptions.InvalidNumberFormat;
import voyatrip.logic.command.exceptions.InvalidTimeFormat;
import voyatrip.logic.command.exceptions.MissingArgument;

public class TransportationCommand extends Command {
    static final String[] INVALID_NAMES = {"all"};

    private String trip;
    private String name;
    private String mode;
    private Integer budget;
    private Integer index;
    private Integer day;

    public TransportationCommand(CommandAction commandAction,
                                 CommandTarget commandTarget,
                                 String trip,
                                 ArrayList<String> arguments)
            throws InvalidArgumentKeyword,
            InvalidArgumentValue,
            InvalidDate,
            InvalidNumberFormat,
            MissingArgument, InvalidTimeFormat {
        super(commandAction, commandTarget);
        this.trip = trip;
        name = null;
        mode = null;
        budget = null;
        index = null;
        day = null;

        processRawArgument(arguments);
    }

    @Override
    protected void processRawArgument(ArrayList<String> arguments)
            throws InvalidArgumentKeyword,
            InvalidArgumentValue,
            InvalidDate,
            InvalidNumberFormat,
            MissingArgument, InvalidTimeFormat {
        super.processRawArgument(arguments);

        processCommandVariation();
    }

    private void processCommandVariation() {
        if (commandAction == CommandAction.DELETE_BY_INDEX && name != null) {
            super.setCommandAction(CommandAction.DELETE_BY_NAME);
        } else if (commandAction == CommandAction.LIST) {
            if (name != null) {
                super.setCommandAction(CommandAction.LIST_BY_NAME);
            } else if (index != null) {
                super.setCommandAction(CommandAction.LIST_BY_INDEX);
            }
        }
    }

    @Override
    protected void matchArgument(String argument) throws InvalidArgumentKeyword, InvalidArgumentValue {
        String argumentKeyword = argument.split("\\s+")[0];
        String argumentValue = argument.replaceFirst(argumentKeyword, "").strip();
        argumentKeyword = argumentKeyword.toLowerCase();

        if (argumentKeyword.isEmpty()) {
            throw new InvalidArgumentKeyword();
        }
        if (!argumentKeyword.equals("all") && argumentValue.isEmpty()) {
            throw new InvalidArgumentValue();
        }

        try {
            switch (argumentKeyword) {
            case "name", "n" -> name = argumentValue;
            case "mode", "m" -> mode = argumentValue;
            case "budget", "b" -> budget = Integer.parseInt(argumentValue);
            case "index", "i" -> index = Integer.parseInt(argumentValue);
            case "day", "d" -> day = Integer.parseInt(argumentValue);
            case "all" -> name = "all";
            default -> throw new InvalidArgumentKeyword();
            }
        } catch (NumberFormatException e) {
            throw new InvalidNumberFormat();
        }
    }

    @Override
    protected void validateArgument() throws InvalidArgumentValue, MissingArgument {
        boolean isAdd = commandAction == CommandAction.ADD;
        boolean isDelete = commandAction == CommandAction.DELETE_BY_INDEX ||
                commandAction == CommandAction.DELETE_BY_NAME;
        boolean isModify = commandAction == CommandAction.MODIFY;
        boolean isList = commandAction == CommandAction.LIST;
        boolean isChangeDirectory = commandAction == CommandAction.CHANGE_DIRECTORY;

        boolean isInvalidChangeDirectoryArgument = index != null || day != null
                || budget != null || name != null;

        if (isInvalidChangeDirectoryArgument && isChangeDirectory) {
            throw new ExtraArgument();
        }

        boolean isMissingAddArgument =
                name == null || mode == null || budget == null || day == null;
        boolean isMissingDeleteArgument = name == null && index == null;
        boolean isMissingModifyArgument = index == null ||
                (name == null && mode == null && budget == null && day == null);
        boolean isMissingListArgument = name == null && index == null;

        if (isAdd && isMissingAddArgument ||
                isDelete && isMissingDeleteArgument ||
                isModify && isMissingModifyArgument ||
                isList && isMissingListArgument) {
            throw new MissingArgument();
        }

        boolean isInvalidBudget = budget != null && budget < 0;
        boolean isInvalidName = !isList && name != null && Arrays.asList(INVALID_NAMES).contains(name);

        if (isInvalidBudget) {
            throw new InvalidBudget();
        }
        if (isInvalidName) {
            throw new InvalidName();
        }
    }

    public String getTrip() {
        return trip;
    }

    public String getName() {
        return name;
    }

    public String getMode() {
        return mode;
    }

    public Integer getBudget() {
        return budget;
    }

    public Integer getIndex() {
        return index;
    }

    public Integer getDay() {
        return day;
    }
}
