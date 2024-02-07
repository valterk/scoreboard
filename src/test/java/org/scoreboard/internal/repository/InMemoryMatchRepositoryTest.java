package org.scoreboard.internal.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.scoreboard.internal.model.Match;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.scoreboard.TestUtils.assertThrowsOnFirstAndSecondAndBothArgumentsAreEqualTo;

class InMemoryMatchRepositoryTest {

    private MatchRepository repository;

    private static final String HOME_TEAM1 = "homeTeam1";
    private static final String HOME_TEAM2 = "homeTeam2";
    private static final String AWAY_TEAM1 = "awayTeam1";
    private static final String AWAY_TEAM2 = "awayTeam2";
    private static final Match MATCH1 = new Match(HOME_TEAM1, AWAY_TEAM1, 0, 0, Instant.now());
    private static final Match MATCH2 = new Match(HOME_TEAM2, AWAY_TEAM2, 0, 0, Instant.now());
    private static final Match MODIFIED_MATCH1 = new Match(HOME_TEAM1, AWAY_TEAM1, 1, 0, MATCH1.startedAt());

    @BeforeEach
    public void init() {
        repository = new InMemoryMatchRepository();
    }

    @Test
    public void allMethodsWithBothTeamNamesAsArgumentsShouldThrowExceptionsForNullValues() {
        assertThrowsOnFirstAndSecondAndBothArgumentsAreEqualTo(NullPointerException.class,
                (v1, v2) -> repository.get(v1, v2), null);
        assertThrowsOnFirstAndSecondAndBothArgumentsAreEqualTo(NullPointerException.class,
                (v1, v2) -> repository.remove(v1, v2), null);
    }

    @Test
    public void saveForNullMatchShouldThrowException() {
        assertThrows(NullPointerException.class, () -> repository.save(null));
    }

    @Test
    public void saveWhenDifferentMatchForAnyOfTeamsIsAlreadyPresentShouldThrowException() {
        //given
        saveFirstMatch();

        //then
        assertThrows(IllegalStateException.class, () -> repository.save(new Match(HOME_TEAM1, "other")));
        assertThrows(IllegalStateException.class, () -> repository.save(new Match("other", HOME_TEAM1)));
        assertThrows(IllegalStateException.class, () -> repository.save(new Match("other", AWAY_TEAM1)));
        assertThrows(IllegalStateException.class, () -> repository.save(new Match(AWAY_TEAM1, "other")));
        assertThrows(IllegalStateException.class, () -> repository.save(new Match(AWAY_TEAM1, HOME_TEAM1)));
    }

    @Test
    public void containsMatchForTeamForNullTeamNameShouldThrowException() {
        assertThrows(NullPointerException.class, () -> repository.containsMatchForTeam(null));
    }

    @Test
    public void containsMatchForTeamForTeamWhoseMatchIsNotPresentShouldReturnFalse() {
        //given
        saveFirstMatch();
        saveSecondMatch();

        //when
        boolean result = repository.containsMatchForTeam("other");

        //then
        assertThat(result).isFalse();
    }

    @Test
    public void containsMatchForTeamShouldReturnTrueForBothHomeAndAwayTeamsFromPresentMatch() {
        //given
        saveFirstMatch();
        saveSecondMatch();

        //when
        boolean containsForHomeTeam = repository.containsMatchForTeam(HOME_TEAM1);
        boolean containsForAwayTeam = repository.containsMatchForTeam(AWAY_TEAM1);

        //then
        assertThat(containsForHomeTeam).isTrue();
        assertThat(containsForAwayTeam).isTrue();
    }

    @Test
    public void containsMatchForTeamShouldReturnTrueForBothHomeAndAwayTeamsFromPresentMatchThatHasBeenModified() {
        //given
        saveFirstMatch();
        saveSecondMatch();

        //when
        boolean containsForHomeTeam = repository.containsMatchForTeam(HOME_TEAM1);
        boolean containsForAwayTeam = repository.containsMatchForTeam(AWAY_TEAM1);

        //then
        assertThat(containsForHomeTeam).isTrue();
        assertThat(containsForAwayTeam).isTrue();
    }

    @Test
    public void containsMatchForTeamShouldReturnFalseForBothHomeAndAwayTeamsFromRemovedMatch() {
        //given
        saveFirstMatch();
        saveSecondMatch();

        //when
        removeFirstMatch();
        boolean containsForHomeTeam = repository.containsMatchForTeam(HOME_TEAM1);
        boolean containsForAwayTeam = repository.containsMatchForTeam(AWAY_TEAM1);

        //then
        assertThat(containsForHomeTeam).isFalse();
        assertThat(containsForAwayTeam).isFalse();
    }

