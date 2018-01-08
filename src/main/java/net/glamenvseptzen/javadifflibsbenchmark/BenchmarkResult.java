package net.glamenvseptzen.javadifflibsbenchmark;

public class BenchmarkResult {
    public final String patternName;
    public final String algName;
    private long elapsedTime = 0;
    private long elapsedCount = 0;
    private int errorCount = 0;

    public BenchmarkResult(final String patternName, final String algName) {
        this.patternName = patternName;
        this.algName = algName;
    }

    public void addElapsedTime(final long elapsed) {
        this.elapsedTime += elapsed;
        this.elapsedCount++;
    }

    public void incrementError() {
        this.errorCount++;
    }

    public String toString() {
        return "pattern[" + patternName + "],algorithm=[" + algName + "],totalElapsedTime=[" + elapsedTime + "],count=["
                + elapsedCount + "],errors=[" + errorCount + "]";
    }

    public Tick tick() {
        return new Tick(this);
    }

    public static class Tick {
        private final long start = System.currentTimeMillis();;
        private final BenchmarkResult br;

        public Tick(final BenchmarkResult br) {
            this.br = br;
        }

        public void tack() {
            final long end = System.currentTimeMillis();
            this.br.addElapsedTime(end - start);
        }
    }

}
