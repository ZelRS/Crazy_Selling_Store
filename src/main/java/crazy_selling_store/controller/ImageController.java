package crazy_selling_store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
/**
 * Контроллер для загрузки картинок.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/src/main/resources")
@CrossOrigin(value = "http://localhost:3000")
public class ImageController {
    /**
     * Получает аватарку по указанному пути.
     *
     * @param avatar Путь к картинке.
     * @return ответ с байтами картинки.
     */
    @GetMapping(value = "/userAvatars/{avatar}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, "image/*"})
    public byte[] getAvatar(@PathVariable("avatar") String avatar) throws IOException {
        Path path = Path.of("src/main/resources/userAvatars/" + avatar);
        return Files.readAllBytes(path);
    }
    /**
     * Получает картинку по указанному пути.
     *
     * @param adPhoto Путь к картинке.
     * @return ответ с байтами картинки.
     */
    @GetMapping(value = "/adImages/{adPhoto}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, "image/*"})
    public byte[] getAdPhoto(@PathVariable("adPhoto") String adPhoto) throws IOException {
        Path path = Path.of("src/main/resources/adImages/" + adPhoto);
        return Files.readAllBytes(path);
    }
}
