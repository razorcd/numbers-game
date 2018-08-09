package com.challenge;

import com.challenge.server.Client;
import com.challenge.server.Messenger;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.JVM)
public class ServerAppTest {

    private ExecutorService executorService;

    private Client client;
    private Messenger<String, String> messenger;

    @Before
    public void setUp() throws Exception {
        Thread.sleep(200L);

        executorService = new ThreadPoolExecutor(
                1, 1,
                0L, MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());

//        Future<?> underTestAppThread =
                executorService.execute(() -> {
            ServerApp.main(new String[]{});
//            new ServerApp();
        });

//        try {
//            underTestAppThread.get();
//        } catch (Exception e) {
//            tearDown();
//        }

        Thread.sleep(1000L);
//        executorService.awaitTermination(5, SECONDS); // time to allow app to start IO

        client = new Client();
        messenger = client.start("127.0.0.1", 9999);
    }

    @After
    public void tearDown() throws Exception {
        messenger.send("EXIT");
        client.stop();
        executorService.shutdown();
        executorService.awaitTermination(1, SECONDS);
        Thread.sleep(1000L);
    }

    @Test
    public void shouldReceiveConnectedWhenConnection() {
        assertEquals("Client should receive connected first.", "connected", messenger.readNextLine());
    }

    @Test
    public void shouldReceiveUnknownCommand() {
        //given connected is read
        messenger.readNextLine();

        //when
        messenger.send("some unknown command");

        //then
        assertTrue("Client should receive unknown command.", messenger.readNextLine().startsWith("unknown command"));
    }

    @Test
    public void shouldAcceptPlayers() {
        //given connected is read
        messenger.readNextLine();

        //when
        messenger.send("ADD_NEW_PLAYER:player1");
        //then
        assertEquals("Client should receive added player1.", "Added player player1 to game.", messenger.readNextLine());

        //and when
        messenger.send("ADD_NEW_PLAYER:player2");
        //then
        assertEquals("Client should receive added player2.", "Added player player2 to game.", messenger.readNextLine());
    }

    @Test
    public void shouldStartAndPlayGame() {
        //given
        messenger.readNextLine();
        messenger.send("ADD_NEW_PLAYER:player1");
        messenger.readNextLine();
        messenger.send("ADD_NEW_PLAYER:player2");
        messenger.readNextLine();

        //when
        messenger.send("START");

        //then
        assertThat("Should receive starting game by player1 with output number.",
                messenger.readNextLine(),
                matchesPattern("Player player1 started game and played a random number 80. The result is Round result: outputNumber 27, winner false."));
    }

    @Test
    public void shouldNotBeAbleToStartGameWithInvalidPlayers() {
        //given
        messenger.readNextLine();
        messenger.send("ADD_NEW_PLAYER:player1");
        messenger.readNextLine();

        //when
        messenger.send("START");

        //then
        assertEquals("Should not be able to start game with invalid players.",
            "ERROR: Can not start game with not valid players: [Player player1] and player 1 has next turn.",
            messenger.readNextLine());
    }

    @Test
    public void shouldNotBeAbleToPlayUnstartedGameWithInvalidPlayers() {
        //given
        messenger.readNextLine();

        //when
        messenger.send("PLAY:11");

        //then
        assertEquals("Should not be able to play game that was not started and has invalid players.",
                "ERROR: Player unknown: Can not play game when players: [] and player 0 has next turn.", messenger.readNextLine());
    }

    @Test
    public void shouldBeAbleToRsStartAnAlreadyInitializedGame(){
        //given
        messenger.readNextLine();
        messenger.send("ADD_NEW_PLAYER:player1");
        messenger.readNextLine();
        messenger.send("ADD_NEW_PLAYER:player2");
        messenger.readNextLine();
        messenger.send("START");
        messenger.readNextLine();
        messenger.send("PLAY:27");
        messenger.readNextLine();
        //when
        messenger.send("START");

        //then
        assertThat("Should be able to restart an already started game.",
                messenger.readNextLine(),
                matchesPattern("Player player1 started game and played a random number 80. The result is Round result: outputNumber 27, winner false."));
    }


    @Test
    public void shouldPlayGoodNumber(){
        //given
        messenger.readNextLine();
        messenger.send("ADD_NEW_PLAYER:player1");
        messenger.readNextLine();
        messenger.send("ADD_NEW_PLAYER:player2");
        messenger.readNextLine();
        messenger.send("START");
        messenger.readNextLine();

        //when
//        String result = messenger.readNextLine();
//        Matcher matcher = Pattern.compile("[0-9]+").matcher(result);
//        matcher.find();matcher.find();matcher.find();
//        String outputNumber = matcher.group();
        messenger.send("PLAY:27");

        //then
        assertThat("Should play player 2 with correct input number.",
                messenger.readNextLine(),
                matchesPattern("Player player2 played number 27. The result is Round result: outputNumber 9, winner false."));
    }

