package dmacc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
		return "input";
	}
	
	@GetMapping("/editActivity/{id}")
	public String editActivity(@PathVariable("id") long id, Model model) {
		Activity a = ar.findById(id).orElse(null);
		model.addAttribute("newActivity", a);
		return "input";
	}
	
	@PostMapping("/updateActivity/{id}")
	public String updateActivity(Activity a, Model model) {
		ar.save(a);
		return viewAllActivities(model);
	}
	
	@GetMapping("/deleteActivity/{id}")
	public String deleteActivity(@PathVariable("id") long id, Model model) {
		Activity a = ar.findById(id).orElse(null);
		ar.delete(a);
		return viewAllActivities(model);
	}
	
	@GetMapping({"/viewAllActivities" })
	public String viewAllActivities(Model model) {
		if(ar.findAll().isEmpty()) {
			return addActivity(model);
			}
		model.addAttribute("allActivities",ar.findAll());
		return "results";
	}
}
