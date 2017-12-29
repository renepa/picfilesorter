package de.repa.filesorter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FilesorterApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilesorterApplication.class, args);
    }
}
