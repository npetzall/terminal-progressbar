package com.github.npetzall.terminal.progressbar;

public interface ProgressListener {
    void starting(int tasks);
    void finishedTask();
    void finished();
}
