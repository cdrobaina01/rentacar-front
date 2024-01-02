package cu.edu.cujae.rentacarfront.services;

import cu.edu.cujae.rentacarfront.dto.GenderDTO;
import cu.edu.cujae.rentacarfront.dto.save.AuxiliarySaveDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GenderService extends BaseService<GenderDTO, AuxiliarySaveDTO> {
    public GenderService(RestTemplate restTemplate) {
        super(restTemplate, "http://localhost:8080/api/gender", GenderDTO[].class, AuxiliarySaveDTO.class);
    }
}
