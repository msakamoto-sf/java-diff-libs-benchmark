package net.glamenvseptzen.javadifflibsbenchmark;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.cloudbees.diff.Diff;
import com.sksamuel.diffpatch.DiffMatchPatch;

import net.glamenvseptzen.javadifflibsbenchmark.BenchmarkResult.Tick;

public class BenchamrkDiffFamily {
    private final Map<String, Map<String, BenchmarkResult>> brdata = new LinkedHashMap<>();
    private final File filesDiffBaseDir;
    private final File filesDiffCmpDir;
    private final boolean enableFilesDiff;
    private final String filesDiffBaseDirAbs;
    private final String filesDiffCmpDirAbs;

    public BenchamrkDiffFamily(final File filesDiffBaseDir, final File filesDiffCmpDir) {
        this.filesDiffBaseDir = filesDiffBaseDir;
        this.filesDiffCmpDir = filesDiffCmpDir;
        if (Objects.nonNull(this.filesDiffBaseDir) && Objects.nonNull(this.filesDiffCmpDir)
                && this.filesDiffBaseDir.isDirectory() && this.filesDiffCmpDir.isDirectory()) {
            enableFilesDiff = true;
            filesDiffBaseDirAbs = this.filesDiffBaseDir.getAbsolutePath();
            filesDiffCmpDirAbs = this.filesDiffCmpDir.getAbsolutePath();
        } else {
            enableFilesDiff = false;
            filesDiffBaseDirAbs = null;
            filesDiffCmpDirAbs = null;
        }
    }

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

    class FilesDiffPair {
        final File base;
        final File cmp;

        FilesDiffPair(final File base, final File cmp) {
            this.base = base;
            this.cmp = cmp;
        }
    }

    private List<FilesDiffPair> filesDiffPairs = new ArrayList<>();

    public void traverse(File dir) {
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                traverse(f);
                continue;
            }
            final String absBasePath = f.getAbsolutePath();
            final String absCmpPath = absBasePath.replace(filesDiffBaseDirAbs, filesDiffCmpDirAbs);
            final File cmpFile = new File(absCmpPath);
            if (!cmpFile.exists()) {
                continue;
            }
            filesDiffPairs.add(new FilesDiffPair(f, cmpFile));
        }
    }

    public void benchmarkFiles() throws IOException {
        if (!enableFilesDiff) {
            return;
        }
        traverse(this.filesDiffBaseDir);
        for (FilesDiffPair pair : filesDiffPairs) {
            final Charset cs = StandardCharsets.ISO_8859_1;
            final String baseStr = new String(Files.readAllBytes(pair.base.toPath()), cs);
            final String cmpStr = new String(Files.readAllBytes(pair.cmp.toPath()), cs);
            List<String> baseByLine = Files.readAllLines(pair.base.toPath(), cs);
            List<String> cmpByLine = Files.readAllLines(pair.cmp.toPath(), cs);
            final String patternName = "files-diff";

            IBenchmark bm = new BenchmarkJavaDiffUtilsByLine();
            bm.diffByLine(getBenchmarkResult(patternName, "java-diff-utils:myers:by-line"), baseByLine, cmpByLine);

            bm = new BenchmarkJGitMyers();
            bm.diffByLine(getBenchmarkResult(patternName, "jgit:myers:by-line"), baseByLine, cmpByLine);

            bm = new BenchmarkJGitHistogram();
            bm.diffByLine(getBenchmarkResult(patternName, "jgit:histogram:by-line"), baseByLine, cmpByLine);

            BenchmarkResult br = getBenchmarkResult(patternName, "diff4j:HuntDiff");
            Tick tick0 = br.tick();
            Diff.diff(baseByLine, cmpByLine, false);
            tick0.tack();

            br = getBenchmarkResult(patternName, "google-diff-match-patch");
            Tick tick1 = br.tick();
            DiffMatchPatch dfp = new DiffMatchPatch();
            dfp.Diff_Timeout = 0.0f; // set no-timeout
            dfp.diff_main(baseStr, cmpStr);
            tick1.tack();
        }
    }

    public void printResults() {
        for (Map<String, BenchmarkResult> patternV : brdata.values()) {
            for (BenchmarkResult br : patternV.values()) {
                System.out.println(br.toString());
            }
        }
    }
}
