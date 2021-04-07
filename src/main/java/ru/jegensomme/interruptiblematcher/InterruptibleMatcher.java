package ru.jegensomme.interruptiblematcher;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

/**
 * This class provides static utility methods for matching text with a regular expression,
 * the execution time of which is limited by a timeout.
 */
public class InterruptibleMatcher {

    private static final long DEFAULT_TIME_OUT = 2;

    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

    /**
     * Tells whether or not text matches the given regular expression.
     *
     * @param text - the string to be matched
     * @param regex – the regular expression to which this string is to be matched
     * @return true if, and only if, the text matches the given regular expression
     * @throws RegexMatchException – if the match execution time exceeds the DEFAULT_TIME_OUT or another error occurred.
     */
    public static boolean matches(String text, String regex) {
        return matches(text, regex, DEFAULT_TIME_OUT, DEFAULT_TIME_UNIT);
    }

    /**
     * Tells whether or not text matches the given regular expression.
     *
     * @param text - the string to be matched
     * @param regex – the regular expression to which this string is to be matched
     * @param timeOut - the execution time after which the RegexMatchException will be thrown
     * @param timeUnit - the time unit of the timeout argument
     * @return true if, and only if, the text matches the given regular expression
     * @throws RegexMatchException – if the match execution time exceeds the timeOut or another error occurred.
     */
    public static boolean matches(String text, String regex, long timeOut, TimeUnit timeUnit) {
        FutureTask<Boolean> futureTask = new FutureTask<>(() ->
                Pattern.compile(regex).matcher(new InterruptibleCharSequence(text)).matches());
        new Thread(futureTask).start();
        try {
            return futureTask.get(timeOut, timeUnit);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            futureTask.cancel(true);
            throw new RegexMatchException(e);
        }
    }
}
