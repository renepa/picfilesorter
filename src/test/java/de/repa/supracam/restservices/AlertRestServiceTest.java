package de.repa.supracam.restservices;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlertRestServiceTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testCamMovingAlert() throws Exception {
        String result = testRestTemplate.getForObject("/rest/camAlert", String.class);
        Assertions.assertThat("alert received");
    }
}
