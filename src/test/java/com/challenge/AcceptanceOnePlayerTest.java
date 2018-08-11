//package com.challenge;
//
//import com.challenge.server.Client;
//import com.challenge.server.SocketChannel;
//import com.challenge.application.utils.PropertiesConfigLoader;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runners.MethodSorters;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import static java.util.concurrent.TimeUnit.MILLISECONDS;
//import static org.hamcrest.Matchers.matchesPattern;
//import static org.junit.Assert.*;
//
//@FixMethodOrder(MethodSorters.JVM)
//public class AcceptanceOnePlayerTest {
//    static {
//        PropertiesConfigLoader.initialize("application.properties");
//    }
//    private static final String SERVER_IP = PropertiesConfigLoader.getProperties().getProperty("com.challenge.server.ip");
//    private static final String SERVER_PORT = PropertiesConfigLoader.getProperties().getProperty("com.challenge.server.port");
//
//    private ExecutorService executorService;
//
//    private Client testClient1;
//    private SocketChannel socketPlayer1;
//
//    /**
//     * Starts application under test in separate thread and connects through tcp/ip.
//     */
//    @Before
//    public void startServer() throws Exception {
//        executorService = Executors.newSingleThreadExecutor();
//        executorService.execute(() ->
//            ServerApp.main(new String[]{}));
//
//        //wait for server under test to start external sockets connections.
//        Thread.sleep(100);
//
//        //start test testClient1
//        testClient1 = new Client();
//        socketPlayer1 = testClient1.start(SERVER_IP, Integer.parseInt(SERVER_PORT));
//
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        socketPlayer1.send("EXIT");
//
//        executorService.shutdown();
//        executorService.awaitTermination(100, MILLISECONDS); // waiting max time for server under test to shutdown
//        executorService.shutdownNow();
//
//        testClient1.stop();
//    }
//
//    @Test
//    public void shouldReceiveConnectedWhenConnection() {
//        assertEquals("Player 1 should receive connected first.", "connected", socketPlayer1.readNextLineSync());
//    }
//
//    @Test
//    public void shouldReceiveUnknownCommand() {
//        //given connected is read
//        socketPlayer1.readNextLineSync();
//
//        //when
//        socketPlayer1.send("some unknown command");
//
//        //then
//        assertTrue("Client should receive unknown command.", socketPlayer1.readNextLineSync().startsWith("unknown command"));
//    }
//
//    @Test
//    public void shouldAcceptPlayers() {
//        //given connected is read
//        socketPlayer1.readNextLineSync();
//
//        //when
//        socketPlayer1.send("ADD_PLAYER:player1");
//        //then
//        assertEquals("Client should receive added player1.", "Added player player1 to game.", socketPlayer1.readNextLineSync());
//
//        //and when
//        socketPlayer1.send("ADD_PLAYER:player2");
//        //then
//        assertEquals("Client should receive added player2.", "Added player player2 to game.", socketPlayer1.readNextLineSync());
//    }
//
//    @Test
//    public void shouldStartAndPlayGame() {
//        //given
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("ADD_PLAYER:player1");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("ADD_PLAYER:player2");
//        socketPlayer1.readNextLineSync();
//
//        //when
//        socketPlayer1.send("START");
//
//        //then
//        assertThat("Should receive starting game by player1 with output number.",
//                socketPlayer1.readNextLineSync(),
//                matchesPattern("Player player1 started game and played a random number 80. The result is Round result: outputNumber 27, winner false."));
//    }
//
//    @Test
//    public void shouldNotBeAbleToStartGameWithInvalidPlayers() {
//        //given
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("ADD_PLAYER:player1");
//        socketPlayer1.readNextLineSync();
//
//        //when
//        socketPlayer1.send("START");
//
//        //then
//        assertEquals("Should not be able to start game with invalid players.",
//            "ERROR: can not create a game with invalid players",
//            socketPlayer1.readNextLineSync());
//    }
//
//    @Test
//    public void shouldNotBeAbleToPlayUnstartedGameWithInvalidPlayers() {
//        //given
//        socketPlayer1.readNextLineSync();
//
//        //when
//        socketPlayer1.send("PLAY:11");
//
//        //then
//        assertEquals("Should not be able to play game that was not started and has invalid players.",
//                "ERROR: Player unknown: can not play game when players: [] and player 0 has next turn.", socketPlayer1.readNextLineSync());
//    }
//
//    @Test
//    public void shouldBeAbleToRsStartAnAlreadyInitializedGame(){
//        //given
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("ADD_PLAYER:player1");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("ADD_PLAYER:player2");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("START");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("PLAY:27");
//        socketPlayer1.readNextLineSync();
//        //when
//        socketPlayer1.send("START");
//
//        //then
//        assertThat("Should be able to restart an already started game.",
//                socketPlayer1.readNextLineSync(),
//                matchesPattern("Player player1 started game and played a random number 80. The result is Round result: outputNumber 27, winner false."));
//    }
//
//
//    @Test
//    public void shouldPlayGoodNumber(){
//        //given
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("ADD_PLAYER:player1");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("ADD_PLAYER:player2");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("START");
//        socketPlayer1.readNextLineSync();
//
//        //when
//        socketPlayer1.send("PLAY:27");
//
//        //then
//        assertThat("Should play player 2 with correct input number.",
//                socketPlayer1.readNextLineSync(),
//                matchesPattern("Player player2 played number 27. The result is Round result: outputNumber 9, winner false."));
//    }
//
//    @Test
//    public void shouldNotPlayBadNumber(){
//        //given
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("ADD_PLAYER:player1");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("ADD_PLAYER:player2");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("START");
//        socketPlayer1.readNextLineSync();
//
//        //when
//        socketPlayer1.send("PLAY:999");
//
//        //then
//        assertThat("Should not play player 2 with incorrect input number.",
//                socketPlayer1.readNextLineSync(),
//                matchesPattern("ERROR: Player player2: can not play 999 after Round result: outputNumber 27, winner false."));
//    }
//
//    @Test
//    public void shouldNotPlayBadInputType(){
//        //given
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("ADD_PLAYER:player1");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("ADD_PLAYER:player2");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("START");
//        socketPlayer1.readNextLineSync();
//
//        //when
//        socketPlayer1.send("PLAY:not_number");
//
//        //then
//        assertEquals("Should not play bad input data types.",
//                "ERROR: Player player2: For input string: \"not_number\"",
//                socketPlayer1.readNextLineSync());
//    }
//
//    @Test
//    public void shouldNotPlayNegativeInput(){
//        //given
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("ADD_PLAYER:player1");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("ADD_PLAYER:player2");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("START");
//        socketPlayer1.readNextLineSync();
//
//        //when
//        socketPlayer1.send("PLAY:-1");
//
//        //then
//        assertEquals("Should not play negative input.",
//                "ERROR: Player player2: can not play -1 after Round result: outputNumber 27, winner false.",
//                socketPlayer1.readNextLineSync());
//    }
//
//    @Test
//    public void shouldBeAbleToGetCurrentStateOfNewGame(){
//        //given
//        socketPlayer1.readNextLineSync();
//
//        //when
//        socketPlayer1.send("STATE");
//
//        //then
//        assertThat("Should be able to get correct current state of new game.",
//                socketPlayer1.readNextLineSync(),
//                matchesPattern("Player unknown is next. Last Round result: outputNumber null, winner false."));
//    }
//
//    @Test
//    public void shouldBeAbleToGetCurrentStateOfStartedGame(){
//        //given
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("ADD_PLAYER:player1");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("ADD_PLAYER:player2");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("START");
//        socketPlayer1.readNextLineSync();
//
//        //when
//        socketPlayer1.send("STATE");
//
//        //then
//        assertThat("Should be able to get correct current state of game.",
//                socketPlayer1.readNextLineSync(),
//                matchesPattern("Player player2 is next. Last Round result: outputNumber 27, winner false."));
//    }
//
//    @Test
//    public void shouldBeAbleToGetCurrentStateOfPlayingGameAfterSuccessfulAttempt(){
//        //given
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("ADD_PLAYER:player1");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("ADD_PLAYER:player2");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("START");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("PLAY:27");
//        socketPlayer1.readNextLineSync();
//
//        //when
//        socketPlayer1.send("STATE");
//
//        //then
//        assertThat("Should be able to get correct current state of game after a failed attempt.",
//                socketPlayer1.readNextLineSync(),
//                matchesPattern("Player player1 is next. Last Round result: outputNumber 9, winner false."));
//    }
//
//
//    @Test
//    public void shouldBeAbleToGetCurrentStateOfPlayingGameAfterFailedAttempt(){
//        //given
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("ADD_PLAYER:player1");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("ADD_PLAYER:player2");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("START");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("PLAY:27");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("PLAY:999");
//        socketPlayer1.readNextLineSync();
//
//        //when
//        socketPlayer1.send("STATE");
//
//        //then
//        assertThat("Should be able to get correct current state of game after a failed attempt.",
//                socketPlayer1.readNextLineSync(),
//                matchesPattern("Player player1 is next. Last Round result: outputNumber 9, winner false."));
//    }
//
//    @Test
//    public void shouldBeAbleToPlayUntilWinning() {
//        //given
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("ADD_PLAYER:player1");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("ADD_PLAYER:player2");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("START");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("PLAY:27");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("PLAY:9");
//        socketPlayer1.readNextLineSync();
//
//        //when
//        socketPlayer1.send("PLAY:3");
//
//        //then
//        assertThat("Should be able to play until winning.",
//                socketPlayer1.readNextLineSync(),
//                matchesPattern("Player player2 played number 3. The result is Round result: outputNumber 1, winner true."));
//    }
//
//    @Test
//    public void shouldNotBeAbleToPlayAfterWinning() {
//        //given
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("ADD_PLAYER:player1");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("ADD_PLAYER:player2");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("START");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("PLAY:27");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("PLAY:9");
//        socketPlayer1.readNextLineSync();
//        socketPlayer1.send("PLAY:3");
//        socketPlayer1.readNextLineSync();
//
//        //when
//        socketPlayer1.send("PLAY:1");
//
//        //then
//        assertThat("Should not be able to play after winning.",
//                socketPlayer1.readNextLineSync(),
//                matchesPattern("ERROR: Player player1: can not play game when Round result: outputNumber 1, winner true."));
//    }
//
//    @Test
//    public void shouldReceiveUnknownCommandWhenSendingUnknownCommand() {
//        //given connected is read
//        socketPlayer1.readNextLineSync();
//
//        //when
//        socketPlayer1.send("EXIT");
//
//        //then
//        assertEquals("Client should receive server shutdownSocket.",
//                "Player unknown exited the game.",
//                socketPlayer1.readNextLineSync());
//    }
//
//}