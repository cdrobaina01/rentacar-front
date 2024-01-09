package cu.edu.cujae.rentacarfront.views;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import cu.edu.cujae.rentacarfront.dto.*;
import cu.edu.cujae.rentacarfront.dto.save.CarSaveDTO;
import cu.edu.cujae.rentacarfront.services.*;
import cu.edu.cujae.rentacarfront.utils.AggregateService;
import cu.edu.cujae.rentacarfront.utils.CarSituation;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;
@Route("car")
@PageTitle("Carro | Rider Rent a Car")
public class CarView extends EntityView<CarDTO, CarSaveDTO>{
    private final CarService carService;
    private final ModelService modelService;
    //private final SituationService situationService;
    private final BrandService brandService;

    private TextField plate;
    private IntegerField km;
    private TextField color;
    private ComboBox<ModelDTO> model;
    private ComboBox<BrandDTO> brand;
    private ComboBox<CarSituation> situation;

    public CarView(AggregateService aggregateService){
        super(aggregateService);
        this.carService = aggregateService.getCarService();
        this.modelService = aggregateService.getModelService();
        this.brandService = aggregateService.getBrandService();
        //this.situationService = aggregateService.getSituationService();
        this.binder = new Binder<>(CarDTO.class);
        this.selectedItem = new CarDTO();
        configureUI();
        binder.bindInstanceFields(this);
        validateBinder();
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
            showInvalidIdentifier();
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
        grid.addColumn(car -> car.getModel().getBrand().getName())
                .setHeader(getTranslation("car.brand"))
                .setSortable(true);
        grid.addColumn(car -> car.getModel().getName())
                .setHeader(getTranslation("car.model"))
                .setSortable(true);
        grid.addColumn(CarDTO::getSituation)
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

        // Desbloquea el campo del pasaporte
        plate.setReadOnly(false);

        // Limpia la selección actual
        selectedItem = null;
        binder.setBean(null);

        // Limpia el estado del Binder
        binder.readBean(null);
    }
    @Override
    protected void updateForm() {
        // Comprueba si hay un elemento seleccionado
        if (selectedItem != null) {
            // Enlaza los campos del formulario al objeto seleccionado
            binder.setBean(selectedItem);
            // Bloquea el campo del pasaporte
            plate.setReadOnly(true);
        } else {
            // Limpia los campos del formulario o establece un comportamiento predeterminado cuando no hay ningún elemento seleccionado
            binder.readBean(null);
        }
    }

    @Override
    protected void refreshGrid() {
        List<CarDTO> all = carService.getAll();
        grid.setItems(all);
    }
    @Override
    protected void validateBinder() {
        // Validación para el campo 'plate'
        binder.forField(plate)
                .withValidator(new StringLengthValidator(
                        "La matrícula debe tener entre 6 y 12 caracteres", 6, 12))
                .bind(CarDTO::getPlate, CarDTO::setPlate);

        // Validación para el campo 'km'
        binder.forField(km)
                .withValidator(kmValue -> kmValue != null && kmValue >= 0,
                        "Debe introducir un kilometraje válido (mayor o igual a 0)")
                .bind(CarDTO::getKm, CarDTO::setKm);

        // Validación para el campo 'color'
        binder.forField(color)
                .withValidator(new StringLengthValidator(
                        "El color debe tener menos de 50 caracteres", 1, 50))
                .bind(CarDTO::getColor, CarDTO::setColor);

        // Validación para el campo 'model'
        binder.forField(model)
                .withValidator(modelValue -> modelValue != null,
                        "Debe seleccionar un modelo")
                .bind(CarDTO::getModel, CarDTO::setModel);

        // Validación para el campo 'brand'
        // Validación para el campo 'brand'
        binder.forField(brand)
                .withValidator(brandValue -> brandValue != null,
                        "Debe seleccionar una marca")
                .bind(carDTO -> {
                            if (carDTO.getModel() != null) {
                                return carDTO.getModel().getBrand();
                            } else {
                                return null;
                            }
                        },
                        (carDTO, brandValue) -> {
                            if (carDTO.getModel() != null) {
                                carDTO.getModel().setBrand(brandValue);
                            }
                        });

        // Validación para el campo 'situation'
        binder.forField(situation)
                .withValidator(situationValue -> situationValue != null,
                        "Debe seleccionar una situación")
                .bind(CarDTO::getSituation, CarDTO::setSituation);
    }

    @Override
    protected FormLayout createFormLayout() {
        FormLayout formLayout = new FormLayout();
        plate = createTextField("Matrícula");
        km = createIntegerField("Kilometraje");
        color = createTextField("Color");
        //brand = createComboBox("Marca", brandService);
        // Agrega un ValueChangeListener a la marca para actualizar los modelos cuando se selecciona una marca
        //model = createComboBox("Modelo", modelService);
        model.setItemLabelGenerator(ModelDTO::getName);
        //situation = createComboBox("Situación", situationService);
        brand.addValueChangeListener(event -> {
            BrandDTO selectedBrand = event.getValue();
            if (selectedBrand != null) {
                List<ModelDTO> modelsByBrand = modelService.findByBrand(selectedBrand);
                model.setItems(modelsByBrand);
                model.setEnabled(true);
            } else {
                model.clear();
                model.setEnabled(false);
            }
        });

        formLayout.add(plate, km, color, brand, model, situation);
        return formLayout;
    }
    @Override
    protected void onAddButtonClick() {
        CarDTO dto = new CarDTO();
        CarSaveDTO save = new CarSaveDTO();
        BinderValidationStatus<CarDTO> status = binder.validate();
        if (status.isOk()) {
            try {
                binder.writeBean(dto);
                fillSaveDTO(dto,save);
                carService.create(save);
                binder.setBean(null);
                cleanForm();
                refreshGrid();
                showAddSuccessNotification();
            } catch (Exception e) {
                showInvalidFieldsNotification();
            }
        } else {
            showInvalidIdentifier();
        }
    }
    @Override
    protected void onUpdateButtonClick() {
        CarDTO dto = binder.getBean();
        BinderValidationStatus<CarDTO> status = binder.validate();

        if (status.isOk()) {
            if (dto != null) {
                try {
                    binder.writeBean(dto);
                    CarSaveDTO save = new CarSaveDTO();
                    fillSaveDTO(dto, save);
                    carService.update(dto.getPlate(),save);
                    binder.setBean(null);
                    cleanForm();
                    refreshGrid();
                    showUpdateSuccessNotification();
                } catch (ValidationException e) {
                    showInvalidFieldsNotification();
                }
            } else {
                showNoSelectionNotification();
            }
        } else {
            showInvalidFieldsNotification();
        }
    }

    @Override
    protected void fillSaveDTO(CarDTO dto, CarSaveDTO save) {
        save.setPlate(dto.getPlate());
        save.setKm(dto.getKm());
        save.setColor(dto.getColor());
        save.setModelId(dto.getModel().getId());
        save.setKm(dto.getKm());
        save.setSituationId(dto.getSituation().ordinal());
    }

    @Override
    protected void onDeleteButtonClick() {
        CarDTO dto = binder.getBean();
        if (dto != null) {
            try {
                carService.delete(dto.getPlate());
                cleanForm();
                refreshGrid();
                showDeleteSuccessNotification();
            } catch (HttpServerErrorException e) {
                if (e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                    showInvalidElementSelected();
                }
            }
        } else {
            showNoSelectionNotification();
        }
    }
}