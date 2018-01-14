package net.glamenvseptzen.javadifflibsbenchmark;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;

public class Main {
    static final byte[] BYTES_00_FF;
    static {
        BYTES_00_FF = new byte[256];
        for (int i = 0; i <= 0xFF; i++) {
            BYTES_00_FF[i] = (byte) i;
        }
    }

    public static void doBenchmark(final int patternLoopCount, final File filesDiffBaseDir,
            final File filesDiffCmpDir) {
        SecureRandom sr = new SecureRandom();
        Random rand = new Random(sr.nextLong());
        BenchamrkDiffFamily bdf = new BenchamrkDiffFamily(filesDiffBaseDir, filesDiffCmpDir);

        System.out.println("p1");
        for (int i = 0; i < patternLoopCount; i++) {
            RandomStringPair p = RandomStringPair.nextEmptyRandomPair(rand);
            bdf.benchmark("base:empty <> cmp:random", p);
            System.out.print(".");
        }
        System.out.println("");

        System.out.println("p2");
        for (int i = 0; i < patternLoopCount; i++) {
            RandomStringPair p = RandomStringPair.nextRandomEmptyPair(rand);
            bdf.benchmark("base:random <> cmp:empty", p);
            System.out.print(".");
        }
        System.out.println("");

        System.out.println("p3");
        for (int i = 0; i < patternLoopCount; i++) {
            RandomStringPair p = RandomStringPair.nextRandomPair(rand);
            bdf.benchmark("base:random <> cmp:random", p);
            System.out.print(".");
        }
        System.out.println("");

        System.out.println("p4");
        for (int i = 0; i < patternLoopCount; i++) {
            RandomStringPair p = RandomStringPair.nextPrefixedRandomPair(rand);
            bdf.benchmark("base:random <> cmp:(base+)random", p);
            System.out.print(".");
        }
        System.out.println("");

        System.out.println("p5");
        for (int i = 0; i < patternLoopCount; i++) {
            RandomStringPair p = RandomStringPair.nextRandomSuffixedPair(rand);
            bdf.benchmark("base:random <> cmp:random(+base)", p);
            System.out.print(".");
        }
        System.out.println("");

        System.out.println("p6");
        for (int i = 0; i < patternLoopCount; i++) {
            RandomStringPair p = RandomStringPair.nextRandomPatchedPair(rand);
            bdf.benchmark("base:random <> cmp:(base x random patch)", p);
            System.out.print(".");
        }
        System.out.println("");

        System.out.println("files-diff");
        try {
            bdf.benchmarkFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }

        bdf.printResults();
    }

    public static void main(String[] args) {
        int sessionCount = 3;
        int patternLoopCount = 5;
        if (args.length == 0) {
            System.out.println("args: <session_count> <pattern_loop_count> <files_diff_base_dir> <files_diff_cmp_dir>");
        }
        if (args.length > 0) {
            sessionCount = Integer.parseInt(args[0]);
        }
        if (args.length > 1) {
            patternLoopCount = Integer.parseInt(args[1]);
        }
        File filesDiffBaseDir = null;
        File filesDiffCmpDir = null;
        if (args.length > 3) {
            filesDiffBaseDir = new File(args[2]);
            filesDiffCmpDir = new File(args[3]);
        }
        for (int i = 1; i <= sessionCount; i++) {
            System.out.println("[Benchmark Session" + i + "]");
            doBenchmark(patternLoopCount, filesDiffBaseDir, filesDiffCmpDir);
        }
    }
}
