// Manages dice rolls, move counts, and interaction with a game interface.
import java.util.Random;

public class Dice {
    private int numMoves; // number of moves a player can make
    private int[] faces, numSteps;
    private Random rand;
    private Interface intrface;

    Dice(){
        rand = new Random();
        intrface = new Interface();
        this.faces = new int[2];
        this.numSteps = new int[2];

        numSteps[0] = 1;
        numSteps[1] = 1;

        numMoves = numSteps[0] + numSteps[1];

        faces[0] = rand.nextInt(1, 7);
        faces[1] = rand.nextInt(1, 7);
        if (faces[0] == faces[1]) {
        	numSteps[0] = 4;
        	numSteps[1] = 0;
        	numMoves = numSteps[0] + numSteps[1];
        }
    }

    // rolls the dice - updatwa face and move values 
    public void roll () { 
    	numSteps[0] = 1;
    	numSteps[1] = 1;

    	numMoves = numSteps[0] + numSteps[1];

    	faces[0] = rand.nextInt(1, 7);
        faces[1] = rand.nextInt(1, 7);
        if (faces[0] == faces[1]) {
        	numSteps[0] = 4;
        	numSteps[1] = 0;
        	numMoves = numSteps[0] + numSteps[1];
        }
    }

    
    /** 
     * @param f1
     * @param f2
     */
    // specifiy face values of the dice and updates number of moves
    public void setFace (int f1, int f2) { 
    	numSteps[0] = 1;
    	numSteps[1] = 1;
    	numMoves = numSteps[0] + numSteps[1];
        faces[0] = f1;
        faces[1] = f2;
        if (faces[0] == faces[1]) {
        	numSteps[0] = 4;
        	numSteps[1] = 0;
        	numMoves = numSteps[0] + numSteps[1];
        }
        intrface.printDice(f1, f2);
    }

    
    /** 
     * @param index
     * @return int
     */
    // face on dice(index)
    public int getFace (int index) { 
    	return switch (index) {
			case 1 -> faces[0];
			case 2 -> faces[1];
			default -> 0;
		};
    }

    
    /** 
     * @param firstStep
     * @param secondStep
     */
    // sets move step values and updates the total move number
    public void setStepVal (int firstStep, int secondStep) { 
    	numSteps[0] = firstStep;
    	numSteps[1] = secondStep;
    	numMoves = numSteps[0] + numSteps[1];
    }

    
    /** 
     * @param index
     * @return int
     */
    // returns the step value for the specified index
    public int getStepVal (int index) {
    	return switch (index) {
			case 1 -> numSteps[0];
			case 2 -> numSteps[1];
			default -> 0;
		};
    }

    
    /** 
     * @param i
     */
    public void decreaseNumMoves (int i) { 
    	switch (i) {
			case 1 -> numSteps[0]--;
			case 2 -> numSteps[1]--;
		};
		numMoves = numSteps[0] + numSteps[1];
    }

    
    /** 
     * @return int
     */
    // getter to return total number of moves
    public int getNumMoves(){
        return numMoves;
    }

    // set all dice to 0
    public void setDiceZero(){
        numSteps[0] = 0;
        numSteps[1] = 0;

        numMoves = numSteps[0] + numSteps[1];

        faces[0] = 0;
        faces[1] = 0;
    }
}

