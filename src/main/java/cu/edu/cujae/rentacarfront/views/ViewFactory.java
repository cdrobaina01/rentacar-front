package cu.edu.cujae.rentacarfront.views;

import cu.edu.cujae.rentacarfront.utils.AggregateService;

public class ViewFactory {
    private final AggregateService aggregateService;

    public ViewFactory(AggregateService aggregateService) {
        this.aggregateService = aggregateService;
    }

    public EntityView<?,?> createView(String viewName) {
        switch (viewName) {
            case "Tourists":
                return new TouristView(aggregateService);
            case "Cars":
                // return new CarView(aggregateService);
            case "Drivers":
                // return new DriverView(aggregateService);
            case "Contracts":
                // return new ContractView(aggregateService);
            default:
                throw new IllegalArgumentException("Invalid view name: " + viewName);
        }
    }
}
