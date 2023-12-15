import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CheckerTest {

    // testing to check white checker creation
    @Test
    public void testCheckerWithWhiteTemplate() {
        CheckerTemplate whiteTemplate = CheckerTemplate.WHITE;
        Checker checker = new Checker(whiteTemplate);
        assertEquals(whiteTemplate, checker.getCheckerTemplate());
        assertEquals(whiteTemplate.toString(), checker.toString());
    }

    // testing to check red checker creation
    @Test
    public void testCheckerWithRedTemplate() {
        CheckerTemplate redTemplate = CheckerTemplate.RED;
        Checker checker = new Checker(redTemplate);
        assertEquals(redTemplate, checker.getCheckerTemplate());
        assertEquals(redTemplate.toString(), checker.toString());
    }
}
