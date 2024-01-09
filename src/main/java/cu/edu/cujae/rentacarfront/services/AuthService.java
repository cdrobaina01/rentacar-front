package cu.edu.cujae.rentacarfront.services;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinService;
import cu.edu.cujae.rentacarfront.dto.LoginRequestDTO;
import cu.edu.cujae.rentacarfront.dto.LoginResponseDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Service
public class AuthService extends BaseService<LoginRequestDTO, LoginResponseDTO> {
    public AuthService(RestTemplate restTemplate) {
        super(restTemplate, "http://localhost:8080/api/",LoginRequestDTO[].class, LoginResponseDTO.class);
    }

    public void login(String username, String password) {
        System.out.println("AuthService.login - inicio");
        LoginRequestDTO loginRequest = new LoginRequestDTO(username, password);
        System.out.println("AuthService.login - loginRequest creado: " + loginRequest);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<LoginRequestDTO> entity = new HttpEntity<>(loginRequest, headers);
        System.out.println("AuthService.login - entity creado: " + entity);

        ResponseEntity<LoginResponseDTO> responseEntity = restTemplate.exchange(apiUrl + "/auth/login", HttpMethod.POST, entity, LoginResponseDTO.class);
        System.out.println("AuthService.login - responseEntity obtenido: " + responseEntity);

        String jwt = responseEntity.getBody().getToken();

        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/"); // Asegúrate de que la cookie está disponible en todo el sitio
        jwtCookie.setMaxAge(7 * 24 * 60 * 60); // Establece la duración de la cookie, por ejemplo, una semana

        ((HttpServletResponse) VaadinService.getCurrentResponse()).addCookie(jwtCookie);


        setJwtToken(jwt);
        System.out.println("AuthService.login - jwt almacenado en BaseService");

        // Redirige al usuario a la vista 'tourist'
        UI.getCurrent().navigate("tourist");
    }

}