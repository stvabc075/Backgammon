// Main logic for a board game application, handling game initialization, user input processing, and game state management.
public class Game {
	Board board = new Board();
	Interface intface = new Interface();
	InputCheck command;

	public void initGame() {
		int startControl = 0;
		boolean matchOverDisplayed = false;
		intface.gameWelcome();
		intface.starterScreen();
		do {
			boolean cmdEntered = false;
			do {
				command = intface.getUserInput(board);

				// when user has started a round
				if (startControl == 1 && !board.isRoundOver()) {
					if (command.roll()) {
						board.rollDice();
						intface.printDice(board.getFace(1), board.getFace(2));
						intface.displayBoard(board);
						intface.showLegalMoves(board);
						cmdEntered = true;
					} else if (command.start()) {
						intface.starterScreen();
						startControl--;
						cmdEntered = true;
					} else if (command.quit()) {
						cmdEntered = true;
					} else if (command.move()) {
						if (board.moveisLegal(command)) {
							board.move(command);
							board.calcPips();
							if (board.isRoundOver()){
								board.addScore();
							}
							intface.displayBoard(board);
							if (board.getTotalNumMoves() != 0) {
								intface.showLegalMoves(board);
							} else if (board.getTotalNumMoves() == 0) {
								board.endTurn();
							}
							cmdEntered = true;
						} else {
							intface.printInvalidCmd();
						}
					} else if (command.showHint()) {
						intface.controls();
					} else if (command.showPip()) {
						intface.printPips(board);
					} else if (command.showLegalMoves()) {
						intface.showLegalMoves(board);
					} else if (command.setDice()){
						board.setFace(command);
						intface.displayBoard(board);
						intface.showLegalMoves(board);
						cmdEntered = true;
					} else if(command.skip()){
						intface.printSkip(board);
						board.addRoundNumber();
						if(!board.isGameOver()){
							board.initBoard();
							board.calcPips();
							intface.firstTurn(board);
							intface.printDice(board.getFace(1), board.getFace(2));
							intface.displayBoard(board);
							intface.showLegalMoves(board);
						}
						cmdEntered = true;
					} else if(command.forfeit()){
						intface.forfeitTurn(board.getPlayer(0));
						board.endTurn();
						board.setZeroDice();
						cmdEntered = true;
					}
					if (!board.isGameOver() && matchOverDisplayed) {
						if (command.start()) {
							startControl--;
							matchOverDisplayed = false;
							cmdEntered = true;
						} else if (command.quit())
							cmdEntered = true;
					}
					if (!matchOverDisplayed && !command.start()) {
						matchOverDisplayed = true;
					}
				}

				// if user has not started a game yet
				if (startControl == 0) {
					if (command.start()) {
						intface.gameIntro(board);
						board.initBoard();
						board.calcPips();
						intface.firstTurn(board);
						intface.printDice(board.getFace(1), board.getFace(2));
						intface.displayBoard(board);
						intface.showLegalMoves(board);
						startControl++;
						cmdEntered = true;
					} else if (command.quit()) {
						cmdEntered = true;
					} else if (command.showHint()) {
						intface.controls();
					} else if (command.showPip()) {
						intface.printPips(board);
					}
				}
			} while (!cmdEntered);
		} while (!command.quit() && !board.isGameOver());

		if (board.isGameOver()) {
			intface.GameOver(board);
		} else {
			intface.displayQuit();
		}
	}
}
