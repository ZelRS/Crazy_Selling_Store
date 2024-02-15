package crazy_selling_store.service;

import crazy_selling_store.dto.ads.Ad;
import crazy_selling_store.dto.ads.Ads;
import crazy_selling_store.dto.ads.CreateOrUpdateAd;
import crazy_selling_store.dto.ads.ExtendedAd;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
/**
 * Интерфейс сервиса для обработки объявлений.
 */
public interface AdService {

    /**
     * Создает новое объявление.
     *
     * @param properties         свойства для создания или обновления объявления
     * @param image              изображение объявления
     * @param authentication     объект аутентификации пользователя
     * @return созданное объявление
     * @throws IOException       если произошла ошибка ввода-вывода
     */
    Ad createAd(CreateOrUpdateAd properties, MultipartFile image, Authentication authentication) throws IOException;

    /**
     * Получает все объявления.
     *
     * @return все объявления
     */
    Ads getAllAds();

    /**
     * Получает объявления пользователя, авторизованного через аутентификацию.
     *
     * @param authentication     объект аутентификации пользователя
     * @return объявления пользователя
     */
    Ads getAuthUserAds(Authentication authentication);

    /**
     * Получает полную информацию об объявлении по его идентификатору.
     *
     * @param id     идентификатор объявления
     * @return полная информация об объявлении
     */
    ExtendedAd getAdFullInfo(Integer id);

    /**
     * Удаляет объявление.
     *
     * @param id       идентификатор объявления для удаления
     * @throws IOException       если произошла ошибка ввода-вывода
     */
    void deleteAd(Integer id) throws IOException;

    /**
     * Обновляет информацию об объявлении.
     *
     * @param id                идентификатор объявления для обновления
     * @param createOrUpdateAd  объект с обновленными данными объявления
     * @return обновленное объявление
     */
    @Transactional
    Ad updateAdInfo(Integer id, CreateOrUpdateAd createOrUpdateAd);

    /**
     * Обновляет фото для объявления.
     *
     * @param id        идентификатор объявления, для которого обновляется фото
     * @param image     новое изображение для объявления
     * @return массив байт нового фото
     * @throws IOException       если произошла ошибка ввода-вывода
     */
    byte[] updateAdPhoto(Integer id, MultipartFile image) throws IOException;
}
