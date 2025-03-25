package voyatrip;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import voyatrip.ui.Message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.time.LocalDate;

class VoyaTripTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private ByteArrayInputStream inContent;
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @BeforeEach
    public void setUpStreams() {
        // Redirect System.out to capture output
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    public void handleExit_exitCommand_printGoodbyeMessage() {
        // Simulate user input
        String input = "exit\n";
        inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent); // Set System.in to use the simulated input

        // Run the main method (or the method that reads input)
        VoyaTrip.main(new String[]{});

        // Verify the output
        String expectedOutput = Message.getWelcomeMessage() + "\n"
                + "\n~ >\n"
                + Message.getGoodbyeMessage();
        assertEquals(expectedOutput, outContent.toString().trim().replace("\r\n", "\n"));
    }

    @Test
    public void handleInput_addTripCorrect_printAddTripMessage() {
        // Simulate user input
        String input = "add trip --name Japan --start 1-5-2022 --end 7-5-2022 --b 1000\nexit\n";
        LocalDate startDate = LocalDate.of(2022, 5, 1);
        LocalDate endDate = LocalDate.of(2022, 5, 7);
        Trip trip = new Trip("Japan", startDate, endDate , 7, 1000);
        inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent); // Set System.in to use the simulated input

        // Run the main method (or the method that reads input)
        VoyaTrip.main(new String[]{});

        // Verify the output
        String expectedOutput = Message.getWelcomeMessage() + "\n"
                + "\n~ >\n"
                + Message.getAddTripMessage() + "\n"
                + trip.abbrInfo() + "\n"
                + Message.getNextCommandMessage() + "\n"
                + "\n~ >\n"
                + Message.getGoodbyeMessage();
        assertEquals(expectedOutput, outContent.toString().trim());
    }

    @Test
    public void handleInput_addTripIncorrect_printIncorrectCommand() {
        String input = "add trip --name Japan --start --end --b 1000\nexit\n";
        inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent); // Set System.in to use the simulated input

        VoyaTrip.main(new String[]{});

        String expectedOutput = Message.getWelcomeMessage() + "\n"
                + "\n~ >\n"
                + Message.getInvalidCommandMessage() + "\n"
                + "\n~ >\n"
                + Message.getGoodbyeMessage();
        assertEquals(expectedOutput, outContent.toString().trim());
    }

    @Test
    public void handleInput_deleteTripByIndexCorrect_printDeleteTripMessage() {
        // Simulate user input
        String input = "add trip --name Japan --start 1-5-2022 --end 7-5-2022 --b 1000\ndelete trip --index 1\nexit\n";
        LocalDate startDate = LocalDate.of(2022, 5, 1);
        LocalDate endDate = LocalDate.of(2022, 5, 7);
        Trip trip = new Trip("Japan", startDate, endDate , 7, 1000);
        inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent); // Set System.in to use the simulated input

        // Run the main method (or the method that reads input)
        VoyaTrip.main(new String[]{});

        // Verify the output
        String expectedOutput = Message.getWelcomeMessage() + "\n"
                + "\n~ >\n"
                + Message.getAddTripMessage() + "\n"
                + trip.abbrInfo() + "\n"
                + Message.getNextCommandMessage() + "\n"
                + "\n~ >\n"
                + Message.getDeleteTripMessage() + "\n"
                + trip.abbrInfo() + "\n"
                + Message.getNextCommandMessage() + "\n"
                + "\n~ >\n"
                + Message.getGoodbyeMessage();
        assertEquals(expectedOutput, outContent.toString().trim());
    }

    @Test
    public void handleInput_deleteTripByIndexIncorrect_printIndexOutOfBoundsMessage() {
        String input = "add trip --name Japan --start 1-5-2022 --end 7-5-2022 --b 1000\ndelete trip --index 2\nexit\n";
        LocalDate startDate = LocalDate.of(2022, 5, 1);
        LocalDate endDate = LocalDate.of(2022, 5, 7);
        Trip trip = new Trip("Japan", startDate, endDate , 7, 1000);
        inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent); // Set System.in to use the simulated input

        VoyaTrip.main(new String[]{});

        String expectedOutput = Message.getWelcomeMessage() + "\n"
                + "\n~ >\n"
                + Message.getAddTripMessage() + "\n"
                + trip.abbrInfo() + "\n"
                + Message.getNextCommandMessage() + "\n"
                + "\n~ >\n"
                + Message.getInvalidCommandMessage() + "\n"
                + "\n~ >\n"
                + Message.getGoodbyeMessage();
        assertEquals(expectedOutput, outContent.toString().trim());
    }

    @Test
    public void handleInput_deleteTripByNameCorrect_printInvalidCommandMessage() {
        // Simulate user input
        String input = "add trip --name Japan --start 1-5-2022 --end 7-5-2022 --b 1000\ndelete trip --name Japan\nexit\n";
        LocalDate startDate = LocalDate.of(2022, 5, 1);
        LocalDate endDate = LocalDate.of(2022, 5, 7);
        Trip trip = new Trip("Japan", startDate, endDate , 7, 1000);
        inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent); // Set System.in to use the simulated input

        // Run the main method (or the method that reads input)
        VoyaTrip.main(new String[]{});

        // Verify the output
        String expectedOutput = Message.getWelcomeMessage() + "\n"
                + "\n~ >\n"
                + Message.getAddTripMessage() + "\n"
                + trip.abbrInfo() + "\n"
                + Message.getNextCommandMessage() + "\n"
                + "\n~ >\n"
                + Message.getDeleteTripMessage() + "\n"
                + trip.abbrInfo() + "\n"
                + Message.getNextCommandMessage() + "\n"
                + "\n~ >\n"
                + Message.getGoodbyeMessage();
        assertEquals(expectedOutput, outContent.toString().trim());
    }

    @Test
    public void handleInput_deleteTripByNameIncorrect_printTripNotFoundCommandMessage() {
        String input = "add trip --name Japan --start 1-5-2022 --end 7-5-2022 --b 1000\ndelete trip --name Singapore\nexit\n";
        LocalDate startDate = LocalDate.of(2022, 5, 1);
        LocalDate endDate = LocalDate.of(2022, 5, 7);
        Trip trip = new Trip("Japan", startDate, endDate , 7, 1000);
        inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent); // Set System.in to use the simulated input

        VoyaTrip.main(new String[]{});

        String expectedOutput = Message.getWelcomeMessage() + "\n"
                + "\n~ >\n"
                + Message.getAddTripMessage() + "\n"
                + trip.abbrInfo() + "\n"
                + Message.getNextCommandMessage() + "\n"
                + "\n~ >\n"
                + Message.getTripNotFoundMessage() + "\n"
                + "\n~ >\n"
                + Message.getGoodbyeMessage();
        assertEquals(expectedOutput, outContent.toString().trim());
    }
}
