package net.glamenvseptzen.javadifflibsbenchmark;

import java.util.List;

import com.github.difflib.algorithm.DiffException;
import com.github.difflib.algorithm.myers.MyersDiff;

import net.glamenvseptzen.javadifflibsbenchmark.BenchmarkResult.Tick;

public class BenchmarkJavaDiffUtilsByLine implements IBenchmark {

    @Override
    public void diffByLine(final BenchmarkResult br, final List<String> base, final List<String> cmp) {
        try {
            MyersDiff<String> myersDiff = new MyersDiff<>();
            Tick tick = br.tick();
            myersDiff.diff(base, cmp);
            tick.tack();
        } catch (DiffException e) {
            br.incrementError();
        }
    }

    @Override
    public void diffByChar(final BenchmarkResult br, final List<Character> base, final List<Character> cmp) {
        try {
            MyersDiff<Character> myersDiff = new MyersDiff<>();
            Tick tick = br.tick();
            myersDiff.diff(base, cmp);
            tick.tack();
        } catch (DiffException e) {
            br.incrementError();
        }
    }

}
