package crazy_selling_store.controller;

import crazy_selling_store.dto.ads.Ad;
import crazy_selling_store.dto.ads.Ads;
import crazy_selling_store.dto.ads.CreateOrUpdateAd;
import crazy_selling_store.dto.ads.ExtendedAd;
import crazy_selling_store.mapper.AdMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/ads")
@Tag(name = "Объявления")
public class AdController {
    private final AdMapper adMapper;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение всех объявлений")
    public ResponseEntity<Ads> getAllAds() {
        Ads stubObj = new Ads(); /*объект-заглушка*/
        return ResponseEntity.ok(stubObj);
    }

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Добавление объявления")
    public ResponseEntity<CreateOrUpdateAd> createAd(@RequestBody CreateOrUpdateAd properties,
                                       @RequestBody MultipartFile image) {
        int stub = 10; /*заглушка*/
        if (stub >= 10) {
            return ResponseEntity.status(HttpStatus.CREATED).body(properties);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение информации об объявлении")
    public ResponseEntity<ExtendedAd> getAdInfo(@PathVariable("id") Integer id) {
        int stub = 10; /*заглушка*/
        ExtendedAd stubObj = new ExtendedAd(); /*объект-заглушка*/
        if (stub > 10) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else if (stub < 10) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(stubObj);
    }

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

    @PatchMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Обновление информации об объявлении")
    public ResponseEntity<Ad> updateAdInfo(@PathVariable("id") Integer id,
                                           @RequestBody(required = false) CreateOrUpdateAd createOrUpdateAd) {
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

    @GetMapping(value = "/me", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение объявлений авторизованного пользователя")
    public ResponseEntity<Ads> getAuthUserAds() {
        Ads stubObj = new Ads(); /*объект-заглушка*/
        int stub = 10; /*заглушка*/
        if (stub > 10) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(stubObj);
    }

    @PatchMapping( value = "/{id}/image",
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
