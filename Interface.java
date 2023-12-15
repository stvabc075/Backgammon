// Manages user interactions for a game, handling input parsing, game messaging, and board display.
import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class Interface {
	private final static String BLANK2 = "  ";
	private final static String BLANK3 = "   ";
	Scanner key;
	InputCheck input;

	Interface() {
		key = new Scanner(System.in);
	}

	Interface(InputStream inputStream) {
		key = new Scanner(inputStream);
	}

	// display message when game starts
	public void gameWelcome() {
		System.out.println("Welcome to Backgammon");
	}

	public InputCheck getUserInput(Board board) {
		boolean keyEntered = false;
		do {
			System.out.print("Enter command: ");
			String in = key.nextLine();
			if (InputCheck.text(in))
				in = readFile(in, key, "Please enter a new command: "); // takes in file and executes command
			if (InputCheck.validMove(in)) {
				input = new InputCheck(in);
				// check the input is a move command
				if (input.move()) {
					String inputUpper = in.trim().toUpperCase();
					// check the input is a 4-char command and is the current player
					if (inputUpper.length() == 4 && board.getPlayer(0) == board.getPlayer(2)) {
						String[] inputs = new String[2];
						inputs[0] = inputUpper.substring(0, 2);
						inputs[1] = inputUpper.substring(2, 4);
						// check if first part of the input is a number and convert it
						if (inputs[0].matches("\\d+")) {
							int num1 = Integer.parseInt(inputs[0]);
							inputs[0] = String.format("%02d", 25 - num1);
						}
						// check if second part of the input is a number and convert it
						if (inputs[1].matches("\\d+")) {
							int num2 = Integer.parseInt(inputs[1]);
							inputs[1] = String.format("%02d", 25 - num2);
						}
						inputUpper = inputs[0] + inputs[1];
					}
					input = new InputCheck(inputUpper);
				}
				keyEntered = true;
			} else {
				System.out.println("The command is invalid. Try again.");
			}
		} while (!keyEntered);
		return input;
	}

	// read file as input to play game
	public String readFile(String string, Scanner in, String promptMessage) { 
		boolean fileReadSuccess = false;
		String fileContent = "";
		do {
			String fileName = InputCheck.recText(string);
			File dir = new File(".");
			File[] exactMatches = dir.listFiles((dir1, name) -> name.equals(fileName));
			File[] caseInsensitiveMatches = dir.listFiles((dir1, name) -> name.equalsIgnoreCase(fileName));
			if (!((caseInsensitiveMatches != null && caseInsensitiveMatches.length > 0)
					&& !(exactMatches != null && exactMatches.length > 0))) {
				try {
					BufferedReader br = new BufferedReader(new FileReader(fileName));
					StringBuilder fileContentBuilder = new StringBuilder();
					String line;
					while ((line = br.readLine()) != null)
						fileContentBuilder.append(line).append("\n");
					br.close();
					fileContent = fileContentBuilder.toString().trim(); 
					fileReadSuccess = true;
				} catch (FileNotFoundException e) {
					System.out.println("Error: File not found - " + e.getMessage());
				} catch (IOException e) {
					System.out.println("Error reading file: " + e.getMessage());
				}
			} else {
				System.out.println(
						"A file exists with only the case of the file name different from the input file name.");
			}
			if (!fileReadSuccess) {
				System.out.print(promptMessage);
				if (in.hasNextLine()) { // Add a condition to check if the Scanner has the next line
					string = in.nextLine();
				} else
					break; // If there's no next line, break the loop
			}
		} while (!fileReadSuccess);
		return fileContent;
	}

	// print a message when player skips their turn
	public void forfeitTurn(Player player){
		System.out.println(player + " forfeits their turn.");
	}

	// initial screen to get player names and number of rounds
	public void gameIntro(Board board) {
		boolean correctInput = false;
		String lengthMsg = "Enter length of the game: ";
		System.out.print("Enter name of Player 1: ");
		board.initPlayer(1);
		System.out.println("Player 1: " + board.getPlayer(1).dispName() + ", Colour: RED");
		System.out.print("Enter name of Player 2: ");
		board.initPlayer(2);
		System.out.println("Player 2: " + board.getPlayer(2).dispName() + ", Colour: WHITE");
		while (!correctInput) {
			System.out.print(lengthMsg);
			String numGameString = key.nextLine();
			if (input.checkText(numGameString))
				numGameString = readFile(numGameString, key, "Please enter length of the game: ");
			try {
				double doubleValue = Double.parseDouble(numGameString);
				if (doubleValue > 0 && Math.floor(doubleValue) == doubleValue) {
					board.setGameNumber((int) doubleValue);
					board.setRound(1);
					correctInput = true;
				} else if (Math.floor(doubleValue) != doubleValue) {
					System.out.println("Error: The entered number is a float, please try again.");
				} else {
					System.out.println("Error: The entered number is not a natural number, please try again.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Error: The entered string cannot be converted to a number, please try again.");
			}
			lengthMsg = "Please enter a new length of the game:";
		}
	}

	// start menu - options to start or exit the game
	public void starterScreen() {
		System.out.println("Enter START to start the game, enter QUIT to exit or enter HINT to view controls");
	}

	// print message for invalid commands
	public void printInvalidCmd() {
		System.out.println("Invalid command, enter HINT to view controls or try again.");
	}

	public void displayBoard(Board board) {
		// calculating number of spaces for board printout
		String currentPipString = Integer.toString(board.getPlayer(0).getPips());
		String redPipString = Integer.toString(board.getPlayer(1).getScore());
		String whitePipString = Integer.toString(board.getPlayer(2).getScore());
		String gameNum = Integer.toString(board.getGameNumber());
		String roundNum = Integer.toString(board.getRound());
		int currentPipBLANK = 4 - currentPipString.length();
		int redScoreInitBLANK = 7 - redPipString.length() / 2;
		int redScoreFinalBLANK = 8 - (redPipString.length() + 1) / 2;
		int whiteScoreInitBLANK = 7 - whitePipString.length() / 2;
		int whiteScoreFinalBLANK = 8 - (whitePipString.length() + 1) / 2;
		int gameTileBLANK = 5 - gameNum.length();
		int roundTileBLANK = 5 - roundNum.length();
		int numberUppoints = Math.max(board.getSize("uppoint"), 1);
		int numberDownpoints = Math.max(board.getSize("downpoint"), 1);



		// Start of Very top Portion






		System.out.println("|---------------------------------------------------------------------|---------------|");
		if (board.getPlayer(0).getColourString() == "R") {
			System.out.print("| Current player's color: " + InterfaceColours.RED  + board.getPlayer(0).getColourString() + InterfaceColours.RESET + "                                 pips: " + board.getPlayer(0).getPips());
		} else if (board.getPlayer(0).getColourString() == "W")
			System.out.print("| Current player's color: " + InterfaceColours.WHITE  + board.getPlayer(0).getColourString() + InterfaceColours.RESET + "                                 pips: " + board.getPlayer(0).getPips());
		for (int i = 0; i < currentPipBLANK; i++)
            System.out.print(" ");
		System.out.print("|   Games: " + board.getGameNumber());
		for (int i = 0; i < gameTileBLANK; i++)
            System.out.print(" ");
		System.out.println("|");








		System.out.println("|---------------------------------------------------------------------|---------------|");
		// print dice results
		if (board.getFace(1) != board.getFace(2)) {
			System.out.print("| Dice:                     ");

			if (board.getMoveStep(1) == 1) {
				System.out.print(board.getFace(1));
			} else
				System.out.print(" ");

			System.out.print("              ");

			if (board.getMoveStep(2) == 1) {
				System.out.print(board.getFace(2));
			} else
				System.out.print(" ");

			System.out.print("                          |");
		} else if (board.getFace(1) == board.getFace(2)) {
			System.out.print("| Dice:      ");
			if (board.getMoveStep(1) >= 1) {
				System.out.print(board.getFace(1));
			} else
				System.out.print(" ");
			System.out.print("              ");
			if (board.getMoveStep(1) >= 2) {
				System.out.print(board.getFace(1));
			} else
				System.out.print(" ");
			System.out.print("              ");
			if (board.getMoveStep(1) >= 3) {
				System.out.print(board.getFace(1));
			} else
				System.out.print(" ");
			System.out.print("              ");
			if (board.getMoveStep(1) == 4) {
				System.out.print(board.getFace(1));
			} else
				System.out.print(" ");
			System.out.print("           |\n");
		}

		System.out.print("   Round: " + board.getRoundNumber());
		for (int i = 0; i < roundTileBLANK; i++)
            System.out.print(" ");
		//System.out.println("|");

		// Printing Current player (Current player is indicated)
		if (board.getPlayer(0).getColourString() == "RED") {
			board.calcPips(); // calulates pip before printing
			System.out.print("\n| Current player's color: " + InterfaceColours.RED
					+ board.getPlayer(0).getColourString() + InterfaceColours.RESET
					+ "                               pips:  " + board.getPlayer(0).getPips() + "|\n");
		} else if (board.getPlayer(0).getColourString() == "WHITE") {
			board.calcPips(); // calulates pip before printing
			System.out.print("\n| Current player's color: " + InterfaceColours.WHITE
					+ board.getPlayer(0).getColourString() + InterfaceColours.RESET
					+ "                             pips:  " + board.getPlayer(0).getPips() + "|\n");
		}



		// End of Very top Portion

		// Top portion of board
		System.out.println("|");
		System.out.println("|---------------------------------------------------------------------|---------------|");
		System.out.println("| " + InterfaceColours.WHITE + "13   14   15   16   17   18" + InterfaceColours.RESET
				+ " | " + InterfaceColours.WHITE + "B2" + InterfaceColours.RESET + " | " + InterfaceColours.WHITE
				+ "19   20   21   22   23   24" + InterfaceColours.RESET + " | " + InterfaceColours.RED + "E1"
				+ InterfaceColours.RESET + " |   " + InterfaceColours.RED + board.getPlayer(1).getColourString()
				+ InterfaceColours.RESET + " Score     |");
		System.out.print("| " + InterfaceColours.RED + "12   11   10   09   08   07" + InterfaceColours.RESET);
		System.out.print(" | " + InterfaceColours.WHITE + "B2" + InterfaceColours.RESET + " | ");
		System.out.print(InterfaceColours.RED + "06   05   04   03   02   01" + InterfaceColours.RESET + " | "
				+ InterfaceColours.RED + "E1" + InterfaceColours.RESET + " |");
		for (int i = 0; i < redScoreInitBLANK; i++)
			System.out.print(" ");
		System.out.print(board.getPlayer(1).getScore());
		for (int i = 0; i < redScoreFinalBLANK; i++)
			System.out.print(" ");
		System.out.println("|");
		// end of top portion

		// Middle portion
		for (int row = 0; row < numberUppoints; row++) {
			System.out.print("| ");
			for (int up = 11; up > 6; up--) {
				Stack<Checker> points = board.getPoint(up);
				if (row < points.size()) {
					System.out.print(points.get(row) + BLANK3);
				} else
					System.out.print(BLANK2 + BLANK3);
			}
			Stack<Checker> points6 = board.getPoint(6);
			if (row < points6.size()) {
				System.out.print(points6.get(row) + " | ");
			} else
				System.out.print(BLANK2 + " | ");
			Stack<Checker> bar = board.getBar(1);
			if (row < bar.size()) {
				System.out.print(bar.get(row) + " | ");
			} else
				System.out.print(BLANK2 + " | ");
			for (int up = 5; up > 0; up--) {
				Stack<Checker> points = board.getPoint(up);
				if (row < points.size()) {
					System.out.print(points.get(row) + BLANK3);
				} else
					System.out.print(BLANK2 + BLANK3);
			}
			Stack<Checker> points0 = board.getPoint(0);
			if (row < points0.size()) {
				System.out.print(points0.get(row));
			} else
				System.out.print(BLANK2);
			if (row == 0) {
				if (board.getEndpoint(0).size() < 10)
					System.out.print(" | " + InterfaceColours.RED + "0" + board.getEndpoint(0).size()
							+ InterfaceColours.RESET + " |---------------|");
				else
					System.out.print(" | " + InterfaceColours.RED + board.getEndpoint(0).size() + InterfaceColours.RESET
							+ " |---------------|");
			} else
				System.out.print(" |    |");
			System.out.println();
		}
		System.out.println("|-----------------------------|----|-----------------------------|----|");
		for (int row = 0; row < numberDownpoints; row++) {
			System.out.print("| ");
			for (int up = 12; up < 17; up++) {
				Stack<Checker> points = board.getPoint(up);
				if (row < numberDownpoints - points.size()) {
					System.out.print(BLANK2 + BLANK3);
				} else
					System.out.print(points.get(numberDownpoints - row - 1) + BLANK3);
			}
			Stack<Checker> points17 = board.getPoint(17);
			if (row < numberDownpoints - points17.size()) {
				System.out.print(BLANK2 + " | ");
			} else
				System.out.print(points17.get(numberDownpoints - row - 1) + " | ");
			Stack<Checker> bar = board.getBar(0);
			if (row < numberDownpoints - bar.size()) {
				System.out.print(BLANK2 + " | ");
			} else
				System.out.print(bar.get(numberDownpoints - row - 1) + " | ");
			for (int up = 18; up < 23; up++) {
				Stack<Checker> points = board.getPoint(up);
				if (row < numberDownpoints - points.size()) {
					System.out.print(BLANK2 + BLANK3);
				} else
					System.out.print(points.get(numberDownpoints - row - 1) + BLANK3);
			}
			Stack<Checker> points23 = board.getPoint(23);
			if (row < numberDownpoints - points23.size()) {
				System.out.print(BLANK2);
			} else
				System.out.print(points23.get(numberDownpoints - row - 1));
			if (row == numberDownpoints - 1) {
				if (board.getEndpoint(1).size() < 10)
					System.out.print(" | " + InterfaceColours.WHITE + "0" + board.getEndpoint(1).size()
							+ InterfaceColours.RESET + " |---------------|");
				else
					System.out.print(" | " + InterfaceColours.WHITE + board.getEndpoint(1).size()
							+ InterfaceColours.RESET + " |---------------|");
			} else
				System.out.print(" |    |");
			System.out.println();
		}
		// end of middle portion

		// Bottom portion of board
		System.out.print("| " + InterfaceColours.WHITE + "12   11   10   09   08   07" + InterfaceColours.RESET);
		System.out.print(" | " + InterfaceColours.RED + "B1" + InterfaceColours.RESET + " | ");
		System.out.println(InterfaceColours.WHITE + "06   05   04   03   02   01" + InterfaceColours.RESET + " | "
				+ InterfaceColours.WHITE + "E2" + InterfaceColours.RESET + " |  " + InterfaceColours.WHITE
				+"  "+ board.getPlayer(2).getColourString() + InterfaceColours.RESET + "  Score   |");
		System.out.print("| " + InterfaceColours.RED + "13   14   15   16   17   18" + InterfaceColours.RESET + " | "
				+ InterfaceColours.RED + "B1" + InterfaceColours.RESET + " | " + InterfaceColours.RED
				+ "19   20   21   22   23   24" + InterfaceColours.RESET + " | " + InterfaceColours.WHITE + "E2"
				+ InterfaceColours.RESET + " |");
		for (int i = 0; i < whiteScoreInitBLANK; i++)
			System.out.print(" ");
		System.out.print(board.getPlayer(2).getScore());
		for (int i = 0; i < whiteScoreFinalBLANK; i++)
			System.out.print(" ");
		System.out.println("|");
		System.out.println("|---------------------------------------------------------------------|---------------|");
		// end of bottom portion
	}

	// print a list of available commands
	public void controls() {
		System.out.println("START: (Re)Start Backgammon game.");
		System.out.println("ROLL: Roll the dice.");
		System.out.println("ROLL + 2 digits: Set the numbers you roll");
		System.out.println("QUIT: Quit the game.");
		System.out.println("PIP: View both players' pips.");
		System.out.println("HINT: Print controls.");
		System.out.println("MOVES: Print legal moves.");
		System.out.println("SKIP: End current round and play next round.");
		System.out.println("FORFEIT: Forfeit current move.");
		System.out.println("Enter \"file:file_name.txt\" will run the commands in that file");
		System.out.println("2 digits (from) + 2 digits (to): Move checkers between points.");
		System.out.println("B + 1 digit + 2 digits: Move a checker from bar to a point.");
		System.out.println("2 digits + E + 1 digit: Move a checker from a point to the endpoint.");
		System.out.println("1 digit or 2 digits: Move a checker by the suggested move list.");
	}

	// prints the result after dice rolled
	public void printDice(int firstFace, int secondFace) {
		if (firstFace != secondFace) {
			System.out.println(
					"The number of 2 dice thrown are " + firstFace + " and " + secondFace + ". You can move 2 times");
		} else if (firstFace == secondFace)
			System.out.println(
					"The number of 2 dice thrown are " + firstFace + " and " + secondFace + ". You can move 4 times");
	}

	// decide who plays first based on roll
	public void firstTurn(Board board) {
		do {
			board.rollDice();
			if (board.getFace(1) > board.getFace(2)) {
				System.out.println("Die 1 is " + board.getFace(1) + ". Die 2 is " + board.getFace(2)
						+ ". Dice 1 > Dice 2 -> Red goes first.");
				board.setCurrentPlayer(1); // Sets player 1 as current player if going first
			} else if (board.getFace(1) < board.getFace(2)) {
				System.out.println("Die 1 is " + board.getFace(1) + ". Die 2 is " + board.getFace(2)
						+ ". Dice 2 > Dice 1 -> White goes first.");
				board.setCurrentPlayer(2); // Sets player 2 as current player if going first
			} else if (board.getFace(1) == board.getFace(2)) {
				System.out.println("Die 1 is " + board.getFace(1) + ". Die 2 is " + board.getFace(2)
						+ ". Dice 1 = Dice 2 -> Reroll");
			}
		} while (board.getFace(1) == board.getFace(2));
	}

	public void GameOver(Board board) { 
		if (board.getPlayer(1).getScore() > board.getPlayer(2).getScore()) {
			System.out.println(board.getPlayer(1).dispName() + " wins the game!");
		} 
		else if (board.getPlayer(1).getScore() < board.getPlayer(2).getScore()) {
			System.out.println(board.getPlayer(2).dispName() + " wins the game!");
		} 
		else if (board.getPlayer(1).getScore() == board.getPlayer(2).getScore())
			System.out.println("Its a draw :(");
		System.out.println("Game over.");
	}
	
	// print message to the screen when user quits
	public void displayQuit() {
		System.out.println("You Quit. Loser");
	}

	// print message when the current player's turn is over
	public void currentTurnOver(Player player) {
		System.out.println(player + " " + player.getColourString() + "  finishes their move. ☺ ");
	}

	// print message when it's the next player's turn
	public void nextPlayerTurn(Player player) {
		System.out.println("Now it's " + player + " " + player.getColourString() + " 's turn to move. Lets go !");
	}

	// print each player's pips
	public void printPips(Board board) {
		System.out.println(board.getPlayer(1).dispName() + "'s pips are " + board.getPlayer(1).getPips() + ".");
		System.out.println(board.getPlayer(2).dispName() + "'s pips are " + board.getPlayer(2).getPips() + ".");
	}

	// print all legal moves
	public void showLegalMoves(Board board) {
		String formattedString, inString;
		int firstStep, secondStep;
		int outputCount = 0;
		String[] startPos = new String[25];
		String[] endPos = new String[25];
		if (board.getPlayer(0) == board.getPlayer(1)) {
			startPos[0] = "B1";
			endPos[24] = "E1";
			for (int i = 1; i <= 24; i++) {
				startPos[25 - i] = String.format("%02d", i);
				endPos[24 - i] = String.format("%02d", i);
			}
		} else if (board.getPlayer(0) == board.getPlayer(2)) {
			startPos[0] = "B2";
			endPos[24] = "E2";
			for (int i = 1; i <= 24; i++) {
				startPos[25 - i] = String.format("%02d", i);
				endPos[24 - i] = String.format("%02d", i);
			}
		}
		for (int i = 0; i < InputCheck.getLegalMoves().length; i++)
			InputCheck.setLegalMoves(i, null);
		for (int i = 0; i < startPos.length; i++)
			for (int j = i; j < endPos.length; j++) {
				formattedString = startPos[i] + endPos[j];
				inString = formattedString;
				if (board.getPlayer(0) == board.getPlayer(2)) {
					String tempStart = startPos[i];
					String tempEnd = endPos[j];
					if (startPos[i].matches("\\d+")) {
						int number1 = Integer.parseInt(startPos[i]);
						tempStart = String.format("%02d", 25 - number1);
					}
					if (endPos[j].matches("\\d+")) {
						int number2 = Integer.parseInt(endPos[j]);
						tempEnd = String.format("%02d", 25 - number2);
					}
					formattedString = tempStart + tempEnd;
				}
				input = new InputCheck(formattedString);
				firstStep = board.getMoveStep(1);
				secondStep = board.getMoveStep(2);
				if (board.moveisLegal(input)) {
					InputCheck.setLegalMoves(outputCount, formattedString);
					outputCount++;
					if (outputCount == 1)
						System.out.println("Here are the legal moves available:");
					if (outputCount > 0 && outputCount < 10)
						System.out.print(" ");
					System.out.println(outputCount + ": " + inString);
				}
				board.setDiceStepVals(firstStep, secondStep);
			}
		if (outputCount == 0)
			System.out.println("No possible moves");

	}

	// print message when the match round is forced to end early
	public void printSkip (Board board) {
		System.out.println("The current round was skipped so no points awarded.");
		if (board.getRoundNumber() == 1) {
			System.out.println("Game Over");
		} else if (board.getRoundNumber() > 1)
			System.out.print("There are " + board.getRoundNumber() + " total rounds, ");
		if (board.getRoundNumber() - board.getRound() == 1) {
			System.out.println("so there is still " + (board.getRoundNumber() - board.getRound()) + " rounds");
		} else if (board.getRoundNumber() - board.getRound() > 1)
			System.out.println("so there are still " + (board.getRoundNumber() - board.getRound()) + " rounds");
	}

	// print message when a round is over
	public void roundOver(Board board){
		System.out.println("Round " + board.getRound() + " over. " + board.getPlayer(0).getColourString() + " wins this round!");
		if (board.getRoundNumber() == 1) {
			System.out.println("Game Over");
		} else if (board.getRoundNumber() > 1)
			System.out.print("There are " + board.getRoundNumber() + " rounds in total, ");
		if (board.getRoundNumber() - board.getRound() == 1) {
			System.out.println("so there is still " + (board.getRoundNumber() - board.getRound()) + " round left. Type SKIP to move onto the next round!");
		} else if (board.getRoundNumber() - board.getRound() > 1)
			System.out.println("so there are still " + (board.getRoundNumber() - board.getRound()) + " rounds left to play. Type SKIP to move onto the next round!");
	}
}