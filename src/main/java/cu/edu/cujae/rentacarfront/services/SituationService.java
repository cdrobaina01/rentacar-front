package cu.edu.cujae.rentacarfront.services;

import cu.edu.cujae.rentacarfront.dto.SituationDTO;
import cu.edu.cujae.rentacarfront.dto.save.AuxiliarySaveDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SituationService extends BaseService<SituationDTO, AuxiliarySaveDTO> {
    public SituationService(RestTemplate restTemplate) {
        super(restTemplate, "http://localhost:8080/api/situation", SituationDTO[].class, AuxiliarySaveDTO.class);
    }
}
