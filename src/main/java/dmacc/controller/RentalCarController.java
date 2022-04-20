package dmacc.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dmacc.beans.Car;
import dmacc.beans.CarRental;
import dmacc.beans.Dealership;
import dmacc.beans.Planner;
import dmacc.repository.CarRentalRepository;
import dmacc.repository.CarRepository;
import dmacc.repository.DealershipRepository;
import dmacc.repository.PlannerRepository;

@Controller
public class RentalCarController 
{
	@Autowired
	CarRepository carRepo;
	@Autowired
	DealershipRepository dealershipRepo;
	@Autowired
	PlannerRepository plannerRepo;
	@Autowired
	CarRentalRepository rentalRepo;

	@GetMapping("/inputCar/{id}")
	public String addNewCar(Model model, @PathVariable("id") long id) {
		Car c = new Car();
		List<Dealership> dealerships = dealershipRepo.findAll();
		model.addAttribute("dealerships", dealerships);
		model.addAttribute("newCar", c);
		model.addAttribute("id", id);
		return "car";
	}

	@PostMapping("/inputCar/{id}")
	public String addNewCar( Car c, Model model, @PathVariable("id") long id) {
		carRepo.save(c);
		return viewCars(model, id);
	}
	@GetMapping("/inputDealership/{id}")
	public String addNewDealership(Model model, @PathVariable("id") long id) {
		Dealership d = new Dealership();
		model.addAttribute("newDealership", d);
		model.addAttribute("id", id);
		return "dealership";
	}

	@PostMapping("/inputDealership/{id}")
	public String addNewDealership(Dealership d, Model model, @PathVariable("id") long id) {
		dealershipRepo.save(d);
		return viewDealerships(model, id);
	}
	@GetMapping("/viewCars/{id}")
	public String viewCars(Model model, @PathVariable("id") long id) {
		List<Car> c = carRepo.findAll();
		if(c.isEmpty()) {
			return addNewCar(model, id);
		}
		model.addAttribute("cars", c);
		model.addAttribute("id", id);
		return "viewCar";
	}
	@GetMapping("/viewDealerships/{id}")
	public String viewDealerships(Model model, @PathVariable("id") long id) {
		List<Dealership> d = dealershipRepo.findAll();
		if(d.isEmpty()) {
			return addNewDealership(model, id);
		}
		model.addAttribute("dealerships", d);
		model.addAttribute("id", id);
		return "viewDealerships";
	}
	@PostMapping("/findCarByState/{id}")
	public String findCarByState(Model model, @RequestParam(name="state") String state, @PathVariable("id") long id) {
			List<Car> c = carRepo.findByDealershipAddressStateOrderByDealershipAddressCity(state);	
		model.addAttribute("cars", c);
		model.addAttribute("id", id);
		return "viewCar";
	}
	@PostMapping("/findDealershipByState/{id}")
	public String findDealershipByState(Model model, @RequestParam(name="state") String state, @PathVariable("id") long id) {
		List<Dealership> d = dealershipRepo.findByAddressState(state);
		model.addAttribute("dealerships", d);
		model.addAttribute("id", id);
		return "viewDealerships";
	}
	@PostMapping("/editCar/{id}") 
	public String editCar(Model model, @RequestParam(name="id") String carId, @RequestParam(name="action") String action, @PathVariable("id") long id) {
		Car c = carRepo.getById(Long.parseLong(carId));
		model.addAttribute("id", id);
		if(action.equals("Edit")) {
			List<Dealership> dealerships = dealershipRepo.findAll();
			model.addAttribute("dealerships", dealerships);
			model.addAttribute("newCar", c);
			return "car";
		}else {
			carRepo.delete(c);
			return viewCars(model, id);
		}	
	}
	@PostMapping("/editDealership/{id}") 
	public String editDealership(Model model, @RequestParam(name="id") String dealershipId, @RequestParam(name="action") String action, @PathVariable("id") long id) {
		Dealership d = dealershipRepo.getById(Long.parseLong(dealershipId));
		model.addAttribute("id", id);
		if(action.equals("Edit")) {
			model.addAttribute("newDealership", d);
			return "dealership";
		}else {
			List<Car> cars = carRepo.findByDealership(d);
			for(Car car : cars) {
				carRepo.delete(car);
			}
			dealershipRepo.delete(d);
			return viewDealerships(model, id);
		}
	}
	@PostMapping("/addCar/{id}")
	public String addCar(Model model, @PathVariable("id") long id, @RequestParam(name="startDate") String startDate, @RequestParam(name="endDate") String endDate, @RequestParam(name="state") String state, @RequestParam(name="id") String planId) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate formatedStartDate = LocalDate.parse(startDate, formatter);
		LocalDate formatedEndDate = LocalDate.parse(endDate, formatter);
		List<LocalDate> listOfDates = formatedStartDate.datesUntil(formatedEndDate.plusDays(1)).collect(Collectors.toList());
		
		for(LocalDate date: listOfDates) {
			System.out.println(date);
		}
		
