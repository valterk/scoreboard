package org.scoreboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScoreboardTest {

    static final String HOME_TEAM1 = "homeTeam1";
    static final String HOME_TEAM2 = "homeTeam2";
    static final String HOME_TEAM3 = "homeTeam3";
    static final String AWAY_TEAM1 = "awayTeam1";
    static final String AWAY_TEAM2 = "awayTeam2";
    static final String AWAY_TEAM3 = "awayTeam3";

    private Scoreboard scoreboard;

    @BeforeEach
    public void init() {
        scoreboard = Scoreboard.getDefaultInstance();
    }

    @Test
    public void allMethodsWithBothTeamNamesAsArgumentsShouldThrowExceptionsForNullValues() {
        assertThrowsOnFirstAndSecondAndBothArgumentsAreEqualTo(NullPointerException.class,
                (v1, v2) -> scoreboard.startMatch(v1, v2), null);
        assertThrowsOnFirstAndSecondAndBothArgumentsAreEqualTo(NullPointerException.class,
                (v1, v2) -> scoreboard.updateScore(v1, v2, 0, 0), null);
        assertThrowsOnFirstAndSecondAndBothArgumentsAreEqualTo(NullPointerException.class,
                (v1, v2) -> scoreboard.finishMatch(v1, v2), null);
    }

    @Test
    public void allMethodsWithBothTeamNamesAsArgumentsShouldThrowExceptionsForEmptyValues() {
        assertThrowsOnFirstAndSecondAndBothArgumentsAreEqualTo(IllegalArgumentException.class,
                (v1, v2) -> scoreboard.startMatch(v1, v2), "");
        assertThrowsOnFirstAndSecondAndBothArgumentsAreEqualTo(IllegalArgumentException.class,
                (v1, v2) -> scoreboard.updateScore(v1, v2, 0, 0), "");
        assertThrowsOnFirstAndSecondAndBothArgumentsAreEqualTo(IllegalArgumentException.class,
                (v1, v2) -> scoreboard.finishMatch(v1, v2), "");
    }

    @Test
    public void allMethodsWithBothTeamNamesAsArgumentsShouldThrowExceptionsIfBothAreEqual() {
        assertThrows(IllegalArgumentException.class, () -> scoreboard.startMatch(HOME_TEAM1, HOME_TEAM1));
        assertThrows(IllegalArgumentException.class, () -> scoreboard.updateScore(HOME_TEAM1, HOME_TEAM1, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> scoreboard.finishMatch(HOME_TEAM1, HOME_TEAM1));
    }

    @Test
    public void updateScoreForNegativeValuesShouldThrowException() {
        //given
        startFirstMatch();

        //then
        assertThrows(IllegalArgumentException.class, () -> updateFirstMatch(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> updateFirstMatch(0, -1));
        assertThrows(IllegalArgumentException.class, () -> updateFirstMatch(-1, -1));
    }

    @Test
    public void failedStartMatchCallShouldNotCauseAnyChangesInTheSummary() {
        //given
        startFirstMatch();
        var initialSummary = scoreboard.getMatchesSummary();

        //when
        assertThrows(IllegalArgumentException.class, () -> scoreboard.startMatch(HOME_TEAM2, ""));
        var lastSummary = scoreboard.getMatchesSummary();

        //then
        assertThat(initialSummary).hasSameElementsAs(lastSummary);
    }

    @Test
    public void failedUpdateScoreCallShouldNotCauseAnyChangesInTheSummary() {
        //given
        startFirstMatch();
        startSecondMatch();
        var initialSummary = scoreboard.getMatchesSummary();

        //when
        assertThrows(IllegalArgumentException.class, () -> updateFirstMatch(-1, 0));
        var lastSummary = scoreboard.getMatchesSummary();

        //then
        assertThat(initialSummary).hasSameElementsAs(lastSummary);
    }

    @Test
    public void failedFinishMatchCallShouldNotCauseAnyChangesInTheSummary() {
        //given
        startFirstMatch();
        startSecondMatch();
        var initialSummary = scoreboard.getMatchesSummary();

        //when
        assertThrows(IllegalArgumentException.class, () -> scoreboard.finishMatch(HOME_TEAM2, ""));
        var lastSummary = scoreboard.getMatchesSummary();

        //then
        assertThat(initialSummary).hasSameElementsAs(lastSummary);
    }

    @Test
    public void startMatchForAlreadyStartedOneShouldThrowException() {
        //given
        startFirstMatch();

        //then
        assertThrows(IllegalStateException.class, this::startFirstMatch);
    }

    @Test
    public void updateScoreForNotStartedMatchShouldThrowException() {
        //given
        startFirstMatch();

        //then
        assertThrows(IllegalStateException.class, () -> updateSecondMatch(1, 0));
    }

    @Test
    public void updateScoreForFinishedMatchShouldThrowException() {
        //given
        startFirstMatch();
        startSecondMatch();
        finishSecondMatch();

        //then
        assertThrows(IllegalStateException.class, () -> updateSecondMatch(1, 0));
    }

    @Test
    public void finishMatchForNotStartedOneShouldThrowException() {
        assertThrows(IllegalStateException.class, this::finishFirstMatch);
    }

    @Test
    public void finishMatchForAlreadyFinishedOneShouldThrowException() {
        //given
        startFirstMatch();
        finishFirstMatch();

        //then
        assertThrows(IllegalStateException.class, this::finishFirstMatch);
    }

    @Test
    public void getMatchesSummaryShouldReturnEmptyListForNewlyCreatedScoreboard() {
        //when
        var summary = scoreboard.getMatchesSummary();

        //then
        assertThat(summary).isEmpty();
    }

    @Test
    public void getMatchesSummaryShouldReturnEmptyListWhenAllMatchesHasBeenFinished() {
        //given
        startFirstMatch();
        startSecondMatch();
        finishFirstMatch();
        finishSecondMatch();

        //when
        var summary = scoreboard.getMatchesSummary();

        //then
        assertThat(summary).isEmpty();
    }

    @Test
    public void startedMatchWithoutAnyScoreUpdatesShouldHaveZeroZeroScore() {
        //given
        startFirstMatch();

        //when
        var summary = scoreboard.getMatchesSummary();

        //then
        assertThat(summary)
                .hasSize(1)
                .containsExactly(firstMatchSummary(0, 0));
    }

    @Test
    public void updateScoreShouldModifyTheScoreForParticularMatch() {
        //given
        startFirstMatch();

        //when
        updateFirstMatch(1, 0);
        var summary = scoreboard.getMatchesSummary();

        //then
        assertThat(summary)
                .hasSize(1)
                .containsExactly(firstMatchSummary(1, 0));
    }

    @Test
    public void updateScoreShouldOverridePreviouslyModifyScoreForParticularMatch() {
        //given
        startFirstMatch();
        updateFirstMatch(1, 0);

        //when
        updateFirstMatch(1, 1);
        var summary = scoreboard.getMatchesSummary();

        //then
        assertThat(summary)
                .hasSize(1)
                .containsExactly(firstMatchSummary(1, 1));
    }

    @Test
    public void updateScoreShouldModifyTheScoreOnlyForParticularMatch() {
        //given
        startFirstMatch();
        startSecondMatch();

        //when
        updateFirstMatch(1, 0);
        updateSecondMatch(0, 1);
        var summary = scoreboard.getMatchesSummary();

        //then
        assertThat(summary)
                .hasSize(2)
                .containsExactlyInAnyOrder(
                        firstMatchSummary(1, 0),
                        secondMatchSummary(0, 1));
    }

    @Test
    public void matchesSummaryIsOrderedByTotalScore() {
        //given
        startFirstMatch();
        startSecondMatch();
        startThirdMatch();

        //when
        updateFirstMatch(1, 5);
        updateSecondMatch(4, 3);
        updateThirdMatch(5, 0);
        var summary = scoreboard.getMatchesSummary();

        //then
        assertThat(summary)
                .hasSize(3)
                .containsExactly(
                        secondMatchSummary(4, 3),
                        firstMatchSummary(1, 5),
                        thirdMatchSummary(5, 0));
    }

    @Test
    public void matchesSummaryForSameTotalScoreIsOrderedByTheMostRecentlyStarted() {
        //given
        startFirstMatch();
        startSecondMatch();
        startThirdMatch();

        //when
        updateFirstMatch(3, 3);
        updateSecondMatch(5, 1);
        updateThirdMatch(2, 4);
        var summary = scoreboard.getMatchesSummary();

        //then
        assertThat(summary)
                .hasSize(3)
                .containsExactly(
                        thirdMatchSummary(2, 4),
                        secondMatchSummary(5, 1),
                        firstMatchSummary(3, 3));
    }

    // asserts throwing exception for all three cases that first, second or both arguments of a particular function have the specified testing value
    static void assertThrowsOnFirstAndSecondAndBothArgumentsAreEqualTo(Class<? extends Throwable> expectedType,
                                                                       BiConsumer<String, String> function, String testingValue) {
        assertThrows(expectedType, () -> function.accept(testingValue, "other"));
        assertThrows(expectedType, () -> function.accept("other", testingValue));
        assertThrows(expectedType, () -> function.accept(testingValue, testingValue));
    }

    void startFirstMatch() {
        scoreboard.startMatch(HOME_TEAM1, AWAY_TEAM1);
    }

    void startSecondMatch() {
        scoreboard.startMatch(HOME_TEAM2, AWAY_TEAM2);
    }

    void startThirdMatch() {
        scoreboard.startMatch(HOME_TEAM3, AWAY_TEAM3);
    }

    void updateFirstMatch(int homeScore, int awayScore) {
        scoreboard.updateScore(HOME_TEAM1, AWAY_TEAM1, homeScore, awayScore);
    }

    void updateSecondMatch(int homeScore, int awayScore) {
        scoreboard.updateScore(HOME_TEAM2, AWAY_TEAM2, homeScore, awayScore);
    }

    void updateThirdMatch(int homeScore, int awayScore) {
        scoreboard.updateScore(HOME_TEAM3, AWAY_TEAM3, homeScore, awayScore);
    }

    void finishFirstMatch() {
        scoreboard.finishMatch(HOME_TEAM1, AWAY_TEAM1);
    }

    void finishSecondMatch() {
        scoreboard.finishMatch(HOME_TEAM2, AWAY_TEAM2);
    }

    MatchSummary firstMatchSummary(int homeScore, int awayScore) {
        return new MatchSummary(HOME_TEAM1, AWAY_TEAM1, homeScore, awayScore);
    }

    MatchSummary secondMatchSummary(int homeScore, int awayScore) {
        return new MatchSummary(HOME_TEAM2, AWAY_TEAM2, homeScore, awayScore);
    }

    MatchSummary thirdMatchSummary(int homeScore, int awayScore) {
        return new MatchSummary(HOME_TEAM3, AWAY_TEAM3, homeScore, awayScore);
    }

}