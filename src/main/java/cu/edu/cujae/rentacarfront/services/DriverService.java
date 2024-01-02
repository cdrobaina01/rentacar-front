package cu.edu.cujae.rentacarfront.services;

import cu.edu.cujae.rentacarfront.dto.DriverDTO;
import cu.edu.cujae.rentacarfront.dto.save.DriverSaveDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DriverService extends BaseService<DriverDTO, DriverSaveDTO> {
    public DriverService(RestTemplate restTemplate) {
        super(restTemplate, "http://localhost:8080/api/driver", DriverDTO[].class, DriverSaveDTO.class);
    }
}
