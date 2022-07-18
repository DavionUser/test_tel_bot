package org.testbot;

import java.util.HashMap;
import java.util.Map;

public class HashMapCurrencyService implements CurrencyService {
    private final Map<Long, Currency> currentCurrency = new HashMap<>();

    public HashMapCurrencyService() {
        System.out.println("HASHMAP MODE is created");
    }

    @Override
    public Currency getCurrency(long chatId) {
        return currentCurrency.getOrDefault(chatId, Currency.USD);
    }

    @Override
    public void setCurrency(long chatId, Currency currency) {
        currentCurrency.put(chatId, currency);
    }
}