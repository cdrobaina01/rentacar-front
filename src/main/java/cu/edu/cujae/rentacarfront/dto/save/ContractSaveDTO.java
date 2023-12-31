package cu.edu.cujae.rentacarfront.dto.save;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ContractSaveDTO {
    private String carPlate;
    private LocalDate startDate;
    private String touristPassport;
    private LocalDate endDate;
    private Integer startKm;
    private LocalDate deliveryDate;
    private Integer endKm;
    private Integer paymethodId;
    private String driverDni;
    private Double value;
}
