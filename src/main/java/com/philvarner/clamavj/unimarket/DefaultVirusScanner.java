package com.philvarner.clamavj.unimarket;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;

/**
 * @author oliver.henlich
 */
public class DefaultVirusScanner extends AbstractVirusScanner {

    private static final String CLAMAV_EXECUTABLE = "/usr/bin/clamscan";
    private static final int SCAN_TIMEOUT = 60;

    public DefaultVirusScanner(File file, boolean failOnProblem, ExecutorService executorService) {
        super(failOnProblem, file, executorService);
    }


    @Override
    public boolean scan() throws IOException {

        final String canonicalPath = file.getCanonicalPath();
        if (!file.exists()) {
            throw new IOException("File does not exist " + canonicalPath);
        }

        Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ProcessBuilder pb = new ProcessBuilder(CLAMAV_EXECUTABLE, canonicalPath);
                Process start = pb.start();
                start.waitFor();
                int exitValue = start.exitValue();
                if (exitValue == 2) {
                    handleProblem("Problem performing clamscan on", canonicalPath);
                }
                // if we have been instructed not to fail when there is a problem don't check result and return true
                return !failOnProblem || (0 == exitValue);
            }
        };

        try {
            return executorService.submit(callable).get(SCAN_TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            handleProblem("Scanning was interrupted", canonicalPath);
        } catch (ExecutionException e) {
            handleProblem("Problem performing scan of", canonicalPath);
        } catch (TimeoutException e) {
            handleProblem("Giving up on scan because it took too long", canonicalPath);
        }
        return !failOnProblem;
    }

    private void handleProblem(String message, String arguments) {
        if (failOnProblem) {
            throw new RuntimeException(message + " " + arguments);
        } else {
            System.err.println(message + " "+ arguments);
        }
    }
}


