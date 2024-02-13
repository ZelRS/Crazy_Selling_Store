package crazy_selling_store.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

//класс загрузки фото
@Service
public class UploadImageService {
    //метод загрузки фото по указанному пути на сервер
    public void uploadImage(MultipartFile image, Path path) throws IOException {
        //если фото по этому пути с таким именем уже существует, то оно удаляется
        Files.deleteIfExists(path);
        //try с ресурсами с буферизацией загружает фото по заданному пути и автоматически закрывает потоки
        try (InputStream is = image.getInputStream();
             OutputStream os = Files.newOutputStream(path, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
    }
}
