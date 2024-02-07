package org.scoreboard;

import org.scoreboard.internal.ScoreboardImpl;

import java.util.List;

/**
 * Scoreboard for live football games.
 */
public interface Scoreboard {

    /**
     * Starts a new match and adds it the scoreboard. All newly created matches have 0-0 score.
     *
     * @param homeTeam name of the home team; cannot be null or empty
     * @param awayTeam name of the away team; cannot be null or empty
     * @throws NullPointerException     if either of the team names is null
     * @throws IllegalArgumentException if either of the team names is empty, or both of them are equal
     * @throws IllegalStateException    if a match between exactly the same home and away teams is currently present on the scoreboard
     */
    void startMatch(String homeTeam, String awayTeam);

    /**
     * Updates the score of a particular match (identified by a pair of team names) with a specified pair of absolute scores.
     *
     * @param homeTeam  name of the home team; cannot be null or empty
     * @param awayTeam  name of the away team; cannot be null or empty
     * @param homeScore score of the home team; cannot be a negative number
     * @param awayScore score of the away team; cannot be a negative number
     * @throws NullPointerException     if either of the team names is null
     * @throws IllegalArgumentException if either of the team names is empty, or both of them are equal, or if either of the team scores is negative
     * @throws IllegalStateException    if a match between exactly the same home and away teams is not present on the scoreboard
     */
    void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore);

    /**
     * Finishes a match currently in progress identified by a pair of team names. This removes the match from the scoreboard.
     *
     * @param homeTeam name of the home team; cannot be null or empty
     * @param awayTeam name of the away team; cannot be null or empty
     * @throws NullPointerException     if either of the team names is null
     * @throws IllegalArgumentException if either of the team names is empty, or both of them are equal
     * @throws IllegalStateException    if a match between exactly the same home and away teams is not present on the scoreboard
     */
    void finishMatch(String homeTeam, String awayTeam);

    /**
     * Gets a summary of matches in progress ordered by their total score.
     * The matches with the same total score will be returned ordered by the most recently started match in the scoreboard.
     *
     * @return a list of instances of the {@link MatchSummary} class
     */
    List<MatchSummary> getMatchesSummary();

    /**
     * @return default scoreboard implementation
     */
    static Scoreboard getDefaultInstance() {
        return new ScoreboardImpl();
    }
}
