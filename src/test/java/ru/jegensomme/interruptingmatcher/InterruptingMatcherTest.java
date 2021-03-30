package ru.jegensomme.interruptingmatcher;

import org.junit.Assert;
import org.junit.Test;
import ru.jegensomme.interrupringmatcher.InterruptingMatcher;

public class InterruptingMatcherTest {

    @Test
    public void matches() {
        Assert.assertThrows(IllegalStateException.class,
                () -> InterruptingMatcher.matches("FOOOOO_BAAAR_FOOOOOOOOO_BA_ ", "(([0-9A-Z]+)([_]?+)*)*"));
        Assert.assertTrue(InterruptingMatcher.matches("aaaaIntelliJ IDEAaaaa", ".*IntelliJ IDEA.*"));
    }
}