package cu.edu.cujae.rentacarfront.services;

import cu.edu.cujae.rentacarfront.dto.ContractDTO;
import cu.edu.cujae.rentacarfront.dto.save.ContractSaveDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ContractService extends BaseService<ContractDTO, ContractSaveDTO> {
    public ContractService(RestTemplate restTemplate) {
        super(restTemplate, "http://localhost:8080/api/contract", ContractDTO[].class, ContractSaveDTO.class);
    }

    public ContractDTO getOne(String plate, String date) {
        return restTemplate.getForObject(apiUrl + "/" + plate + "/" + date, ContractDTO.class);
    }

    public void update(String plate, String date, ContractSaveDTO updatedContract) {
        restTemplate.put(apiUrl + "/" + plate + "/" + date, updatedContract);
    }

    public void delete(String plate, String date) {
        restTemplate.delete(apiUrl + "/" + plate + "/" + date);
    }
}
