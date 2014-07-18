package com.philvarner.clamavj.test;

import com.philvarner.clamavj.ClamScan;
import com.philvarner.clamavj.ScanResult;
import com.philvarner.clamavj.unimarket.DefaultVirusScanner;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author oliver.henlich
 */
public class UnimarketTestCase {
    private static ClamScan scanner = new ClamScan("localhost", 3310, 60000);
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

    private File bad = new File("src/test/resources/bad");
    private File good = new File("src/test/resources/good");

//    private File file = new File("src/test/resources/binaryfilevirus");

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
        System.out.println("Scanning with clamd " + bad);
        long start = System.currentTimeMillis();


        ScanResult scan = scanner.scan(new FileInputStream(bad));

        long end = System.currentTimeMillis();
        System.out.println("time = " + (end - start));
        System.out.println("scan.getStatus() = " + scan.getStatus());
    }


    @Test
    public void testWithExec() throws IOException {
        System.out.println("Scanning with exec " + bad);
        long start = System.currentTimeMillis();


        DefaultVirusScanner s = new DefaultVirusScanner(bad, true, executorService);
        boolean scan = s.scan();


        long end = System.currentTimeMillis();
        System.out.println("time = " + (end - start));
        System.out.println("scan = " + scan);
    }


    @Test
    public void testWithDaemonMultipleThreads() throws FileNotFoundException, InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();


        Map<Future, File> results = new HashMap();
        for (int i = 0; i < 100; i++) {
            final File file = i % 2 == 0 ? good : bad;
            System.out.println("adding " + file);
            results.put(
                    fixedThreadPool.submit(new Callable<ScanResult>() {
                        @Override
                        public ScanResult call() throws Exception {
                            return scanner.scan(new FileInputStream(file));
                        }

                    }),
                    file);
        }
        System.out.println("results = " + results);

        fixedThreadPool.awaitTermination(30, TimeUnit.SECONDS);

        System.out.println("results = " + results);
        for (Map.Entry<Future, File> entry : results.entrySet()) {
            File file = entry.getValue();
            ScanResult.Status expected = file.getName().endsWith("bad") ? ScanResult.Status.FAILED : ScanResult.Status.PASSED;
            ScanResult.Status actual = ((ScanResult) entry.getKey().get()).getStatus();
            System.out.println("file = " + file);
            System.out.println("expected = " + expected);
            System.out.println("actual = " + actual);
            Assert.assertEquals(expected, actual);
        }



        long end = System.currentTimeMillis();
        System.out.println("time = "+(end-start));
    }


}
