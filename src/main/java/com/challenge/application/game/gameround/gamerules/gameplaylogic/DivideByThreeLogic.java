package com.challenge.application.game.gameround.gamerules.gameplaylogic;

import com.challenge.application.game.domain.GameRoundInput;
import com.challenge.application.game.domain.OutputNumber;
import com.challenge.application.game.validator.Validator;
import com.challenge.application.utils.PropertiesConfigLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulated game logic that divides input number by {@link #DIVIDER}.
 */
public class DivideByThreeLogic implements IGameRoundLogic {

    /**
     * The divider value.
     */
    private static final int DIVIDER = Integer.parseInt(PropertiesConfigLoader.getProperties()
            .getProperty("com.challenge.application.game.divider"));

    private final List<Validator<GameRoundInput>> validators;

    /**
     * Create new divide by three game logic.
     */
    public DivideByThreeLogic() {
        this.validators = new ArrayList<>();
    }

    /**
     * Apply game logic to game round input and generate output number.
     *
     * @param gameRoundInput the game round input representing user input and last round output.
     * @return [OutputNumber] the resulted output number after applying current game logic.
     */
    @Override
    public OutputNumber apply(final GameRoundInput gameRoundInput) {
        validateOrThrow(gameRoundInput);
        return applyLogic(gameRoundInput);
    }

    /**
     * Add validator to customize current algorithm.
     *
     * @param validator the game round input validator.
     */
    public void addValidator(Validator<GameRoundInput> validator) {
        validators.add(validator);
    }

    /**
     * Validates game round input with any defined validators.
     *
     * @param gameRoundInput game round input to validate to validate.
     */
    private void validateOrThrow(GameRoundInput gameRoundInput) {
        validators.forEach(validator -> validator.validateOrThrow(gameRoundInput));
    }

    private OutputNumber applyLogic(GameRoundInput gameRoundInput) {
        return new OutputNumber(gameRoundInput.getValue() / DIVIDER);
    }
}
