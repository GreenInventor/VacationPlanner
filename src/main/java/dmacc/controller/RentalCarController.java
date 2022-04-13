package dmacc.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import dmacc.beans.Car;
import dmacc.beans.Dealership;
import dmacc.repository.AccountRepository;

@Controller
public class RentalCarController {
	@Autowired
	AccountRepository repo;

	@GetMapping("/inputCar")
	public String addNewCar(Model model) {
		Car c = new Car();
		model.addAttribute("newCar", c);
		return "";
	}

	@PostMapping("/inputCar")
	public String addNewCar(@ModelAttribute Car c, Model model) {
		return "";
	}
	@GetMapping("/inputDealership")
	public String addNewDealership(Model model) {
		Dealership d = new Dealership();
		model.addAttribute("newDealership", d);
		return "";
	}

	@PostMapping("/inputDealership")
	public String addNewDealership(@ModelAttribute Dealership d, Model model) {
		return "";
	}
}
