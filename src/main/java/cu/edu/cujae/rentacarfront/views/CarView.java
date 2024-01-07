package cu.edu.cujae.rentacarfront.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import cu.edu.cujae.rentacarfront.dto.*;

import cu.edu.cujae.rentacarfront.dto.save.CarSaveDTO;
import cu.edu.cujae.rentacarfront.dto.save.DriverSaveDTO;
import cu.edu.cujae.rentacarfront.dto.save.TouristSaveDTO;
import cu.edu.cujae.rentacarfront.services.CarService;
import cu.edu.cujae.rentacarfront.services.ModelService;
import cu.edu.cujae.rentacarfront.services.SituationService;
import cu.edu.cujae.rentacarfront.utils.AggregateService;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;

@Route("car")
@PageTitle("Carro | Rider Rent a Car")
public class CarView extends EntityView<CarDTO, CarSaveDTO>{
    private final CarService carService;
    private final ModelService modelService;
    private final SituationService situationService;
    private TextField plate;
    private IntegerField km;
    private TextField color;
    private ComboBox<ModelDTO> model;
    private ComboBox<BrandDTO> brand;
    private ComboBox<SituationDTO> situation;
    public CarView(AggregateService aggregateService){
        super(aggregateService);
        this.carService = aggregateService.getCarService();
        this.modelService = aggregateService.getModelService();
        this.situationService = aggregateService.getSituationService();
        this.binder = new Binder<>(CarDTO.class);
        this.selectedItem = new CarDTO();
        configureUI();
        binder.bindInstanceFields(this);
    }
    @Override
    protected void onSearchButtonClick() {
        List<CarDTO> result = carService.getAll();
        List<CarDTO> auxiliar = new ArrayList<>();
        for(CarDTO car : result){
            if(car.getPlate().toLowerCase().contains(searchField.getValue().toLowerCase()) ){
                auxiliar.add(car);
            }
        }
        if(auxiliar.isEmpty()){
            Notification notification = new Notification(
                    "¡No existen elementos con ese identificador!", 3000, Notification.Position.MIDDLE);
            notification.open();
        }
        else{
            updateGrid(auxiliar);
        }
    }
    @Override
    protected void setDataGrid() {
        grid.addColumn(CarDTO::getPlate)
                .setHeader(getTranslation("car.plate"))
                .setSortable(true);
        grid.addColumn(CarDTO::getKm)
                .setHeader(getTranslation("car.km"))
                .setSortable(true);
        grid.addColumn(CarDTO::getColor)
                .setHeader(getTranslation("car.color"))
                .setSortable(true);
        grid.addColumn(car -> car.getModel().getName())
                .setHeader(getTranslation("car.model"))
                .setSortable(true);
        grid.addColumn(car -> car.getModel().getBrand().getName())
                .setHeader(getTranslation("car.brand"))
                .setSortable(true);
        grid.addColumn(car -> car.getSituation().getName())
                .setHeader(getTranslation("car.situation"))
                .setSortable(true);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    }
    @Override
    protected void cleanForm() {
        plate.clear();
        km.clear();
        color.clear();
        model.clear();
        brand.clear();
        situation.clear();

        // Desbloquea el campo de la placa
        plate.setReadOnly(false);

        // Limpia la selección actual
        selectedItem = null;
        binder.setBean(null);
    }
    @Override
    protected void updateForm() {
        plate.setValue(selectedItem.getPlate());
        km.setValue(selectedItem.getKm());
        color.setValue(selectedItem.getColor());
        model.setValue(selectedItem.getModel());

        situation.setValue(selectedItem.getSituation());
        // Bloquea el campo de la placa
        plate.setReadOnly(true);
    }
    @Override
    protected FormLayout createFormLayout() {
        FormLayout formLayout = new FormLayout();
        plate = createTextField("Matrícula");
        km = createIntegerField("Kilometraje");
        color = createTextField("Color");
        model = createComboBox("Modelo", modelService);
        situation = createComboBox("Situación", situationService);
        formLayout.add(plate, km, color, model, situation);
        return formLayout;
    }
    @Override
    protected void onAddButtonClick() {
        CarDTO dto = new CarDTO();
        CarSaveDTO save = new CarSaveDTO();
        if (binder.isValid()) {
            try {
                binder.writeBean(dto);
                fillSaveDTO(dto,save);
                carService.create(save);
                binder.setBean(null);
                cleanForm();
                refreshGrid();
            } catch (Exception e) {
                showInvalidFieldsNotification();
            }
        } else {
            Notification notification = new Notification(
                    "Tienes campos inválidos", 5000, Notification.Position.MIDDLE);
            notification.open();
        }
    }

    @Override
    protected void fillSaveDTO(CarDTO dto, CarSaveDTO save) {
        save.setPlate(dto.getPlate());
        save.setKm(dto.getKm());
        save.setColor(dto.getColor());
        save.setModelId(dto.getModel().getId());
        save.setSituationId(dto.getSituation().getId());
    }

    @Override
    protected void onDeleteButtonClick() {
        CarDTO dto = binder.getBean();

        if (dto != null) {
            try {
                carService.delete(dto.getPlate());
                cleanForm();
                refreshGrid();
            } catch (Exception e) {
                Notification notification = new Notification(
                        "El elemento no puede ser eliminado", 3000, Notification.Position.MIDDLE);
                notification.open();
            }
        } else {
            showNoSelectionNotification();
        }
    }
    @Override
    protected void onUpdateButtonClick() {
        CarDTO dto = binder.getBean();
        System.out.println("Binder antes de la actualización: " + binder.getBean());
        System.out.println("dto antes de la actualización: " + dto);
        if (binder.isValid()) {
            if (dto != null) {
                try {
                    binder.writeBean(dto);
                    System.out.println("Binder después de la actualización: " + binder.getBean());
                    CarSaveDTO save = new CarSaveDTO();
                    fillSaveDTO(dto, save);
                    carService.update(dto.getPlate(),save);
                    binder.setBean(null);
                    cleanForm();
                    refreshGrid();
                } catch (ValidationException e) {
                    showInvalidFieldsNotification();
                }
            } else {
                showNoSelectionNotification();
            }
        }
        else {
            // Muestra una única notificación de error
            Notification notification = new Notification(
                    "Tienes campos inválidos", 5000, Notification.Position.MIDDLE);
            notification.open();
        }

    }

    @Override
    protected void configureUI() {
        // Implementación de la configuración de la interfaz de usuario si es necesario
    }
    @Override
    protected void refreshGrid() {
        List<CarDTO> all = carService.getAll();
        grid.setItems(all);
    }
    @Override
    protected void validateBinder() {
        binder.forField(plate)
                .asRequired("La matrícula es requerida")
                .bind(CarDTO::getPlate, CarDTO::setPlate);

        binder.forField(km)
                .asRequired("El kilometraje es requerido")
                .bind(CarDTO::getKm, CarDTO::setKm);

        binder.forField(color)
                .asRequired("El color es requerido")
                .bind(CarDTO::getColor, CarDTO::setColor);

        binder.forField(model)
                .asRequired("El modelo es requerido")
                .bind(CarDTO::getModel, CarDTO::setModel);

        binder.forField(situation)
                .asRequired("La situación es requerida")
                .bind(CarDTO::getSituation, CarDTO::setSituation);
    }
}
