package de.repa.filesorter.infrastructure.active;

import de.repa.filesorter.application.FileGroupApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rest/fileGroupService")
public class FileGroupApplicationServiceRestAdapter {

    @Autowired
    private FileGroupApplicationService fileGroupApplicationService;

    @Async
    @RequestMapping(method = RequestMethod.GET, path = "/groupByDate")
    public void groupFilesByDate() {
        fileGroupApplicationService.groupFilesByDate();
    }

}
