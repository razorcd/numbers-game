package com.challenge.application.controller.commands;

import com.challenge.application.gameofthree.ai.IGameRoundAi;
import com.challenge.application.gameofthree.game.Game;
import com.challenge.application.gameofthree.game.IGameService;
import com.challenge.application.gameofthree.game.domain.InputNumber;
import com.challenge.application.gameofthree.game.domain.OutputNumber;
import com.challenge.application.gameofthree.game.domain.PlayerAggregate;
import com.challenge.application.gameofthree.gameround.domain.GameRoundResult;
import com.challenge.application.gameofthree.model.IPlayer;
import com.challenge.server.ISocketChannel;

public class PlayMachine extends ChainableCommand<String> {

    private IGameService gameService;
    private ISocketChannel socketChannel;
    private IGameRoundAi gameRoundAi;

    /**
     * Play command for machine if it is next in turn.
     *
     * @param gameService service to interact with running game.
     * @param socketChannel socket adapter.
     * @param gameRoundAi AI algorithm to calculate next game input for machine to play.
     */
    public PlayMachine(IGameService gameService, ISocketChannel socketChannel, IGameRoundAi gameRoundAi) {
        this.gameService = gameService;
        this.socketChannel = socketChannel;
        this.gameRoundAi = gameRoundAi;
    }

    /**
     * Execute play command for machine player.
     *
     * @param rawInputNumber the playing input number.
     */
    @Override
    public void execute(String rawInputNumber) {
        playMachineRecursive();

        Game gameAfterPlay = gameService.getGame();
        GameRoundResult gameRoundResultAfterPlay = gameAfterPlay.getGameRoundResult();
        doNext(String.valueOf(gameRoundResultAfterPlay.getOutputNumber().getValue()));
    }

    private void playMachineRecursive() {
        Game gameBeforePlay = gameService.getGame();
        PlayerAggregate playersBeforePlay = gameBeforePlay.getPlayerAggregate();
        IPlayer nextPlayer = playersBeforePlay.getRootPlayer();
        OutputNumber lastOutputNumber = gameBeforePlay.getGameRoundResult().getOutputNumber();

        if (gameBeforePlay.getGameRoundResult().isWinner()) return;

        if (nextPlayer.isAi()) {
            InputNumber calculatedNumberByAi = gameRoundAi.calculateNextInputNumberFor(lastOutputNumber);

            gameService.play(calculatedNumberByAi, nextPlayer);

            Game gameAfterPlay = gameService.getGame();
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