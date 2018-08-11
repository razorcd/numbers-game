package com.challenge.application.controller.commands;

import com.challenge.application.game.Game;
import com.challenge.application.game.GameManager;
import com.challenge.application.game.domain.GameRoundResult;
import com.challenge.application.game.domain.InputNumber;
import com.challenge.application.game.domain.OutputNumber;
import com.challenge.application.game.domain.PlayerAggregate;
import com.challenge.application.game.model.IPlayer;
import com.challenge.application.game.service.ai.IGameRoundAi;
import com.challenge.server.SocketChannel;

public class PlayMachine extends ChainableCommand<String> {

    private GameManager gameManager;
    private SocketChannel socketChannel;
    private IGameRoundAi gameRoundAi;

    /**
     * Play command for machine if it is next in turn.
     *
     * @param gameManager holds the state of the application.
     * @param socketChannel socket adapter.
     * @param gameRoundAi AI algorithm to calculate next game input for machine to play.
     */
    public PlayMachine(GameManager gameManager, SocketChannel socketChannel, IGameRoundAi gameRoundAi) {
        this.gameManager = gameManager;
        this.socketChannel = socketChannel;
        this.gameRoundAi = gameRoundAi;
    }

    /**
     * Execute play command.
     *
     * @param rawInputNumber the played input number.
     */
    @Override
    public void execute(String rawInputNumber) {
//        InputNumber inputNumber = new InputNumber(Integer.parseInt(rawInputNumber));

        playMachineRecursive();

        Game gameAfterPlay = gameManager.getGame();
        GameRoundResult gameRoundResultAfterPlay = gameAfterPlay.getGameRoundResult();
        doNext(String.valueOf(gameRoundResultAfterPlay.getOutputNumber().getValue()));
    }

    void playMachineRecursive() {
        Game gameBeforePlay = gameManager.getGame();
        PlayerAggregate playersBeforePlay = gameBeforePlay.getPlayerAggregate();
        IPlayer nextPlayer = playersBeforePlay.getRootPlayer();
        OutputNumber lastOutputNumber = gameBeforePlay.getGameRoundResult().getOutputNumber();

        if (gameBeforePlay.getGameRoundResult().isWinner()) return;

        if (nextPlayer.isAi()) {
            InputNumber calculatedNumberByAi = gameRoundAi.calculateNextInputNumberFor(lastOutputNumber);

            gameManager.play(calculatedNumberByAi, nextPlayer);

            Game gameAfterPlay = gameManager.getGame();
            String message2 = buildFinalMessage(nextPlayer, gameAfterPlay, calculatedNumberByAi.toString());

            socketChannel.broadcast(message2);

            playMachineRecursive();
        }
    }

    private String buildFinalMessage(IPlayer playingCurrentPlayer, Game gameAfterPlay, String inputNumber) {
        GameRoundResult playingRoundResult = gameAfterPlay.getGameRoundResult();

        return "\n"+playingCurrentPlayer +
                " played number " +
                inputNumber +
                ". The result is " +
                playingRoundResult;
    }
}