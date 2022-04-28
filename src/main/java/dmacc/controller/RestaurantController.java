package dmacc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import dmacc.beans.Restaurant;
import dmacc.repository.PlannerRepository;
import dmacc.repository.RestaurantRepository;

@Controller
public class RestaurantController {
	@Autowired
	RestaurantRepository repo;
	@Autowired
	PlannerRepository plannerRepo;
	
	@GetMapping("/inputRestaurant/{id}")
	public String addNewRestaurant(Model model, @PathVariable("id") long id) {
		Restaurant r = new Restaurant();
		model.addAttribute("newRestaurant", r);
		model.addAttribute("id", id);
		return "restaurant";
	}

	@PostMapping("/inputRestaurant/{id}")
	public String addNewRestaurant(@ModelAttribute Restaurant r, Model model, @PathVariable("id") long id) {
		repo.save(r);
		return viewAllRestaurants(model, id);
	}
	
	@GetMapping("/viewAllRestaurants/{id}")
	public String viewAllRestaurants(Model model, @PathVariable("id") long id) {
		if (repo.findAll().isEmpty()) {
			return addNewRestaurant(model, id);
		}
		model.addAttribute("restaurants", repo.findAll());
		model.addAttribute("id", id);
		return "viewAllRestaurants";
	}
}
