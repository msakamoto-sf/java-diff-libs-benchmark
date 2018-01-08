package net.glamenvseptzen.javadifflibsbenchmark;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cloudbees.diff.Diff;
import com.sksamuel.diffpatch.DiffMatchPatch;

import net.glamenvseptzen.javadifflibsbenchmark.BenchmarkResult.Tick;

public class BenchamrkDiffFamily {
    private final Map<String, Map<String, BenchmarkResult>> brdata = new LinkedHashMap<>();

    private BenchmarkResult getBenchmarkResult(final String patternName, final String algName) {
        Map<String, BenchmarkResult> algToBr = brdata.getOrDefault(patternName, new LinkedHashMap<>());
        brdata.put(patternName, algToBr);
        BenchmarkResult br = algToBr.getOrDefault(algName, new BenchmarkResult(patternName, algName));
        algToBr.put(algName, br);
        return br;
    }

    public void benchmark(final String patternName, final RandomStringPair p) {
        List<String> baseByLine = Arrays.asList(p.base.split("\n"));
        List<String> cmpByLine = Arrays.asList(p.cmp.split("\n"));
        List<Character> baseByChar = p.strToCharList(p.base);
        List<Character> cmpByChar = p.strToCharList(p.cmp);

        IBenchmark bm = new BenchmarkJavaDiffUtilsByLine();
        bm.diffByLine(getBenchmarkResult(patternName, "java-diff-utils:myers:by-line"), baseByLine, cmpByLine);
        bm.diffByChar(getBenchmarkResult(patternName, "java-diff-utils:myers:by-char"), baseByChar, cmpByChar);

        bm = new BenchmarkJGitMyers();
        bm.diffByLine(getBenchmarkResult(patternName, "jgit:myers:by-line"), baseByLine, cmpByLine);
        bm.diffByChar(getBenchmarkResult(patternName, "jgit:myers:by-char"), baseByChar, cmpByChar);

        bm = new BenchmarkJGitHistogram();
        bm.diffByLine(getBenchmarkResult(patternName, "jgit:histogram:by-line"), baseByLine, cmpByLine);
        bm.diffByChar(getBenchmarkResult(patternName, "jgit:histogram:by-char"), baseByChar, cmpByChar);

        BenchmarkResult br = getBenchmarkResult(patternName, "diff4j:HuntDiff");
        Tick tick0 = br.tick();
        Diff.diff(baseByLine, cmpByLine, false);
        tick0.tack();

        br = getBenchmarkResult(patternName, "google-diff-match-patch");
        Tick tick1 = br.tick();
        DiffMatchPatch dfp = new DiffMatchPatch();
        dfp.Diff_Timeout = 0.0f; // set no-timeout
        dfp.diff_main(p.base, p.cmp);
        tick1.tack();
    }

    public void printResults() {
        for (Map<String, BenchmarkResult> patternV : brdata.values()) {
            for (BenchmarkResult br : patternV.values()) {
                System.out.println(br.toString());
            }
        }
    }
}
