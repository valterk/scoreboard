# Live Football World Cup Scoreboard!

## Description

It's a Java library that provides a simple live scoreboard functionality.

It can be used through a `org.scoreboard.Scoreboard` interface that provides a static method `getDefaultInstance()` 
to get an instance of its default implementation. This interface contains the following methods:
* `startMatch(String homeTeam, String awayTeam)` - starts a new match and adds it the scoreboard;
* `updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore)` - updates the score of a particular match
(identified by a pair of team names) with a specified pair of absolute scores;
* `finishMatch(String homeTeam, String awayTeam)` - finishes a match currently in progress identified by a pair of team names,
this removes the match from the scoreboard;
* `getMatchesSummary()` - gets a summary of matches in progress ordered by their total score and then by the most recently started match;

There is also a `org.scoreboard.Main` class with a `main` method that serves as an example usage of that functionality.

The whole implementation logic is located under the `org.scoreboard.internal` package. The most important parts of it are:
* `org.scoreboard.internal.model.Match` - a record that serves as an underlying model representing a single match;
* `org.scoreboard.internal.repository.MatchRepository` - an interface that serves as a repository for storing `Match` objects;
* `org.scoreboard.internal.repository.InMemoryMatchRepository` - simple in-memory implementation ot the `MatchRepository`
that uses hash maps under the hood;
* `org.scoreboard.internal.ScoreboardImpl` - default `Scoreboard` implementation that uses the repository mentioned above
to provide its functionality, it can be also customized by calling its constructor with other `MatchRepository` implementation
or other comparator for `Match` objects to customize the order of summaries returned by it;

## Assumptions

* matches are identified by a pair of home and away team names;
* team name must be a non-empty string;
* a match where both home and away team have the same value is not valid;
* scores have to be a non-negative integer values;
* the scoreboard won't allow to start a match if any of its teams already plays a match (it's present int the scoreboard);
* provided default scoreboard implementation is not thread safe and its methods shouldn't be called concurrently by multiple threads;

## Building and testing

To be able to build it you need to have Maven 3 and JDK 17 installed on your machine.

* to build it execute `mvn clean package` command;
* there are a bunch of tests written in a TDD manner, to execute only them use `mvn test` command;
* you can play around with it by modifying provided `org.scoreboard.Main.main` method, to execute it use `mvn exec:java` command; 
