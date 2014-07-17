package com.philvarner.clamavj.unimarket;

import java.io.IOException;

/**
 * @author oliver.henlich
 */
public interface VirusScanner {
    /**
     * Returns true if the scanner has found no problems.
     *
     * @return true if the scanner has found no problems.
     * @throws java.io.IOException if there is a problem reading the file to scan.
     * @throws RuntimeException    on any other scanning problem.
     */
    boolean scan() throws IOException;
}
