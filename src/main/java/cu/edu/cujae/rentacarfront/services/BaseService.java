package cu.edu.cujae.rentacarfront.services;

import org.springframework.web.client.RestTemplate;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class BaseService<T, S> {
    protected RestTemplate restTemplate;
    protected String apiUrl;
    protected Class<T[]> arrayClass;
    protected Class<S> saveClass;

    public BaseService(RestTemplate restTemplate, String apiUrl, Class<T[]> arrayClass, Class<S> saveClass) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.arrayClass = arrayClass;
        this.saveClass = saveClass;

    }
    public List<T> getAll() {
        return List.of(restTemplate.getForObject(apiUrl, arrayClass));
    }

    public T getOne(String id) {
        return restTemplate.getForObject(apiUrl + "/" + id, (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    public void delete(String id) {
        restTemplate.delete(apiUrl + "/" + id);
    }

    public void update(String id, S updatedEntity) {
        restTemplate.put(apiUrl + "/" + id, updatedEntity);
    }

    public void create(S newEntity) {
        restTemplate.postForObject(apiUrl, newEntity, saveClass);
    }
}
