package com.github.npetzall.terminal.progressbar;

import ch.qos.logback.core.OutputStreamAppender;
import org.fusesource.jansi.AnsiConsole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.lang.invoke.MethodHandles;

public class ProgressAwareConsoleAppender<E> extends OutputStreamAppender<E> {

    private final static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ProgressManager progressManager = ProgressManager.getInstance();
    private final PrintStream printStream = AnsiConsole.out();
    private final Thread progressUpdater = new Thread(() -> {
      while(isStarted()) {
          lock.lock();
          try {
              progressManager.render(printStream);
          } finally {
              lock.unlock();
          }
          try {
              Thread.sleep(500);
          } catch (InterruptedException e) {
              LOG.debug("progressUpdater interrupted while sleeping");
          }
      }
    }, "ConsoleProgressUpdater");

    @Override
    public void start() {
        setOutputStream(printStream);
        super.start();
        progressUpdater.setDaemon(true);
        progressUpdater.start();
    }

    @Override
    protected void subAppend(E event) {
        if (!isStarted()) {
            return;
        }
        super.subAppend(event);
        lock.lock();
        try {
            progressManager.render(printStream);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void stop() {
        super.stop();
        progressUpdater.interrupt();
    }
}
