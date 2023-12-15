import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerTest {
    
    private Player player;
    
    // setting up a player "Anthony" with red checker before each test
    @BeforeEach 
    void setUp() throws Exception {
        player = new Player("Anthony", CheckerTemplate.RED);
    }
    
    // test to ensure that the player object is successfully created
    @Test 
    void testPlayerObject() {
        assertNotNull(player);
    }
    
    // test to check if the displayed name is correctly formatted with red color code
    @Test 
    void testDisplayName() {
        assertEquals("\033[0;31m" + "Anthony" + "\033[0m", player.dispName());
    }
    
    // test to check if the player's checker property is correctly set to RED
    @Test 
    void testGetCheckerTemplate() {
        assertEquals(CheckerTemplate.RED, player.getCheckerTemplate());
    }

    // test to check if the correct colour string is returned
    @Test 
    void testGetColourName() {
        assertEquals("R", player.getColourString());
    }
    
    // test to check if the initial pips value is correctly set to 167
    @Test 
    void testGetPips() {
        assertEquals(167, player.getPips());
    }
    
    // test to check set and get pips value 
    @Test 
    void testSetPips() {
        player.setPips(150);
        assertEquals(150, player.getPips());
    }
    
    // test to check if the initial score is correctly set to 0
    @Test 
    void testGetScore() {
        assertEquals(0, player.getScore());
    }
    
    // test to checkset,  add and get score
    @Test 
    void testSetAndAddScore() {
        player.setScore(10);
        player.addScore(5);
        assertEquals(15, player.getScore());
    }
    
    // test to check if the toString method returns the correct string format
    @Test 
    void testToString() {
        assertEquals("\033[0;31m" + "Anthony" + "\033[0m" + " {167} pips", player.toString());
    }
}
