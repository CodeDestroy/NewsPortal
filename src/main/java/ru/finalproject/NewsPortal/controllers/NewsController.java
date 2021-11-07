package ru.finalproject.NewsPortal.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.finalproject.NewsPortal.models.Post;
import ru.finalproject.NewsPortal.repos.PostRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

    @GetMapping("/news/upload")
    public String uploadNews(Model model) {

        return "uploadNews";
    }

    @PostMapping(path = "/news/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        String returnablePage = "redirect:/news";
        if (!file.isEmpty()) {
            deleteAllFilesFolder("src/main/resources/files/Unzip");
            unzip(file);
            int count = 0;
            try (Stream<Path> files = Files.list(Paths.get("src/main/resources/files/Unzip"))) {
                count = (int) files.count();
            }
            if (count == 1) {
                FileReader reader = new FileReader("src/main/resources/files/Unzip/article.txt");
                try (BufferedReader bufReader = new BufferedReader(reader)) {
                    int i = 0;
                    List<String> lines = new ArrayList<>();
                    String line = bufReader.readLine();
                    while (line != null) {
                        lines.add(line);
                        i++;
                        line = bufReader.readLine();
                    }
                    if (i < 2) {
                        model.addAttribute("error", "Not enough information in text file");
                        returnablePage = "uploadNews";
                    } else {
                        String title = lines.get(0);
                        String text = "";
                        for (int j = 1; j < lines.size(); j++) {
                            text += lines.get(j);
                            text += "\n";
                        }
                        Post post = new Post(title, text);
                        postRepository.save(post);

                    }

                } catch (IOException ex) {
                    model.addAttribute("error", "File read error");
                    returnablePage = "uploadNews";
                }
            } else {
                model.addAttribute("error", "Too many text files in zip archive");
                returnablePage = "uploadNews";
            }
        }
        return returnablePage;

    }

    public void deleteAllFilesFolder(String path) {
        for (File myFile : new File(path).listFiles())
            if (myFile.isFile()) myFile.delete();
    }

    public void unzip(MultipartFile file) throws IOException {
        ZipInputStream inputStream = new ZipInputStream(file.getInputStream());
        Path path = Paths.get("src/main/resources/files/Unzip");
        for (ZipEntry entry; (entry = inputStream.getNextEntry()) != null; ) {
            Path resolvedPath = path.resolve(entry.getName());
            if (!entry.isDirectory()) {
                Files.createDirectories(resolvedPath.getParent());
                Files.copy(inputStream, resolvedPath);
            } else {
                Files.createDirectories(resolvedPath);
            }
        }
    }

}