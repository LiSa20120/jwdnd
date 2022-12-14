package com.udacity.vehicles.service;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarService {
    @Autowired
    CarRepository carRepository;

    @Autowired
    MapsClient mapsClient;

    @Autowired
    PriceClient priceClient;

    /**
     * Gathers a list of all vehicles
     * @return a list of all vehicles in the CarRepository
     */
    public List<Car> list() {
        return carRepository.findAll().stream().map(car -> {
            setPrice(car);
            setAddress(car);
            return car;
        }).collect(Collectors.toList());
    }

    /**
     * Gets car information by ID (or throws exception if non-existent)
     * @param id the ID number of the car to gather information on
     * @return the requested car's information, including location and price
     */
    public Car findById(Long id) {
        var car = carRepository.findById(id).orElseThrow(CarNotFoundException::new);
        setPrice(car);
        setAddress(car);
        return car;
    }

    /**
     * Either creates or updates a vehicle, based on prior existence of car
     * @param car A car object, which can be either new or existing
     * @return the new/updated car is stored in the carRepository
     */
    public Car save(Car car) {
        if (car.getId() != null) {
            return carRepository.findById(car.getId())
                    .map(carToBeUpdated -> {
                        carToBeUpdated.setDetails(car.getDetails());
                        carToBeUpdated.setLocation(car.getLocation());
                        carToBeUpdated.setCondition(car.getCondition());
                        return carRepository.save(carToBeUpdated);
                    }).orElseThrow(CarNotFoundException::new);
        }

        return carRepository.save(car);
    }

    /**
     * Deletes a given car by ID
     * @param id the ID number of the car to delete
     */
    public void delete(Long id) {
        var car = carRepository.findById(id).orElseThrow(CarNotFoundException::new);
        carRepository.deleteById(id);
    }

    private void setPrice(Car car) {
        car.setPrice(priceClient.getPrice(car.getId()));
    }

    private void setAddress(Car car) {
        car.setLocation(mapsClient.getAddress(car.getLocation()));
    }
}
