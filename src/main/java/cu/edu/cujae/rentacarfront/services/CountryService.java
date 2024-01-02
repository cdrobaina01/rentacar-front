package cu.edu.cujae.rentacarfront.services;

import cu.edu.cujae.rentacarfront.dto.CountryDTO;
import cu.edu.cujae.rentacarfront.dto.save.AuxiliarySaveDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CountryService extends BaseService<CountryDTO, AuxiliarySaveDTO> {
    public CountryService(RestTemplate restTemplate) {
        super(restTemplate, "http://localhost:8080/api/country", CountryDTO[].class,  AuxiliarySaveDTO.class);
    }
}
