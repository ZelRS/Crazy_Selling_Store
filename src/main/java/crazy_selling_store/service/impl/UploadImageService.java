package crazy_selling_store.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * Класс для загрузки изображений.
 */
@Service
public class UploadImageService {

    /**
     * Метод загрузки изображения по указанному пути на сервер.
     *
     * @param image изображение для загрузки
     * @param path  путь для сохранения изображения
     * @throws IOException если происходит ошибка ввода-вывода при загрузке изображения
     */
    @Transactional
    public void uploadImage(MultipartFile image, Path path) throws IOException {
        // Если изображение по указанному пути с таким именем уже существует, оно удаляется
        Files.deleteIfExists(path);

        // Try с ресурсами с буферизацией загружает изображение по заданному пути и автоматически закрывает потоки
        try (InputStream is = image.getInputStream();
             OutputStream os = Files.newOutputStream(path, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
    }
}
