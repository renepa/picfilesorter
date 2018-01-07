package de.repa.filesorter.infrastructure.passive.ftp;

import de.repa.filesorter.files.model.FilesByDayDirectory;
import de.repa.filesorter.files.model.ValidFileName;
import org.assertj.core.api.Assertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.FileSystemEntry;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static de.repa.filesorter.infrastructure.passive.ftp.FileNameTestHelper.createAssertedValidFileName;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class FtpClientWithFakeFtpServerTest {

    @Autowired
    private FtpClient ftpClient;

    private static FakeFtpServer fakeFtpServer;

    @BeforeClass
    public static void initFakeFtpServer() {
        if (fakeFtpServer != null && fakeFtpServer.isStarted()) {
            fakeFtpServer.stop();
        }
        fakeFtpServer = new FakeFtpServer();
        fakeFtpServer.setServerControlPort(9999); // use any free port
        UserAccount userAccount = new UserAccount("user", "password", "/");
        fakeFtpServer.addUserAccount(userAccount);
        fakeFtpServer.start();
    }

    @AfterClass
    public static void deInitFakeFtpServer() {
        if (fakeFtpServer.isStarted()) {
            fakeFtpServer.stop();
        }
    }

    @Test
    public void testFtpMockServer() throws Exception {
        createEmptyFilesystemInFakeServer();
        List<ValidFileName> validFileNames = ftpClient.loadValidFileNamesOfPicturesInRootDir();
        Assertions.assertThat(validFileNames).isEmpty();
    }

    @Test
    public void testGetAllFileNames() throws Exception {
        createFilesystemInFakeServer(Arrays.asList(
                "/cam/2017_02_12_18_29_12.jpg",
                "/cam/2017_02_12_07_36_49.jpg",
                "/cam/2017_02_10_23_05_00.jpg",
                "/cam/2017_02_11_15_38_30.jpg",
                "/cam/2017_02_11_15_46_18.jpg",
                "/cam/2017_02_11_1546_18.jpg",
                "/cam/2017_02_11_15_46_18.jpog",
                "/cam/201702_11_15_46_18.jpg"
        ));

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
        createEmptyFilesystemInFakeServer();
        List<ValidFileName> validNamesOfPicturesInRootDir = ftpClient.loadValidFileNamesOfPicturesInRootDir();
        Assertions.assertThat(validNamesOfPicturesInRootDir).isNotNull();
        Assertions.assertThat(validNamesOfPicturesInRootDir).isEmpty();
    }

    @Test
    public void testFtpConnectionError() throws Exception {
        createFilesystemInFakeServer(Arrays.asList(
                "/cam/2017_02_12_18_29_12.jpg",
                "/cam/2017_02_12_07_36_49.jpg"
        ));

        fakeFtpServer.setCommandHandler("NLST", new MockFtpServerExceptionCommandHandler(new RuntimeException()));
        List<ValidFileName> validNamesOfPicturesInRootDir = ftpClient.loadValidFileNamesOfPicturesInRootDir();
        Assertions.assertThat(validNamesOfPicturesInRootDir).isNotNull();
        Assertions.assertThat(validNamesOfPicturesInRootDir).isEmpty();

        initFakeFtpServer();
    }

    @Test
    public void testCutFilesFromRootIntoDirectory() throws Exception {
        createFilesystemInFakeServer(Arrays.asList(
                "/cam/2017_02_19_18_09_22.jpg",
                "/cam/2017_02_19_22_09_22.jpg",
                "/cam/2017_05_22_11_22_28.jpg",
                "/cam/2017_05_22_10_44_25.jpg"
        ));
        FilesByDayDirectory validFileNamesOfDay = FilesByDayDirectory.createValidFileNamesOfDay("2017_02_19");
        validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_19_18_09_22.jpg"));
        validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_19_22_09_22.jpg"));

        FilesByDayDirectory validFileNamesOfDay2 = FilesByDayDirectory.createValidFileNamesOfDay("2017_05_22");
        validFileNamesOfDay2.addValidFileName(createAssertedValidFileName("2017_05_22_11_22_28.jpg"));
        validFileNamesOfDay2.addValidFileName(createAssertedValidFileName("2017_05_22_10_44_25.jpg"));

        ftpClient.cutFilesFromRootToDayDirectory(
                new HashSet(Arrays.asList(validFileNamesOfDay, validFileNamesOfDay2)));

        FileSystem fileSystem = fakeFtpServer.getFileSystem();
        Assertions.assertThat(fileSystem.exists("/cam/2017_02_19/2017_02_19_18_09_22.jpg")).isTrue();
        Assertions.assertThat(fileSystem.exists("/cam/2017_02_19/2017_02_19_22_09_22.jpg")).isTrue();
        Assertions.assertThat(fileSystem.exists("/cam/2017_05_22/2017_05_22_11_22_28.jpg")).isTrue();
        Assertions.assertThat(fileSystem.exists("/cam/2017_05_22/2017_05_22_10_44_25.jpg")).isTrue();

        Assertions.assertThat(fileSystem.exists("/cam/2017_02_19_18_09_22.jpg")).isFalse();
        Assertions.assertThat(fileSystem.exists("/cam/2017_02_19_22_09_22.jpg")).isFalse();
        Assertions.assertThat(fileSystem.exists("/cam/2017_05_22_11_22_28.jpg")).isFalse();
        Assertions.assertThat(fileSystem.exists("/cam/2017_05_22_10_44_25.jpg")).isFalse();
    }


    @Test
    public void testCutFilesAndDirectoryAlreadyExists() throws Exception {
        createFilesystemInFakeServer(Arrays.asList(
                "/cam/2017_02_19_18_09_22.jpg",
                "/cam/2017_02_19_22_09_22.jpg",
                "/cam/2017_02_19/2017_02_19_10_09_22.jpg",
                "/cam/2017_02_19/2017_02_19_11_09_22.jpg"
        ));
        FilesByDayDirectory validFileNamesOfDay = FilesByDayDirectory.createValidFileNamesOfDay("2017_02_19");
        validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_19_18_09_22.jpg"));
        validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_19_22_09_22.jpg"));

        ftpClient.cutFilesFromRootToDayDirectory(new HashSet<>(Arrays.asList(validFileNamesOfDay)));

        FileSystem fileSystem = fakeFtpServer.getFileSystem();
        Assertions.assertThat(fileSystem.exists("/cam/2017_02_19/2017_02_19_18_09_22.jpg")).isTrue();
        Assertions.assertThat(fileSystem.exists("/cam/2017_02_19/2017_02_19_22_09_22.jpg")).isTrue();
        Assertions.assertThat(fileSystem.exists("/cam/2017_02_19/2017_02_19_10_09_22.jpg")).isTrue();
        Assertions.assertThat(fileSystem.exists("/cam/2017_02_19/2017_02_19_11_09_22.jpg")).isTrue();

        Assertions.assertThat(fileSystem.exists("/cam/2017_02_19_18_09_22.jpg")).isFalse();
        Assertions.assertThat(fileSystem.exists("/cam/2017_02_19_22_09_22.jpg")).isFalse();
    }

    @Test
    public void testCutFilesAndFileNotExists() throws Exception {
        createEmptyFilesystemInFakeServer();
        FilesByDayDirectory validFileNamesOfDay = FilesByDayDirectory.createValidFileNamesOfDay("2017_02_19");
        validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_19_18_09_22.jpg"));
        validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_19_22_09_22.jpg"));

        ftpClient.cutFilesFromRootToDayDirectory(new HashSet<>(Arrays.asList(validFileNamesOfDay)));

        FileSystem fileSystem = fakeFtpServer.getFileSystem();
        Assertions.assertThat(fileSystem.exists("/cam/2017_02_19")).isTrue();
        Assertions.assertThat(fileSystem.exists("/cam/2017_02_19/2017_02_19_18_09_22.jpg")).isFalse();
        Assertions.assertThat(fileSystem.exists("/cam/2017_02_19/2017_02_19_22_09_22.jpg")).isFalse();
    }

    @Test
    public void testCutFilesAndFileAlreadyExists() throws Exception {
        FileSystem fileSystem = new UnixFakeFileSystem();
        FileEntry existing = new FileEntry("/cam/2017_02_19/2017_02_19_18_09_22.jpg");
        existing.setOwner("FIRST");
        fileSystem.add(existing);

        FileEntry toMove = new FileEntry("/cam/2017_02_19_18_09_22.jpg");
        toMove.setOwner("SECOND");
        fileSystem.add(toMove);

        fakeFtpServer.setFileSystem(fileSystem);

        FilesByDayDirectory validFileNamesOfDay = FilesByDayDirectory.createValidFileNamesOfDay("2017_02_19");
        validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_19_18_09_22.jpg"));

        ftpClient.cutFilesFromRootToDayDirectory(new HashSet<>(Arrays.asList(validFileNamesOfDay)));

        FileSystem fileSystemFromServer = fakeFtpServer.getFileSystem();
        FileSystemEntry fileInTargetDir = fileSystemFromServer.getEntry("/cam/2017_02_19/2017_02_19_18_09_22.jpg");
        Assertions.assertThat(fileInTargetDir).isNotNull();
        Assertions.assertThat(fileInTargetDir.getOwner()).isEqualTo("FIRST");

        FileSystemEntry fileInRootDir = fileSystemFromServer.getEntry("/cam/2017_02_19_18_09_22.jpg");
        Assertions.assertThat(fileInRootDir).isNotNull();
        Assertions.assertThat(fileInRootDir.getOwner()).isEqualTo("SECOND");
    }

    @Test
    public void testCutFilesWithConnectionError() throws Exception {
        createFilesystemInFakeServer(Arrays.asList(
                "/cam/2017_02_12_18_29_12.jpg",
                "/cam/2017_02_12_07_36_49.jpg"
        ));

        fakeFtpServer.setCommandHandler("NLST", new MockFtpServerExceptionCommandHandler(new RuntimeException()));

        FilesByDayDirectory validFileNamesOfDay = FilesByDayDirectory.createValidFileNamesOfDay("2017_02_19");
        validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_19_18_09_22.jpg"));
        validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_19_22_09_22.jpg"));
        try {
            ftpClient.cutFilesFromRootToDayDirectory(new HashSet<>(Arrays.asList(validFileNamesOfDay)));
            Assertions.fail("Exception should be thrown");
        } catch (FileClientException e) {
            Assertions.assertThat(e.getCause()).isInstanceOf(IOException.class);
        }
    }

    private void createFilesystemInFakeServer(List<String> fileNames) {
        FileSystem fileSystem = new UnixFakeFileSystem();
        fileNames.forEach(fileName -> fileSystem.add(new FileEntry(fileName)));
        fakeFtpServer.setFileSystem(fileSystem);
    }

    private void createEmptyFilesystemInFakeServer() {
        FileSystem fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new FileEntry("/cam"));
        fakeFtpServer.setFileSystem(fileSystem);
    }
}
