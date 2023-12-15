import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DiceTest {
    
    private Dice dice;

    // instantiate new dice object before each test
    @Before
    public void setUp() {
        dice = new Dice();
    }

    // testing constructor
    @Test
    public void testConstructor() {
        assertTrue("Initial face value should be between 1 and 6", dice.getFace(1) >= 1 && dice.getFace(1) <= 6);
        assertTrue("Initial face value should be between 1 and 6", dice.getFace(2) >= 1 && dice.getFace(2) <= 6);
    }

    // test roll
    @Test
    public void testRoll() {
        dice.roll();
        assertTrue("Face value after roll should be between 1 and 6", dice.getFace(1) >= 1 && dice.getFace(1) <= 6);
        assertTrue("Face value after roll should be between 1 and 6", dice.getFace(2) >= 1 && dice.getFace(2) <= 6);
    }

    // testing setting and getting face values
    @Test
    public void testSetAndGetFace() {
        dice.setFace(3, 4);
        assertEquals("First face should be set to 3", 3, dice.getFace(1));
        assertEquals("Second face should be set to 4", 4, dice.getFace(2));
    }

    // testing num moves for different faces
    @Test
    public void testNumMoves() {
        dice.setFace(2, 3);
        assertEquals("Num moves should be 2 for different faces", 2, dice.getNumMoves());
    }

    // testing num moves for same faces
    @Test
    public void testNumMovesForSameFaces() {
        dice.setFace(4, 4);
        assertEquals("Num moves should be 4 for same faces", 4, dice.getNumMoves());
    }

    // test decrementing num moves
    @Test
    public void testDecreaseNumMoves() {
        dice.setFace(2, 2);
        dice.decreaseNumMoves(1);
        assertEquals("Num moves should decrease by 1", 3, dice.getNumMoves());
    }

    // test setting dice to zero
    @Test
    public void testSetDiceZero() {
        dice.setDiceZero();
        assertEquals("Num moves should be 0 after setting dice to zero", 0, dice.getNumMoves());
        assertEquals("First face should be 0 after setting dice to zero", 0, dice.getFace(1));
        assertEquals("Second face should be 0 after setting dice to zero", 0, dice.getFace(2));
    }

    // testing setting step values
    @Test
    public void testSetAndGetStepVal() {
        dice.setStepVal(2, 3);
        assertEquals("Step value for first dice should be set correctly", 2, dice.getStepVal(1));
        assertEquals("Step value for second dice should be set correctly", 3, dice.getStepVal(2));
    }
    
    // testing total num moves after setting step values
    @Test
    public void testNumMovesAfterSettingStepVal() {
        dice.setStepVal(2, 3);
        assertEquals("Num moves should be the sum of both step values", 5, dice.getNumMoves());
    }
}