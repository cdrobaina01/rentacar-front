package cu.edu.cujae.rentacarfront.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ContractDTO {
    private CarDTO car;
    private LocalDate startDate;
    private TouristDTO tourist;
    private LocalDate endDate;
    private Integer startKm;
    private LocalDate deliveryDate;
    private Integer endKm;
    private PaymethodDTO paymethod;
    private DriverDTO driver;
    private Double value;
}
