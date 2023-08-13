package com.zic.springboot.demo.zicCoolApp.services;

import com.zic.springboot.demo.zicCoolApp.tasks.RedisPingTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class LocalFileService {
    static final String BLOG_DIRECTORY = "allBlogs";
    private static final Logger logger = LoggerFactory.getLogger(LocalFileService.class);

    private File createDirectory() {
        // Create directory
        File directory = new File(BLOG_DIRECTORY);
        if (!directory.exists()) {
            if (directory.mkdir()) {
                logger.info("Directory created successfully.");
            } else {
                logger.info("Failed to create directory.");
                return null; // Exit if unable to create directory
            }
        } else {
            logger.info("Directory already exists.");
        }
        return directory;
    }

    public void saveContent(String name, String content) {
        File directory = createDirectory();
        // Create file inside the directory and write content
        File file = new File(directory, name);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
            logger.info("File created and content written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getContent(String name) {
        Path filePath = Paths.get(BLOG_DIRECTORY, name);
        try {
            List<String> lines = Files.readAllLines(filePath);

            // Convert the list of lines to a single string
            String content = String.join("\n", lines);

            logger.info("Content of the file:");
            logger.info(content);
            return content;
        } catch (IOException e) {
            logger.error("Cannot get content of this blog");
        }
        return "";
    }

    public void deleteContent(String name) {
        Path filePath = Paths.get(BLOG_DIRECTORY, name);

        try {
            Files.delete(filePath);
            logger.info("File deleted successfully.");
        } catch (IOException e) {
            logger.info("Error deleting the file.");
        }
    }

    public static void main(String[] args) {
        new LocalFileService().saveContent("test", "test2");
        System.out.println(new LocalFileService().getContent("test"));
    }
}
