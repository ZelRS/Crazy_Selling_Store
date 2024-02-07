package crazy_selling_store.controller;

import crazy_selling_store.dto.ads.Ad;
import crazy_selling_store.dto.ads.Ads;
import crazy_selling_store.dto.ads.CreateOrUpdateAd;
import crazy_selling_store.dto.ads.ExtendedAd;
import crazy_selling_store.service.impl.AdServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.MediaType.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/ads")
@Tag(name = "Объявления")
public class AdController {
    private final AdServiceImpl adService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение всех объявлений")
    public ResponseEntity<Ads> getAllAds() {
        log.info("Получен список всех объявлений");
        return ResponseEntity.ok(adService.getAllAds());
    }

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Добавление объявления")
    public ResponseEntity<Ad> createAd(@RequestPart CreateOrUpdateAd properties,
                                       @RequestPart MultipartFile image,
                                       Authentication authentication) throws IOException {
        log.info("Попытка создания объявления (Пользователь: " + authentication.getName() + ")");
        Ad ad = adService.createAd(properties, image, authentication);
        if (ad == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("Объявление \"" + properties.getTitle() +
                "\" успешно создано (Пользователь: " + authentication.getName() + ")");
        return ResponseEntity.status(HttpStatus.CREATED).body(ad);

    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение информации об объявлении")
    public ResponseEntity<ExtendedAd> getAdFullInfo(@PathVariable("id") Integer id,
                                                    Authentication authentication) {
        ExtendedAd extendedAd = adService.getAdFullInfo(id);
        if (extendedAd == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Полная информация об объявлении \"" + extendedAd.getTitle() +
                "\" успешно получена (Пользователь: " + authentication.getName() + ")");
        return ResponseEntity.ok(extendedAd);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление объявления")
    @PreAuthorize(value = "hasRole('ADMIN') or @adServiceImpl.isAdAuthor(authentication.getName(), #id)")
    public ResponseEntity<Void> deleteAd(@PathVariable("id") Integer id) throws IOException {
        log.info("Попытка удаления объявления");
        adService.deleteAd(id);
        log.info("Объявление успешно удалено");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Обновление информации об объявлении")
    @PreAuthorize(value = "hasRole('ADMIN') or @adServiceImpl.isAdAuthor(authentication.getName(), #id)")
    public ResponseEntity<Ad> updateAdInfo(@PathVariable("id") Integer id,
                                           @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        log.info("Попытка изменения информации об объявлении \"" + createOrUpdateAd.getTitle() + "\"");
        Ad ad = adService.updateAdInfo(id, createOrUpdateAd);
        log.info("Информация объявления \"" + createOrUpdateAd.getTitle() + "\" успешно обновлена");
        return ResponseEntity.ok(ad);
    }

    @GetMapping(value = "/me", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение объявлений авторизованного пользователя")
    public ResponseEntity<Ads> getAuthUserAds(Authentication authentication) {
        log.info("Объявления пользователя " + authentication.getName() + " успешно получены");
        return ResponseEntity.ok(adService.getAuthUserAds(authentication));

    }

    @PatchMapping(value = "/{id}/image",
            consumes = MULTIPART_FORM_DATA_VALUE,
            produces = APPLICATION_OCTET_STREAM_VALUE)
    @Operation(summary = "Обновление картинки объявления")
    @PreAuthorize(value = "hasRole('ADMIN') or @adServiceImpl.isAdAuthor(authentication.getName(), #id)")
    public ResponseEntity<byte[]> updateAdPhoto(@PathVariable("id") Integer id,
                                                @RequestParam MultipartFile image) throws IOException {
        log.info("Попытка изменения изображения объявления");
        byte[] imageBytes = adService.updateAdPhoto(id, image);
        log.info("Изображение успешно обновлено");
        return ResponseEntity.ok(imageBytes);
    }
}
