package net.glamenvseptzen.javadifflibsbenchmark;

import java.util.List;

import org.eclipse.jgit.diff.DiffAlgorithm;
import org.eclipse.jgit.diff.MyersDiff;

import net.glamenvseptzen.javadifflibsbenchmark.BenchmarkResult.Tick;

public class BenchmarkJGitMyers implements IBenchmark {

    @Override
    public void diffByLine(final BenchmarkResult br, final List<String> base, final List<String> cmp) {
        DiffAlgorithm da = MyersDiff.INSTANCE;
        Tick tick = br.tick();
        da.diff(new DataListComparator<>(), new DataList<>(base), new DataList<>(cmp));
        tick.tack();
    }

    @Override
    public void diffByChar(final BenchmarkResult br, final List<Character> base, final List<Character> cmp) {
        DiffAlgorithm da = MyersDiff.INSTANCE;
        Tick tick = br.tick();
        da.diff(new DataListComparator<>(), new DataList<>(base), new DataList<>(cmp));
        tick.tack();
    }
}
