package com.challenge.game;


public class InputNumber {

    private static final int LOW_BOUNDARY = 2;

    private int value;

    /**
     * Initialise a value object for input
     * @param value the value on the input
     */
    public InputNumber(int value) {
        this.value = value;
    }


    /**
     * Validates value is within expected boundaries.
     * @return [boolean] if valid
     */
    public boolean isValid() {
        return value >= LOW_BOUNDARY;
    }
}
