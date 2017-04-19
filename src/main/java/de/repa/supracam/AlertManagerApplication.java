package de.repa.supracam;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.ftp.session.AbstractFtpSessionFactory;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AlertManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlertManagerApplication.class, args);
    }

    @Bean
    public AbstractFtpSessionFactory<FTPClient> ftpSessionFactory() {
        AbstractFtpSessionFactory<FTPClient> sf = new DefaultFtpSessionFactory();
        sf.setHost("tinahenkensiefken.de");
        sf.setPort(21);
        sf.setUsername("f00c32bd");
        sf.setPassword("uPVUqtxMeVgCcRe2");
        return sf;
    }
}
