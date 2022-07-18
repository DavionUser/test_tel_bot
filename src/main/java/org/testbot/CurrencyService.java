package org.testbot;

public interface CurrencyService {
    static CurrencyService getInstance() {
        return new HashMapCurrencyService();
    }

    Currency getCurrency(long chatId);

    void setCurrency(long chatId, Currency currency);
}
