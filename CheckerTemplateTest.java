import org.junit.Test;
import static org.junit.Assert.*;

public class CheckerTemplateTest {

    // test creating white checker
    @Test
    public void testWhiteChecker() {
        CheckerTemplate whiteChecker = CheckerTemplate.WHITE;
        assertEquals("W", whiteChecker.getColour());
        assertEquals(InterfaceColours.WHITE + " o" + InterfaceColours.RESET, whiteChecker.toString());
        assertEquals(InterfaceColours.WHITE, whiteChecker.getDisplay());
    }

    // test creating red checker
    @Test
    public void testRedChecker() {
        CheckerTemplate redChecker = CheckerTemplate.RED;
        assertEquals("R", redChecker.getColour());
        assertEquals(InterfaceColours.RED + " o" + InterfaceColours.RESET, redChecker.toString());
        assertEquals(InterfaceColours.RED, redChecker.getDisplay());
    }
 
    // testing methods
    @Test
    public void testGetColour() {
        assertEquals("W", CheckerTemplate.WHITE.getColour());
        assertEquals("R", CheckerTemplate.RED.getColour());
    }
    @Test
    public void testToString() {
        assertEquals(InterfaceColours.WHITE + " o" + InterfaceColours.RESET, CheckerTemplate.WHITE.toString());
        assertEquals(InterfaceColours.RED + " o" + InterfaceColours.RESET, CheckerTemplate.RED.toString());
    }
    @Test
    public void testGetDisplay() {
        assertEquals(InterfaceColours.WHITE, CheckerTemplate.WHITE.getDisplay());
        assertEquals(InterfaceColours.RED, CheckerTemplate.RED.getDisplay());
    }
}
