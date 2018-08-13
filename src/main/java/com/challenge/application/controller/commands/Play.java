package com.challenge.application.controller.commands;

import com.challenge.application.controller.exceptionhandler.GameExceptionHandler;
import com.challenge.application.game.Game;
import com.challenge.application.game.GameManager;
import com.challenge.application.game.domain.GameRoundResult;
import com.challenge.application.game.domain.InputNumber;
import com.challenge.application.game.domain.PlayerAggregate;
import com.challenge.application.game.exception.GameException;
import com.challenge.application.game.model.Human;
import com.challenge.application.game.model.IPlayer;
import com.challenge.server.SocketChannel;

public class Play extends ChainableCommand<String> {

    private GameManager gameManager;
    private SocketChannel socketChannel;

    /**
     * Play command.
     *
     * @param gameManager holds the state of the application.
     * @param socketChannel socket adapter.
     */
    public Play(GameManager gameManager, SocketChannel socketChannel) {
        this.gameManager = gameManager;
        this.socketChannel = socketChannel;
    }

    /**
     * Execute play command.
     *
     * @param rawInputNumber the playing input number.
     */
    @Override //TODO: test
    public void execute(String rawInputNumber) {
        IPlayer authorizedPlayer = new Human(Thread.currentThread().getName(), "");  //inject authorized user
        InputNumber parsedRawInputNumber = parseRawInputNumber(rawInputNumber);

        Game gameBeforePlay = gameManager.getGame();
        PlayerAggregate playersBeforePlay = gameBeforePlay.getPlayerAggregate();

        try {
            gameManager.play(parsedRawInputNumber, authorizedPlayer);
        } catch (GameException ex) {
            new GameExceptionHandler(socketChannel).handle(ex, authorizedPlayer);
            return;
        }

        Game gameAfterPlay = gameManager.getGame();
        String message = buildFinalMessage(playersBeforePlay.getRootPlayer(), gameAfterPlay, rawInputNumber);

        socketChannel.broadcast(message);

        GameRoundResult gameRoundResultAfterPlay = gameAfterPlay.getGameRoundResult();
        doNext(String.valueOf(gameRoundResultAfterPlay.getOutputNumber().getValue()));
    }

    private String buildFinalMessage(IPlayer playingCurrentPlayer, Game gameAfterPlay, String inputNumber) {
        GameRoundResult playingRoundResult = gameAfterPlay.getGameRoundResult();

        return playingCurrentPlayer +
                " played number " +
                inputNumber +
                ". The result is " +
                playingRoundResult;
    }

    private InputNumber parseRawInputNumber(String rawInputNumber) {
        try {
            return new InputNumber(Integer.parseInt(rawInputNumber));
        } catch (NumberFormatException ex) {
            socketChannel.send("ERROR: "+ex.getMessage());
        }
        return null;
    }
}