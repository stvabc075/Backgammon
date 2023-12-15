// CheckerTemplate enum defines the constant types and properties of Backgammon pieces

public enum CheckerTemplate {

    WHITE("W", InterfaceColours.WHITE + " o" + InterfaceColours.RESET, InterfaceColours.WHITE),
    RED("R", InterfaceColours.RED + " o"+ InterfaceColours.RESET, InterfaceColours.RED);

    private String colour, symbol, display;

    // constructor
    CheckerTemplate(String colour, String symbol, String display) {
        this.colour = colour;
        this.symbol = symbol;
        this.display = display;
    }

    public String getColour() { // colour value
        return colour;
    }

    public String toString(){ // return string type of checker
        return symbol;
    }

    public String getDisplay(){ // display value of checker
        return display;
    }
}

