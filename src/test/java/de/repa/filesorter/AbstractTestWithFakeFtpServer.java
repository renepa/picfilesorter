package de.repa.filesorter;

import de.repa.filesorter.infrastructure.passive.ftp.FtpClientWithFakeFtpServerTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;

import java.util.List;

public class AbstractTestWithFakeFtpServer {

    protected static FakeFtpServer fakeFtpServer;

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

    protected void createFilesystemInFakeServer(List<String> fileNames) {
        FileSystem fileSystem = new UnixFakeFileSystem();
        fileNames.forEach(fileName -> fileSystem.add(new FileEntry(fileName)));
        fakeFtpServer.setFileSystem(fileSystem);
    }

    protected void createEmptyFilesystemInFakeServer() {
        FileSystem fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new FileEntry("/cam"));
        fakeFtpServer.setFileSystem(fileSystem);
    }
}
