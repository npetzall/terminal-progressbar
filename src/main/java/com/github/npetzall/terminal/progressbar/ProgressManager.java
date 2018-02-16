package com.github.npetzall.terminal.progressbar;

import org.fusesource.jansi.Ansi;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import static org.fusesource.jansi.Ansi.ansi;

public class ProgressManager {

    private static final ProgressManager progressManager = new ProgressManager();

    public static ProgressManager getInstance() {
        return progressManager;
    }

    private ProgressManager() {}

    private List<ProgressLine> progressLineList = new LinkedList<>();

    public ProgressLine newProgressLine(String label) {
        return new ProgressLine(label, this);
    }

    public void starting(ProgressLine progressLine) {
        progressLineList.add(progressLine);
    }

    public void finished(ProgressLine progressLine) {
        progressLineList.remove(progressLine);
    }

    public synchronized void render(PrintStream ps){
        if(progressLineList.isEmpty()) {
            return;
        }
        Ansi ansi = ansi().eraseLine().a("Running tasks:").newline();
        for(int i = 0; i< progressLineList.size(); i++){
            ansi.eraseLine().cursorRight(4).a(progressLineList.get(i).toString()).newline();
        }
        ansi.cursorUpLine(progressLineList.size() + 1).cursorToColumn(0);
        ps.print(ansi);
    }

}
