package cu.edu.cujae.rentacarfront.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SituationDTO {
    private Integer id;
    private String name;
}
