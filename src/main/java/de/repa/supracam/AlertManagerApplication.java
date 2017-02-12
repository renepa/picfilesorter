package de.repa.supracam;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;

@SpringBootApplication
public class AlertManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlertManagerApplication.class, args);
    }

    @Bean
    public SessionFactory<FTPFile> ftpSessionFactory() {
        DefaultFtpSessionFactory sf = new DefaultFtpSessionFactory();
        sf.setHost("tinahenkensiefken.de");
        sf.setPort(21);
        sf.setUsername("f00c32bd");
        sf.setPassword("uPVUqtxMeVgCcRe2");
        return new CachingSessionFactory<FTPFile>(sf);
    }
}
