package dmacc.controller;

import java.util.List;
import java.util.Objects;

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
	@GetMapping("/selectDealership")
	public String selectDealership(Model model) {
		List<Dealership> d = dealershipRepo.findAll();
		model.addAttribute("allDealerships", d);
		return "selectDealership";
	}
	@PostMapping("/selectDealership")
	public String selectDealership(Model model, @RequestParam(name = "selectedDealership", required = false) String selectedDealership) {
		System.out.println(selectedDealership);
		Dealership d = dealershipRepo.getById(Long.parseLong(selectedDealership));
		List<Car> c = carRepo.findAll();
		model.addAttribute("dealership", d);
		model.addAttribute("cars", c);
		return "manageDealershipCars";
	}
}
