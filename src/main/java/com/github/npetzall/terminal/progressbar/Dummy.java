package com.github.npetzall.terminal.progressbar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class Dummy {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void main(String[] args) throws InterruptedException {
        ProgressAwareOutputStream.install();
        ProgressLine evens = ProgressManager.getInstance().newProgressLine("even");
        ProgressLine odds = ProgressManager.getInstance().newProgressLine("odds");
        evens.starting(50);
        odds.starting(50);
        for (int i=1; i<= 100; i++) {
            if (i%5==0)
                LOG.info("Info Message #"+i);
            if (i%2 == 0)
                evens.finishedTask();
            else
                odds.finishedTask();
            Thread.sleep(50);
        }
    }
}
