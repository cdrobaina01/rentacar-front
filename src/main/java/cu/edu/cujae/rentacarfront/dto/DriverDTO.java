package cu.edu.cujae.rentacarfront.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DriverDTO {
    private String dni;
    private String name;
    private String address;
    private String email;
    private CategoryDTO category;
}
