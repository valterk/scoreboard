package org.scoreboard.internal.compare;

import org.junit.jupiter.api.Test;
import org.scoreboard.internal.model.Match;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultComparatorSupplierTest {

    private static final Comparator<Match> comparator = DefaultComparatorSupplier.get();

    static final Match FIRST_MATCH = new Match("homeTeam1", "awayTeam1", 0, 0, Instant.now());
    static final Match SECOND_MATCH = new Match("homeTeam2", "awayTeam2", 3, 4, Instant.now());
    static final Match THIRD_MATCH = new Match("homeTeam3", "awayTeam3", 2, 2, Instant.now());
    static final Match FOURTH_MATCH = new Match("homeTeam4", "awayTeam4", 4, 3, Instant.now());

    @Test
    public void sortingWitDefaultComparatorShouldEndUpWithListOrderedByTotalScore() {
        //given
        List<Match> input = List.of(FIRST_MATCH, SECOND_MATCH, THIRD_MATCH);

        //when
        var sortedMatches = sort(input);

        //then
        assertThat(sortedMatches)
                .hasSize(3)
                .containsExactly(SECOND_MATCH, THIRD_MATCH, FIRST_MATCH);
    }

    @Test
    public void sortingWitDefaultComparatorShouldEndUpWithListOrderedByTotalScoreAndThenByTheMostRecentlyStarted() {
        //given
        List<Match> input = List.of(FIRST_MATCH, SECOND_MATCH, THIRD_MATCH, FOURTH_MATCH);

        //when
        var sortedMatches = sort(input);

        //then
        assertThat(sortedMatches)
                .hasSize(4)
                .containsExactly(FOURTH_MATCH, SECOND_MATCH, THIRD_MATCH, FIRST_MATCH);
    }

    static List<Match> sort(List<Match> inputList) {
        return inputList.stream()
                .sorted(comparator)
                .toList();
    }

}