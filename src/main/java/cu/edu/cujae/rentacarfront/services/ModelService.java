package cu.edu.cujae.rentacarfront.services;

import cu.edu.cujae.rentacarfront.dto.BrandDTO;
import cu.edu.cujae.rentacarfront.dto.ModelDTO;
import cu.edu.cujae.rentacarfront.dto.save.ModelSaveDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModelService extends BaseService<ModelDTO, ModelSaveDTO> {
    public ModelService(RestTemplate restTemplate) {
        super(restTemplate, "http://localhost:8080/api/model", ModelDTO[].class, ModelSaveDTO.class);
    }
    public List<ModelDTO> findByBrand(BrandDTO brand){
        List<ModelDTO> models = getAll();
        List<ModelDTO> result = new ArrayList<>();
        for(ModelDTO model : models) {
            if (model.getBrand().getId() == brand.getId()) {
                result.add(model);
            }
        }
        return result;
    }
}