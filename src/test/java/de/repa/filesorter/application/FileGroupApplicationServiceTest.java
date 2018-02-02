package de.repa.filesorter.application;

import de.repa.filesorter.AbstractTestWithFakeFtpServer;
import org.assertj.core.api.Assertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class FileGroupApplicationServiceTest extends AbstractTestWithFakeFtpServer {

    @Autowired
    private FileGroupApplicationService fileGroupApplicationService;

    @Test
    public void testGroupFiles() throws Exception {
        createFilesystemInFakeServer(Arrays.asList(
                "/cam/2017_02_11_15_38_30.jpg",
                "/cam/2017_02_11_15_46_18.jpg"
        ));

        fileGroupApplicationService.groupFilesByDate();
        List filesInRootPath = fakeFtpServer.getFileSystem().listNames("/cam");
        Assertions.assertThat(filesInRootPath)
                .containsExactlyInAnyOrder("2017_02_11");
    }
}
