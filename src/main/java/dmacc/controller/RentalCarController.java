package dmacc.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
		model.addAttribute("id", id);
		return "adminHome";
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
		model.addAttribute("id", id);
		return "adminHome";
	}
	@GetMapping("/viewCars/{id}")
	public String viewCars(Model model, @PathVariable("id") long id) {
		List<Car> c = carRepo.findAll();
		model.addAttribute("cars", c);
		model.addAttribute("id", id);
		return "viewCar";
	}
	@GetMapping("/viewDealerships/{id}")
	public String viewDealerships(Model model, @PathVariable("id") long id) {
		List<Dealership> d = dealershipRepo.findAll();
		model.addAttribute("dealerships", d);
		model.addAttribute("id", id);
		return "viewDealerships";
	}
	@PostMapping("/findCarByState/{id}")
	public String findCarByState(Model model, @RequestParam(name="state") String state, @PathVariable("id") long id) {
			List<Car> c = carRepo.findByDealershipAddressState(state);	
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
			List<Car> cars = carRepo.findAll();
			model.addAttribute("cars", cars);
			return "viewCar";
		}	
	}
	@PostMapping("/editDealership/{id}") 
	public String editDealership(Model model, @RequestParam(name="id") String dealershipId, @RequestParam(name="action") String action, @PathVariable("id") long id) {
		Dealership d = dealershipRepo.getById(Long.parseLong(dealershipId));
		model.addAttribute("id", id);
		if(action.equals("Edit")) {
			model.addAttribute("newDealership", d);
			return "car";
		}else {
			dealershipRepo.delete(d);
			List<Dealership> dealerships = dealershipRepo.findAll();
			model.addAttribute("dealerships", dealerships);
			return "viewDealerships";
		}
	}
}
