package cu.edu.cujae.rentacarfront.services;

import cu.edu.cujae.rentacarfront.dto.DriverDTO;
import cu.edu.cujae.rentacarfront.dto.FeeDTO;
import cu.edu.cujae.rentacarfront.dto.save.FeeSaveDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FeeService extends BaseService<FeeDTO, FeeSaveDTO> {
    public FeeService(RestTemplate restTemplate) {
        super(restTemplate, "http://localhost:8080/api/fee", FeeDTO[].class, FeeSaveDTO.class);
    }
}
