package org.scoreboard.internal.convert;

import org.scoreboard.MatchSummary;
import org.scoreboard.internal.model.Match;

import java.util.function.Function;

/**
 * Converts {@link Match} objects to {@link MatchSummary} instances.
 */
public class MatchToMatchSummaryConverter implements Function<Match, MatchSummary> {

    @Override
    public MatchSummary apply(Match match) {
        return new MatchSummary(match.homeTeam(), match.awayTeam(), match.homeScore(), match.awayScore());
    }
}