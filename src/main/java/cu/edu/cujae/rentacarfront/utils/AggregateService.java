package cu.edu.cujae.rentacarfront.utils;

import cu.edu.cujae.rentacarfront.services.*;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class AggregateService {
    // getters for individual services
    private final TouristService touristService;
    private final CarService carService;
    private final DriverService driverService;
    private final ContractService contractService;
    private final BrandService brandService;
    private final FeeService feeService;
    private final ModelService modelService;
    private final PaymethodService paymethodService;
    private final AuthService authService;
    public AggregateService(TouristService touristService, CarService carService,
                            DriverService driverService, ContractService contractService,
                            BrandService brandService, FeeService feeService,
                            ModelService modelService, PaymethodService paymethodService,
                            AuthService authService) {

        this.touristService = touristService;
        this.carService = carService;
        this.driverService = driverService;
        this.contractService = contractService;
        this.brandService = brandService;
        this.feeService = feeService;
        this.modelService = modelService;
        this.paymethodService = paymethodService;
        this.authService = authService;
    }

}
