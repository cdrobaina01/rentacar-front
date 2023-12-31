package cu.edu.cujae.rentacarfront.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import cu.edu.cujae.rentacarfront.dto.TouristDTO;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@Route("tourist")
@PageTitle("Tourist | Rider Rent a Car")
public class TouristView extends VerticalLayout {

    private final Grid<TouristDTO> grid = new Grid<>();
    private final Binder<TouristDTO> binder = new Binder<>(TouristDTO.class);
    private TouristDTO touristSeleccionado;

    public TouristView() {
        configureGrid();
        configureForm();
        configureButtons();

        add(grid);
    }

    private void configureGrid() {
        grid.addColumn(TouristDTO::getName)
                .setHeader(getTranslation("tourist.name"))
                .setSortable(true);
        grid.addColumn(TouristDTO::getPassport)
                .setHeader(getTranslation("tourist.passport"))
                .setSortable(true);
        grid.addColumn(TouristDTO::getAge)
                .setHeader(getTranslation("tourist.age"))
                .setSortable(true);
        grid.addColumn(TouristDTO::getEmail)
                .setHeader(getTranslation("tourist.email"))
                .setSortable(true);
        grid.addColumn(TouristDTO::getPhone)
                .setHeader(getTranslation("tourist.phone"))
                .setSortable(true);
        grid.addColumn(TouristDTO::getCountry)
                .setHeader(getTranslation("tourist.country"))
                .setSortable(true);
        grid.addColumn(TouristDTO::getGender)
                .setHeader(getTranslation("tourist.gender"))
                .setSortable(true);

        ClientHttpConnector connector = new ReactorClientHttpConnector();
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/api")
                .clientConnector(connector)
                .build();
        Flux<TouristDTO> touristDTOFlux = webClient.get()
                .uri("/tourist")
                .retrieve()
                .bodyToFlux(TouristDTO.class);

        List<TouristDTO> tourists = touristDTOFlux.collectList().block();
        ListDataProvider<TouristDTO> dataProvider = new ListDataProvider<>(tourists);
        grid.setDataProvider(dataProvider);

        grid.asSingleSelect().addValueChangeListener(event -> {
            touristSeleccionado = event.getValue();
            binder.setBean(touristSeleccionado);
        });
    }

    private void configureForm() {
        TextField nameField = new TextField();
        binder.forField(nameField)
                .bind(TouristDTO::getName, TouristDTO::setName);

        // Haz lo mismo para los demás campos

        FormLayout form = new FormLayout();
        form.add(nameField); // Añade los demás campos

        add(form);
    }

    private void configureButtons() {
        Button addButton = new Button(getTranslation("button.add"), clickEvent -> {
            // Aquí puedes añadir un nuevo turista
        });

        Button deleteButton = new Button(getTranslation("button.delete"), clickEvent -> {
            // Aquí puedes eliminar el turista seleccionado
        });

        add(addButton, deleteButton);
    }
}
