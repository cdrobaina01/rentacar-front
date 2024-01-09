package cu.edu.cujae.rentacarfront.services;
import com.vaadin.flow.server.VaadinService;

import cu.edu.cujae.rentacarfront.dto.LoginRequestDTO;
import cu.edu.cujae.rentacarfront.dto.LoginResponseDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AuthService extends BaseService<LoginRequestDTO, LoginResponseDTO> {
    public AuthService(RestTemplate restTemplate, String apiUrl) {
        super(restTemplate, apiUrl, LoginRequestDTO[].class, LoginResponseDTO.class);
    }

    public void login(String username, String password) {
        // Crea un LoginRequestDTO con el nombre de usuario y la contraseña
        LoginRequestDTO loginRequest = new LoginRequestDTO(username, password);

        // Haz una solicitud POST a /api/auth/login con el LoginRequestDTO
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<LoginRequestDTO> entity = new HttpEntity<>(loginRequest, headers);
        ResponseEntity<LoginResponseDTO> responseEntity = restTemplate.exchange(apiUrl + "/auth/login", HttpMethod.POST, entity, LoginResponseDTO.class);

        // Si las credenciales son correctas, la API devolverá un LoginResponseDTO con el token JWT
        String jwt = responseEntity.getBody().getJwt();

        // Almacena el JWT en una cookie
        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setHttpOnly(true); // Esto hace que la cookie sea accesible solo a través de HTTP, no a través de JavaScript

        // Añade la cookie a la respuesta
        VaadinService.getCurrentResponse().addCookie(jwtCookie);

        // Almacena el JWT en BaseService para que pueda ser utilizado por otros servicios
        setJwtToken(jwt);
    }
}