package com.challenge;

import com.challenge.server.Client;
import com.challenge.server.Messenger;
import com.challenge.utils.PropertiesConfigLoader;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.JVM)
public class AcceptanceTest {

    private static final String SERVER_IP = PropertiesConfigLoader.getProperties().getProperty("com.challenge.server.ip", "127.0.0.1");
    private static final String SERVER_PORT = PropertiesConfigLoader.getProperties().getProperty("com.challenge.server.port", "9999");

    private ExecutorService executorService;

    private Client client;
    private Messenger messenger;

    @Before
    public void startServer() throws Exception {
        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() ->
            ServerApp.main(new String[]{}));

        Thread.sleep(400L); //for server under test to start

        client = new Client();
        messenger = client.start(SERVER_IP, Integer.parseInt(SERVER_PORT));
    }

    @After
    public void tearDown() throws Exception {
        messenger.send("EXIT");
        executorService.shutdown();
        executorService.awaitTermination(2, SECONDS);
        client.stop();

    }

    @Test
    public void shouldReceiveConnectedWhenConnection() {
        assertEquals("Client should receive connected first.", "connected", messenger.readNextLineSync());
    }

    @Test
    public void shouldReceiveUnknownCommand() {
        //given connected is read
        messenger.readNextLineSync();

        //when
        messenger.send("some unknown command");

        //then
        assertTrue("Client should receive unknown command.", messenger.readNextLineSync().startsWith("unknown command"));
    }

    @Test
    public void shouldAcceptPlayers() {
        //given connected is read
        messenger.readNextLineSync();

        //when
        messenger.send("ADD_PLAYER:player1");
        //then
        assertEquals("Client should receive added player1.", "Added player player1 to game.", messenger.readNextLineSync());

        //and when
        messenger.send("ADD_PLAYER:player2");
        //then
        assertEquals("Client should receive added player2.", "Added player player2 to game.", messenger.readNextLineSync());
    }

    @Test
    public void shouldStartAndPlayGame() {
        //given
        messenger.readNextLineSync();
        messenger.send("ADD_PLAYER:player1");
        messenger.readNextLineSync();
        messenger.send("ADD_PLAYER:player2");
        messenger.readNextLineSync();

        //when
        messenger.send("START");

        //then
        assertThat("Should receive starting game by player1 with output number.",
                messenger.readNextLineSync(),
                matchesPattern("Player player1 started game and played a random number 80. The result is Round result: outputNumber 27, winner false."));
    }

    @Test
    public void shouldNotBeAbleToStartGameWithInvalidPlayers() {
        //given
        messenger.readNextLineSync();
        messenger.send("ADD_PLAYER:player1");
        messenger.readNextLineSync();

        //when
        messenger.send("START");

        //then
        assertEquals("Should not be able to start game with invalid players.",
            "ERROR: can not create a game with invalid players",
            messenger.readNextLineSync());
    }

    @Test
    public void shouldNotBeAbleToPlayUnstartedGameWithInvalidPlayers() {
        //given
        messenger.readNextLineSync();

        //when
        messenger.send("PLAY:11");

        //then
        assertEquals("Should not be able to play game that was not started and has invalid players.",
                "ERROR: Player unknown: can not play game when players: [] and player 0 has next turn.", messenger.readNextLineSync());
    }

    @Test
    public void shouldBeAbleToRsStartAnAlreadyInitializedGame(){
        //given
        messenger.readNextLineSync();
        messenger.send("ADD_PLAYER:player1");
        messenger.readNextLineSync();
        messenger.send("ADD_PLAYER:player2");
        messenger.readNextLineSync();
        messenger.send("START");
        messenger.readNextLineSync();
        messenger.send("PLAY:27");
        messenger.readNextLineSync();
        //when
        messenger.send("START");

        //then
        assertThat("Should be able to restart an already started game.",
                messenger.readNextLineSync(),
                matchesPattern("Player player1 started game and played a random number 80. The result is Round result: outputNumber 27, winner false."));
    }


    @Test
    public void shouldPlayGoodNumber(){
        //given
        messenger.readNextLineSync();
        messenger.send("ADD_PLAYER:player1");
        messenger.readNextLineSync();
        messenger.send("ADD_PLAYER:player2");
        messenger.readNextLineSync();
        messenger.send("START");
        messenger.readNextLineSync();

        //when
        messenger.send("PLAY:27");

        //then
        assertThat("Should play player 2 with correct input number.",
                messenger.readNextLineSync(),
                matchesPattern("Player player2 played number 27. The result is Round result: outputNumber 9, winner false."));
    }

    @Test
    public void shouldNotPlayBadNumber(){
        //given
        messenger.readNextLineSync();
        messenger.send("ADD_PLAYER:player1");
        messenger.readNextLineSync();
        messenger.send("ADD_PLAYER:player2");
        messenger.readNextLineSync();
        messenger.send("START");
        messenger.readNextLineSync();

        //when
        messenger.send("PLAY:999");

        //then
        assertThat("Should not play player 2 with incorrect input number.",
                messenger.readNextLineSync(),
                matchesPattern("ERROR: Player player2: can not play 999 after Round result: outputNumber 27, winner false."));
    }

    @Test
    public void shouldNotPlayBadInputType(){
        //given
        messenger.readNextLineSync();
        messenger.send("ADD_PLAYER:player1");
        messenger.readNextLineSync();
        messenger.send("ADD_PLAYER:player2");
        messenger.readNextLineSync();
        messenger.send("START");
        messenger.readNextLineSync();

        //when
        messenger.send("PLAY:not_number");

        //then
        assertEquals("Should not play bad input data types.",
                "ERROR: Player player2: For input string: \"not_number\"",
                messenger.readNextLineSync());
    }

    @Test
    public void shouldNotPlayNegativeInput(){
        //given
        messenger.readNextLineSync();
        messenger.send("ADD_PLAYER:player1");
        messenger.readNextLineSync();
        messenger.send("ADD_PLAYER:player2");
        messenger.readNextLineSync();
        messenger.send("START");
        messenger.readNextLineSync();

        //when
        messenger.send("PLAY:-1");

        //then
        assertEquals("Should not play negative input.",
                "ERROR: Player player2: can not play -1 after Round result: outputNumber 27, winner false.",
                messenger.readNextLineSync());
    }

    @Test
    public void shouldBeAbleToGetCurrentStateOfNewGame(){
        //given
        messenger.readNextLineSync();

        //when
        messenger.send("STATE");

        //then
        assertThat("Should be able to get correct current state of new game.",
                messenger.readNextLineSync(),
                matchesPattern("Player unknown is next. Last Round result: outputNumber null, winner false."));
    }

    @Test
    public void shouldBeAbleToGetCurrentStateOfStartedGame(){
        //given
        messenger.readNextLineSync();
        messenger.send("ADD_PLAYER:player1");
        messenger.readNextLineSync();
        messenger.send("ADD_PLAYER:player2");
        messenger.readNextLineSync();
        messenger.send("START");
        messenger.readNextLineSync();

        //when
        messenger.send("STATE");

        //then
        assertThat("Should be able to get correct current state of game.",
                messenger.readNextLineSync(),
                matchesPattern("Player player2 is next. Last Round result: outputNumber 27, winner false."));
    }

    @Test
    public void shouldBeAbleToGetCurrentStateOfPlayingGameAfterSuccessfulAttempt(){
        //given
        messenger.readNextLineSync();
        messenger.send("ADD_PLAYER:player1");
        messenger.readNextLineSync();
        messenger.send("ADD_PLAYER:player2");
        messenger.readNextLineSync();
        messenger.send("START");
        messenger.readNextLineSync();
        messenger.send("PLAY:27");
        messenger.readNextLineSync();

        //when
        messenger.send("STATE");

        //then
        assertThat("Should be able to get correct current state of game after a failed attempt.",
                messenger.readNextLineSync(),
                matchesPattern("Player player1 is next. Last Round result: outputNumber 9, winner false."));
    }


    @Test
    public void shouldBeAbleToGetCurrentStateOfPlayingGameAfterFailedAttempt(){
        //given
        messenger.readNextLineSync();
        messenger.send("ADD_PLAYER:player1");
        messenger.readNextLineSync();
        messenger.send("ADD_PLAYER:player2");
        messenger.readNextLineSync();
        messenger.send("START");
        messenger.readNextLineSync();
        messenger.send("PLAY:27");
        messenger.readNextLineSync();
        messenger.send("PLAY:999");
        messenger.readNextLineSync();

        //when
        messenger.send("STATE");

        //then
        assertThat("Should be able to get correct current state of game after a failed attempt.",
                messenger.readNextLineSync(),
                matchesPattern("Player player1 is next. Last Round result: outputNumber 9, winner false."));
    }

    @Test
    public void shouldBeAbleToPlayUntilWinning() {
        //given
        messenger.readNextLineSync();
        messenger.send("ADD_PLAYER:player1");
        messenger.readNextLineSync();
        messenger.send("ADD_PLAYER:player2");
        messenger.readNextLineSync();
        messenger.send("START");
        messenger.readNextLineSync();
        messenger.send("PLAY:27");
        messenger.readNextLineSync();
        messenger.send("PLAY:9");
        messenger.readNextLineSync();

        //when
        messenger.send("PLAY:3");

        //then
        assertThat("Should be able to play until winning.",
                messenger.readNextLineSync(),
                matchesPattern("Player player2 played number 3. The result is Round result: outputNumber 1, winner true."));
    }

    @Test
    public void shouldNotBeAbleToPlayAfterWinning() {
        //given
        messenger.readNextLineSync();
        messenger.send("ADD_PLAYER:player1");
        messenger.readNextLineSync();
        messenger.send("ADD_PLAYER:player2");
        messenger.readNextLineSync();
        messenger.send("START");
        messenger.readNextLineSync();
        messenger.send("PLAY:27");
        messenger.readNextLineSync();
        messenger.send("PLAY:9");
        messenger.readNextLineSync();
        messenger.send("PLAY:3");
        messenger.readNextLineSync();

        //when
        messenger.send("PLAY:1");

        //then
        assertThat("Should not be able to play after winning.",
                messenger.readNextLineSync(),
                matchesPattern("ERROR: Player player1: can not play game when Round result: outputNumber 1, winner true."));
    }

    @Test
    public void shouldReceiveUnknownCommandWhenSendingUnknownCommand() {
        //given connected is read
        messenger.readNextLineSync();

        //when
        messenger.send("EXIT");

        //then
        assertEquals("Client should receive server shutdownSocket.",
                "server socket shutdown",
                messenger.readNextLineSync());
    }

}