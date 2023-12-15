import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;


public class Board {
	public static final int NUM_POINTS = 24;
	public static final int NUM_BARS = CheckerTemplate.values().length;
	public static final int NUM_ENDPOINTS = CheckerTemplate.values().length;

	private List<Stack<Checker>> points;
	private List<Stack<Checker>> bars;
	private List<Stack<Checker>> endpoints;

	private Player[] players;
	private Interface intrface;
	private Scanner input;
	private Dice dice;
	private int roundNumber = 1;
	private int gameNumber;

	// TEST METHODS
	public void setPlayer(int index, Player player) { 
	    if (index >= 0 && index < players.length) {
	        players[index] = player;
	    }
	}

	Board() {
		intrface = new Interface();
		input = new Scanner(System.in);
		dice = new Dice();
		// 3 player instances - 2 ingame, 1 for current player
		this.players = new Player[3];

		points = new ArrayList<>(NUM_POINTS);
		bars = new ArrayList<>(NUM_BARS);
		endpoints = new ArrayList<>(NUM_ENDPOINTS);

		// initialise points, bars, terminus'
		for (int i = 0; i < NUM_POINTS; i++) {
			points.add(new Stack<>());
		}
		for (int i = 0; i < NUM_BARS; i++) {
			bars.add(new Stack<>());
		}
		for (int i = 0; i < NUM_ENDPOINTS; i++) {
			endpoints.add(new Stack<>());
		}
	}

	// testing function
	public Board (InputStream inStream) { 
        this.input = new Scanner(inStream);
        this.players = new Player[3];
    }

	// initialize the board
	public void initBoard() {
		// clear ALL from outset
		for (int i = 0; i < 24; i++)
			points.get(i).clear();
		for (int i = 0; i < 2; i++)
			bars.get(i).clear();
		for (int i = 0; i < 2; i++)
			endpoints.get(i).clear();
		// adding checkers to starting positions
		for (int i = 0; i < 2; i++) {
			points.get(0).push(new Checker(CheckerTemplate.WHITE));
			points.get(23).push(new Checker(CheckerTemplate.RED));
		}
		for (int i = 0; i < 3; i++) {
			points.get(16).push(new Checker(CheckerTemplate.WHITE));
			points.get(7).push(new Checker(CheckerTemplate.RED));
		}
		for (int i = 0; i < 5; i++) {
			points.get(11).push(new Checker(CheckerTemplate.WHITE));
			points.get(18).push(new Checker(CheckerTemplate.WHITE));
			points.get(5).push(new Checker(CheckerTemplate.RED));
			points.get(12).push(new Checker(CheckerTemplate.RED));
		}
	}

	// initialise player given index in array
	public void initPlayer(int playerIndex) {
		String nameString = input.nextLine();
		if (InputCheck.text(nameString))
			nameString = intrface.readFile(nameString, input, "Please enter new player name: ");
		if (playerIndex == 1) {
			players[playerIndex] = new Player(nameString, CheckerTemplate.RED);
		} else if (playerIndex == 2)
			players[playerIndex] = new Player(nameString, CheckerTemplate.WHITE);
	}

	// return player object at given index
	public Player getPlayer(int playerIndex) {
		return switch (playerIndex) {
			case 0 -> players[0];
			case 1 -> players[1];
			case 2 -> players[2];
			default -> players[0];
		};
	}



	public void setCurrentPlayer(int playerIndex) {
		this.players[0] = players[playerIndex];
	}

	// end current player's turn
	public void endTurn() {
		if (!isRoundOver()) {
			if (players[0] == players[1]) {
				players[0] = players[2];
				intrface.currentTurnOver(players[1]);
				intrface.nextPlayerTurn(players[2]);
			} 
			else if (players[0] == players[2]) {
				players[0] = players[1];
				intrface.currentTurnOver(players[2]);
				intrface.nextPlayerTurn(players[1]);
			}
		} 
		else if (isRoundOver()) {
			if (players[0] == players[1]) {
				intrface.currentTurnOver(players[1]);
			} else if (players[0] == players[2])
				intrface.currentTurnOver(players[2]);
		}
	}

