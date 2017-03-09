package de.repa.supracam.infrastructure.ftp;

import de.repa.supracam.files.model.FilesByDayDirectory;
import de.repa.supracam.files.model.ValidFileName;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.integration.file.remote.session.Session;
import org.springframework.integration.file.remote.session.SessionFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static de.repa.supracam.infrastructure.ftp.FileNameTestHelper.createAssertedValidFileName;

public class FtpClientTest {

    private Session sessionMock;
    private FtpClient ftpClient;
    SessionFactory sessionFactoryMock;

    @Before
    public void init() {
        this.sessionMock = Mockito.mock(Session.class);
        this.sessionFactoryMock = Mockito.mock(SessionFactory.class);
        Mockito.when(sessionFactoryMock.getSession()).thenReturn(sessionMock);
        ftpClient = new FtpClient(sessionFactoryMock);
    }

    @Test
    public void testGetAllFileNames() throws Exception {
        Mockito.when(sessionMock.listNames(Mockito.anyString()))
                .thenReturn(new String[]{
                        "cam/.",
                        "cam/..",
                        "cam/2017_02_12_18_29_12.jpg",
                        "cam/2017_02_12_07_36_49.jpg",
                        "cam/2017_02_10_23_05_00.jpg",
                        "cam/2017_02_11_15_38_30.jpg",
                        "cam/2017_02_11_15_46_18.jpg",
                        "cam/2017_02_11_1546_18.jpg",
                        "cam/2017_02_11_15_46_18.jpog",
                        "cam/201702_11_15_46_18.jpg"
                });
        List<ValidFileName> resultList = ftpClient.loadNamesOfPicturesInRootDir();
        Assertions.assertThat(resultList).extracting("value").containsExactlyInAnyOrder(
                "2017_02_12_18_29_12.jpg",
                "2017_02_12_07_36_49.jpg",
                "2017_02_10_23_05_00.jpg",
                "2017_02_11_15_38_30.jpg",
                "2017_02_11_15_46_18.jpg"
        );
    }

    @Test
    public void testGetEmptyListOfFiles() throws Exception {
        Mockito.when(sessionMock.listNames(Mockito.anyString())).thenReturn(new String[]{});
        List<ValidFileName> validNamesOfPicturesInRootDir = ftpClient.loadNamesOfPicturesInRootDir();
        Assertions.assertThat(validNamesOfPicturesInRootDir).isNotNull();
        Assertions.assertThat(validNamesOfPicturesInRootDir).isEmpty();
    }

    @Test
    public void testGetEmptyListOfFilesWhenResultNull() throws Exception {
        Mockito.when(sessionMock.listNames(Mockito.anyString())).thenReturn(null);
        List<ValidFileName> validNamesOfPicturesInRootDir = ftpClient.loadNamesOfPicturesInRootDir();
        Assertions.assertThat(validNamesOfPicturesInRootDir).isNotNull();
        Assertions.assertThat(validNamesOfPicturesInRootDir).isEmpty();
    }

    @Test
    public void testFtpConnectionError() throws Exception {
        Mockito.when(sessionMock.listNames(Mockito.anyString())).thenThrow(IOException.class);
        List<ValidFileName> validNamesOfPicturesInRootDir = ftpClient.loadNamesOfPicturesInRootDir();
        Assertions.assertThat(validNamesOfPicturesInRootDir).isNotNull();
        Assertions.assertThat(validNamesOfPicturesInRootDir).isEmpty();
    }

