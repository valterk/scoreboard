package org.scoreboard.internal;

import org.scoreboard.MatchSummary;
import org.scoreboard.Scoreboard;
import org.scoreboard.internal.compare.DefaultComparatorSupplier;
import org.scoreboard.internal.convert.MatchToMatchSummaryConverter;
import org.scoreboard.internal.model.Match;
import org.scoreboard.internal.repository.InMemoryMatchRepository;
import org.scoreboard.internal.repository.MatchRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Default implementation of the {@link Scoreboard}.
 */
public class ScoreboardImpl implements Scoreboard {

    private final MatchRepository matchRepository;
    private final Comparator<Match> matchComparator;
    private final Function<Match, MatchSummary> toSummaryConverter;

    /**
     * Creates this {@link Scoreboard} implementation with specified {@link MatchRepository}.
     *
     * @param matchRepository match repository to be used by the scoreboard
     */
    public ScoreboardImpl(MatchRepository matchRepository, Comparator<Match> matchComparator,
                          Function<Match, MatchSummary> toSummaryConverter) {
        this.matchRepository = matchRepository;
        this.matchComparator = matchComparator;
        this.toSummaryConverter = toSummaryConverter;
    }

    /**
     * Creates this {@link Scoreboard} implementation with default {@link InMemoryMatchRepository}.
     */
    public ScoreboardImpl() {
        this(new InMemoryMatchRepository(), DefaultComparatorSupplier.get(), new MatchToMatchSummaryConverter());
    }

    @Override
    public void startMatch(String homeTeam, String awayTeam) {
        validateTeams(homeTeam, awayTeam);

        if (matchRepository.containsMatchForTeam(homeTeam)) {
            throw new IllegalStateException(String.format("Team %s already plays a match", homeTeam));
        }
        if (matchRepository.containsMatchForTeam(awayTeam)) {
            throw new IllegalStateException(String.format("Team %s already plays a match", awayTeam));
        }

        matchRepository.save(new Match(homeTeam, awayTeam));
    }

    @Override
    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        validateTeams(homeTeam, awayTeam);
        validateScores(homeScore, awayScore);

        Match currentMatch = matchRepository.get(homeTeam, awayTeam)
                .orElseThrow(() -> new IllegalStateException(String.format("There is no match %s - %s ", homeTeam, awayTeam)));

        matchRepository.save(currentMatch.withModifiedScore(homeScore, awayScore));
    }

    @Override
    public void finishMatch(String homeTeam, String awayTeam) {
        validateTeams(homeTeam, awayTeam);

        matchRepository.get(homeTeam, awayTeam)
                .orElseThrow(() -> new IllegalStateException(String.format("There is no match %s - %s ", homeTeam, awayTeam)));

        matchRepository.remove(homeTeam, awayTeam);
    }

    @Override
    public List<MatchSummary> getMatchesSummary() {
        return matchRepository.listAllMatches().stream()
                .sorted(matchComparator)
                .map(toSummaryConverter)
                .toList();
    }

    private static void validateTeams(String homeTeam, String awayTeam) {
        Objects.requireNonNull(homeTeam, "Home team name cannot be null");
        Objects.requireNonNull(awayTeam, "Away team name cannot be null");
        if (homeTeam.isEmpty()) throw new IllegalArgumentException("Home team name cannot be empty");
        if (awayTeam.isEmpty()) throw new IllegalArgumentException("Away team name cannot be empty");
        if (homeTeam.equals(awayTeam))
            throw new IllegalArgumentException("Home and away team names cannot be the same");
    }

    private static void validateScores(int homeScore, int awayScore) {
        if (homeScore < 0) throw new IllegalArgumentException("Home score cannot be negative");
        if (awayScore < 0) throw new IllegalArgumentException("Away score cannot be negative");
    }

}
