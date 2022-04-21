package dmacc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dmacc.beans.Activity;
import dmacc.repository.ActivityRepository;

@Controller
public class ActivityController {
	@Autowired
	ActivityRepository ar;
	
	@GetMapping("/addActivity/{id}")
	public String addActivity(Model model, @PathVariable("id") long id) {
		Activity a = new Activity();
		model.addAttribute("newActivity", a);
		model.addAttribute("id", id);
		return "addActivity";
	}
	

	@PostMapping("/addActivity/{id}")
	public String addActivity(Activity a, Model model, @PathVariable("id") long id) {
		ar.save(a);
		return viewAllActivities(model, id);
	}
	
	@GetMapping("/viewAllActivities/{id}")
	public String viewAllActivities(Model model, @PathVariable("id") long id) {
		if(ar.findAll().isEmpty()) {
			return addActivity(model, id);
		}
		model.addAttribute("activities", ar.findAll());
		model.addAttribute("id", id);
		return "viewAllActivities";
	}
	@PostMapping("/editActivity/{id}") 
	public String editActivity(Model model, @RequestParam(name="id") String activityId, @RequestParam(name="action") String action, @PathVariable("id") long id) {
		Activity a = ar.getById(Long.parseLong(activityId));
		if(action.equals("Edit")) {
			model.addAttribute("newActivity", a);
			model.addAttribute("id", id);
			return "addActivity";
		}else {
			ar.delete(a);
			return viewAllActivities(model, id);
		}	
	}
}
