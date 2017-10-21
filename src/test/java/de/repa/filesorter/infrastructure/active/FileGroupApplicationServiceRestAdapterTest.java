package de.repa.filesorter.infrastructure.active;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileGroupApplicationServiceRestAdapterTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testCamMovingAlert() throws Exception {
        testRestTemplate.getForObject("/rest/fileGroupService/groupByDate", String.class);
        Assertions.assertThat("alert received");
    }
}
