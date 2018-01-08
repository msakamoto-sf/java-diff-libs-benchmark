package net.glamenvseptzen.javadifflibsbenchmark;

import java.util.List;

import org.eclipse.jgit.diff.Sequence;

/**
 * copied from https://github.com/wumpz/java-diff-utils/blob/diffutils-2.2/src/main/java/com/github/difflib/algorithm/jgit/HistogramDiff.java
 * @param <T>
 */
public class DataList<T> extends Sequence {
    final List<T> data;

    public DataList(List<T> data) {
        this.data = data;
    }

    @Override
    public int size() {
        return data.size();
    }
}
