package de.repa.supracam.ftp;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.integration.file.remote.session.Session;
import org.springframework.integration.file.remote.session.SessionFactory;

import java.io.IOException;
import java.util.List;

public class FtpClientTest {

    private Session sessionMock;
    private FtpClient ftpClient;

    @Before
    public void init() {
        this.sessionMock = Mockito.mock(Session.class);
        SessionFactory sessionFactoryMock = Mockito.mock(SessionFactory.class);
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
                        "cam/2017_02_11_15_46_18.jpg"
                });
        List<String> resultList = ftpClient.getNamesOfPicturesInRootDir();
        Assertions.assertThat(resultList).contains(
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
        List<String> validNamesOfPicturesInRootDir = ftpClient.getNamesOfPicturesInRootDir();
        Assertions.assertThat(validNamesOfPicturesInRootDir).isNotNull();
        Assertions.assertThat(validNamesOfPicturesInRootDir).isEmpty();
    }

    @Test
    public void testGetEmptyListOfFilesWhenResultNull() throws Exception {
        Mockito.when(sessionMock.listNames(Mockito.anyString())).thenReturn(null);
        List<String> validNamesOfPicturesInRootDir = ftpClient.getNamesOfPicturesInRootDir();
        Assertions.assertThat(validNamesOfPicturesInRootDir).isNotNull();
        Assertions.assertThat(validNamesOfPicturesInRootDir).isEmpty();
    }

    @Test
    public void testFtpConnectionError() throws Exception {
        Mockito.when(sessionMock.listNames(Mockito.anyString())).thenThrow(IOException.class);
        List<String> validNamesOfPicturesInRootDir = ftpClient.getNamesOfPicturesInRootDir();
        Assertions.assertThat(validNamesOfPicturesInRootDir).isNotNull();
        Assertions.assertThat(validNamesOfPicturesInRootDir).isEmpty();
    }
}
