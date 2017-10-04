package de.repa.filesorter.infrastructure.ftp;

import de.repa.filesorter.files.model.FilesByDayDirectory;
import de.repa.filesorter.files.model.ValidFileName;
import org.apache.commons.net.ftp.FTPClient;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.integration.ftp.session.AbstractFtpSessionFactory;
import org.springframework.integration.ftp.session.FtpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static de.repa.filesorter.infrastructure.ftp.FileNameTestHelper.createAssertedValidFileName;
import static org.mockito.Mockito.*;

public class FtpClientTest {

    private FtpSession sessionMock;
    private FtpClient ftpClient;
    AbstractFtpSessionFactory sessionFactoryMock;

    @Before
    public void init() throws IOException {
        this.sessionMock = mock(FtpSession.class);
        this.sessionFactoryMock = mock(AbstractFtpSessionFactory.class);
        when(sessionFactoryMock.getSession()).thenReturn(sessionMock);
        this.ftpClient = new FtpClient(sessionFactoryMock);

        FTPClient ftpClientMock = mock(FTPClient.class);
        when(sessionMock.getClientInstance()).thenReturn(ftpClientMock);
    }

    @Test
    public void testGetAllFileNames() throws Exception {
        when(sessionMock.listNames(anyString()))
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
        List<ValidFileName> resultList = ftpClient.loadValidFileNamesOfPicturesInRootDir();
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
        when(sessionMock.listNames(anyString())).thenReturn(new String[]{});
        List<ValidFileName> validNamesOfPicturesInRootDir = ftpClient.loadValidFileNamesOfPicturesInRootDir();
        Assertions.assertThat(validNamesOfPicturesInRootDir).isNotNull();
        Assertions.assertThat(validNamesOfPicturesInRootDir).isEmpty();
    }

    @Test
    public void testGetEmptyListOfFilesWhenResultNull() throws Exception {
        when(sessionMock.listNames(anyString())).thenReturn(null);
        List<ValidFileName> validNamesOfPicturesInRootDir = ftpClient.loadValidFileNamesOfPicturesInRootDir();
        Assertions.assertThat(validNamesOfPicturesInRootDir).isNotNull();
        Assertions.assertThat(validNamesOfPicturesInRootDir).isEmpty();
    }

    @Test
    public void testFtpConnectionError() throws Exception {
        when(sessionMock.listNames(anyString())).thenThrow(IOException.class);
        List<ValidFileName> validNamesOfPicturesInRootDir = ftpClient.loadValidFileNamesOfPicturesInRootDir();
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
        when(sessionMock.mkdir(mkDirCaptor.capture())).thenReturn(true);

        FTPClient ftpSessionClient = sessionMock.getClientInstance();
        ArgumentCaptor<String> renameCap = ArgumentCaptor.forClass(String.class);
        when(ftpSessionClient.rename(anyObject(), renameCap.capture())).thenReturn(true);

        when(sessionMock.exists(contains("jpg"))).thenReturn(true);

        ftpClient.cutFilesFromRootToDayDirectory(
                new HashSet(Arrays.asList(validFileNamesOfDay, validFileNamesOfDay2)));

        Assertions.assertThat(mkDirCaptor.getAllValues()).containsExactlyInAnyOrder("cam/2017_02_19", "cam/2017_05_22");

        verify(ftpSessionClient).rename(eq("cam/2017_05_22_11_22_28.jpg"), eq("cam/2017_05_22/2017_05_22_11_22_28.jpg"));
        verify(ftpSessionClient).rename(eq("cam/2017_05_22_10_44_25.jpg"), eq("cam/2017_05_22/2017_05_22_10_44_25.jpg"));
        verify(ftpSessionClient).rename(eq("cam/2017_02_19_18_09_22.jpg"), eq("cam/2017_02_19/2017_02_19_18_09_22.jpg"));
        verify(ftpSessionClient).rename(eq("cam/2017_02_19_22_09_22.jpg"), eq("cam/2017_02_19/2017_02_19_22_09_22.jpg"));
    }

    @Test
    public void testCutFilesAndDirectoryAlreadyExists() throws Exception {
        FilesByDayDirectory validFileNamesOfDay = FilesByDayDirectory.createValidFileNamesOfDay("2017_02_19");
        validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_19_18_09_22.jpg"));
        validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_19_22_09_22.jpg"));

        when(sessionMock.exists(eq("cam/2017_02_19"))).thenReturn(true);

        ftpClient.cutFilesFromRootToDayDirectory(new HashSet<FilesByDayDirectory>(Arrays.asList(validFileNamesOfDay)));

        verify(sessionMock, times(0)).mkdir(anyString());
    }

    @Test
    public void testCutFilesAndDirectoryNotExists() throws Exception {
        FilesByDayDirectory validFileNamesOfDay = FilesByDayDirectory.createValidFileNamesOfDay("2017_02_19");
        validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_19_18_09_22.jpg"));
        validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_19_22_09_22.jpg"));

        when(sessionMock.exists(eq("cam/2017_02_19"))).thenReturn(false);

        ftpClient.cutFilesFromRootToDayDirectory(new HashSet<FilesByDayDirectory>(Arrays.asList(validFileNamesOfDay)));

        verify(sessionMock, times(1)).mkdir(anyString());
    }

    @Test
    public void testCutFilesAndFileNotExists() throws Exception {
        FilesByDayDirectory validFileNamesOfDay = FilesByDayDirectory.createValidFileNamesOfDay("2017_02_19");
        validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_19_18_09_22.jpg"));
        validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_19_22_09_22.jpg"));

        when(sessionMock.exists(eq("cam/2017_02_19"))).thenReturn(true);

        ftpClient.cutFilesFromRootToDayDirectory(new HashSet<FilesByDayDirectory>(Arrays.asList(validFileNamesOfDay)));

        verify(sessionMock, times(0)).write(anyObject(), anyString());
    }

    @Test
    public void testCutFilesWithConnectionError() throws Exception {
        FilesByDayDirectory validFileNamesOfDay = FilesByDayDirectory.createValidFileNamesOfDay("2017_02_19");
        validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_19_18_09_22.jpg"));
        validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_19_22_09_22.jpg"));

        when(sessionMock.exists(anyString())).thenThrow(IOException.class);

        try {
            ftpClient.cutFilesFromRootToDayDirectory(new HashSet<FilesByDayDirectory>(Arrays.asList(validFileNamesOfDay)));
            Assertions.fail("Exception should be thrown");
        } catch (FileClientException e) {
            Assertions.assertThat(e.getCause()).isInstanceOf(IOException.class);
        }
    }
}
