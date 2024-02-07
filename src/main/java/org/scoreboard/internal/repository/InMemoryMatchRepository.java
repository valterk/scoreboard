package org.scoreboard.internal.repository;

import org.scoreboard.internal.model.Match;

import java.util.List;
import java.util.Optional;

/**
 * In memory implementation of the {@link MatchRepository} interface.
 */
public class InMemoryMatchRepository implements MatchRepository {
    @Override
    public void save(Match match) {

    }

    @Override
    public boolean containsMatchForTeam(String teamName) {
        return false;
    }

    @Override
    public Optional<Match> get(String homeTeam, String awayTeam) {
        return Optional.empty();
    }

    @Override
    public void remove(String homeTeam, String awayTeam) {

    }

    @Override
    public List<Match> listAllMatches() {
        return null;
    }
}
