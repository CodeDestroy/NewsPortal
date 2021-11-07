package ru.finalproject.NewsPortal.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.finalproject.NewsPortal.models.Post;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @GetMapping("/")
    public String homePage(Model model) {
        return "redirect:/news";
    }

    @GetMapping("/about")
    public String aboutpage(Model model) {
        model.addAttribute("attr", "");
        return "about";
    }


}
