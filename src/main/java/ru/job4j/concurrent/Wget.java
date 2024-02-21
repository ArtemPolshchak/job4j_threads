package ru.job4j.concurrent;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Wget implements Runnable {
    private final String url;
    private final int speed;
    private final String file;
    private static final int SECOND = 1000;

    public Wget(String url, int speed, String file) {
        this.url = url;
        this.speed = speed;
        this.file = file;
    }

    @Override
    public void run() {
        try (InputStream in = new URL(url).openStream();
             FileOutputStream out = new FileOutputStream(file)) {

            long start = System.currentTimeMillis();
            int totalBytesRead = 0;
            int bytesRead;

            while ((bytesRead = in.read()) != -1) {
                out.write(bytesRead);
                totalBytesRead += bytesRead;

                if (totalBytesRead >= speed) {
                    long elapsedTime = System.currentTimeMillis() - start;

                    if (elapsedTime < SECOND) {

                        long sleepTime = SECOND - elapsedTime;
                        Thread.sleep(sleepTime);

                        System.out.println("sleep for: " + sleepTime);
                        System.out.println("readBytes: " + totalBytesRead);
                    }

                    start = System.currentTimeMillis();
                    totalBytesRead = 0;
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 2) {
            throw new IllegalArgumentException("Usage: java Wget <URL> <speed>");
        }
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        String file = args[2];

        Thread wget = new Thread(new Wget(url, speed, file));
        wget.start();
        wget.join();
    }
}
