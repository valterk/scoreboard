package org.scoreboard.internal.model;

import org.scoreboard.internal.repository.MatchRepository;

import java.time.Instant;

/**
 * Represents a particular match stored by {@link MatchRepository} implementations.
 *
 * @param homeTeam  name of the home team; cannot be null or empty
 * @param awayTeam  name of the away team; cannot be null or empty
 * @param homeScore score of the home team; cannot be a negative number
 * @param awayScore score of the away team; cannot be a negative number
 * @param startedAt timestamp when the match started
 */
public record Match(String homeTeam, String awayTeam, int homeScore, int awayScore, Instant startedAt) {

    /**
     * Creates the match instance with specified team names. Score will be set to 0 - 0, and the started time to the current one.
     *
     * @param homeTeam name of the home team
     * @param awayTeam name of the away team
     */
    public Match(String homeTeam, String awayTeam) {
        this(homeTeam, awayTeam, 0, 0, Instant.now());
    }

    /**
     * Creates a copy of this match with provided scores.
     *
     * @param homeScore score of the home team
     * @param awayScore score of the away team
     * @return A copy of a match with modified scores.
     */
    public Match withModifiedScore(int homeScore, int awayScore) {
        return new Match(homeTeam, awayTeam, homeScore, awayScore, startedAt);
    }
}
