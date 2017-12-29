package de.repa.filesorter;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.ftp.session.AbstractFtpSessionFactory;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.stereotype.Component;

@Configuration
public class FtpClientProducer {
    @Value("${ftp.host}")
    private String host;

    @Value("${ftp.port}")
    private String port;

    @Value("${ftp.username}")
    private String username;

    @Value("${ftp.password}")
    private String password;

    @Bean
    public AbstractFtpSessionFactory<FTPClient> ftpSessionFactory() {
        AbstractFtpSessionFactory<FTPClient> sf = new DefaultFtpSessionFactory();
        sf.setHost(host);
        sf.setPort(Integer.valueOf(port));
        sf.setUsername(username);
        sf.setPassword(password);
        return sf;
    }
}
