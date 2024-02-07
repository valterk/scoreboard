package org.scoreboard;

import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        System.out.println("""
                    ___________________________________
                    | Hello from the Live Scoreboard! |
                    -----------------------------------
                    """);

        Scoreboard scoreboard = Scoreboard.getDefaultInstance();

        scoreboard.startMatch("Argentina", "Brazil");
        scoreboard.startMatch("Portugal", "Germany");
        scoreboard.updateScore("Portugal", "Germany", 1, 0);
        scoreboard.updateScore("Argentina", "Brazil", 0, 1);

        printSummary(scoreboard);

        scoreboard.startMatch("Japan", "Poland");
        scoreboard.updateScore("Japan", "Poland", 0 , 1);
        scoreboard.updateScore("Japan", "Poland", 0 , 2);

        printSummary(scoreboard);

        scoreboard.updateScore("Portugal", "Germany", 1, 1);
        scoreboard.finishMatch("Argentina", "Brazil");
        scoreboard.startMatch("France", "Spain");
        scoreboard.startMatch("Brazil", "Chile");
        scoreboard.updateScore("France", "Spain", 0, 1);
        scoreboard.updateScore("Brazil", "Chile", 1, 0);

        printSummary(scoreboard);

        scoreboard.finishMatch("Portugal", "Germany");
        scoreboard.finishMatch("France", "Spain");

        printSummary(scoreboard);
    }

    private static void printSummary(Scoreboard scoreboard) {
        System.out.println("\nCURRENT SCOREBOARD:");
        System.out.println(scoreboard.getMatchesSummary().stream()
                .map(MatchSummary::toString)
                .collect(Collectors.joining("\n")));
        System.out.println();
    }
}
