package cu.edu.cujae.rentacarfront.services;

import cu.edu.cujae.rentacarfront.dto.TouristDTO;
import cu.edu.cujae.rentacarfront.dto.save.TouristSaveDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class TouristService extends BaseService<TouristDTO, TouristSaveDTO> {
    public TouristService(RestTemplate restTemplate) {
        super(restTemplate, "http://localhost:8080/api/tourist", TouristDTO[].class, TouristSaveDTO.class);
    }
}
