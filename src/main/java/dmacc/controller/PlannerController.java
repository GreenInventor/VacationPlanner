package dmacc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import dmacc.beans.Planner;
import dmacc.repository.PlannerRepository;

/**
 * @author Carson Allbee callbee
 *CIS175 - Fall 2021
 * Apr 13, 2022
 */
@Controller
public class PlannerController {
	
	@Autowired
	PlannerRepository repo;
	
	@GetMapping("/viewAll")
	public String viewAllPlans(Model model) {
		model.addAttribute("plans", repo.findAll());
		return "plannerviewer";
	}
	
	@GetMapping("/inputPlanner")
	public String addNewPlan(Model model) {
		Planner p = new Planner();
		model.addAttribute("newPlanner",p);
		return "planner";
	}
	
	@PostMapping("/inputPlanner")
	public String addNewPlan(@ModelAttribute Planner p, Model model) {
		repo.save(p);
		System.out.println("Planner Created");
		System.out.println(p.toString());
		return viewAllPlans(model);
	}
	
	@GetMapping("/edit/{id}")
	public String updatePlanner(@PathVariable("id") long id, Model model) {
		Planner p = repo.findById(id).orElse(null);
		model.addAttribute("newPlanner", p);
		return "planner";
	}
	
	@PostMapping("/update/{id}")
	public String revisePlanner(Planner p, Model model) {
		repo.save(p);
		return viewAllPlans(model);
	}
	

}