    @Test
    public void getForNonExistingMatchShouldReturnEmptyResult() {
        //given
        saveFirstMatch();

        //when
        var result = repository.get("otherTeam", "yetAnotherTeam");

        //then
        assertThat(result).isEmpty();
    }

    @Test
    public void getForExistingMatchShouldReturnProperMatch() {
        //given
        saveFirstMatch();
        saveSecondMatch();

        //when
        var firstGet = getFirstMatch();
        var secondGet = getSecondMatch();

        //then
        assertThat(firstGet)
                .isPresent()
                .contains(MATCH1);
        assertThat(secondGet)
                .isPresent()
                .contains(MATCH2);
    }

    @Test
    public void getForExistingModifiedMatchShouldReturnProperMatch() {
        //given
        saveFirstMatch();
        saveSecondMatch();

        //when
        repository.save(MODIFIED_MATCH1);
        var firstGet = getFirstMatch();
        var secondGet = getSecondMatch();

        //then
        assertThat(firstGet)
                .isPresent()
                .contains(MODIFIED_MATCH1);
        assertThat(secondGet)
                .isPresent()
                .contains(MATCH2);
    }

    @Test
    public void getForRemovedMatchShouldReturnEmptyResult() {
        //given
        saveFirstMatch();
        saveSecondMatch();

        //when
        removeFirstMatch();
        var result = getFirstMatch();

        //then
        assertThat(result).isEmpty();
    }

    @Test
    public void removeNonExistingMatchShouldThrowException() {
        //given
        saveFirstMatch();

        //then
        assertThrows(IllegalStateException.class, () -> repository.remove("otherTeam", "yetAnotherTeam"));
    }

    @Test
    public void removeExistingMatchShouldRemoveProperMatch() {
        //given
        saveFirstMatch();
        saveSecondMatch();

        //when
        removeFirstMatch();
        var firstGet = getFirstMatch();
        var secondGet = getSecondMatch();

        //then
        assertThat(firstGet).isEmpty();
        assertThat(secondGet)
                .isPresent()
                .contains(MATCH2);
    }

    @Test
    public void removeExistingModifiedMatchShouldRemoveProperMatch() {
        //given
        saveFirstMatch();
        saveSecondMatch();

        //when
        repository.save(MODIFIED_MATCH1);
        removeFirstMatch();
        var firstGet = getFirstMatch();
        var secondGet = getSecondMatch();

        //then
        assertThat(firstGet).isEmpty();
        assertThat(secondGet)
                .isPresent()
                .contains(MATCH2);
    }

    @Test
    public void listAllMatchesShouldReturnEmptyListForNewlyCreatedScoreboard() {
        //when
        var allMatches = repository.listAllMatches();

        //then
        assertThat(allMatches).isEmpty();
    }

    @Test
    public void listAllMatchesShouldReturnAllSavedMatches() {
        //given
        saveFirstMatch();
        saveSecondMatch();

        //when
        var allMatches = repository.listAllMatches();

        //then
        assertThat(allMatches)
                .hasSize(2)
                .containsExactlyInAnyOrder(MATCH1, MATCH2);
    }

    @Test
    public void listAllMatchesShouldReturnAllSavedAndModifiedMatches() {
        //given
        saveFirstMatch();
        saveSecondMatch();

        //when
        repository.save(MODIFIED_MATCH1);
        var allMatches = repository.listAllMatches();

        //then
        assertThat(allMatches)
                .hasSize(2)
                .containsExactlyInAnyOrder(MODIFIED_MATCH1, MATCH2);
    }

    @Test
    public void listAllMatchesShouldReturnAllMatchesButRemoved() {
        //given
        saveFirstMatch();
        saveSecondMatch();

        //when
        removeFirstMatch();
        var allMatches = repository.listAllMatches();

        //then
        assertThat(allMatches)
                .hasSize(1)
                .containsExactlyInAnyOrder(MATCH2);
    }

    void saveFirstMatch() {
        repository.save(MATCH1);
    }

    void saveSecondMatch() {
        repository.save(MATCH2);
    }

    Optional<Match> getFirstMatch() {
        return repository.get(HOME_TEAM1, AWAY_TEAM1);
    }

    Optional<Match> getSecondMatch() {
        return repository.get(HOME_TEAM2, AWAY_TEAM2);
    }

    void removeFirstMatch() {
        repository.remove(HOME_TEAM1, AWAY_TEAM1);
    }
}