package ru.finalproject.NewsPortal.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.finalproject.NewsPortal.models.Post;
import ru.finalproject.NewsPortal.repos.PostRepository;

@Controller
public class NewsUploadController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/news/upload")
    public String uploadNews(Model model) {
        return "uploadNews";
    }
    @GetMapping(path = "/news/upload2")
    public String uploadNewsByText(Model model){
        return "uploadNewsByText";
    }

    @PostMapping(path = "/news/upload2")
    public String newsAdd (@RequestParam String title, @RequestParam String text, Model model){
        Post post = new Post(title, text);
        postRepository.save(post);
        return "redirect:/news";
    }
}
