package com.github.npetzall.terminal.progressbar;

import org.fusesource.jansi.AnsiConsole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintStream;

public class ProgressAwareOutputStream extends PrintStream {

    private static final PrintStream printStream = AnsiConsole.out();
    private static final ProgressManager progressManager = ProgressManager.getInstance();

    private static final ProgressAwareOutputStream progressAwareOutputStream = new ProgressAwareOutputStream();

    private final Thread progressUpdater = new Thread(() -> {
        Logger log = LoggerFactory.getLogger("ProgressUpdater");
        while(true) {
                progressManager.render(printStream);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                log.debug("interrupted while sleeping");
            }
        }
    }, "ConsoleProgressUpdater");

    public static void install() {
        System.setOut(progressAwareOutputStream);
        progressAwareOutputStream.startUpdater();
    }

    private ProgressAwareOutputStream() {
        super(printStream, true);
    }

    private void startUpdater() {
        progressUpdater.setDaemon(true);
        progressUpdater.start();
    }

    @Override
    public void write(int b) {
        synchronized (this) {
            super.write(b);
            if ((b == '\n')) {
                progressManager.render(printStream);
            }
        }
    }

    @Override
    public void write(byte[] buf, int off, int len) {
        synchronized (this) {
            super.write(buf, off, len);
            progressManager.render(printStream);
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        synchronized (this) {
            super.write(b);
            progressManager.render(printStream);
        }
    }
}
