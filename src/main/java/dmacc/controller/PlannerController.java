package dmacc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
	
	

}
