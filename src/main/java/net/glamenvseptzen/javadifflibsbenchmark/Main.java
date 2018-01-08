package net.glamenvseptzen.javadifflibsbenchmark;

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

    static final int LOOP_PER_PATTERNS = 5;

    public static void doBenchmark() {
        SecureRandom sr = new SecureRandom();
        Random rand = new Random(sr.nextLong());
        BenchamrkDiffFamily bdf = new BenchamrkDiffFamily();

        System.out.println("p1");
        for (int i = 0; i < LOOP_PER_PATTERNS; i++) {
            RandomStringPair p = RandomStringPair.nextEmptyRandomPair(rand);
            bdf.benchmark("base:empty <> cmp:random", p);
            System.out.print(".");
        }
        System.out.println("");

        System.out.println("p2");
        for (int i = 0; i < LOOP_PER_PATTERNS; i++) {
            RandomStringPair p = RandomStringPair.nextRandomEmptyPair(rand);
            bdf.benchmark("base:random <> cmp:empty", p);
            System.out.print(".");
        }
        System.out.println("");

        System.out.println("p3");
        for (int i = 0; i < LOOP_PER_PATTERNS; i++) {
            RandomStringPair p = RandomStringPair.nextRandomPair(rand);
            bdf.benchmark("base:random <> cmp:random", p);
            System.out.print(".");
        }
        System.out.println("");

        System.out.println("p4");
        for (int i = 0; i < LOOP_PER_PATTERNS; i++) {
            RandomStringPair p = RandomStringPair.nextPrefixedRandomPair(rand);
            bdf.benchmark("base:random <> cmp:(base+)random", p);
            System.out.print(".");
        }
        System.out.println("");

        System.out.println("p5");
        for (int i = 0; i < LOOP_PER_PATTERNS; i++) {
            RandomStringPair p = RandomStringPair.nextRandomSuffixedPair(rand);
            bdf.benchmark("base:random <> cmp:random(+base)", p);
            System.out.print(".");
        }
        System.out.println("");

        System.out.println("p6");
        for (int i = 0; i < LOOP_PER_PATTERNS; i++) {
            RandomStringPair p = RandomStringPair.nextRandomPatchedPair(rand);
            bdf.benchmark("base:random <> cmp:(base x random patch)", p);
            System.out.print(".");
        }
        System.out.println("");

        bdf.printResults();
    }

    public static void main(String[] args) {
        System.out.println("[Benchmark Session1]");
        doBenchmark();
        System.out.println("[Benchmark Session2]");
        doBenchmark();
        System.out.println("[Benchmark Session3]");
        doBenchmark();
    }
}
