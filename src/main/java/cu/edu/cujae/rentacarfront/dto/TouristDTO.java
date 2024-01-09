package cu.edu.cujae.rentacarfront.dto;

import cu.edu.cujae.rentacarfront.utils.TouristGender;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TouristDTO {
    private String passport;
    private String name;
    private Integer age;
    private String phone;
    private String email;
    private TouristGender gender;
    private String country;
}
