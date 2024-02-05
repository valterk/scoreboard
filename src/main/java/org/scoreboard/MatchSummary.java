package org.scoreboard;

/**
 * Represents a summary for a single ongoing match.
 *
 * @param homeTeam  name of the home team
 * @param awayTeam  name of the away team
 * @param homeScore score of the home team
 * @param awayScore score of the away team
 */
public record MatchSummary(String homeTeam, String awayTeam, int homeScore, int awayScore) {
}
