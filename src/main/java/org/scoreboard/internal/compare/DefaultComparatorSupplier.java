package org.scoreboard.internal.compare;

import org.scoreboard.internal.model.Match;

import java.util.Comparator;

/**
 * Supplies default comparator for {@link Match} instances.
 * The comparator should firstly order matches by their total score and then by the most recently started ones.
 */
public class DefaultComparatorSupplier {

    private DefaultComparatorSupplier() {
    }

    public static Comparator<Match> get() {
        return Comparator.<Match>comparingInt(m -> m.homeScore() + m.awayScore())
                .thenComparing(Match::startedAt)
                .reversed();
    }
}
