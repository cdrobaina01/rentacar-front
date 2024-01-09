package cu.edu.cujae.rentacarfront.services;

import com.vaadin.flow.server.VaadinService;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.ParameterizedType;
import java.util.List;

@RequiredArgsConstructor
public abstract class BaseService<T, S> {
    protected RestTemplate restTemplate;
    protected String apiUrl;
    protected Class<T[]> arrayClass;
    protected Class<S> saveClass;
    protected String jwtToken;

    public BaseService(RestTemplate restTemplate, String apiUrl, Class<T[]> arrayClass, Class<S> saveClass) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.arrayClass = arrayClass;
        this.saveClass = saveClass;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public List<T> getAll() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + obtenerCookie("jwt"));
        System.out.println(headers);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<T[]> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, arrayClass);
        System.out.println("BaseService.getAll - responseEntity: " + responseEntity);
        return List.of(responseEntity.getBody());
    }

    public T getOne(String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<T> responseEntity = restTemplate.exchange(apiUrl + "/" + id, HttpMethod.GET, entity, (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        return responseEntity.getBody();
    }

    public void delete(String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        restTemplate.exchange(apiUrl + "/" + id, HttpMethod.DELETE, entity, Void.class);
    }

    public void update(String id, S updatedEntity) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<S> entity = new HttpEntity<>(updatedEntity, headers);

        restTemplate.exchange(apiUrl + "/" + id, HttpMethod.PUT, entity, Void.class);
    }

    public void create(S newEntity) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<S> entity = new HttpEntity<>(newEntity, headers);

        restTemplate.exchange(apiUrl, HttpMethod.POST, entity, saveClass);
    }
    public String obtenerCookie(String nombreCookie) {
        // Obtener todas las cookies
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();

        // Buscar la cookie deseada
        for (Cookie cookie : cookies) {
            if (nombreCookie.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        // Si no se encuentra la cookie, devolver null
        return null;
    }

}
