package de.repa.supracam.files;

import de.repa.supracam.files.model.FilesByDayDirectory;
import de.repa.supracam.files.model.ValidFileName;
import de.repa.supracam.infrastructure.FileLoadClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class FileLoadService {

    private FileLoadClient fileLoadClient;
    private FileNameService fileNameService;

    @Autowired
    public FileLoadService(FileLoadClient fileLoadClient, FileNameService fileNameService) {
        this.fileLoadClient = fileLoadClient;
        this.fileNameService = fileNameService;
    }

    public Set<FilesByDayDirectory> loadAllFreeValidFileNamesInRootDirectoryGroupByDay() {
        List<String> fileNameList = fileLoadClient.loadNamesOfPicturesInRootDir();
        List<ValidFileName> validFileNames = fileNameService.filerValidFileNames(fileNameList);
        return fileNameService.groupFileNamesByDay(validFileNames);
    }

}
