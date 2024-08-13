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
        synchronized (accounts) {
            var fromOpt = getById(fromId);
            var toOpt = getById(toId);

            if (fromOpt.isEmpty() || toOpt.isEmpty() || !hasSufficientFunds(fromOpt.get(), amount)) {
                return false;
            }

            updateAccount(fromOpt.get(), -amount);
            updateAccount(toOpt.get(), amount);

            return true;
        }
    }

    private boolean hasSufficientFunds(Account from, int amount) {
        return from.amount() >= amount;
    }

    private void updateAccount(Account account, int amountChange) {
        update(new Account(account.id(), account.amount() + amountChange));
    }
}

