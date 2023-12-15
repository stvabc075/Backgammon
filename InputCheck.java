//  Interprets and processes user inputs for game commands, manages dice face values, and tracks legal moves for a board game.
public class InputCheck {
    public enum gameCommand{
        MOVE,
        ROLL,
        QUIT,
        START,
        PIP,
        HINT,
        SHOWLEGALMOVES,
        SETDICE,
        FORFEIT,
        SKIP
    };

    private gameCommand command; // all possible commands
    private int[] faces;
    private String srcPile, destPile;
    private static String[] legalMoves = new String[100];
    private String[] dice;

    // parse user's input and assigning values to appropiate variable
    InputCheck(String input){
        this.faces = new int[2];
        this.dice = new String[2];
        String inputUpper = input.toUpperCase().trim(); // ensure case-insensitivity
        if (inputUpper.equals("ROLL")) {
            command = gameCommand.ROLL;
        }
        else if (inputUpper.equals("QUIT")) {
            command = gameCommand.QUIT;
        }
        else if (inputUpper.equals("START")) {
            command = gameCommand.START;
        }
        else if (inputUpper.equals("PIP")) {
            command = gameCommand.PIP;
        }
        else if (inputUpper.equals("HINT")) {
            command = gameCommand.HINT;
        }
        else if (inputUpper.equals("MOVES")) {
			command = gameCommand.SHOWLEGALMOVES;
		}
        else if (inputUpper.equals("FORFEIT")) {
            command = gameCommand.FORFEIT;
        }
        else if (inputUpper.equals("SKIP")) {
            command = gameCommand.SKIP;
        }
        else if (inputUpper.matches("([1-9]|0[1-9]|[1-9][0-9])") && legalMoves[Integer.parseInt(inputUpper) - 1] != null){
            command = gameCommand.MOVE;
            srcPile = legalMoves[Integer.parseInt(inputUpper) - 1].substring(0,2);
            destPile = legalMoves[Integer.parseInt(inputUpper) - 1].substring(2,4); 
        }
        else if (inputUpper.matches("(0[1-9]|1[0-9]|2[0-4]|B[1-2])(0[1-9]|1[0-9]|2[0-4]|E[1-2])")) {
			command = gameCommand.MOVE;
			srcPile = inputUpper.substring(0, 2);
			destPile = inputUpper.substring(2, 4);
        }
        else if (inputUpper.matches("ROLL[1-6][1-6]")) {
			command = gameCommand.SETDICE;
			dice[0] = inputUpper.substring(4, 5);
			dice[1] = inputUpper.substring(5, 6);
			faces[0] = Integer.parseInt(dice[0]);
			faces[1] = Integer.parseInt(dice[1]);
        }
    }
    
    /** 
     * @param input
     * @return boolean
     */
    // check if input is valid move/command
    public static boolean validMove(String input){
        String inputUpper = input.toUpperCase().trim();
        return (inputUpper.equals("QUIT")) || (inputUpper.equals("ROLL")) || (inputUpper.equals("FORFEIT"))
                || (inputUpper.equals("START")) || (inputUpper.equals("PIP")) || (inputUpper.equals("HINT")) 
                || (inputUpper.equals("MOVES")) || inputUpper.matches("ROLL[1-6][1-6]") || (inputUpper.equals("SKIP"))
                || inputUpper.matches("(0[1-9]|1[0-9]|2[0-4]|B[1-2])(0[1-9]|1[0-9]|2[0-4]|E[1-2])") 
                || input.matches("([1-9]|0[1-9]|[1-9][0-9])") && legalMoves[Integer.parseInt(input) - 1] != null;
    }

    
    /** 
     * @return boolean
     */
    // checking source of move
    public boolean fromBarMove(){
        return srcPile.matches("B1|B2");
    }
    
    /** 
     * @return boolean
     */
    public boolean fromPointMove(){
        return srcPile.matches("0[1-9]|1[0-9]|2[0-4]");
    }

    
    /** 
     * @return boolean
     */
    // checking destination of move
    public boolean toEndMove(){
        return destPile.matches("E1|E2");
    }
    
    /** 
     * @return boolean
     */
    public boolean toPointMove(){
        return destPile.matches("0[1-9]|1[0-9]|2[0-4]");
    }

    
    /** 
     * @param bar
     * @return int
     */
    // convert bar and endpoints to appropriate index
    private int barToInt(String bar){
        return switch(bar){
        case "B1" -> 0;
        case "B2" -> 1;
        default -> 0;
        };
    }
    
    /** 
     * @param endpoint
     * @return int
     */
    private int endToInt(String endpoint){
        return switch(endpoint){
        case "E1" -> 0;
        case "E2" -> 1;
        default -> 0;
        };
    }

    
    /** 
     * @param input
     * @return boolean
     */
    // check the input string is a text file   
    public static boolean text (String input) { 
		String inputFormatted = input.trim();
		return inputFormatted.matches("file:(.+\\.txt)");
	}

    /** 
     * @param input
     * @return String
     */
    // recieves the text from the input string if it's a text file
    public static String recText(String input) { 
    String inputFormatted = input.trim();
    if (inputFormatted.length() > 5) {
        return inputFormatted.substring(5);
    } else 
        return ""; 
}

    /** 
     * @return boolean
     */
    // boolean methods to check the user's command
    public boolean move(){
        return command == gameCommand.MOVE;
    }
    public boolean roll(){
        return command == gameCommand.ROLL;
    }
    public boolean quit(){
        return command == gameCommand.QUIT;
    }
    public boolean start(){
        return command == gameCommand.START;
    }
    public boolean showPip(){
        return command == gameCommand.PIP;
    }
    public boolean showHint(){
        return command == gameCommand.HINT;
    }
    public boolean showLegalMoves(){
        return command == gameCommand.SHOWLEGALMOVES;
    }
    public boolean setDice () { 
		return command == gameCommand.SETDICE;
	}
    public boolean skip(){
        return command == gameCommand.SKIP;
    }
    public boolean forfeit(){
        return command == gameCommand.FORFEIT;
    }

    /** 
     * @param input
     * @return String
     */
    public boolean checkText(String input){
        String trimmed = input.trim();
        return trimmed.matches("test:(.+\\.txt)");
    }

    // return the index of the source pile
    public int getSrcPile() {
        if(fromPointMove()){
            return Integer.parseInt(srcPile) - 1;
        }
        else{
            return barToInt(srcPile);
        }
    }

    // return index of destination pile
    public int getDestPile(){
        if (toPointMove()){
            return Integer.parseInt(destPile) - 1;
        }
        else{
            return endToInt(destPile);
        }
    }

    /** 
     * @param index
     * @return int
     */
    // returns the  face value 
    public int getFace (int index) { 
		return switch (index) {
			case 1 -> faces[0];
			case 2 -> faces[1];
			default -> 0;
		};
	}

    /** 
     * @param in
     * @param legalMove
     */
    // adds all the legal moves at an index to array
    public static void setLegalMoves (int i, String legalMove) { 
		legalMoves[i] = legalMove;
	}
	
    // returns array of legal moves
	public static String[] getLegalMoves () { 
		return legalMoves;
	}

    // METHODS FOR UNIT TESTING
    public gameCommand getCommand () { 
	    return command;
	}
}
