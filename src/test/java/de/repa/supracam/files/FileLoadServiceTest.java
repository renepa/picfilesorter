package de.repa.supracam.files;

import de.repa.supracam.files.model.FilesByDayDirectory;
import de.repa.supracam.infrastructure.FileLoadClient;
import junit.framework.TestCase;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class FileLoadServiceTest extends TestCase {

    private FileLoadService fileLoadService;
    private FileLoadClient fileLoadClientMock;

    @Override
    public void setUp() throws Exception {
        fileLoadClientMock = Mockito.mock(FileLoadClient.class);
        FileNameService fileNameService = new FileNameService();
        this.fileLoadService = new FileLoadService(fileLoadClientMock, fileNameService);
    }

    @Test
    public void testLoadAllFreeValidFilesInRootDir() throws Exception {
        List<String> fileList = Arrays.asList("2017_02_12_18_29_12.jpg",
                "2017_02_12_07_36_49.jpg",
                "2017_02_10_23_06_00", //not legal
                "2017_02_11_15_38_30.jpg",
                "2017_02_11_1546_18.jpg",//not legal
                "2017_02_11_15_47_18.jpg",
                "2017_02_09_18_29_12.jpg");
        Mockito.when(fileLoadClientMock.loadNamesOfPicturesInRootDir()).thenReturn(fileList);

        Set<FilesByDayDirectory> filesByDayDirectories =
                fileLoadService.loadAllFreeValidFileNamesInRootDirectoryGroupByDay();
        Assertions.assertThat(filesByDayDirectories).hasSize(3);
        Assertions.assertThat(filesByDayDirectories)
                .extracting("day")
                .contains("2017_02_12", "2017_02_11", "2017_02_09");
    }
}