	private boolean isPointClear(int src, int dest) {
		if (src < 0)
			src = 0;
		if (dest > 23)
			dest = 23;
		for (int i = src + 1; i < dest; i++) {
			if (!points.get(i).empty() && points.get(i).peek().getCheckerTemplate() == players[0].getCheckerTemp()) {
				return false;
			}
		}
		return true;
	}

	// total distance number that player can move
	public int getTotalNumMoves() {
		return dice.getNumMoves();
	}

	private int getPlayerNumber() { 
		if (players[0].getCheckerTemplate() == CheckerTemplate.WHITE) {
			return 1;
		} else // otherwise RED
			return 0;
	}

	// determines furthest occupied point in the player's inner table or the
	// starting point of their move
	private int findFurthestOccupiedpoint(InputCheck command, List<Stack<Checker>> points, int playerIndex) {
		int maxpoint = -1;
		if (playerIndex == 1) {
			for (int i = 0; i <= 5; i++) {
				Stack<Checker> pointi = points.get(i);
				if (!pointi.isEmpty())
					maxpoint = i;
			}
			if (command.getSrcPile() >= 6)
				maxpoint = command.getSrcPile();
		}

		else if (playerIndex == 2) {
			for (int i = 23; i >= 18; i--) {
				Stack<Checker> pointi = points.get(i);
				if (!pointi.isEmpty())
					maxpoint = i;
			}
			if (command.getSrcPile() <= 17)
				maxpoint = command.getSrcPile();
		}
		return maxpoint;
	}

	// DICE METHODS
	public void rollDice() {
		dice.roll();
	}
	public void setZeroDice(){
		dice.setDiceZero();
	}
		// set dice face according to user input
	public void setFace(InputCheck command) {
		dice.setFace(command.getFace(1), command.getFace(2));
	}

	// return dice face for the given index
	public int getFace(int index) {
		return switch (index) {
			case 1 -> dice.getFace(1);
			case 2 -> dice.getFace(2);
			default -> 0;
		};
	}

	// return move step of the dice given the index
	public int getMoveStep(int index) {
		return switch (index) {
			case 1 -> dice.getStepVal(1);
			case 2 -> dice.getStepVal(2);
			default -> 0;
		};
	}

	// set the value of each dice step
	public void setDiceStepVals(int stepOne, int stepTwo) {
		dice.setStepVal(stepOne, stepTwo);
	}

	public Stack<Checker> getPoint(int i) {
		return points.get(i);
	}

	public Stack<Checker> getBar(int i) {
		return bars.get(i);
	}

	public Stack<Checker> getEndpoint(int i) {
		return endpoints.get(i);
	}

	// game and round methods
	public int getRoundNumber() {
		return roundNumber;
	}
	public int getRound() {
		return roundNumber;
	}
	public void setRound(int roundnum) {
		this.roundNumber = roundnum;
	}
	public void addRoundNumber() {
		roundNumber++;
	}
	public int getGameNumber () { 
		return gameNumber;
	}
	public void setGameNumber (int gameNumber) { 
		this.gameNumber = gameNumber;
	}
	public boolean isRoundOver() {
		for (Stack<Checker> endpoint : endpoints)
			if (endpoint.size() == 15)
				return true;
		return false;
	}
	public boolean isGameOver(){
		return gameNumber + 1 == roundNumber;
	}
		public void addGamenumber(){
		gameNumber++;
	}


	// score methods
	public void setZeroScore(){
		players[1].setScore(0);
		players[2].setScore(0);
	}
	public void addScore(){
		players[0].setScore(10);
	}

	// return size of the largest stack for given index
	public int getSize(String index) {
		int uppointSize = 0;
		int downpointSize = 0;
		List<Stack<Checker>> up12points = points.subList(0, 12);
		List<Stack<Checker>> down12points = points.subList(12, 24);
		for (Stack<Checker> point : up12points)
			if (point.size() > uppointSize)
				uppointSize = point.size();
		if (bars.get(1).size() > uppointSize)
			uppointSize = bars.get(1).size();
		for (Stack<Checker> point : down12points)
			if (point.size() > downpointSize)
				downpointSize = point.size();
		if (bars.get(0).size() > downpointSize)
			downpointSize = bars.get(0).size();
		return switch (index) {
			case "uppoint" -> uppointSize;
			case "downpoint" -> downpointSize;
			default -> 0;
		};
	}

