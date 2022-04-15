package dmacc.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dmacc.beans.Car;
import dmacc.beans.Dealership;
import dmacc.repository.CarRepository;
import dmacc.repository.DealershipRepository;

@Controller
public class RentalCarController {
	@Autowired
	CarRepository carRepo;
	@Autowired
	DealershipRepository dealershipRepo;

	@GetMapping("/inputCar")
	public String addNewCar(Model model) {
		Car c = new Car();
		List<Dealership> dealerships = dealershipRepo.findAll();
		model.addAttribute("dealerships", dealerships);
		model.addAttribute("newCar", c);
		return "car";
	}

	@PostMapping("/inputCar")
	public String addNewCar(@ModelAttribute Car c, Model model) {
		carRepo.save(c);
		return "adminHome";
	}
	@GetMapping("/inputDealership")
	public String addNewDealership(Model model) {
		Dealership d = new Dealership();
		model.addAttribute("newDealership", d);
		return "dealership";
	}

	@PostMapping("/inputDealership")
	public String addNewDealership(@ModelAttribute Dealership d, Model model) {
		dealershipRepo.save(d);
		return "adminHome";
	}
	@GetMapping("/viewCars")
	public String viewCars(Model model) {
		List<Car> c = carRepo.findAll();
		model.addAttribute("cars", c);
		return "viewCar";
	}
	@GetMapping("/viewDealerships")
	public String viewDealerships(Model model) {
		List<Dealership> d = dealershipRepo.findAll();
		model.addAttribute("dealerships", d);
		return "viewDealerships";
	}
	@PostMapping("/findCarByState")
	public String findCarByState(Model model, @RequestParam(name="state") String state) {
		List<Dealership> d = dealershipRepo.findByAddressState(state);
		List<Car>cars = new ArrayList<>();
		for(Dealership currentDealership : d) {
			List<Car> c = carRepo.findByDealership(currentDealership);
			cars.addAll(c);
		}		
		model.addAttribute("cars", cars);
		return "viewCar";
	}
	@PostMapping("/findDealershipByState")
	public String findDealershipByState(Model model, @RequestParam(name="state") String state) {
		List<Dealership> d = dealershipRepo.findByAddressState(state);
		model.addAttribute("dealerships", d);
		return "viewDealerships";
	}
}
