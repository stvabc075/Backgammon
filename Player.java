// Stores properties and methods for a game player, including name, score, pips, and a "CheckerTemplate" for display customization
public class Player {
    private String name;
    private int score, pips;
    private CheckerTemplate checkerTemp;

    Player(String name, CheckerTemplate checkerTemp){
        this.name = name;
        this.checkerTemp = checkerTemp;
        this.score = 0;
        this.pips = 167;
    }

    public String dispName(){
        return checkerTemp.getDisplay() + name + InterfaceColours.RESET;
    }

    // getter functions
    public CheckerTemplate getCheckerTemp(){
        return checkerTemp;
    }
    public String getColourString(){
        return checkerTemp.getColour();
    }
    public int getScore () { 
        return score;
    } 
    public CheckerTemplate getCheckerTemplate () {
        return checkerTemp;
    }
    public int getPips(){ 
        return pips;
    }
    // setter functions
    public void setPips (int pips) {
        this.pips = pips;
    }
    public void setScore(int score){
        this.score = score;
    }

    public void addScore(int score){
        this.score += score;
    }
    public String toString () {
        return checkerTemp.getDisplay() + name + InterfaceColours.RESET + " {" + this.pips + "} pips";
    }
}
