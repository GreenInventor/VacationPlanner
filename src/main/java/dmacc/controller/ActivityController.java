package dmacc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import dmacc.beans.Activities;

@Controller
public class ActivityController {
	@GetMapping("/inputActivity")
	public String addNewActivity(Model model) {
		Activities a = new Activities();
		model.addAttribute("newActivity", a);
		return "input";
	}
	
	//@PostMapping("/inputActivity")
	//public String addNewActivity(@ModelAttribute Activities a, Model model) {
		//repo.save(a);

	//}
}