    @Test
    public void shouldNotPlayBadNumber(){
        //given
        messenger.readNextLine();
        messenger.send("ADD_NEW_PLAYER:player1");
        messenger.readNextLine();
        messenger.send("ADD_NEW_PLAYER:player2");
        messenger.readNextLine();
        messenger.send("START");
        messenger.readNextLine();

        //when
        messenger.send("PLAY:999");

        //then
        assertThat("Should not play player 2 with incorrect input number.",
                messenger.readNextLine(),
                matchesPattern("Player player2: Can not play 999 after Round result: outputNumber 27, winner false."));
    }

    @Test
    public void shouldNotPlayBadInputType(){
        //given
        messenger.readNextLine();
        messenger.send("ADD_NEW_PLAYER:player1");
        messenger.readNextLine();
        messenger.send("ADD_NEW_PLAYER:player2");
        messenger.readNextLine();
        messenger.send("START");
        messenger.readNextLine();

        //when
        messenger.send("PLAY:not_number");

        //then
        assertEquals("Should not play bad input data types.",
                "ERROR: Player player2: For input string: \"not_number\"",
                messenger.readNextLine());
    }

    @Test
    public void shouldBeAbleToGetCurrentStateOfNewGame(){
        //given
        messenger.readNextLine();

        //when
        messenger.send("STATE");

        //then
        assertThat("Should be able to get correct current state of new game.",
                messenger.readNextLine(),
                matchesPattern("Player unknown is next. Last Round result: outputNumber null, winner false."));
    }

    @Test
    public void shouldBeAbleToGetCurrentStateOfStartedGame(){
        //given
        messenger.readNextLine();
        messenger.send("ADD_NEW_PLAYER:player1");
        messenger.readNextLine();
        messenger.send("ADD_NEW_PLAYER:player2");
        messenger.readNextLine();
        messenger.send("START");
        messenger.readNextLine();

        //when
        messenger.send("STATE");

        //then
        assertThat("Should be able to get correct current state of game.",
                messenger.readNextLine(),
                matchesPattern("Player player2 is next. Last Round result: outputNumber 27, winner false."));
    }

    @Test
    public void shouldBeAbleToGetCurrentStateOfPlayingGameAfterSuccessfulAttempt(){
        //given
        messenger.readNextLine();
        messenger.send("ADD_NEW_PLAYER:player1");
        messenger.readNextLine();
        messenger.send("ADD_NEW_PLAYER:player2");
        messenger.readNextLine();
        messenger.send("START");
        messenger.readNextLine();
        messenger.send("PLAY:27");
        messenger.readNextLine();

        //when
        messenger.send("STATE");

        //then
        assertThat("Should be able to get correct current state of game after a failed attempt.",
                messenger.readNextLine(),
                matchesPattern("Player player1 is next. Last Round result: outputNumber 9, winner false."));
    }


    @Test
    public void shouldBeAbleToGetCurrentStateOfPlayingGameAfterFailedAttempt(){
        //given
        messenger.readNextLine();
        messenger.send("ADD_NEW_PLAYER:player1");
        messenger.readNextLine();
        messenger.send("ADD_NEW_PLAYER:player2");
        messenger.readNextLine();
        messenger.send("START");
        messenger.readNextLine();
        messenger.send("PLAY:27");
        messenger.readNextLine();
        messenger.send("PLAY:999");
        messenger.readNextLine();

        //when
        messenger.send("STATE");

        //then
        assertThat("Should be able to get correct current state of game after a failed attempt.",
                messenger.readNextLine(),
                matchesPattern("Player player1 is next. Last Round result: outputNumber 9, winner false."));
    }

    @Test
    public void shouldBeAbleToPlayUntilWinning() {
        //given
        messenger.readNextLine();
        messenger.send("ADD_NEW_PLAYER:player1");
        messenger.readNextLine();
        messenger.send("ADD_NEW_PLAYER:player2");
        messenger.readNextLine();
        messenger.send("START");
        messenger.readNextLine();
        messenger.send("PLAY:27");
        messenger.readNextLine();
        messenger.send("PLAY:9");
        messenger.readNextLine();

        //when
        messenger.send("PLAY:3");

        //then
        assertThat("Should be able to play until winning.",
                messenger.readNextLine(),
                matchesPattern("Player player2 played number 3. The result is Round result: outputNumber 1, winner true."));
    }

    @Test
    public void shouldNotBeAbleToPlayAfterWinning() {
        //given
        messenger.readNextLine();
        messenger.send("ADD_NEW_PLAYER:player1");
        messenger.readNextLine();
        messenger.send("ADD_NEW_PLAYER:player2");
        messenger.readNextLine();
        messenger.send("START");
        messenger.readNextLine();
        messenger.send("PLAY:27");
        messenger.readNextLine();
        messenger.send("PLAY:9");
        messenger.readNextLine();
        messenger.send("PLAY:3");
        messenger.readNextLine();

        //when
        messenger.send("PLAY:1");

        //then
        assertThat("Should not be able to play after winning.",
                messenger.readNextLine(),
                matchesPattern("ERROR: Player player1: Can not play after Round result: outputNumber 1, winner true."));
    }

    @Test
    public void shouldReceiveUnknownCommandWhenSendingUnknownCommand() {
        //given connected is read
        messenger.readNextLine();

        //when
        messenger.send("EXIT");

        //then
        assertEquals("Client should receive server shutdown.", "server shutdown", messenger.readNextLine());
    }

}