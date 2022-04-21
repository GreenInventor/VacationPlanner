package dmacc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import dmacc.beans.Activity;
import dmacc.repository.ActivityRepository;

@Controller
public class ActivityController {
	@Autowired
	ActivityRepository ar;
	
	@GetMapping("/addActivity")
	public String addActivity(Model model) {
		Activity a = new Activity();
		model.addAttribute("newActivity", a);
		return "addActivity";
	}
	

	@PostMapping("/addActivity")
	public String addActivity(@ModelAttribute Activity a, Model model) {
		System.out.println("reached it");
		ar.save(a);
		return viewAllActivities(model);
	}
	
	@GetMapping("/viewAllActivities")
	public String viewAllActivities(Model model) {
		if(ar.findAll().isEmpty()) {
			return addActivity(model);
		}
		model.addAttribute("activities", ar.findAll());
		return "viewAllActivities";
	}
}