    @Test
    public void testCutFilesFromRootIntoDirectory() throws Exception {
        FilesByDayDirectory validFileNamesOfDay = FilesByDayDirectory.createValidFileNamesOfDay("2017_02_19");
        validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_19_18_09_22.jpg"));
        validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_19_22_09_22.jpg"));

        FilesByDayDirectory validFileNamesOfDay2 = FilesByDayDirectory.createValidFileNamesOfDay("2017_05_22");
        validFileNamesOfDay2.addValidFileName(createAssertedValidFileName("2017_05_22_11_22_28.jpg"));
        validFileNamesOfDay2.addValidFileName(createAssertedValidFileName("2017_05_22_10_44_25.jpg"));

        ArgumentCaptor<String> mkDirCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.when(sessionMock.mkdir(mkDirCaptor.capture())).thenReturn(true);

        ArgumentCaptor<String> writeCap = ArgumentCaptor.forClass(String.class);
        Mockito.doNothing().when(sessionMock).write(Mockito.anyObject(), writeCap.capture());

        Mockito.when(sessionMock.exists(Mockito.contains("jpg"))).thenReturn(true);

        ftpClient.cutFilesFromRootToDayDirectory(
                new HashSet<FilesByDayDirectory>(Arrays.asList(validFileNamesOfDay, validFileNamesOfDay2)));

        Assertions.assertThat(mkDirCaptor.getAllValues()).contains("cam/2017_02_19", "cam/2017_05_22");

        Assertions.assertThat(writeCap.getAllValues())
                .contains(
                        "cam/2017_05_22/2017_05_22_11_22_28.jpg",
                        "cam/2017_05_22/2017_05_22_10_44_25.jpg",
                        "cam/2017_02_19/2017_02_19_18_09_22.jpg",
                        "cam/2017_02_19/2017_02_19_22_09_22.jpg");
    }

    @Test
    public void testCutFilesAndDirectoryAlreadyExists() throws Exception {
        FilesByDayDirectory validFileNamesOfDay = FilesByDayDirectory.createValidFileNamesOfDay("2017_02_19");
        validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_19_18_09_22.jpg"));
        validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_19_22_09_22.jpg"));

        Mockito.when(sessionMock.exists(Mockito.eq("cam/2017_02_19"))).thenReturn(true);

        ftpClient.cutFilesFromRootToDayDirectory(new HashSet<FilesByDayDirectory>(Arrays.asList(validFileNamesOfDay)));

        Mockito.verify(sessionMock, Mockito.times(0)).mkdir(Mockito.anyString());
    }

    @Test
    public void testCutFilesAndDirectoryNotExists() throws Exception {
        FilesByDayDirectory validFileNamesOfDay = FilesByDayDirectory.createValidFileNamesOfDay("2017_02_19");
        validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_19_18_09_22.jpg"));
        validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_19_22_09_22.jpg"));

        Mockito.when(sessionMock.exists(Mockito.eq("cam/2017_02_19"))).thenReturn(false);

        ftpClient.cutFilesFromRootToDayDirectory(new HashSet<FilesByDayDirectory>(Arrays.asList(validFileNamesOfDay)));

        Mockito.verify(sessionMock, Mockito.times(1)).mkdir(Mockito.anyString());
    }

    @Test
    public void testCutFilesAndFileNotExists() throws Exception {
        FilesByDayDirectory validFileNamesOfDay = FilesByDayDirectory.createValidFileNamesOfDay("2017_02_19");
        validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_19_18_09_22.jpg"));
        validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_19_22_09_22.jpg"));

        Mockito.when(sessionMock.exists(Mockito.eq("cam/2017_02_19"))).thenReturn(true);

        ftpClient.cutFilesFromRootToDayDirectory(new HashSet<FilesByDayDirectory>(Arrays.asList(validFileNamesOfDay)));

        Mockito.verify(sessionMock, Mockito.times(0)).write(Mockito.anyObject(), Mockito.anyString());
    }

    @Test
    public void testCutFilesWithConnectionError() throws Exception {
        FilesByDayDirectory validFileNamesOfDay = FilesByDayDirectory.createValidFileNamesOfDay("2017_02_19");
        validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_19_18_09_22.jpg"));
        validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_19_22_09_22.jpg"));

        Mockito.when(sessionMock.exists(Mockito.anyString())).thenThrow(IOException.class);

        try {
            ftpClient.cutFilesFromRootToDayDirectory(new HashSet<FilesByDayDirectory>(Arrays.asList(validFileNamesOfDay)));
            Assertions.fail("Exception should be thrown");
        } catch (FileClientException e) {
            Assertions.assertThat(e.getCause()).isInstanceOf(IOException.class);
        }
    }
}