		List<Car> cars = carRepo.findByDealershipAddressStateOrderByDealershipAddressCity(state);
		List<Car> avalibleCars = new ArrayList<Car>();
		List<String> cities = new ArrayList<String>();
		for(Car car : cars) {
			String city = car.getDealership().getAddress().getCity();
			try {
			if(!car.getDaysRented().stream().anyMatch(listOfDates::contains)) {
				avalibleCars.add(car);
				if(!cities.contains(city)) {
					cities.add(city);
				}
			}
			}catch(Exception e) {
				avalibleCars.add(car);
				if(!cities.contains(city)) {
					cities.add(city);
				}
			}
			}
		model.addAttribute("planId", planId);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("cars", avalibleCars);
		model.addAttribute("id", id);
		model.addAttribute("cities", cities);
		model.addAttribute("state", state);
		return "rentCar";
	}
	@PostMapping("/rentCar/{id}")
	public String rentCar(Model model, @PathVariable("id") long id, @RequestParam(name="startDate") String startDate, @RequestParam(name="endDate") String endDate, @RequestParam(name="id") String carId, @RequestParam(name="planId") String planId) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate formatedStartDate = LocalDate.parse(startDate, formatter);
		LocalDate formatedEndDate = LocalDate.parse(endDate, formatter);
		List<LocalDate> listOfDates = formatedStartDate.datesUntil(formatedEndDate.plusDays(1)).collect(Collectors.toList());
		CarRental rental = new CarRental();
		Car car = carRepo.getById(Long.parseLong(carId));
		ArrayList<LocalDate> rentalDates = new ArrayList<LocalDate>();
		try {
			rentalDates.addAll(car.getDaysRented());
		}catch(Exception e) {}
			
		rentalDates.addAll(listOfDates);
		car.setDaysRented(rentalDates);
		carRepo.save(car);
		rental.setRentalStartDate(formatedStartDate);
		rental.setRentalEndDate(formatedEndDate);
		rental.setRentalTotal(car.getPricePerDay() * listOfDates.size());
		rental.setCar(car);
		rentalRepo.save(rental);
		Planner plan = plannerRepo.getById(Long.parseLong(planId));
		List<CarRental> rentals = plan.getCarRentals();
		rentals.add(rental);
		plan.setCarRentals(rentals);
		plannerRepo.save(plan);
		System.out.println(rentalDates);
		model.addAttribute("plan", plan);
		return "planner";
	}
	@PostMapping("/findCarByCity/{id}")
	public String findCarByCity(Model model, @PathVariable("id") long id, @RequestParam(name="startDate") String startDate, @RequestParam(name="endDate") String endDate, @RequestParam(name="city") String city, @RequestParam(name="planId") String planId, @RequestParam(name="state") String state) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate formatedStartDate = LocalDate.parse(startDate, formatter);
		LocalDate formatedEndDate = LocalDate.parse(endDate, formatter);
		List<LocalDate> listOfDates = formatedStartDate.datesUntil(formatedEndDate).collect(Collectors.toList());	
		List<Car> avalibleCars = new ArrayList<Car>();
		List<String> cities = new ArrayList<String>();
		if(!city.equals("All Cities")) {
			List<Car> c = carRepo.findByDealershipAddressCityOrderByDealershipName(city);	
			for(Car car : c) {
				try {
				if(car.getDaysRented().stream().anyMatch(listOfDates::contains)) {
					avalibleCars.add(car);
				}
				}catch(Exception e) {
					avalibleCars.add(car);
				}
				}
		}else {
			return addCar(model,id,startDate,endDate,state,planId);
		}
		List<Car> stateCars = carRepo.findByDealershipAddressStateOrderByDealershipAddressCity(state);
		for(Car car : stateCars) {
			String testCity = car.getDealership().getAddress().getCity();
			if(!cities.contains(testCity)) {
				cities.add(testCity);
			}
		}
			model.addAttribute("planId", planId);
			model.addAttribute("startDate", startDate);
			model.addAttribute("endDate", endDate);
			model.addAttribute("cars", avalibleCars);
			model.addAttribute("id", id);
			model.addAttribute("cities", cities);
			model.addAttribute("state", state);
		return "rentCar";
	}
	@PostMapping("/cancelRentalCar/{id}") 
	public String cancelRentalCar(Model model, @RequestParam(name="rentalId") String rentalId, @RequestParam(name="planId") String planId,@PathVariable("id") long id) {
		CarRental rental = rentalRepo.getById(Long.parseLong(rentalId));
		LocalDate startDate = rental.getRentalStartDate();
		LocalDate endDate = rental.getRentalEndDate();
		List<LocalDate> listOfDates = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());	
		Car c = rental.getCar();
		ArrayList<LocalDate> dates = c.getDaysRented();
		for(LocalDate date : listOfDates) {
			dates.remove(date);
		}
		c.setDaysRented(dates);
		carRepo.save(c);
		Planner plan = plannerRepo.getById(Long.parseLong(planId));
		List<CarRental> rentals = plan.getCarRentals();
		rentals.remove(rental);
		plan.setCarRentals(rentals);
		plannerRepo.save(plan);
		rentalRepo.delete(rental);
		model.addAttribute("id", id);
		model.addAttribute("plan", plan);
		return "planner";
	}
}
