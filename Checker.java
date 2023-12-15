// Checker class represents an individual game piece and its associated behaviors, 
// with a reference to its type through the CheckerTemplate enum.

public class Checker {
    private CheckerTemplate checkerTemp;

    Checker(CheckerTemplate checkerTemp){
        this.checkerTemp = checkerTemp;
    }

    public CheckerTemplate getCheckerTemplate(){
        return checkerTemp;
    }

    public String toString(){
        return checkerTemp.toString();
    }
}
