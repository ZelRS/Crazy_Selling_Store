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

    //    выполнено
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение всех объявлений")
    public ResponseEntity<Ads> getAllAds() {
        log.info("bbbbbbbbbbbbbb");
        return ResponseEntity.ok(adService.getAllAds());
    }

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Добавление объявления")
    public ResponseEntity<Ad> createAd(@RequestPart CreateOrUpdateAd properties,
                                                     @RequestPart MultipartFile image,
                                                     Authentication authentication) throws IOException {
        log.info("ыыыыыыыы");
        return ResponseEntity.status(HttpStatus.CREATED).body(adService.createAd(properties, image, authentication));
    }

//    выполнено
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение информации об объявлении")
    public ResponseEntity<ExtendedAd> getAdFullInfo(@PathVariable("id") Integer id) {
        ExtendedAd extendedAd = adService.getAdFullInfo(id);
        if (extendedAd == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("1");
        return ResponseEntity.ok(extendedAd);
    }

//    не выполнено
    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление объявления")
    public ResponseEntity<Void> deleteAd(@PathVariable("id") Integer id) {
        int stub = 10; /*заглушка*/
        if (stub > 10) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else if (stub < 10 && stub > 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else if (stub <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


//    не выполнено
    @PatchMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Обновление информации об объявлении")
    public ResponseEntity<Ad> updateAdInfo(@PathVariable("id") Integer id,
                                           @RequestBody(required = false) CreateOrUpdateAd createOrUpdateAd) {
//        adService.updateAdInfo(id, createOrUpdateAd);


        Ad stubObj = new Ad(); /*объект-заглушка*/
        int stub = 10; /*заглушка*/
        if (stub > 10) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else if (stub < 10 && stub > 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else if (stub <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(stubObj);
    }

    //    выполнено
    @GetMapping(value = "/me", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение объявлений авторизованного пользователя")
    public ResponseEntity<Ads> getAuthUserAds(Authentication authentication) {
        log.info("sssssssssssss");
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(adService.getAuthUserAds(authentication));

    }


//    не выолнено
    @PatchMapping(value = "/{id}/image",
            consumes = MULTIPART_FORM_DATA_VALUE,
            produces = APPLICATION_OCTET_STREAM_VALUE)
    @Operation(summary = "Обновление картинки объявления")
    public ResponseEntity<String[]> updateAdPhoto(@PathVariable("id") Integer id,
                                                  @RequestParam MultipartFile image) {
        String[] stubArr = new String[1]; /*массив-заглушка*/
        int stub = 10; /*заглушка*/
        if (stub > 10) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else if (stub < 10 && stub > 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else if (stub <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(stubArr);
    }
}
