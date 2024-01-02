package cu.edu.cujae.rentacarfront.services;

import cu.edu.cujae.rentacarfront.dto.BrandDTO;
import cu.edu.cujae.rentacarfront.dto.save.AuxiliarySaveDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BrandService extends BaseService<BrandDTO, AuxiliarySaveDTO> {
    public BrandService(RestTemplate restTemplate) {
        super(restTemplate, "http://localhost:8080/api/brand", BrandDTO[].class, AuxiliarySaveDTO.class);
    }
}
