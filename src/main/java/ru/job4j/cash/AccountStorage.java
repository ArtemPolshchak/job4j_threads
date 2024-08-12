package ru.job4j.cash;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class AccountStorage {

    private final ConcurrentHashMap<Integer, Account> accounts = new ConcurrentHashMap<>();

    public boolean add(Account account) {
       return accounts.putIfAbsent(account.id(), account) == null;
    }

    public boolean update(Account account) {
        return accounts.replace(account.id(), account) != null;
    }

    public void delete(int id) {
        accounts.remove(id);
    }

    public Optional<Account> getById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public boolean transfer(int fromId, int toId, int amount) {
        boolean result = false;
        Integer lowerId = Math.min(fromId, toId);
        Integer higherId = Math.max(fromId, toId);

        synchronized (lowerId) {
            synchronized (higherId) {
                Account from = accounts.get(fromId);
                Account to = accounts.get(toId);

                if (!areAccountsValid(from, to) || !hasSufficientFunds(from, amount)) {
                    return result;
                }

                accounts.compute(fromId, (id, acc) ->
                        new Account(acc.id(), acc.amount() - amount)
                );

                accounts.compute(toId, (id, acc) ->
                        new Account(acc.id(), acc.amount() + amount)
                );
                result = true;
            }
        }
        return result;
    }

    private boolean areAccountsValid(Account from, Account to) {
        return from != null && to != null;
    }

    private boolean hasSufficientFunds(Account from, int amount) {
        return from.amount() >= amount;
    }
}

