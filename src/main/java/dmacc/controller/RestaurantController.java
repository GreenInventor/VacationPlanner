package dmacc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dmacc.beans.Restaurant;
import dmacc.repository.PlannerRepository;
import dmacc.repository.RestaurantRepository;

@Controller
public class RestaurantController {
	@Autowired
	RestaurantRepository repo;
	@Autowired
	PlannerRepository plannerRepo;
	
	@GetMapping("/addRestaurant/{id}")
	public String addNewRestaurant(Model model, @PathVariable("id") long id) {
		Restaurant r = new Restaurant();
		model.addAttribute("newRestaurant", r);
		model.addAttribute("id", id);
		return "addRestaurant";
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
	@PostMapping("/editRestaurants/{id}")
	public String editRestaurants(Model model, @RequestParam(name = "id") String eventId, @RequestParam(name = "action") String action, @PathVariable("id") long id) {
		Restaurant r = repo.getById(Long.parseLong(eventId));
		if (action.equals("Edit")) {
			model.addAttribute("newRestaurant", r);
			model.addAttribute("id", id);
			return "event";
		} else {
			repo.delete(r);
			return viewAllRestaurants(model, id);
		}
	}
}
