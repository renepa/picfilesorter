package de.repa.filesorter.infrastructure.passive.ftp;

import de.repa.filesorter.files.model.FilesByDayDirectory;
import de.repa.filesorter.files.model.ValidFileName;
import de.repa.filesorter.files.FileLoadClient;
import de.repa.filesorter.files.FileWriteClient;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //TODO: properties file
    private static final String PATH = "cam";

    private AbstractFtpSessionFactory<FTPClient> ftpSessionFactory;

    @Autowired
    public FtpClient(AbstractFtpSessionFactory<FTPClient> ftpSessionFactory) {
        this.ftpSessionFactory = ftpSessionFactory;
    }

    public List<ValidFileName> loadValidFileNamesOfPicturesInRootDir() {
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
        logger.info("Start to cut files for day {} with {} files", directoryName, validFileNames.size());
        for (ValidFileName validFileName : validFileNames) {
            String sourcePath = PATH + "/" + validFileName.getValue();
            String targetPath = targetDirectoryPath + "/" + validFileName.getValue();
            ftpSession.getClientInstance().rename(sourcePath, targetPath);
            logger.debug("Moved file: {} to {}", sourcePath, targetPath);
        }
        logger.info("Finished cut files for day {} with {} files", directoryName, validFileNames.size());
    }

    private void createDirectoryIfNotExists(Session ftpSession, String dirName) throws IOException {
        if (!ftpSession.exists(PATH + "/" + dirName)) {
            ftpSession.mkdir(PATH + "/" + dirName);
            logger.info("Created directory {}", dirName);
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
