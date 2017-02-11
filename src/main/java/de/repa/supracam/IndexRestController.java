package de.repa.supracam;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class IndexRestController {
    @RequestMapping("/")
    public String index() {
        return "index.html";
    }
}