package ru.jegensomme.interrupringmatcher;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

public class InterruptingMatcher {

    public static boolean matches(String text, String regex) {
        FutureTask<Boolean> futureTask = new FutureTask<>(() ->
                Pattern.compile(regex).matcher(new InterruptingCharSequence(text)).matches());
        new Thread(futureTask).start();
        try {
            return futureTask.get(2, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            futureTask.cancel(true);
            throw new IllegalStateException("Regex match error", e);
        }
    }

    private static class InterruptingCharSequence implements CharSequence {

        CharSequence inner;

        InterruptingCharSequence(CharSequence inner) {
            super();
            this.inner = inner;
        }

        @Override
        public char charAt(int index) {
            if (Thread.currentThread().isInterrupted()) {
                throw new RuntimeException("Interrupted!");
            }
            return inner.charAt(index);
        }

        @Override
        public int length() {
            return inner.length();
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return new InterruptingCharSequence(inner.subSequence(start, end));
        }

        @Override
        public String toString() {
            return inner.toString();
        }
    }
}
