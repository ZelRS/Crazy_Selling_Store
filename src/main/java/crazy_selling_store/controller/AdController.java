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
        return ResponseEntity.ok(adService.getAllAds());
    }

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Добавление объявления")
    public ResponseEntity<Ad> createAd(@ModelAttribute CreateOrUpdateAd properties,
                                       @RequestPart MultipartFile image,
                                       Authentication authentication) throws IOException {
        Ad ad = adService.createAd(properties, image, authentication);
        if (ad == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(ad);

    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение информации об объявлении")
    public ResponseEntity<ExtendedAd> getAdFullInfo(@PathVariable("id") Integer id) {
        ExtendedAd extendedAd = adService.getAdFullInfo(id);
        if (extendedAd == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(extendedAd);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление объявления")
    @PreAuthorize(value = "hasRole('ADMIN') or @adServiceImpl.isAdAuthor(authentication.getName(), #id)")
    public ResponseEntity<Void> deleteAd(@PathVariable("id") Integer id) throws IOException {
        adService.deleteAd(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Обновление информации об объявлении")
    @PreAuthorize(value = "hasRole('ADMIN') or @adServiceImpl.isAdAuthor(authentication.getName(), #id)")
    public ResponseEntity<Ad> updateAdInfo(@PathVariable("id") Integer id,
                                           @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        Ad ad = adService.updateAdInfo(id, createOrUpdateAd);
        return ResponseEntity.ok(ad);
    }

    @GetMapping(value = "/me", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение объявлений авторизованного пользователя")
    public ResponseEntity<Ads> getAuthUserAds(Authentication authentication) {
        return ResponseEntity.ok(adService.getAuthUserAds(authentication));

    }

    @PatchMapping(value = "/{id}/image",
            consumes = MULTIPART_FORM_DATA_VALUE,
            produces = APPLICATION_OCTET_STREAM_VALUE)
    @Operation(summary = "Обновление картинки объявления")
    @PreAuthorize(value = "hasRole('ADMIN') or @adServiceImpl.isAdAuthor(authentication.getName(), #id)")
    public ResponseEntity<byte[]> updateAdPhoto(@PathVariable("id") Integer id,
                                                @RequestParam MultipartFile image) throws IOException {
        byte[] imageBytes = adService.updateAdPhoto(id, image);
        return ResponseEntity.ok(imageBytes);
    }
}
