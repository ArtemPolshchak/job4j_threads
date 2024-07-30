package ru.job4j.concurrent;

public class DCLSingleton {

    private static volatile DCLSingleton instance;

    private DCLSingleton() {}

    /**
     * Ошибка в этом коде связана с отсутствием ключевого слова "volatile" при объявлении "instance"
     * Без его двойная проверка на "null" может не работать корректно из-за особенностей многопоточности
     * @return instance
     */
    private static DCLSingleton getInstance() {
        if (instance == null) {
            synchronized (DCLSingleton.class) {
                if (instance == null) {
                    instance = new DCLSingleton();
                }
            }
        }
        return instance;
    }
}
