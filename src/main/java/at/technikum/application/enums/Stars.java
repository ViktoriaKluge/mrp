package at.technikum.application.enums;

import at.technikum.application.exception.UnprocessableEntityException;

public enum Stars {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    ;

    private final int value;

    Stars(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Stars fromValue(int value) {
        for (Stars s : Stars.values()) {
            if (s.value == value) {
                return s;
            }
        }
        throw new UnprocessableEntityException("Stars value only between 1 and 5");
    }
}
