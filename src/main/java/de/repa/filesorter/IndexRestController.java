package de.repa.filesorter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexRestController {
    @RequestMapping("/")
    public String index() {
        return "index.html";
    }
}