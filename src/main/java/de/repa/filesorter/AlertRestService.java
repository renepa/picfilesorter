package de.repa.filesorter;

import de.repa.filesorter.files.FileGroupService;
import de.repa.filesorter.files.model.FilesByDayDirectory;
import de.repa.filesorter.files.model.ValidFileName;
import de.repa.filesorter.files.FileLoadClient;
import de.repa.filesorter.files.FileWriteClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("rest")
public class AlertRestService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FileLoadClient fileLoadClient;

    @Autowired
    private FileWriteClient fileWriteClient;

    @Autowired
    private FileGroupService fileGroupService;

    @RequestMapping(method = RequestMethod.GET, path = "/camAlert")
    @Async
    public String camMovingAlert() {
        logger.info("Received rest request for cam alert");
        List<ValidFileName> validFileNames = fileLoadClient.loadValidFileNamesOfPicturesInRootDir();
        Set<FilesByDayDirectory> filesByDayDirectories = fileGroupService.groupFileNamesByDay(validFileNames);
        fileWriteClient.cutFilesFromRootToDayDirectory(filesByDayDirectories);
        return "alert received";
    }
}
