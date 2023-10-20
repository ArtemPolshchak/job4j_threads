package ru.job4j.concurrent;

public class Wget {
    public static void main(String[] args) {
        Thread thread = new Thread(
                () -> {
                    try {
                        System.out.println("Start loading ... ");

                        for (int i = 0; i < 100; i++) {
                            System.out.println("\rLoading : " + i + "%");
                            Thread.sleep(1000);
                        }
                        System.out.println("Loaded : 100%");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );
        thread.start();
        System.out.println("Main");
    }
}
