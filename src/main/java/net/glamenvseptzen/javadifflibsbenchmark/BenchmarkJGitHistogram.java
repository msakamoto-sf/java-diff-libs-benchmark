package net.glamenvseptzen.javadifflibsbenchmark;

import java.util.List;

import org.eclipse.jgit.diff.HistogramDiff;

import net.glamenvseptzen.javadifflibsbenchmark.BenchmarkResult.Tick;

public class BenchmarkJGitHistogram implements IBenchmark {

    @Override
    public void diffByLine(final BenchmarkResult br, final List<String> base, final List<String> cmp) {
        HistogramDiff hd = new HistogramDiff();
        Tick tick = br.tick();
        hd.diff(new DataListComparator<>(), new DataList<>(base), new DataList<>(cmp));
        tick.tack();
    }

    @Override
    public void diffByChar(final BenchmarkResult br, final List<Character> base, final List<Character> cmp) {
        HistogramDiff hd = new HistogramDiff();
        Tick tick = br.tick();
        hd.diff(new DataListComparator<>(), new DataList<>(base), new DataList<>(cmp));
        tick.tack();
    }
}

