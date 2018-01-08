package net.glamenvseptzen.javadifflibsbenchmark;

import org.eclipse.jgit.diff.SequenceComparator;

/**
 * copied from https://github.com/wumpz/java-diff-utils/blob/diffutils-2.2/src/main/java/com/github/difflib/algorithm/jgit/HistogramDiff.java
 * @param <T>
 */
public class DataListComparator<T> extends SequenceComparator<DataList<T>> {

    @Override
    public boolean equals(DataList<T> original, int orgIdx, DataList<T> revised, int revIdx) {
        return original.data.get(orgIdx).equals(revised.data.get(revIdx));
    }

    @Override
    public int hash(DataList<T> s, int i) {
        return s.data.get(i).hashCode();
    }

}
