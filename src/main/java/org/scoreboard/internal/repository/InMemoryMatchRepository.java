package org.scoreboard.internal.repository;

import org.scoreboard.internal.model.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * In memory implementation of the {@link MatchRepository} interface.
 */
public class InMemoryMatchRepository implements MatchRepository {

    private static final Logger log = LoggerFactory.getLogger(InMemoryMatchRepository.class);

    private final Map<String, Match> homeMap = new HashMap<>();
    private final Map<String, Match> awayMap = new HashMap<>();

    @Override
    public void save(Match match) {
        log.info("Saving a match - {}", match);
        Objects.requireNonNull(match, "Cannot save a null match");

        matchesForTeams(match.homeTeam(), match.awayTeam())
                .filter((m) -> !areTheSameMatches(m, match))
                .findFirst().ifPresent(m -> {
                    throw new IllegalStateException(String.format(
                            "Cannot save the %s - %s match, because there is already a match %s - %s",
                            match.homeTeam(), match.awayTeam(), m.homeTeam(), m.awayTeam()));
                });

        homeMap.put(match.homeTeam(), match);
        awayMap.put(match.awayTeam(), match);
    }

    @Override
    public boolean containsMatchForTeam(String teamName) {
        validateTeamName(teamName);
        return homeMap.containsKey(teamName) || awayMap.containsKey(teamName);
    }

    @Override
    public Optional<Match> get(String homeTeam, String awayTeam) {
        validateTeamName(homeTeam);
        validateTeamName(awayTeam);

        Match matchForHomeTeam = homeMap.get(homeTeam);
        Match matchForAwayTeam = awayMap.get(awayTeam);

        if (areTheSameMatches(matchForHomeTeam, matchForAwayTeam)) {
            return Optional.of(matchForHomeTeam);
        }
        return Optional.empty();
    }

    @Override
    public void remove(String homeTeam, String awayTeam) {
        log.info("Removing the match between {} and {}", homeTeam, awayTeam);
        validateTeamName(homeTeam);
        validateTeamName(awayTeam);

        Match matchForHomeTeam = homeMap.get(homeTeam);
        Match matchForAwayTeam = awayMap.get(awayTeam);

        if (areTheSameMatches(matchForHomeTeam, matchForAwayTeam)) {
            homeMap.remove(homeTeam);
            awayMap.remove(awayTeam);
        } else {
            throw new IllegalStateException(String.format("There is no %s - %s match in the repository", homeTeam, awayTeam));
        }
    }

    @Override
    public List<Match> listAllMatches() {
        return homeMap.values().stream()
                .toList();
    }

    private Stream<Match> matchesForTeams(String t1, String t2) {
        return Stream.of(homeMap.get(t1), homeMap.get(t2), awayMap.get(t1), awayMap.get(t2))
                .filter(Objects::nonNull);
    }

    private static boolean areTheSameMatches(Match m1, Match m2) {
        return m1 != null && m2 != null
                && m1.homeTeam().equals(m2.homeTeam())
                && m1.awayTeam().equals(m2.awayTeam());
    }

    private static void validateTeamName(String teamName) {
        Objects.requireNonNull(teamName, "Team name cannot be null");
    }
}
