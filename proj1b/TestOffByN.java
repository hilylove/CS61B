import org.junit.Test;

import static org.junit.Assert.*;

public class TestOffByN {
    CharacterComparator offBy5 = new OffByN(5);

    @Test
    public void testEqualChars() {
        assertTrue(offBy5.equalChars('a', 'f'));
        assertTrue(offBy5.equalChars('#', '('));

        assertFalse(offBy5.equalChars('a', 'a'));
        assertFalse(offBy5.equalChars('a', 'A'));
    }
}
