package ru.jegensomme.interruptingmatcher;

import org.junit.Assert;
import org.junit.Test;
import ru.jegensomme.interruptiblematcher.InterruptibleMatcher;
import ru.jegensomme.interruptiblematcher.RegexMatchException;

import java.util.concurrent.TimeUnit;

public class InterruptibleMatcherTest {

    @Test
    public void matches() {
        Assert.assertThrows(RegexMatchException.class,
                () -> InterruptibleMatcher.matches("FOOOOO_BAAAR_FOOOOOOOOO_BA_ ", "(([0-9A-Z]+)([_]?+)*)*"));
        Assert.assertThrows(RegexMatchException.class,
                () -> InterruptibleMatcher.matches("a().characteristics.characteristics.characteristics;",
                        "(?<assignationtype>[^=(new|throw new)]\\w+\\s+)?(?:(?<assignationTargets>(?<assignationtarget>\\w+\\.)*)(?<assignation>\\w+(?:\\[\\w+\\])?\\s*(?:=|:\\*=)\\s*))?(?<stereotype>new|throw new)?\\s?(?<targets>(?<target>[^\\.]+\\.)*)(?<name>[_\\w]+)\\((?<arguments>(?<argument>[^,]+,?)*)\\);",
                        5, TimeUnit.SECONDS));
        Assert.assertTrue(InterruptibleMatcher.matches("aaaaIntelliJ IDEAaaaa", ".*IntelliJ IDEA.*"));
    }
}