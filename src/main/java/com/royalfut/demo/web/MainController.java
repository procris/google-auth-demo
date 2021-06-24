package com.royalfut.demo.web;

import org.springframework.stereotype.Controller;

@Controller
public class MainController {
    private String index() {
        return "index";
    }
}
