package cu.edu.cujae.rentacarfront.services;

import cu.edu.cujae.rentacarfront.dto.PaymethodDTO;
import cu.edu.cujae.rentacarfront.dto.save.AuxiliarySaveDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymethodService extends BaseService<PaymethodDTO, AuxiliarySaveDTO> {
    public PaymethodService(RestTemplate restTemplate) {
        super(restTemplate, "http://localhost:8080/api/paymethod", PaymethodDTO[].class, AuxiliarySaveDTO.class);
    }
}
