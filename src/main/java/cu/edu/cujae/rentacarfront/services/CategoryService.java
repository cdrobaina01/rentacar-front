package cu.edu.cujae.rentacarfront.services;

import cu.edu.cujae.rentacarfront.dto.CategoryDTO;
import cu.edu.cujae.rentacarfront.dto.save.AuxiliarySaveDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CategoryService extends BaseService<CategoryDTO, AuxiliarySaveDTO> {
    public CategoryService(RestTemplate restTemplate) {
        super(restTemplate, "http://localhost:8080/api/category", CategoryDTO[].class, AuxiliarySaveDTO.class);
    }
}
