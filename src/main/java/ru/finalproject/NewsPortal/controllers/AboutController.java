package ru.finalproject.NewsPortal.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {
    @GetMapping("/about")
    public String aboutpage(Model model) {
        model.addAttribute("attr", "");
        return "about";
    }
}
