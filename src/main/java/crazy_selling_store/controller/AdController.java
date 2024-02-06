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

    //полностью исправен
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение всех объявлений")
    public ResponseEntity<Ads> getAllAds() {
        return ResponseEntity.ok(adService.getAllAds());
    }

    //полностью исправен
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Добавление объявления")
    public ResponseEntity<Ad> createAd(@ModelAttribute CreateOrUpdateAd properties,
                                       @RequestPart MultipartFile image,
                                       Authentication authentication) throws IOException {
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(adService.createAd(properties, image, authentication));

    }

    //полностью исправен
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение информации об объявлении")
    public ResponseEntity<ExtendedAd> getAdFullInfo(@PathVariable("id") Integer id, Authentication authentication) {
        ExtendedAd extendedAd = adService.getAdFullInfo(id);
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (extendedAd == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(extendedAd);
    }


    //полностью исправен
    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление объявления")
    @PreAuthorize(value = "hasRole('ADMIN') or @adServiceImpl.isAdAuthor(authentication.getName(), #id)")
    public ResponseEntity<Void> deleteAd(@PathVariable("id") Integer id, Authentication authentication) throws IOException {
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        adService.deleteAd(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //полностью исправен
    @PatchMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Обновление информации об объявлении")
    @PreAuthorize(value = "hasRole('ADMIN') or @adServiceImpl.isAdAuthor(authentication.getName(), #id)")
    public ResponseEntity<Ad> updateAdInfo(@PathVariable("id") Integer id,
                                           @RequestBody CreateOrUpdateAd createOrUpdateAd,
                                           Authentication authentication) {
        Ad ad = adService.updateAdInfo(id, createOrUpdateAd);
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(ad);
    }

    //полностью исправен
    @GetMapping(value = "/me", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение объявлений авторизованного пользователя")
    public ResponseEntity<Ads> getAuthUserAds(Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(adService.getAuthUserAds(authentication));

    }

    //полностью исправен
    @PatchMapping(value = "/{id}/image",
            consumes = MULTIPART_FORM_DATA_VALUE,
            produces = APPLICATION_OCTET_STREAM_VALUE)
    @Operation(summary = "Обновление картинки объявления")
    @PreAuthorize(value = "hasRole('ADMIN') or @adServiceImpl.isAdAuthor(authentication.getName(), #id)")
    public ResponseEntity<byte[]> updateAdPhoto(@PathVariable("id") Integer id,
                                                @RequestParam MultipartFile image,
                                                Authentication authentication) throws IOException {
        byte[] imageBytes = adService.updateAdPhoto(id, image);
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(imageBytes);
    }
}
