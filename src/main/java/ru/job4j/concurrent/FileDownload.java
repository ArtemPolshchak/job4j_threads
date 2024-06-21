package ru.job4j.concurrent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class FileDownload {
    public static void main(String[] args) throws Exception {
        var startAt = System.currentTimeMillis();
        System.out.println("startAt: = " + startAt);
        var file = new File("tmp.xml");
        String url = "https://raw.githubusercontent.com/peterarsentev/course_test/master/pom.xml";
        try (var in = new URL(url).openStream();
             var out = new FileOutputStream(file)) {

            System.out.println("System.currentTimeMills: = " + System.currentTimeMillis());
            System.out.println("System.currentTimeMills - startAt: = " + (System.currentTimeMillis() - startAt) + " ms");
            System.out.println("Open connection: " + (System.currentTimeMillis() - startAt) + " ms");

            var dataBuffer = new byte[512];

            int bytesRead;

            while ((bytesRead = in.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                System.out.println("bytesRead =" + bytesRead);
                //System.out.println("Write 512 bytes : " + (System.currentTimeMillis() - startAt) + " ms");
                System.out.println("dataBuffer.length = " + dataBuffer.length);
                var downloadAt = System.nanoTime();
                out.write(dataBuffer, 0, bytesRead);
                System.out.println("Read 512 bytes : " + (System.nanoTime() - downloadAt) + " nano.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(Files.size(file.toPath()) + " bytes");
    }
}
