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

            if (fromOpt.isEmpty() || toOpt.isEmpty()) {
                return false;
            }

            Account from = fromOpt.get();
            Account to = toOpt.get();

            if (from.amount() < amount) {
                return false;
            }

            update(new Account(from.id(), from.amount() - amount));
            update(new Account(to.id(), to.amount() + amount));

            return true;
        }
    }
}
