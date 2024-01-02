package cu.edu.cujae.rentacarfront.services;

import cu.edu.cujae.rentacarfront.dto.ModelDTO;
import cu.edu.cujae.rentacarfront.dto.save.ModelSaveDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ModelService extends BaseService<ModelDTO, ModelSaveDTO> {
    public ModelService(RestTemplate restTemplate) {
        super(restTemplate, "http://localhost:8080/api/model", ModelDTO[].class, ModelSaveDTO.class);
    }
}