	// calculate number of pips for each player
	public void calcPips() {
		int pip1 = 0;
		int pip2 = 0;
		for (int i = 0; i < 24; i++) {
			if (!points.get(i).empty()) {
				if (points.get(i).peek().getCheckerTemplate() == CheckerTemplate.RED) {
					pip1 += (i + 1) * points.get(i).size();
				} else if (points.get(i).peek().getCheckerTemplate() == CheckerTemplate.WHITE) {
					pip2 += (24 - i) * points.get(i).size();
				}
			}
			if (endpoints.get(0).size() == 15)
				pip1 = 0;
			if (endpoints.get(1).size() == 15)
				pip2 = 0;
			players[1].setPips(pip1);
			players[2].setPips(pip2);
		}
	}

	public boolean moveisLegal(InputCheck command) {
		boolean isLegal = false;
		// Check a move from the bar to a point is legal based on checker ownership,
		// dice roll, and board state conditions
		if (command.fromBarMove() && command.toPointMove()) {
			Stack<Checker> bar = bars.get(command.getSrcPile());
			Stack<Checker> point = points.get(command.getDestPile());
			if (!bar.empty()) {
				// validate a move from the bar to a point, ensuring checker ownership, move
				// legality based on dice rolls, and ensuring path and destination point
				// conditions are met
				if (bar.peek().getCheckerTemplate() == players[0].getCheckerTemplate()
						&& (point.empty() || point.size() == 1
								|| bar.peek().getCheckerTemplate() == point.peek().getCheckerTemplate())
						&& dice.getNumMoves() != 0) {
					if (dice.getFace(1) != dice.getFace(2)) {

						for (int i = 1; i <= 2; i++) {
							if (dice.getStepVal(i) != 0 && (players[0] == players[1]
									&& command.getSrcPile() + 24 == command.getDestPile() + dice.getFace(i)
									|| players[0] == players[2]
											&& command.getSrcPile() + dice.getFace(i) == command.getDestPile() + 2)) {
								isLegal = true;
								dice.decreaseNumMoves(i);
							}
						}

						if (dice.getStepVal(1) != 0
								&& dice.getStepVal(2) != 0
								&& bar.size() == 1
								&& (players[0] == players[1]
										&& command.getSrcPile() + 24 == command.getDestPile() + dice.getFace(1)
												+ dice.getFace(2)
										&& (points.get(24 - dice.getFace(1)).empty()
												|| points.get(24 - dice.getFace(2)).empty()
												|| points.get(24 - dice.getFace(1)).peek()
														.getCheckerTemplate() == players[1].getCheckerTemplate()
												|| points.get(24 - dice.getFace(2)).peek()
														.getCheckerTemplate() == players[1].getCheckerTemplate())
										|| players[0] == players[2]
												&& command.getSrcPile() + dice.getFace(1)
														+ dice.getFace(2) == command.getDestPile() + 2
												&& (points.get(dice.getFace(1) - 1).empty()
														|| points.get(dice.getFace(2) - 1).empty()
														|| points.get(dice.getFace(1) - 1).peek()
																.getCheckerTemplate() == players[2].getCheckerTemplate()
														|| points.get(dice.getFace(2) - 1).peek()
																.getCheckerTemplate() == players[2]
																		.getCheckerTemplate()))) {
							isLegal = true;
							dice.decreaseNumMoves(1);
							dice.decreaseNumMoves(2);
						}
					}
					if (dice.getFace(1) == dice.getFace(2) && dice.getStepVal(1) != 0) {
						boolean validMove = true;
						boolean isValidStep = true;
						if (players[0] == players[1]) {
							if (command.getSrcPile() + 24 == command.getDestPile() + dice.getFace(1)
									&& (points.get(24 - dice.getFace(1)).empty()
											|| points.get(24 - dice.getFace(1)).size() == 1
											|| points.get(24 - dice.getFace(1)).peek()
													.getCheckerTemplate() == players[1].getCheckerTemplate())) {
								isLegal = true;
								dice.decreaseNumMoves(1);
							}
							for (int i = 2; i <= dice.getStepVal(1); i++) {
								if (command.getSrcPile() + 24 == command.getDestPile() + dice.getFace(1) * i
										&& bar.size() == 1) {
									for (int j = 1; j <= i - 1; j++) {
										isValidStep = points.get(24 - dice.getFace(1) * j).empty()
												|| points.get(24 - dice.getFace(1) * j).peek()
														.getCheckerTemplate() == players[1].getCheckerTemplate();
										validMove = validMove && isValidStep;
									}
									isValidStep = points.get(24 - dice.getFace(1) * i).empty()
											|| points.get(24 - dice.getFace(1) * i).peek()
													.getCheckerTemplate() == players[1].getCheckerTemplate()
											|| points.get(24 - dice.getFace(1) * i).size() == 1;
									validMove = validMove && isValidStep;
									if (validMove) {
										isLegal = true;
										for (int j = 0; j < i; j++)
											dice.decreaseNumMoves(1);
									}
								}
							}
						}
						if (players[0] == players[2]) {
							if (command.getSrcPile() + dice.getFace(1) == command.getDestPile() + 2
									&& (points.get(dice.getFace(1) - 1).empty()
											|| points.get(dice.getFace(1) - 1).size() == 1
											|| points.get(dice.getFace(1) - 1).peek().getCheckerTemplate() == players[2]
													.getCheckerTemplate())) {
								isLegal = true;
								dice.decreaseNumMoves(1);
							}
							for (int i = 2; i <= dice.getStepVal(1); i++)
								if (command.getSrcPile() + dice.getFace(1) * i == command.getDestPile() + 2
										&& bar.size() == 1) {
									for (int j = 1; j <= i - 1; j++) {
										isValidStep = points.get(dice.getFace(1) * j - 1).empty()
												|| points.get(dice.getFace(1) * j - 1).peek()
														.getCheckerTemplate() == players[2].getCheckerTemplate();
										validMove = validMove && isValidStep;
									}
									isValidStep = points.get(dice.getFace(1) * i - 1).empty()
											|| points.get(dice.getFace(1) * i - 1).peek()
													.getCheckerTemplate() == players[2].getCheckerTemplate()
											|| points.get(dice.getFace(1) * i - 1).size() == 1;
									validMove = validMove && isValidStep;
									if (validMove) {
										isLegal = true;
										for (int j = 0; j < i; j++)
											dice.decreaseNumMoves(1);
									}
								}
						}
					}
				}
			}
		} else if (command.fromPointMove() && command.toEndMove()) {
			Stack<Checker> point = points.get(command.getSrcPile());
			Stack<Checker> endpoint = endpoints.get(command.getDestPile());
			int finalStage = endpoint.size();
			for (int i = 0; i < 6; i++) {
				if (!points.get(i).empty())
					if (players[0] == players[1]
							&& points.get(i).peek().getCheckerTemplate() == players[0].getCheckerTemplate()) {
						finalStage += points.get(i).size();
						if (command.getSrcPile() >= 6)
							finalStage += 1;
					}
				if (!points.get(i + 18).empty())
					if (players[0] == players[2]
							&& points.get(i + 18).peek().getCheckerTemplate() == players[0].getCheckerTemplate()) {
						finalStage += points.get(i + 18).size();
						if (command.getSrcPile() <= 17)
							finalStage += 1;
					}
			}
			if (!point.empty())
				if (getPlayerNumber() == command.getDestPile()
						&& point.peek().getCheckerTemplate() == players[0].getCheckerTemplate() && finalStage == 15
						&& dice.getNumMoves() != 0) {
					int maxpoint = -1;
					if (dice.getFace(1) != dice.getFace(2)) {
						int diceIndexToDecrement = -1;
						if (players[0] == players[1]) {
							maxpoint = findFurthestOccupiedpoint(command, points, 1);
							for (int i = 1; i <= 2; i++) {
								if (dice.getStepVal(i) != 0 && command.getSrcPile() == maxpoint
										&& command.getSrcPile() + 1 < command.getDestPile() + dice.getFace(i)
										|| dice.getStepVal(i) != 0 && command.getSrcPile() + 1 == command.getDestPile()
												+ dice.getFace(i)) {
									isLegal = true;
									if (diceIndexToDecrement == -1
											|| dice.getFace(i) > dice.getFace(diceIndexToDecrement))
										diceIndexToDecrement = i;
								}
							}
							if (diceIndexToDecrement != -1)
								dice.decreaseNumMoves(diceIndexToDecrement);
							if (dice.getStepVal(1) != 0 && dice.getStepVal(2) != 0
									&& command.getSrcPile() + 1 > command.getDestPile() + dice.getFace(1)
									&& command.getSrcPile() + 1 > command.getDestPile() + dice.getFace(2))
								if (command.getSrcPile() == maxpoint
										&& command.getSrcPile() + 1 < command.getDestPile() + dice.getFace(1)
												+ dice.getFace(2)
										&& (points.get(command.getSrcPile() - dice.getFace(1)).empty()
												|| points.get(command.getSrcPile() - dice.getFace(2)).empty()
												|| points.get(command.getSrcPile() - dice.getFace(1)).peek()
														.getCheckerTemplate() == players[1].getCheckerTemplate()
												|| points.get(command.getSrcPile() - dice.getFace(2)).peek()
														.getCheckerTemplate() == players[1].getCheckerTemplate())
										&& point.size() == 1
										&& (isPointClear(command.getSrcPile() - dice.getFace(1), command.getSrcPile())
												|| isPointClear(command.getSrcPile() - dice.getFace(2),
														command.getSrcPile()))
										|| command.getSrcPile() + 1 == command.getDestPile() + dice.getFace(1)
												+ dice.getFace(2)
												&& (points.get(command.getSrcPile() - dice.getFace(1)).empty()
														|| points.get(command.getSrcPile() - dice.getFace(2)).empty()
														|| points.get(command.getSrcPile() - dice.getFace(1)).peek()
																.getCheckerTemplate() == players[1].getCheckerTemplate()
														|| points.get(command.getSrcPile() - dice.getFace(2)).peek()
																.getCheckerTemplate() == players[1]
																		.getCheckerTemplate())) {
									isLegal = true;
									dice.decreaseNumMoves(1);
									dice.decreaseNumMoves(2);
								}
						}
						if (players[0] == players[2]) {
							maxpoint = findFurthestOccupiedpoint(command, points, 2);
							for (int i = 1; i <= 2; i++) {
								if (dice.getStepVal(i) != 0 && command.getSrcPile() == maxpoint
										&& command.getSrcPile() + dice.getFace(i) > command.getDestPile() + 23
										|| dice.getStepVal(i) != 0 && command.getSrcPile()
												+ dice.getFace(i) == command.getDestPile() + 23) {
									isLegal = true;
									if (diceIndexToDecrement == -1
											|| dice.getFace(i) > dice.getFace(diceIndexToDecrement))
										diceIndexToDecrement = i;
								}
							}
							if (diceIndexToDecrement != -1)
								dice.decreaseNumMoves(diceIndexToDecrement);
							if (dice.getStepVal(1) != 0 && dice.getStepVal(2) != 0
									&& command.getSrcPile() + dice.getFace(1) < command.getDestPile() + 23
									&& command.getSrcPile() + dice.getFace(2) < command.getDestPile() + 23)
								if (command.getSrcPile() == maxpoint
										&& command.getSrcPile() + dice.getFace(1)
												+ dice.getFace(2) > command.getDestPile() + 23
										&& (points.get(command.getSrcPile() + dice.getFace(1)).empty()
												|| points.get(command.getSrcPile() + dice.getFace(2)).empty()
												|| points.get(command.getSrcPile() + dice.getFace(1)).peek()
														.getCheckerTemplate() == players[2].getCheckerTemplate()
												|| points.get(command.getSrcPile() + dice.getFace(2)).peek()
														.getCheckerTemplate() == players[2].getCheckerTemplate())
										&& point.size() == 1
										&& (isPointClear(command.getSrcPile(), command.getSrcPile() + dice.getFace(1))
												|| isPointClear(command.getSrcPile(),
														command.getSrcPile() + dice.getFace(2)))
										|| command.getSrcPile() + dice.getFace(1)
												+ dice.getFace(2) == command.getDestPile() + 23
												&& (points.get(command.getSrcPile() + dice.getFace(1)).empty()
														|| points.get(command.getSrcPile() + dice.getFace(2)).empty()
														|| points.get(command.getSrcPile() + dice.getFace(1)).peek()
																.getCheckerTemplate() == players[2].getCheckerTemplate()
														|| points.get(command.getSrcPile() + dice.getFace(2)).peek()
																.getCheckerTemplate() == players[2]
																		.getCheckerTemplate())) {
									isLegal = true;
									dice.decreaseNumMoves(1);
									dice.decreaseNumMoves(2);
								}
						}
					}
					if (dice.getFace(1) == dice.getFace(2) && dice.getStepVal(1) != 0) {
						boolean validMove = true;
						boolean isValidStep = true;
						boolean shouldBreak = false;
						if (players[0] == players[1]) {
							maxpoint = findFurthestOccupiedpoint(command, points, 1);
							if (command.getSrcPile() == maxpoint
									&& command.getSrcPile() + 1 < command.getDestPile() + dice.getFace(1)
									|| command.getSrcPile() + 1 == command.getDestPile() + dice.getFace(1)) {
								isLegal = true;
								dice.decreaseNumMoves(1);
							}
							for (int i = 2; i <= dice.getStepVal(1) && !shouldBreak; i++)
								if (command.getSrcPile() == maxpoint
										&& command.getSrcPile() + 1 < command.getDestPile() + dice.getFace(1) * i) {
									for (int j = 1; j <= i - 1
											&& command.getSrcPile() - dice.getFace(1) * j >= 0; j++) {
										isValidStep = points.get(command.getSrcPile() - dice.getFace(1) * j).empty()
												|| points.get(command.getSrcPile() - dice.getFace(1) * j).peek()
														.getCheckerTemplate() == players[1].getCheckerTemplate();
										validMove = validMove && isValidStep;
									}
									isValidStep = point.size() == 1 && isPointClear(
											command.getSrcPile() - dice.getFace(1) * (i - 1), command.getSrcPile());
									validMove = validMove && isValidStep;
									if (validMove) {
										isLegal = true;
										for (int j = 0; j < i; j++)
											dice.decreaseNumMoves(1);
									}
									shouldBreak = true;
								} else if (command.getSrcPile() + 1 == command.getDestPile() + dice.getFace(1) * i) {
									for (int j = 1; j <= i - 1; j++) {
										isValidStep = points.get(command.getSrcPile() - dice.getFace(1) * j).empty()
												|| points.get(command.getSrcPile() - dice.getFace(1) * j).peek()
														.getCheckerTemplate() == players[1].getCheckerTemplate();
										validMove = validMove && isValidStep;
									}
									if (validMove) {
										isLegal = true;
										for (int j = 0; j < i; j++)
											dice.decreaseNumMoves(1);
									}
								}
						}
						if (players[0] == players[2]) {
							maxpoint = findFurthestOccupiedpoint(command, points, 2);
							if (command.getSrcPile() == maxpoint
									&& command.getSrcPile() + dice.getFace(1) > command.getDestPile() + 23
									|| command.getSrcPile() + dice.getFace(1) == command.getDestPile() + 23) {
								isLegal = true;
								dice.decreaseNumMoves(1);
							}
							for (int i = 2; i <= dice.getStepVal(1) && !shouldBreak; i++)
								if (command.getSrcPile() == maxpoint
										&& command.getSrcPile() + dice.getFace(1) * i > command.getDestPile() + 23) {
									for (int j = 1; j <= i - 1
											&& command.getSrcPile() + dice.getFace(1) * j <= 23; j++) {
										isValidStep = points.get(command.getSrcPile() + dice.getFace(1) * j).empty()
												|| points.get(command.getSrcPile() + dice.getFace(1) * j).peek()
														.getCheckerTemplate() == players[2].getCheckerTemplate();
										validMove = validMove && isValidStep;
									}
									isValidStep = point.size() == 1 && isPointClear(command.getSrcPile(),
											command.getSrcPile() + dice.getFace(1) * (i - 1));
									validMove = validMove && isValidStep;
									if (validMove) {
										isLegal = true;
										for (int j = 0; j < i; j++)
											dice.decreaseNumMoves(1);
									}
									shouldBreak = true;
								} else if (command.getSrcPile() + dice.getFace(1) * i == command.getDestPile() + 23) {
									for (int j = 1; j <= i - 1; j++) {
										isValidStep = points.get(command.getSrcPile() + dice.getFace(1) * j).empty()
												|| points.get(command.getSrcPile() + dice.getFace(1) * j).peek()
														.getCheckerTemplate() == players[2].getCheckerTemplate();
										validMove = validMove && isValidStep;
									}
									if (validMove) {
										isLegal = true;
										for (int j = 0; j < i; j++)
											dice.decreaseNumMoves(1);
									}
								}
						}
					}
				}
		} else if (command.fromPointMove() && command.toPointMove()) {
			Stack<Checker> frompoint = points.get(command.getSrcPile());
			Stack<Checker> topoint = points.get(command.getDestPile());
			if (!frompoint.empty())
				if (bars.get(getPlayerNumber()).empty()
						&& frompoint.peek().getCheckerTemplate() == players[0].getCheckerTemplate()
						&& (topoint.empty() || topoint.size() == 1
								|| frompoint.peek().getCheckerTemplate() == topoint.peek().getCheckerTemplate())
						&& dice.getNumMoves() != 0) {
					if (dice.getFace(1) != dice.getFace(2)) {
						for (int i = 1; i <= 2; i++)
							if (dice.getStepVal(i) != 0 && (players[0] == players[1]
									&& command.getSrcPile() == command.getDestPile() + dice.getFace(i)
									|| players[0] == players[2]
											&& command.getSrcPile() + dice.getFace(i) == command.getDestPile())) {
								isLegal = true;
								dice.decreaseNumMoves(i);
							}
						if (dice.getStepVal(1) != 0 && dice.getStepVal(2) != 0
								&& (players[0] == players[1]
										&& command.getSrcPile() == command.getDestPile() + dice.getFace(1)
												+ dice.getFace(2)
										&& (points.get(command.getSrcPile() - dice.getFace(1)).empty()
												|| points.get(command.getSrcPile() - dice.getFace(2)).empty()
												|| points.get(command.getSrcPile() - dice.getFace(1)).peek()
														.getCheckerTemplate() == players[1].getCheckerTemplate()
												|| points.get(command.getSrcPile() - dice.getFace(2)).peek()
														.getCheckerTemplate() == players[1].getCheckerTemplate())
										|| players[0] == players[2]
												&& command.getSrcPile() + dice.getFace(1) + dice.getFace(2) == command
														.getDestPile()
												&& (points.get(command.getSrcPile() + dice.getFace(1)).empty()
														|| points.get(command.getSrcPile() + dice.getFace(2)).empty()
														|| points.get(command.getSrcPile() + dice.getFace(1)).peek()
																.getCheckerTemplate() == players[2].getCheckerTemplate()
														|| points.get(command.getSrcPile() + dice.getFace(2)).peek()
																.getCheckerTemplate() == players[2]
																		.getCheckerTemplate()))) {
							isLegal = true;
							dice.decreaseNumMoves(1);
							dice.decreaseNumMoves(2);
						}
					}
					if (dice.getFace(1) == dice.getFace(2) && dice.getStepVal(1) != 0) {
						boolean validMove = true;
						boolean isValidStep = true;
						if (players[0] == players[1]) {
							if (command.getSrcPile() == command.getDestPile() + dice.getFace(1)
									&& (points.get(command.getSrcPile() - dice.getFace(1)).empty()
											|| points.get(command.getSrcPile() - dice.getFace(1)).size() == 1
											|| points.get(command.getSrcPile() - dice.getFace(1)).peek()
													.getCheckerTemplate() == players[1].getCheckerTemplate())) {
								isLegal = true;
								dice.decreaseNumMoves(1);
							}
							for (int i = 2; i <= dice.getStepVal(1); i++) {
								if (command.getSrcPile() == command.getDestPile() + dice.getFace(1) * i) {
									for (int j = 1; j <= i - 1; j++) {
										isValidStep = points.get(command.getSrcPile() - dice.getFace(1) * j).empty()
												|| points.get(command.getSrcPile() - dice.getFace(1) * j).peek()
														.getCheckerTemplate() == players[1].getCheckerTemplate();
										validMove = validMove && isValidStep;
									}
									isValidStep = points.get(command.getSrcPile() - dice.getFace(1) * i).empty()
											|| points.get(command.getSrcPile() - dice.getFace(1) * i).peek()
													.getCheckerTemplate() == players[1].getCheckerTemplate()
											|| points.get(command.getSrcPile() - dice.getFace(1) * i).size() == 1;
									validMove = validMove && isValidStep;
									if (validMove) {
										isLegal = true;
										for (int j = 0; j < i; j++)
											dice.decreaseNumMoves(1);
									}
								}
							}
						}
						if (players[0] == players[2]) {
							if (command.getSrcPile() + dice.getFace(1) == command.getDestPile()
									&& (points.get(command.getSrcPile() + dice.getFace(1)).empty()
											|| points.get(command.getSrcPile() + dice.getFace(1)).size() == 1
											|| points.get(command.getSrcPile() + dice.getFace(1)).peek()
													.getCheckerTemplate() == players[2].getCheckerTemplate())) {
								isLegal = true;
								dice.decreaseNumMoves(1);
							}
							for (int i = 2; i <= dice.getStepVal(1); i++) {
								if (command.getSrcPile() + dice.getFace(1) * i == command.getDestPile()) {
									for (int j = 1; j <= i - 1; j++) {
										isValidStep = points.get(command.getSrcPile() + dice.getFace(1) * j).empty()
												|| points.get(command.getSrcPile() + dice.getFace(1) * j).peek()
														.getCheckerTemplate() == players[2].getCheckerTemplate();
										validMove = validMove && isValidStep;
									}
									isValidStep = points.get(command.getSrcPile() + dice.getFace(1) * i).empty()
											|| points.get(command.getSrcPile() + dice.getFace(1) * i).peek()
													.getCheckerTemplate() == players[2].getCheckerTemplate()
											|| points.get(command.getSrcPile() + dice.getFace(1) * i).size() == 1;
									validMove = validMove && isValidStep;
									if (validMove) {
										isLegal = true;
										for (int j = 0; j < i; j++)
											dice.decreaseNumMoves(1);
									}
								}
							}
						}
					}
				}
		}
		return isLegal;
	}

