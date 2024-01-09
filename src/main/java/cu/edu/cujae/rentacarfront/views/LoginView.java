package cu.edu.cujae.rentacarfront.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import cu.edu.cujae.rentacarfront.dto.LoginRequestDTO;
import cu.edu.cujae.rentacarfront.services.AuthService;
import cu.edu.cujae.rentacarfront.services.TouristService;
import cu.edu.cujae.rentacarfront.utils.AggregateService;

@Route("login")
@PageTitle("Inicio de sesion | Rider Rent a Car")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    protected final AggregateService aggregateService;
    private final AuthService authService;
    private final LoginForm login = new LoginForm();

    public LoginView(AggregateService aggregateService) {
        this.aggregateService = aggregateService;
        authService = aggregateService.getAuthService();
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        login.addLoginListener(e -> {
            // Crea un LoginRequestDTO con el nombre de usuario y la contraseña
            LoginRequestDTO loginRequest = new LoginRequestDTO(e.getUsername(), e.getPassword());

            // Haz una solicitud POST a /api/auth/login con el LoginRequestDTO
            // Si las credenciales son correctas, la API devolverá un LoginResponseDTO con el token JWT
            // Aquí necesitarás llamar a tu AuthService para hacer la solicitud y manejar la respuesta
            authService.login(loginRequest.getUsername(), loginRequest.getPassword());
        });

        add(new H1("Rider"), new H2("Rent a Car"), login);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // informa al usuario sobre un error de autenticación
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }
}