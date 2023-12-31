package cu.edu.cujae.rentacarfront.dto;

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
    private GenderDTO gender;
    private CountryDTO country;
}
