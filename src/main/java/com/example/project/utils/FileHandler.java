package com.example.project.utils;

import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.*;

@NoArgsConstructor
public class FileHandler {

    public static String save(MultipartFile file, Long userId) {
        try {
            File directory = new File("..\\ImgStorage");

            if (!directory.exists()) {
                directory.mkdir();
            }
            Path root = Paths.get("..\\ImgStorage");
            removeIfUserHadPicture(directory,userId);
            CopyOption[] options = {StandardCopyOption.REPLACE_EXISTING};
            Files.copy(file.getInputStream(), root.resolve(userId + "-" + file.getOriginalFilename()), options);
            return file.getOriginalFilename();
        } catch (Exception e) {
            throw new RuntimeException("Cant save file error: " + e.getMessage());
        }
    }


    public static Resource load(String filename) {
        try {
            Path root = Paths.get("..\\ImgStorage");
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Cant read file");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error");
        }
    }

    public static void removeIfUserHadPicture(File directory,Long userId){
        FileFilter fileFilter = file-> file.getName().startsWith(userId+"-");
        File[] allFiles = directory.listFiles(fileFilter);
        if(allFiles != null){
            for(File file : allFiles){
                file.delete();
            }
        }
    }

}