	// move a checker on the board given a command
	public void move(InputCheck command) {
		if (command.fromBarMove() && command.toPointMove()) {
			Stack<Checker> bar = bars.get(command.getSrcPile());
			Stack<Checker> point = points.get(command.getDestPile());
			if (point.size() == 1 && bar.peek().getCheckerTemplate() != point.peek().getCheckerTemplate()) {
				Checker barPiece = bar.pop();
				if (point.peek().getCheckerTemplate() == CheckerTemplate.WHITE) {
					Checker pointPiece = point.pop();
					bars.get(1).push(pointPiece);
				} else if (point.peek().getCheckerTemplate() == CheckerTemplate.RED) {
					Checker pointPiece = point.pop();
					bars.get(0).push(pointPiece);
				}
				point.push(barPiece);
			} else {
				Checker barPiece = bar.pop();
				point.push(barPiece);
			}
		} else if (command.fromPointMove() && command.toEndMove()) {
			Stack<Checker> point = points.get(command.getSrcPile());
			Stack<Checker> endpoint = endpoints.get(command.getDestPile());
			Checker pointPiece = point.pop();
			endpoint.push(pointPiece);
		} else if (command.fromPointMove() && command.toPointMove()) {
			Stack<Checker> frompoint = points.get(command.getSrcPile());
			Stack<Checker> topoint = points.get(command.getDestPile());
			if (topoint.size() == 1 && frompoint.peek().getCheckerTemplate() != topoint.peek().getCheckerTemplate()) {
				Checker frompointPiece = frompoint.pop();
				if (topoint.peek().getCheckerTemplate() == CheckerTemplate.WHITE) {
					Checker topointPiece = topoint.pop();
					bars.get(1).push(topointPiece);
				} else if (topoint.peek().getCheckerTemplate() == CheckerTemplate.RED) {
					Checker topointPiece = topoint.pop();
					bars.get(0).push(topointPiece);
				}
				topoint.push(frompointPiece);
			} else {
				Checker frompointPiece = frompoint.pop();
				topoint.push(frompointPiece);
			}
		}
	}

	
}
