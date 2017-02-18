package de.repa.supracam.restservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rest")
public class AlertRestService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(method = RequestMethod.GET, path = "/camAlert")
    public String camMovingAlert() {
        logger.info("Received rest request for cam alert");
        return "alert received";
    }
}
