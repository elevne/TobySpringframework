package org.example.fifthchapter;

public enum Level {

    GOLD(3, null),
    SILVER(2, GOLD),
    BASIC(1, SILVER);

    private final int value;
    private final Level next;

    Level(int value, Level next) {
        this.value = value;
        this.next = next;
    }

    public Level next() {
        return this.next;
    }

    public int getValue() {
        return value;
    }

    public static Level valueOf(int value) {
        switch (value) {
            case 1: return Level.BASIC;
            case 2: return Level.SILVER;
            case 3: return Level.GOLD;
            default: throw new AssertionError("Unknown value: " + value);
        }
    }

}
