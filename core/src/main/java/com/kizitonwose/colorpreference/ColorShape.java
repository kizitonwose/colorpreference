package com.kizitonwose.colorpreference;

/**
 * Created by Kizito Nwose on 10/1/2016.
 */
public enum ColorShape {
    CIRCLE(1), SQUARE(2);

    private final int value;

    ColorShape(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
