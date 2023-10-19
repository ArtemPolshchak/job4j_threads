package ru.job4j.concurrent;

public class ThreadState {
    public static void main(String[] args) {
        Thread first = new Thread(
                () -> System.out.println("First thread name is = " + Thread.currentThread().getName())
        );

        Thread second = new Thread(
                () -> System.out.println("Second thread name is = " + Thread.currentThread().getName())
        );

        System.out.println("State of first thread = " + first.getState());
        System.out.println("State of second thread = " + second.getState());

        first.start();
        second.start();

        while((first.getState() != Thread.State.TERMINATED) && (second.getState() != Thread.State.TERMINATED)) {
            System.out.println("First thread = " + first.getState());
            System.out.println("Second thread = " + second.getState());
        }

        System.out.println("State of first thread = " + first.getState());
        System.out.println("State of second thread = " + second.getState());
        System.out.println("The work of thread has done");
    }
}
