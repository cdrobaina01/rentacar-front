package cu.edu.cujae.rentacarfront.services;

import cu.edu.cujae.rentacarfront.dto.CarDTO;
import cu.edu.cujae.rentacarfront.dto.save.CarSaveDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CarService extends BaseService<CarDTO, CarSaveDTO> {
    public CarService(RestTemplate restTemplate) {
        super(restTemplate, "http://localhost:8080/api/car", CarDTO[].class, CarSaveDTO.class);
    }
}
