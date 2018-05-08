package de.repa.filesorter.application;

import de.repa.filesorter.domain.files.FilesByDayDirectoryFactory;
import de.repa.filesorter.domain.files.FileLoadClient;
import de.repa.filesorter.domain.files.FileWriteClient;
import de.repa.filesorter.domain.files.FilesByDayDirectory;
import de.repa.filesorter.domain.files.ValidFileName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class FileGroupApplicationService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FileLoadClient fileLoadClient;

    @Autowired
    private FileWriteClient fileWriteClient;

    @Autowired
    private FilesByDayDirectoryFactory filesByDayDirectoryFactory;

    public void groupFilesByDate() {
        logger.info("Received rest request for cam alert");
        List<ValidFileName> unGroupedFilesInRootDirectory = fileLoadClient.loadValidFileNamesOfPicturesInRootDir();
        Set<FilesByDayDirectory> filesByDayDirectories = filesByDayDirectoryFactory.createFilesByDayDirectories(unGroupedFilesInRootDirectory);
        fileWriteClient.cutFilesFromRootToDayDirectory(filesByDayDirectories);
    }
}
