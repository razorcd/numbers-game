package com.challenge.application.game.domain;

import com.challenge.application.game.model.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class PlayerAggregateTest {

    private PlayerAggregate playerAggregate;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getRootPlayer() {
        Player player1 = new Player("p1");
        playerAggregate = new PlayerAggregate(Arrays.asList(player1, new Player("p2")), 0);

        assertEquals("Root player aggregate is player 1.", player1, playerAggregate.getRootPlayer());
    }

    @Test
    public void getNext() {
        Player player2 = new Player("p2");
        playerAggregate = new PlayerAggregate(Arrays.asList(new Player("p1"), player2), 0);

        assertEquals("When getting next aggregate, root player aggregate is player 2.", player2, playerAggregate.getNext().getRootPlayer());
    }

    @Test
    public void getNextCyclic() {
        Player player1 = new Player("p1");
        playerAggregate = new PlayerAggregate(Arrays.asList(player1, new Player("p1")), 0);

        assertEquals("When getting cyclic aggregate, root player aggregate is player 1.", player1, playerAggregate.getNext().getNext().getRootPlayer());
    }

    @Test
    public void isNotValidWithLowerNumberOfPlayers() {
        playerAggregate = new PlayerAggregate(Arrays.asList(new Player("p1")), 0);

        assertFalse("Is not valid with lower number of players than allowed.", playerAggregate.isValid());

    }

    @Test
    public void isNotValidWithHigherNumberOfPlayers() {
        playerAggregate = new PlayerAggregate(Arrays.asList(new Player("p1"), new Player("p2"), new Player("p3")), 0);

        assertFalse("Is not valid with higher number of players than allowed.", playerAggregate.isValid());

    }
}