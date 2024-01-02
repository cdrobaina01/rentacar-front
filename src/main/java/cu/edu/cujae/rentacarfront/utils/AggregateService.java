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
    private final CountryService countryService;

    private final FeeService feeService;

    private final GenderService genderService;

    private final ModelService modelService;

    private final PaymethodService paymethodService;

    private final SituationService situationService;



    public AggregateService(TouristService touristService, CarService carService,
                            DriverService driverService, ContractService contractService,
                            BrandService brandService, CountryService countryService,
                            FeeService feeService, GenderService genderService,
                            ModelService modelService, PaymethodService paymethodService,
                            SituationService situationService) {
        this.touristService = touristService;
        this.carService = carService;
        this.driverService = driverService;
        this.contractService = contractService;
        this.brandService = brandService;
        this.countryService = countryService;
        this.feeService = feeService;
        this.genderService = genderService;
        this.modelService = modelService;
        this.paymethodService = paymethodService;
        this.situationService = situationService;
    }

}
