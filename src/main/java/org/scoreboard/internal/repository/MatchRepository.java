package org.scoreboard.internal.repository;

import org.scoreboard.internal.model.Match;

import java.util.List;
import java.util.Optional;

/**
 * Repository to store {@link Match} instances.
 */
public interface MatchRepository {

    /**
     * Stores specified {@link Match} instance in the repository.
     * It will overwrite any previously saved match with the same home and away team names respectively.
     * If for any of the teams in the adding match repository already contains a match that is not the same match
     * (different home or away team) an exception will be thrown.
     *
     * @param match match to be stored; cannot be null
     * @throws NullPointerException  if provided match is null
     * @throws IllegalStateException if for any of teams in the specified match repository contains a different match
     */
    void save(Match match);

    /**
     * Checks whether the repository contains any match for specified team name (either home or away).
     *
     * @param teamName name of the team; cannot be null
     * @return true if such match is present, false otherwise
     * @throws NullPointerException if the team name is null
     */
    boolean containsMatchForTeam(String teamName);

    /**
     * Gets a match for specified teams from the repository.
     *
     * @param homeTeam name of the home team; cannot be null
     * @param awayTeam name of the away team; cannot be null
     * @return optional with the requested match if present, empty optional otherwise
     * @throws NullPointerException if either of the team names is null
     */
    Optional<Match> get(String homeTeam, String awayTeam);

    /**
     * Removes a match for specified teams from the repository if present.
     *
     * @param homeTeam name of the home team; cannot be null
     * @param awayTeam name of the away team; cannot be null
     * @throws NullPointerException if either of the team names is null
     * @throws IllegalStateException if there is no such match stored in the repository
     */
    void remove(String homeTeam, String awayTeam);

    /**
     * @return a list of matches stored in the repository
     */
    List<Match> listAllMatches();
}
