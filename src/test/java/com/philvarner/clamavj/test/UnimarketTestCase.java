package com.philvarner.clamavj.test;

import com.philvarner.clamavj.ClamScan;
import com.philvarner.clamavj.unimarket.DefaultVirusScanner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
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

    @BeforeClass
    public static void beforeClass() {
        System.out.println("-------------------------------------");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("-------------------------------------");
    }

    @Test
    public void testWithDaemon() throws FileNotFoundException {
        System.out.println("Scanning with clamd " + file);
        long start = System.currentTimeMillis();


        scanner.scan(new FileInputStream(file));


        long end = System.currentTimeMillis();
        System.out.println("time = " + (end - start));

    }


    @Test
    public void testWithExec() throws IOException {
        System.out.println("Scanning with exec " + file);
        long start = System.currentTimeMillis();


        DefaultVirusScanner s = new DefaultVirusScanner(file, true, executorService);
        s.scan();


        long end = System.currentTimeMillis();
        System.out.println("time = " + (end - start));
    }
}
