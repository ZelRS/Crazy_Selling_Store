package crazy_selling_store.filter;


import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//класс, реализующий фильтр, который обрабатывает CORS (Cross-Origin Resource Sharing) и добавляет
//заголовок "Access-Control-Allow-Credentials" в ответ. Заголовок указывает, что браузер должен включить
//отправку кросс-доменных запросов с учетными данными (credentials), такими как куки или HTTP-авторизация
@Component
public class BasicAuthCorsFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
