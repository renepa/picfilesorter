package de.repa.supracam.ftp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.file.remote.session.Session;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FtpClient {

    //TODO: properties file
    private static final String PATH = "cam";

    private SessionFactory ftpSessionFactory;

    @Autowired
    public FtpClient(SessionFactory sessionFactory) {
        this.ftpSessionFactory = sessionFactory;
    }

    public List<String> getNamesOfPicturesInRootDir() {
        try (Session ftpSession = ftpSessionFactory.getSession()) {
            String[] fileNames = ftpSession.listNames(PATH);
            return extractFileNames(fileNames);
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private List<String> extractFileNames(String... fileNamesWithPath) {
        if (fileNamesWithPath != null && fileNamesWithPath.length != 0) {
            return Arrays.asList(fileNamesWithPath).stream()
                    .map(filename -> filename.substring(filename.lastIndexOf('/') + 1))
                    .filter(filename -> !filename.equals(".") && !filename.equals(".."))
                    .collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }
}
