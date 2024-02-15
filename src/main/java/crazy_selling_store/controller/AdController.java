package crazy_selling_store.controller;

import crazy_selling_store.dto.ads.Ad;
import crazy_selling_store.dto.ads.Ads;
import crazy_selling_store.dto.ads.CreateOrUpdateAd;
import crazy_selling_store.dto.ads.ExtendedAd;
import crazy_selling_store.service.impl.AdServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.MediaType.*;

/**
 * Контроллер для работы с объявлениями.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/ads")
@Tag(name = "Объявления")
public class AdController {
    /**
     * Сервис для работы с объявлениями.
     */
    private final AdServiceImpl adService;

    /**
     * Получение всех объявлений.
     * @return ResponseEntity содержит список всех объявлений.
     */
    @Operation(
            summary = "Получение всех объявлений",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = Ads.class)))})
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Ads> getAllAds() {
        // Логирование получения всех объявлений
        log.info("Получен список всех объявлений");
        return ResponseEntity.ok(adService.getAllAds());
    }
    /**
     * Создание нового объявления.
     * @param properties Параметры нового объявления.
     * @param image Картинка объявления.
     * @param authentication Данные авторизации пользователя.
     * @return ResponseEntity содержит новое объявление или код статуса ответа при неудаче (
     * HttpStatus.UNPROCESSABLE_ENTITY).
     */
    @Operation(
            summary = "Добавление объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Ad.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content())})
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
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
    /**
     * Получение расширенной информации объявления.
     * @param id Идентификатор объявления.
     * @return ResponseEntity содержит расширенный набор параметров объявления.
     */
    @Operation(
            summary = "Получение информации об объявлении",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExtendedAd.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content())})
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
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
    /**
     * Удаление объявления.
     * @param id Идентификатор удаляемого объявления.
     * @return ResponseEntity содержит код статуса ответа (
     * HttpStatus.NO_CONTENT при успешном удалении, HttpStatus.NOT_FOUND при неудачном удалении).
     */
    @Operation(
            summary = "Удаление объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "No Content",
                            content = @Content()),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content()),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content())})
    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasRole('ADMIN') or @authUserValidator.isAdAuthor(authentication.getName(), #id)")
    public ResponseEntity<Void> deleteAd(@PathVariable("id") Integer id) throws IOException {
        log.info("Попытка удаления объявления");
        adService.deleteAd(id);
        log.info("Объявление успешно удалено");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    /**
     * Обновление информации объявления.
     * @param id Идентификатор объявления.
     * @param createOrUpdateAd Обновленная версия объявления.
     * @return ResponseEntity содержит измененное объявление.
     */
    @Operation(
            summary = "Обновление информации об объявлении",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Ad.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content()),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content())})
    @PatchMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasRole('ADMIN') or @authUserValidator.isAdAuthor(authentication.getName(), #id)")
    public ResponseEntity<Ad> updateAdInfo(@PathVariable("id") Integer id,
                                           @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        log.info("Попытка изменения информации об объявлении \"" + createOrUpdateAd.getTitle() + "\"");
        Ad ad = adService.updateAdInfo(id, createOrUpdateAd);
        log.info("Информация объявления \"" + createOrUpdateAd.getTitle() + "\" успешно обновлена");
        return ResponseEntity.ok(ad);
    }
    /**
     * Получение объявлений авторизованного пользователя.
     * @param authentication Данные авторизации пользователя.
     * @return ResponseEntity содержит список объявлений пользователя.
     */
    @Operation(
            summary = "Получение объявлений авторизованного пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Ads.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content())})
    @GetMapping(value = "/me", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Ads> getAuthUserAds(Authentication authentication) {
        log.info("Объявления пользователя " + authentication.getName() + " успешно получены");
        return ResponseEntity.ok(adService.getAuthUserAds(authentication));

    }
    /**
     * Обновление картинки объявления.
     * @param id Идентификатор объявления, к которому относится картинка.
     * @param image Новая картинка объявления.
     * @return ResponseEntity содержит новую картинку объявления или код статуса ответа при неудаче (
     * HttpStatus.NOT_FOUND).
     */
    @Operation(
            summary = "Обновление картинки объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/octet-stream",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    type = "string",
                                                    format = "byte")))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content()),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content())})
    @PatchMapping(value = "/{id}/image", consumes = MULTIPART_FORM_DATA_VALUE,
            produces = APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize(value = "hasRole('ADMIN') or @authUserValidator.isAdAuthor(authentication.getName(), #id)")
    public ResponseEntity<byte[]> updateAdPhoto(@PathVariable("id") Integer id,
                                                @RequestParam MultipartFile image) throws IOException {
        log.info("Попытка изменения изображения объявления");
        byte[] imageBytes = adService.updateAdPhoto(id, image);
        log.info("Изображение успешно обновлено");
        return ResponseEntity.ok(imageBytes);
    }
}
