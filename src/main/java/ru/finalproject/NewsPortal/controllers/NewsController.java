package ru.finalproject.NewsPortal.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.finalproject.NewsPortal.models.Post;
import ru.finalproject.NewsPortal.repos.PostRepository;
import java.util.ArrayList;
import java.util.Collections;

@Controller

public class NewsController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/news")
    public String newsMain(Model model) {
        ArrayList<Post> posts1 = (ArrayList<Post>) postRepository.findAll();
        Collections.reverse(posts1);
        model.addAttribute("posts", posts1);
        return "newsMain";
    }
}
