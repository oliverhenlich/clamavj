package com.philvarner.clamavj.unimarket;


import java.io.File;
import java.util.concurrent.ExecutorService;

/**
 * @author oliver.henlich
 */
public abstract class AbstractVirusScanner implements VirusScanner {
    protected final File file;
    protected final boolean failOnProblem;
    protected final ExecutorService executorService;

    public AbstractVirusScanner(boolean failOnProblem, File file, ExecutorService executorService) {
        this.failOnProblem = failOnProblem;
        this.file = file;
        this.executorService = executorService;
    }
}
