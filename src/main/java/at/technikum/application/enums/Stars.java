package at.technikum.application.enums;

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
}
