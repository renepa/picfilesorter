package de.repa.supracam.infrastructure.ftp;

import de.repa.supracam.files.model.FilesByDayDirectory;
import de.repa.supracam.files.model.ValidFileName;
import de.repa.supracam.infrastructure.FileLoadClient;
import de.repa.supracam.infrastructure.FileWriteClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.file.remote.session.Session;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FtpClient implements FileLoadClient, FileWriteClient {

    //TODO: properties file
    private static final String PATH = "cam";

    private SessionFactory ftpSessionFactory;

    @Autowired
    public FtpClient(SessionFactory sessionFactory) {
        this.ftpSessionFactory = sessionFactory;
    }

    public List<String> loadNamesOfPicturesInRootDir() {
        try (Session ftpSession = ftpSessionFactory.getSession()) {
            String[] fileNames = ftpSession.listNames(PATH);
            return extractFileNames(fileNames);
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    public void cutFilesFromRootToDayDirectory(Set<FilesByDayDirectory> fileDirectories) throws FileClientException {
        try (Session ftpSession = ftpSessionFactory.getSession()) {
            for (FilesByDayDirectory fileDirectory : fileDirectories) {
                createDirectoryIfNotExists(ftpSession, fileDirectory.getDay());
                cutFilesIntoDirectory(ftpSession, fileDirectory.getFileNames(), fileDirectory.getDay());
            }
        } catch (IOException e) {
            throw new FileClientException(e);
        }
    }

    private void cutFilesIntoDirectory(Session ftpSession, Set<ValidFileName> validFileNames, String directoryName) throws IOException {
        String directoryPath = PATH + "/" + directoryName;
        for (ValidFileName validFileName : validFileNames) {
            String sourcePath = PATH + "/" + validFileName.getValue();
            if (ftpSession.exists(sourcePath)) {
                InputStream inputStream = ftpSession.readRaw(sourcePath);
                ftpSession.write(inputStream, directoryPath + "/" + validFileName.getValue());
            }
        }
    }

    private void createDirectoryIfNotExists(Session ftpSession, String dirName) throws IOException {
        if (!ftpSession.exists(PATH + "/" + dirName)) {
            ftpSession.mkdir(PATH + "/" + dirName);
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
