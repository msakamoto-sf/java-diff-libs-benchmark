package net.glamenvseptzen.javadifflibsbenchmark;

import java.util.List;

public interface IBenchmark {
    public void diffByLine(final BenchmarkResult br, final List<String> base, final List<String> cmp);
    public void diffByChar(final BenchmarkResult br, final List<Character> base, final List<Character> cmp);
}
