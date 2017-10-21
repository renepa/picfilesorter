package de.repa.filesorter.application;

import de.repa.filesorter.files.FileGroupService;
import de.repa.filesorter.files.FileLoadClient;
import de.repa.filesorter.files.FileWriteClient;
import de.repa.filesorter.files.model.FilesByDayDirectory;
import de.repa.filesorter.files.model.ValidFileName;
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
    private FileGroupService fileGroupService;

    public void camMovingAlert() {
        logger.info("Received rest request for cam alert");
        List<ValidFileName> unGroupedFilesInRootDirectory = fileLoadClient.loadValidFileNamesOfPicturesInRootDir();
        Set<FilesByDayDirectory> filesByDayDirectories = fileGroupService.groupFileNamesByDay(unGroupedFilesInRootDirectory);
        fileWriteClient.cutFilesFromRootToDayDirectory(filesByDayDirectories);
    }
}
