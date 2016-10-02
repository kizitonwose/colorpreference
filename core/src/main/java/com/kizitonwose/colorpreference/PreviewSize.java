package com.kizitonwose.colorpreference;

/**
 * Created by Kizito Nwose on 10/2/2016.
 */
public enum PreviewSize {
    NORMAL, LARGE;

    public static PreviewSize getSize(int num) {
        switch (num) {
            case 1:
                return NORMAL;
            case 2:
                return LARGE;
            default:
                return NORMAL;
        }
    }
}
