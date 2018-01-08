package net.glamenvseptzen.javadifflibsbenchmark;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomStringPair {
    private static final int RAND_MAX_LEN = 2048;
    public final String base;
    public final String cmp;

    private RandomStringPair(final String base, final String cmp) {
        this.base = base;
        this.cmp = cmp;
    }

    public List<Character> strToCharList(final String src) {
        List<Character> r = new ArrayList<>(src.length());
        for (char c : src.toCharArray()) {
            r.add(c);
        }
        return r;
    }

    @Override
    public String toString() {
        return "base[" + base + "]\ncmp[" + cmp + "]\n";
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        SecureRandom sr = new SecureRandom();
        Random rand = new Random(sr.nextLong());
        for (int i = 0; i < 5; i++) {
            RandomStringPair p = RandomStringPair.nextEmptyRandomPair(rand);
            System.out.println(p);
        }
        System.out.println("--------------------------");
        for (int i = 0; i < 5; i++) {
            RandomStringPair p = RandomStringPair.nextRandomEmptyPair(rand);
            System.out.println(p);
        }
        System.out.println("--------------------------");
        for (int i = 0; i < 5; i++) {
            RandomStringPair p = RandomStringPair.nextRandomPair(rand);
            System.out.println(p);
        }
        System.out.println("--------------------------");
        for (int i = 0; i < 5; i++) {
            RandomStringPair p = RandomStringPair.nextPrefixedRandomPair(rand);
            System.out.println(p);
        }

        System.out.println("--------------------------");
        for (int i = 0; i < 5; i++) {
            RandomStringPair p = RandomStringPair.nextRandomSuffixedPair(rand);
            System.out.println(p);
        }

        System.out.println("--------------------------");
        HexDumper hd = new HexDumper();
        for (int i = 0; i < 5; i++) {
            RandomStringPair p = RandomStringPair.nextRandomPatchedPair(rand);
            System.out.println(hd.dump(p.base.getBytes(StandardCharsets.ISO_8859_1)));
            System.out.println(hd.dump(p.cmp.getBytes(StandardCharsets.ISO_8859_1)));
        }
    }

    public static RandomStringPair nextEmptyRandomPair(Random rand) {
        int len2 = rand.nextInt(RAND_MAX_LEN) + 1;
        byte[] cmpBytes = new byte[len2];
        rand.nextBytes(cmpBytes);
        return new RandomStringPair("", new String(cmpBytes, StandardCharsets.ISO_8859_1));
    }

    public static RandomStringPair nextRandomEmptyPair(Random rand) {
        int len1 = rand.nextInt(RAND_MAX_LEN) + 1;
        byte[] baseBytes = new byte[len1];
        rand.nextBytes(baseBytes);
        return new RandomStringPair(new String(baseBytes, StandardCharsets.ISO_8859_1), "");
    }

    public static RandomStringPair nextRandomPair(Random rand) {
        int len1 = rand.nextInt(RAND_MAX_LEN) + 1;
        int len2 = rand.nextInt(RAND_MAX_LEN) + 1;
        byte[] baseBytes = new byte[len1];
        byte[] cmpBytes = new byte[len2];
        rand.nextBytes(baseBytes);
        rand.nextBytes(cmpBytes);
        return new RandomStringPair(new String(baseBytes, StandardCharsets.ISO_8859_1),
                new String(cmpBytes, StandardCharsets.ISO_8859_1));
    }

    public static RandomStringPair nextPrefixedRandomPair(Random rand) {
        int len1 = rand.nextInt(RAND_MAX_LEN) + 1;
        int len2 = rand.nextInt(RAND_MAX_LEN) + 1;
        byte[] baseBytes = new byte[len1];
        byte[] cmpBytes = new byte[len2];
        rand.nextBytes(baseBytes);
        rand.nextBytes(cmpBytes);
        String base = new String(baseBytes, StandardCharsets.ISO_8859_1);
        String cmp = base + new String(cmpBytes, StandardCharsets.ISO_8859_1);
        return new RandomStringPair(base, cmp);
    }

    public static RandomStringPair nextRandomSuffixedPair(Random rand) {
        int len1 = rand.nextInt(RAND_MAX_LEN) + 1;
        int len2 = rand.nextInt(RAND_MAX_LEN) + 1;
        byte[] baseBytes = new byte[len1];
        byte[] cmpBytes = new byte[len2];
        rand.nextBytes(baseBytes);
        rand.nextBytes(cmpBytes);
        String base = new String(baseBytes, StandardCharsets.ISO_8859_1);
        String cmp = new String(cmpBytes, StandardCharsets.ISO_8859_1) + base;
        return new RandomStringPair(base, cmp);
    }

    public static RandomStringPair nextRandomPatchedPair(Random rand) {
        final int patchCount = rand.nextInt(16) + 5;
        StringBuilder base = new StringBuilder();
        StringBuilder cmp = new StringBuilder();
        for (int i = 0; i < patchCount; i++) {
            int len1 = rand.nextInt(RAND_MAX_LEN) + 1;
            byte[] baseBytes = new byte[len1];
            rand.nextBytes(baseBytes);
            String basestr = new String(baseBytes, StandardCharsets.ISO_8859_1);
            base.append(basestr);
            final int op = rand.nextInt(4);
            if (0 == op) {
                // edit (and add random length)
                int len2 = rand.nextInt(RAND_MAX_LEN) + 1;
                byte[] cmpBytes = new byte[len2];
                rand.nextBytes(cmpBytes);
                String cmpstr = new String(cmpBytes, StandardCharsets.ISO_8859_1);
                cmp.append(cmpstr);
            } else if (1 == op) {
                // edit (replace same length)
                byte[] cmpBytes = new byte[len1];
                rand.nextBytes(cmpBytes);
                String cmpstr = new String(cmpBytes, StandardCharsets.ISO_8859_1);
                cmp.append(cmpstr);
            } else if (2 == op) {
                // same part
                cmp.append(basestr);
            } else {
                // skip (= delete)
            }
        }
        return new RandomStringPair(base.toString(), cmp.toString());
    }
}
