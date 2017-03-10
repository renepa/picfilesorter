package de.repa.supracam.infrastructure.ftp;

import de.repa.supracam.files.model.FilesByDayDirectory;
import de.repa.supracam.files.model.ValidFileName;
import de.repa.supracam.infrastructure.FileLoadClient;
import de.repa.supracam.infrastructure.FileWriteClient;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.file.remote.session.Session;
import org.springframework.integration.ftp.session.AbstractFtpSessionFactory;
import org.springframework.integration.ftp.session.FtpSession;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FtpClient implements FileLoadClient, FileWriteClient {

    //TODO: properties file
    private static final String PATH = "cam";

    private AbstractFtpSessionFactory<FTPClient> ftpSessionFactory;

    @Autowired
    public FtpClient(AbstractFtpSessionFactory<FTPClient> ftpSessionFactory) {
        this.ftpSessionFactory = ftpSessionFactory;
    }

    public List<ValidFileName> loadNamesOfPicturesInRootDir() {
        try (Session ftpSession = ftpSessionFactory.getSession()) {
            String[] fileNames = ftpSession.listNames(PATH);
            List<String> extractedFilenames = extractFileNames(fileNames);
            return filerValidFileNames(extractedFilenames);
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    public void cutFilesFromRootToDayDirectory(Set<FilesByDayDirectory> fileDirectories) throws FileClientException {
        try (FtpSession ftpSession = ftpSessionFactory.getSession()) {
            for (FilesByDayDirectory fileDirectory : fileDirectories) {
                createDirectoryIfNotExists(ftpSession, fileDirectory.getDay());
                cutFilesIntoDirectory(ftpSession, fileDirectory.getFileNames(), fileDirectory.getDay());
            }
        } catch (IOException e) {
            throw new FileClientException(e);
        }
    }

    private void cutFilesIntoDirectory(FtpSession ftpSession, Set<ValidFileName> validFileNames, String directoryName) throws IOException {
        String targetDirectoryPath = PATH + "/" + directoryName;
        for (ValidFileName validFileName : validFileNames) {
            String sourcePath = PATH + "/" + validFileName.getValue();
            if (ftpSession.exists(sourcePath)) {
                String targetPath = targetDirectoryPath + "/" + validFileName.getValue();
                ftpSession.getClientInstance().rename(sourcePath, targetPath);
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

    private List<ValidFileName> filerValidFileNames(List<String> fileNamesToValidate) {
        List<ValidFileName> resultList = new ArrayList<>();
        if (fileNamesToValidate != null) {
            for (String fileName : fileNamesToValidate) {
                Optional<ValidFileName> build = ValidFileName.builder().build(fileName);
                if (build.isPresent()) {
                    resultList.add(build.get());
                }
            }
        }
        return resultList;
    }
}
