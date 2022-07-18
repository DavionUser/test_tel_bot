package org.testbot;

public enum Currency {
    UAH (980),
    USD (840),
    EUR (978),
    PLN (985);

    private final int id;

    Currency(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
