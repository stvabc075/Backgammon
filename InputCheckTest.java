import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class InputCheckTest {
    private InputCheck cmd;

	@BeforeEach
	void setUp() throws Exception {
		cmd = null;
	}

    // test object creation 
	@Test 
	void testcmd() {
		cmd = new InputCheck("SKIP");
		assertEquals(InputCheck.gameCommand.SKIP, cmd.getCommand());
	}

    // testing if inputs are valid
	@Test 
	void testValidMove() {
		assertTrue(InputCheck.validMove("ROLL"));
		assertTrue(InputCheck.validMove("QUIT"));
		assertTrue(InputCheck.validMove("START"));
		assertTrue(InputCheck.validMove("SKIP"));
		assertTrue(InputCheck.validMove("PIP"));
		assertTrue(InputCheck.validMove("HINT"));
		assertTrue(InputCheck.validMove("MOVES"));
		assertTrue(InputCheck.validMove("FORFEIT"));
		assertTrue(InputCheck.validMove("ROLL11"));
		assertTrue(InputCheck.validMove("0109"));
		assertFalse(InputCheck.validMove("Invalid"));
	}

    // testing boolean methods
	@Test 
	void testQuit() {
		cmd = new InputCheck("QUIT");
		assertTrue(cmd.quit());
	}
	@Test 
	void testRoll() {
		cmd = new InputCheck("ROLL");
		assertTrue(cmd.roll());
	}
	@Test 
	void testStart() {
		cmd = new InputCheck("START");
		assertTrue(cmd.start());
	}
	@Test 
	void testPips() {
		cmd = new InputCheck("PIP");
		assertTrue(cmd.showPip());
	}
	@Test 
	void testSkip() {
		cmd = new InputCheck("SKIP");
		assertTrue(cmd.skip());
	}
	@Test
	void testShowHint() {
		cmd = new InputCheck("HINT");
		assertTrue(cmd.showHint());
	}
	@Test
	void testLegalMoves() {
		cmd = new InputCheck("MOVES");
		assertTrue(cmd.showLegalMoves());
	}
	@Test 
	void testForfeit() {
		cmd = new InputCheck("FORFEIT");
		assertTrue(cmd.forfeit());
	}
	@Test 
	void testSetDice() {
		cmd = new InputCheck("ROLL11");
		assertTrue(cmd.setDice());
	}
	@Test 
	void testMove() {
		cmd = new InputCheck("0109");
		assertTrue(cmd.move());
	}

    // testing moving from bars, lanes, endpoints
	@Test 
	void testMoveFromBar() {
		cmd = new InputCheck("B102");
		assertTrue(cmd.fromBarMove());
	}
	@Test 
	void testMoveFromLane() {
		cmd = new InputCheck("0109");
		assertTrue(cmd.fromPointMove());
	}
	@Test 
	void testMoveToLane() {
		cmd = new InputCheck("0109");
		assertTrue(cmd.toPointMove());
	}
	@Test 
	void testMoveToEndpoint() {
		cmd = new InputCheck("01E1");
		assertTrue(cmd.toEndMove());
	}

    // testing source and destination index
	@Test
	void testSrcPile() {
		cmd = new InputCheck("0109");
		assertEquals(0, cmd.getSrcPile());
	}
	@Test 
	void testDestPile() {
		cmd = new InputCheck("0109");
		assertEquals(8, cmd.getDestPile());
	}
}
