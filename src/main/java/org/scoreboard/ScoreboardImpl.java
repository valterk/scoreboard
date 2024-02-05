package org.scoreboard;

import java.util.List;

/**
 * Default implementation of the {@link Scoreboard}.
 */
public class ScoreboardImpl implements Scoreboard {

    @Override
    public void startMatch(String homeTeam, String awayTeam) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void finishMatch(String homeTeam, String awayTeam) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<MatchSummary> getMatchesSummary() {
        throw new UnsupportedOperationException();
    }
}
