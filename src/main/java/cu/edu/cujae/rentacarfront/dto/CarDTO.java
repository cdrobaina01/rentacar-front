package cu.edu.cujae.rentacarfront.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CarDTO {
    private String plate;
    private Integer km;
    private String color;
    private ModelDTO model;
    private SituationDTO situation;
}
