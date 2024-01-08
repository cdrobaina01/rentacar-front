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
    private final BrandService brandService;

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
        this.brandService = aggregateService.getBrandService();
        this.situationService = aggregateService.getSituationService();
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
        brand = createComboBox("Marca", brandService);
        // Agrega un ValueChangeListener a la marca para actualizar los modelos cuando se selecciona una marca
        model = createComboBox("Modelo", modelService);
        model.setItemLabelGenerator(ModelDTO::getName);
        situation = createComboBox("Situación", situationService);
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
        System.out.println("Antes de llenar save - DTO: " + dto + ", SAVE: " + save);

        save.setPlate(dto.getPlate());
        save.setKm(dto.getKm());
        save.setColor(dto.getColor());
        save.setModelId(dto.getModel().getId());
        save.setKm(dto.getKm());
        save.setSituationId(dto.getSituation().getId());
        System.out.println("Despues de llenar save - DTO: " + dto + ", SAVE: " + save);

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
/*
java.lang.RuntimeException: com.vaadin.flow.data.binder.BindingException: An exception has been thrown inside binding logic for the field element [label='Marca']
	at com.vaadin.flow.server.communication.rpc.PublishedServerEventHandlerRpcHandler.invokeMethod(PublishedServerEventHandlerRpcHandler.java:234) ~[flow-server-24.3.2.jar:24.3.2]
	at com.vaadin.flow.server.communication.rpc.PublishedServerEventHandlerRpcHandler.invokeMethod(PublishedServerEventHandlerRpcHandler.java:204) ~[flow-server-24.3.2.jar:24.3.2]
	at com.vaadin.flow.server.communication.rpc.PublishedServerEventHandlerRpcHandler.invokeMethod(PublishedServerEventHandlerRpcHandler.java:150) ~[flow-server-24.3.2.jar:24.3.2]
	at com.vaadin.flow.server.communication.rpc.PublishedServerEventHandlerRpcHandler.handleNode(PublishedServerEventHandlerRpcHandler.java:133) ~[flow-server-24.3.2.jar:24.3.2]
	at com.vaadin.flow.server.communication.rpc.AbstractRpcInvocationHandler.handle(AbstractRpcInvocationHandler.java:74) ~[flow-server-24.3.2.jar:24.3.2]
	at com.vaadin.flow.server.communication.ServerRpcHandler.handleInvocationData(ServerRpcHandler.java:466) ~[flow-server-24.3.2.jar:24.3.2]
	at com.vaadin.flow.server.communication.ServerRpcHandler.lambda$handleInvocations$4(ServerRpcHandler.java:447) ~[flow-server-24.3.2.jar:24.3.2]
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511) ~[na:na]
	at com.vaadin.flow.server.communication.ServerRpcHandler.handleInvocations(ServerRpcHandler.java:447) ~[flow-server-24.3.2.jar:24.3.2]
	at com.vaadin.flow.server.communication.ServerRpcHandler.handleRpc(ServerRpcHandler.java:324) ~[flow-server-24.3.2.jar:24.3.2]
	at com.vaadin.flow.server.communication.UidlRequestHandler.synchronizedHandleRequest(UidlRequestHandler.java:114) ~[flow-server-24.3.2.jar:24.3.2]
	at com.vaadin.flow.server.SynchronizedRequestHandler.handleRequest(SynchronizedRequestHandler.java:40) ~[flow-server-24.3.2.jar:24.3.2]
	at com.vaadin.flow.server.VaadinService.handleRequest(VaadinService.java:1573) ~[flow-server-24.3.2.jar:24.3.2]
	at com.vaadin.flow.server.VaadinServlet.service(VaadinServlet.java:398) ~[flow-server-24.3.2.jar:24.3.2]
	at com.vaadin.flow.spring.SpringServlet.service(SpringServlet.java:106) ~[vaadin-spring-24.3.2.jar:na]
	at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658) ~[tomcat-embed-core-10.1.16.jar:6.0]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:205) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.core.ApplicationDispatcher.invoke(ApplicationDispatcher.java:642) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.core.ApplicationDispatcher.processRequest(ApplicationDispatcher.java:408) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.core.ApplicationDispatcher.doForward(ApplicationDispatcher.java:313) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.core.ApplicationDispatcher.forward(ApplicationDispatcher.java:277) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.springframework.web.servlet.mvc.ServletForwardingController.handleRequestInternal(ServletForwardingController.java:141) ~[spring-webmvc-6.1.1.jar:6.1.1]
	at org.springframework.web.servlet.mvc.AbstractController.handleRequest(AbstractController.java:178) ~[spring-webmvc-6.1.1.jar:6.1.1]
	at org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter.handle(SimpleControllerHandlerAdapter.java:51) ~[spring-webmvc-6.1.1.jar:6.1.1]
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1089) ~[spring-webmvc-6.1.1.jar:6.1.1]
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:979) ~[spring-webmvc-6.1.1.jar:6.1.1]
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1014) ~[spring-webmvc-6.1.1.jar:6.1.1]
	at org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:914) ~[spring-webmvc-6.1.1.jar:6.1.1]
	at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:590) ~[tomcat-embed-core-10.1.16.jar:6.0]
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:885) ~[spring-webmvc-6.1.1.jar:6.1.1]
	at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658) ~[tomcat-embed-core-10.1.16.jar:6.0]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:205) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:51) ~[tomcat-embed-websocket-10.1.16.jar:10.1.16]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100) ~[spring-web-6.1.1.jar:6.1.1]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.1.jar:6.1.1]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93) ~[spring-web-6.1.1.jar:6.1.1]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.1.jar:6.1.1]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.springframework.web.filter.ServerHttpObservationFilter.doFilterInternal(ServerHttpObservationFilter.java:109) ~[spring-web-6.1.1.jar:6.1.1]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.1.jar:6.1.1]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201) ~[spring-web-6.1.1.jar:6.1.1]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.1.jar:6.1.1]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:167) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:90) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:482) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:115) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:93) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:340) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:391) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:896) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1744) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
	at java.base/java.lang.Thread.run(Thread.java:1589) ~[na:na]
Caused by: com.vaadin.flow.data.binder.BindingException: An exception has been thrown inside binding logic for the field element [label='Marca']
	at com.vaadin.flow.data.binder.Binder$BindingImpl.execute(Binder.java:1597) ~[flow-data-24.3.2.jar:24.3.2]
	at com.vaadin.flow.data.binder.Binder$BindingImpl.initFieldValue(Binder.java:1393) ~[flow-data-24.3.2.jar:24.3.2]
	at com.vaadin.flow.data.binder.Binder.lambda$setBean$1(Binder.java:2157) ~[flow-data-24.3.2.jar:24.3.2]
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511) ~[na:na]
	at com.vaadin.flow.data.binder.Binder.setBean(Binder.java:2157) ~[flow-data-24.3.2.jar:24.3.2]
	at cu.edu.cujae.rentacarfront.views.CarView.updateForm(CarView.java:113) ~[classes/:na]
	at cu.edu.cujae.rentacarfront.views.EntityView.lambda$configureGridListener$3fab9f70$1(EntityView.java:143) ~[classes/:na]
	at com.vaadin.flow.component.grid.AbstractGridSingleSelectionModel$1.lambda$addValueChangeListener$864f22c8$1(AbstractGridSingleSelectionModel.java:133) ~[vaadin-grid-flow-24.3.2.jar:na]
	at com.vaadin.flow.component.ComponentEventBus.fireEventForListener(ComponentEventBus.java:239) ~[flow-server-24.3.2.jar:24.3.2]
	at com.vaadin.flow.component.ComponentEventBus.fireEvent(ComponentEventBus.java:228) ~[flow-server-24.3.2.jar:24.3.2]
	at com.vaadin.flow.component.Component.fireEvent(Component.java:411) ~[flow-server-24.3.2.jar:24.3.2]
	at com.vaadin.flow.component.grid.Grid.access$000(Grid.java:218) ~[vaadin-grid-flow-24.3.2.jar:na]
	at com.vaadin.flow.component.grid.Grid$SelectionMode$1.fireSelectionEvent(Grid.java:353) ~[vaadin-grid-flow-24.3.2.jar:na]
	at com.vaadin.flow.component.grid.AbstractGridSingleSelectionModel.doSelect(AbstractGridSingleSelectionModel.java:192) ~[vaadin-grid-flow-24.3.2.jar:na]
	at com.vaadin.flow.component.grid.AbstractGridSingleSelectionModel.selectFromClient(AbstractGridSingleSelectionModel.java:66) ~[vaadin-grid-flow-24.3.2.jar:na]
	at java.base/java.util.Optional.ifPresent(Optional.java:178) ~[na:na]
	at com.vaadin.flow.component.grid.Grid.select(Grid.java:3348) ~[vaadin-grid-flow-24.3.2.jar:na]
	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:578) ~[na:na]
	at com.vaadin.flow.server.communication.rpc.PublishedServerEventHandlerRpcHandler.invokeMethod(PublishedServerEventHandlerRpcHandler.java:227) ~[flow-server-24.3.2.jar:24.3.2]
	... 68 common frames omitted
Caused by: java.lang.NullPointerException: Cannot invoke "cu.edu.cujae.rentacarfront.dto.ModelDTO.getBrand()" because the return value of "cu.edu.cujae.rentacarfront.dto.CarDTO.getModel()" is null
	at cu.edu.cujae.rentacarfront.views.CarView.lambda$validateBinder$ba6e7b7d$1(CarView.java:153) ~[classes/:na]
	at com.vaadin.flow.data.binder.Binder$BindingImpl.lambda$initFieldValue$1(Binder.java:1394) ~[flow-data-24.3.2.jar:24.3.2]
	at com.vaadin.flow.data.binder.Binder$BindingImpl.execute(Binder.java:1583) ~[flow-data-24.3.2.jar:24.3.2]
	... 87 common frames omitted
 */