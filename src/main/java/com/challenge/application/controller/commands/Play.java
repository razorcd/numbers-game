package com.challenge.application.controller.commands;

import com.challenge.application.controller.exceptionhandler.GameExceptionHandler;
import com.challenge.application.gameofthree.exception.GameException;
import com.challenge.application.gameofthree.game.Game;
import com.challenge.application.gameofthree.game.GameService;
import com.challenge.application.gameofthree.game.domain.InputNumber;
import com.challenge.application.gameofthree.game.domain.PlayerAggregate;
import com.challenge.application.gameofthree.gameround.domain.GameRoundResult;
import com.challenge.application.gameofthree.model.Human;
import com.challenge.application.gameofthree.model.IPlayer;
import com.challenge.server.SocketChannel;

public class Play extends ChainableCommand<String> {

    private GameService gameService;
    private SocketChannel socketChannel;

    /**
     * Play command.
     *
     * @param gameService service to interact with running game.
     * @param socketChannel socket adapter.
     */
    public Play(GameService gameService, SocketChannel socketChannel) {
        this.gameService = gameService;
        this.socketChannel = socketChannel;
    }

    /**
     * Execute play command.
     *
     * @param rawInputNumber the playing input number.
     */
    @Override
    public void execute(String rawInputNumber) {
        IPlayer authorizedPlayer = new Human(Thread.currentThread().getName(), "");  //inject authorized user
        InputNumber parsedRawInputNumber = parseRawInputNumber(rawInputNumber);

        Game gameBeforePlay = gameService.getGame();
        PlayerAggregate playersBeforePlay = gameBeforePlay.getPlayerAggregate();

        try {
            gameService.play(parsedRawInputNumber, authorizedPlayer);
        } catch (GameException ex) {
            new GameExceptionHandler(socketChannel).handle(ex, authorizedPlayer);
            return;
        }

        Game gameAfterPlay = gameService.getGame();
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