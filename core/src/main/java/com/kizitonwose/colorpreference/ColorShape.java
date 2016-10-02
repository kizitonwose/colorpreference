package com.kizitonwose.colorpreference;

import java.io.Serializable;

/**
 * Created by Kizito Nwose on 10/1/2016.
 */
public enum ColorShape implements Serializable {
    CIRCLE(1), SQUARE(2);

    private final int value;

    ColorShape(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ColorShape getShape(int num) {
        switch (num) {
            case 1:
                return CIRCLE;
            case 2:
                return SQUARE;
            default:
                return CIRCLE;
        }
    }
}
