import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

class Interfacetest {

	private Interface Interface;
	private Board board;
	private InputCheck InputCheck;

    @BeforeEach
    void setUp() {
        Interface = new Interface();
        board = new Board();
        board.setPlayer(1, new Player("Player 1", CheckerTemplate.RED)); // Initialize players with the virtual inputs
	    board.setPlayer(2, new Player("Player 2", CheckerTemplate.WHITE));
	    board.setCurrentPlayer(1);
	    InputCheck = new InputCheck("R12"); // Create a InputCheck with 2 dice numbers as specified for the move
        board.setFace(InputCheck); // Set the dice face values
        board.initBoard();
        board.setGameNumber(3);
    }

    @Test // Test the Interface constructor
    void testInterface() {
        assertNotNull(Interface);
    }

    @Test // Test the displayWelcome method
    void testDisplayWelcome() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Interface.gameWelcome();
        assertEquals("Welcome to Backgammon\n", outputStream.toString());
    }

    @Test // Test the displayStart method
    void test() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Interface.starterScreen();
        assertEquals("Enter START to start the game, enter QUIT to exit or enter HINT to view controls\n", outputStream.toString());
    }


    @Test // Test the displayPiece method
    void testDisplayPiece() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Interface.displayBoard(board);
        String expectedOutput = """
            |---------------------------------------------------------------------|---------------|
            | Current player's color: [0;31mR[0m                                 pips: 167 |   Games: 3    |
            |---------------------------------------------------------------------|---------------|
            | Dice:      0              0              0              0           |
               Round: 1    |
            |---------------------------------------------------------------------|---------------|
            | [0;37m13   14   15   16   17   18[0m | [0;37mB2[0m | [0;37m19   20   21   22   23   24[0m | [0;31mE1[0m |   [0;31mR[0m Score     |
            | [0;31m12   11   10   09   08   07[0m | [0;37mB2[0m | [0;31m06   05   04   03   02   01[0m | [0;31mE1[0m |       0       |
            | [0;37m o[0m                  [0;31m o[0m      |    | [0;31m o[0m                       [0;37m o[0m | [0;31m00[0m |---------------|
            | [0;37m o[0m                  [0;31m o[0m      |    | [0;31m o[0m                       [0;37m o[0m |    |
            | [0;37m o[0m                  [0;31m o[0m      |    | [0;31m o[0m                          |    |
            | [0;37m o[0m                          |    | [0;31m o[0m                          |    |
            | [0;37m o[0m                          |    | [0;31m o[0m                          |    |
            |-----------------------------|----|-----------------------------|----|
            | [0;31m o[0m                          |    | [0;37m o[0m                          |    |
            | [0;31m o[0m                          |    | [0;37m o[0m                          |    |
            | [0;31m o[0m                  [0;37m o[0m      |    | [0;37m o[0m                          |    |
            | [0;31m o[0m                  [0;37m o[0m      |    | [0;37m o[0m                       [0;31m o[0m |    |
            | [0;31m o[0m                  [0;37m o[0m      |    | [0;37m o[0m                       [0;31m o[0m | [0;37m00[0m |---------------|
            | [0;37m12   11   10   09   08   07[0m | [0;31mB1[0m | [0;37m06   05   04   03   02   01[0m | [0;37mE2[0m |  [0;37m  W[0m  Score   |
            | [0;31m13   14   15   16   17   18[0m | [0;31mB1[0m | [0;31m19   20   21   22   23   24[0m | [0;37mE2[0m |       0       |
            |---------------------------------------------------------------------|---------------|           
                """;;
        assertEquals(expectedOutput, outputStream.toString());
    }



    @Test
    void testShowAllAllowedMoves() { // This test checks if the showAllAllowedMoves() function outputs the correct allowed moves.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Interface.showLegalMoves(board);
        String expectedOutput = "No possible moves\n"; // The expected output for the scenario
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    void testFirstDiceRoll() { // This test checks if the FirstDiceRoll() function changes the board state as expected.
        board.setFace(InputCheck); // Set up a scenario for testing FirstDiceRoll()
        Interface.firstTurn(board);
        assertTrue(board.getFace(1) != 0 && board.getFace(2) != 0); // Check if the board state has changed as expected
    }

    // @Test
    // void testgameIntro() { // This test checks if the gameIntro() function correctly reads the provided input and sets the match number.
    //     String input = "Player 1\nPlayer 2\n5\n";
    //     InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    //     Interface Interface = new Interface(inputStream);
    //     String inputPlayer = "Player 1\\n" + //
    //             "Player 2\\n" + //
    //             "5\\n" + //
    //             ""; // Provide virtual inputs
    //     InputStream in = new ByteArrayInputStream(inputPlayer.getBytes());
    //     Board board = new Board(in); // Create a new Board instance with the provided input stream
    //     System.setIn(in);
    //     Interface.gameIntro(board);
    //     int expectedMatchNumber = 5;
    //     assertEquals(expectedMatchNumber, board.getGameNumber());
    // }


    @Test
    void testroundOver() { // This test checks if the roundOver() function outputs the correct message.
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        board.setGameNumber(5); // Set up a scenario for testing roundOver()
        board.setRound(1);
        Interface.roundOver(board);
        String expectedOutput = "Round 1 over. R wins this round!\n" + //
                "Game Over\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void testprintSkip() { // This test checks if the printSkip() function outputs the correct message.
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Interface.printSkip(board);
        String expectedOutput = "The current round was skipped so no points awarded.\n" + //
                "Game Over\n";
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    void testGameOver() { // This test checks if the GameOver() function outputs the correct message.
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Interface.GameOver(board);
        String expectedOutput = "Its a draw :(\nGame over.\n";
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    void testDisplayQuit() { // This test checks if the displayQuit() function outputs the correct message.
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Interface.displayQuit();
        String expectedOutput = "You Quit. Loser\n";
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    void testPlayerTurnCurrent() { // This test checks if the playerTurnCurrent() function outputs the correct message.
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Interface.currentTurnOver(board.getPlayer(1));
        String expectedOutput = "[0;31mPlayer 1[0m {167} pips R  finishes their move. ☺ \n" ;
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    void testnextPlayerTurn() { // This test checks if the nextPlayerTurn() function outputs the correct message.
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Interface.nextPlayerTurn(board.getPlayer(2));
        String expectedOutput = "Now it's [0;37mPlayer 2[0m {167} pips W 's turn to move. Lets go !\n";
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    void testprintDice() { // This test checks if the printDice() function outputs the correct message.
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Interface.printDice(2, 3);
        String expectedOutput = "The number of 2 dice thrown are 2 and 3. You can move 2 times\n";
        assertEquals(expectedOutput, outputStream.toString());
    }


    @Test
    void testprintPips() { // This test checks if the printPips() function outputs the correct message.
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Interface.printPips(board);
        String expectedOutput = "[0;31mPlayer 1[0m's pips are 167.\n" + //
                "[0;37mPlayer 2[0m's pips are 167.\n" + //
                "";
        assertEquals(expectedOutput, outputStream.toString());
    }




    @Test
    void testreadFile() throws IOException { // This test checks if the readFile() function correctly reads the content from the provided file.
        Path tempFile = Files.createTempFile("", ".txt"); // Create a temporary file with some content
        String fileContent = "pip\n";
        Files.write(tempFile, fileContent.getBytes());
        String inputString = tempFile.toString() + "\n"; // Create a Scanner with the temporary file's path as input
        Scanner scanner = new Scanner(new ByteArrayInputStream(inputString.getBytes())); // Read the content from the temporary file
        String promptMessage = "Please enter the file name: ";
        String actualContent = Interface.readFile("test:test.txt", scanner, promptMessage); // M.txt is a file located locally on my computer.
        assertEquals(fileContent.replaceAll("\\s+", " ").trim(), actualContent.replaceAll("\\s+", " ").trim()); // Compare the expected content with the actual content read from the file after normalizing strings
        Files.delete(tempFile); // Delete the temporary file
        scanner.close(); // Close the scanner
    }

	@Test
	void testShowHint() { // This test checks if the showHint() function outputs the correct message.
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Interface.controls();
        String expectedOutput = """ 
    START: (Re)Start Backgammon game.
    ROLL: Roll the dice.
    ROLL + 2 digits: Set the numbers you roll
    QUIT: Quit the game.
    PIP: View both players' pips.
    HINT: Print controls.
    MOVES: Print legal moves.
    SKIP: End current round and play next round.
    FORFEIT: Forfeit current move.
    Enter \"file:file_name.txt\" will run the commands in that file
    2 digits (from) + 2 digits (to): Move checkers between points.
    B + 1 digit + 2 digits: Move a checker from bar to a point.
    2 digits + E + 1 digit: Move a checker from a point to the endpoint.
    1 digit or 2 digits: Move a checker by the suggested move list.
    """;
        assertEquals(expectedOutput, outputStream.toString());
	}
}
