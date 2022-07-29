package com.example.project.utils;

import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;

@NoArgsConstructor
public class FileHandler {




//TODO zmienic zeby do bazy danych albo samo wykrywalo sciezke
    public static String save(MultipartFile file,Long userId) {
        try{
            Path root = Paths.get("C:\\Studia\\Inzynierka\\TeamFinder\\ImgStorage");
            CopyOption[] options = { StandardCopyOption.REPLACE_EXISTING};
            Files.copy(file.getInputStream(),root.resolve(userId+"-"+file.getOriginalFilename()),options);
            return file.getOriginalFilename();
        }catch(Exception e){
            throw new RuntimeException("Cant save file error: "+e.getMessage());
        }
    }


    public static Resource load(String filename) {
        try{
            Path root = Paths.get("C:\\Studia\\Inzynierka\\TeamFinder\\ImgStorage");
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()){
                return resource;
            }else{
                throw new RuntimeException("Cant read file");
            }
        }catch(Exception e){
            throw new RuntimeException("Error");
        }
    }

}
