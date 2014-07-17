package com.philvarner.clamavj.test;

import com.philvarner.clamavj.ClamScan;
import com.philvarner.clamavj.unimarket.DefaultVirusScanner;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author oliver.henlich
 */
public class UnimarketTestCase {
    private static ClamScan scanner = new ClamScan("localhost", 3310, 60000);
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    private File file = new File("src/test/resources/eicar.txt");

    @Test
    public void testWithDaemon() throws FileNotFoundException {
        long start = System.currentTimeMillis();


        scanner.scan(new FileInputStream(file));


        long end = System.currentTimeMillis();
        System.out.println("time = " + (end - start));

    }


    @Test
    public void testWithExec() throws IOException {
        long start = System.currentTimeMillis();


        DefaultVirusScanner s = new DefaultVirusScanner(file, true, executorService);
        s.scan();


        long end = System.currentTimeMillis();
        System.out.println("time = " + (end - start));
    }
}
