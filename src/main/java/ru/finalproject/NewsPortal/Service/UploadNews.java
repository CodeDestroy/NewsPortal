package ru.finalproject.NewsPortal.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.finalproject.NewsPortal.models.Post;
import ru.finalproject.NewsPortal.repos.PostRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Controller
public class UploadNews {
    @Autowired
    private PostRepository postRepository;
    private String unzipPath = "C:\\UploadedNews";
    public void deleteAllFilesFolder(String path) {
        Arrays.stream(new File(path).listFiles()).filter(File::isFile).forEach(File::delete);
    }

    @PostMapping
    public void unzip(MultipartFile file) throws IOException {
        ZipInputStream inputStream = new ZipInputStream(file.getInputStream());
        Path path = Paths.get(unzipPath);
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


    @PostMapping(path = "/news/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) throws IOException {

        System.setProperty("file.encoding", "UTF-8");
        String returnablePage = "redirect:/news";
        if (!file.isEmpty()) {
            unzip(file);
            System.out.print(file.getName());
            int count = 0;
            try (Stream<Path> files = Files.list(Paths.get(unzipPath))) {
                count = (int) files.count();
                System.out.print("Count files: " + count);
            }
            if (count == 1) {
                FileReader reader = new FileReader(unzipPath + "/article.txt", StandardCharsets.UTF_8);
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
}
