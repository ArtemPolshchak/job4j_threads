package ru.job4j.concurrent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Wget implements Runnable {
    private final String url;
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        long startAt = System.currentTimeMillis();
        File file = new File("tmp.xml");

        try (InputStream in = new URL(url).openStream();
             FileOutputStream out = new FileOutputStream(file)) {

            System.out.println("Open connection: System.currentTimeMills:" + (System.currentTimeMillis() - startAt) + " ms");
            System.out.println("Desired download speed: " + speed + " bytes/s");

            byte[] dataBuffer = new byte[512];
            int bytesRead;

            while ((bytesRead = in.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                long  downloadAt = System.nanoTime();
                out.write(dataBuffer, 0, bytesRead);
                long  downloadTime = System.nanoTime() - downloadAt;
                long  downloadedBytes = dataBuffer.length * 1_000_000 / downloadTime;

                System.out.println("Read " + bytesRead + " bytes in " + downloadTime + " nanoseconds.");
                System.out.println("Downloaded " + downloadedBytes + " bytes.");

                long sleepTime = downloadedBytes / speed;

                if (downloadedBytes > speed) {
                    System.out.println("Sleeping for " + sleepTime + " milliseconds.");
                    Thread.sleep(sleepTime);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 2) {
            System.out.println("Usage: java Wget <URL> <speed>");
            return;
        }
        String url = args[0];
        int speed = Integer.parseInt(args[1]);

        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }
}
