package com.challenge.application.game.domain;

import com.challenge.application.game.model.Human;
import com.challenge.application.game.model.IPlayer;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PlayerAggregateTest {

    private PlayerAggregate playerAggregate;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getRootPlayer() {
        IPlayer player1 = new Human("1", "p1");
        playerAggregate = new PlayerAggregate(Arrays.asList(player1, new Human("2", "p2")), 0);

        assertEquals("Root player aggregate is player 1.", player1, playerAggregate.getRootPlayer());
    }

    @Test
    public void getNext() {
        IPlayer player2 = new Human("2", "p2");
        playerAggregate = new PlayerAggregate(Arrays.asList(new Human("1", "p1"), player2), 0);

        assertEquals("When getting next aggregate, root player aggregate is player 2.", player2, playerAggregate.next().getRootPlayer());
    }

    @Test
    public void getNextCyclic() {
        IPlayer player1 = new Human("1", "p1");
        playerAggregate = new PlayerAggregate(Arrays.asList(player1, new Human("1", "p1")), 0);

        assertEquals("When getting cyclic aggregate, root player aggregate is player 1.", player1, playerAggregate.next().next().getRootPlayer());
    }

    @Test
    public void isNotValidWithLowerNumberOfPlayers() {
        playerAggregate = new PlayerAggregate(Arrays.asList(new Human("1", "p1")), 0);

        assertFalse("Is not valid with lower number of players than allowed.", playerAggregate.isValid());

    }

    @Test
    public void isNotValidWithHigherNumberOfPlayers() {
        playerAggregate = new PlayerAggregate(Arrays.asList(new Human("1", "p1"), new Human("1", "p2"), new Human("1", "p3")), 0);

        assertFalse("Is not valid with higher number of players than allowed.", playerAggregate.isValid());

    }
}