package dmacc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dmacc.beans.Account;
import dmacc.beans.Hotel;
import dmacc.beans.Planner;
import dmacc.repository.AccountRepository;
import dmacc.repository.HotelRepository;
import dmacc.repository.PlannerRepository;

@Controller
public class PlannerController {
	@Autowired
	PlannerRepository repo;
	@Autowired
	AccountRepository aRepo;
	@Autowired
	HotelRepository hotelRepo; 
	
	@GetMapping("/adminHome/{id}")
	public String viewAllPlans(Model model, @PathVariable("id") long id) {
		model.addAttribute("plans", repo.findAll());
		model.addAttribute("id", id);
		return "adminHome";
	}
	
	@GetMapping("/createPlan/{id}")
	public String addNewPlan(Model model, @PathVariable("id") long id) {
		Planner p = new Planner();
		model.addAttribute("newPlan", p);
		model.addAttribute("id", id);
		return "plan";
	}
	
	@PostMapping("/createPlan/{id}")
	public String addNewPlan(Planner p, Model model, @PathVariable("id") long id) {
		Account a = aRepo.getById(id);
		p.setAccount(a);
		repo.save(p);
		System.out.println("Planner Created");
		System.out.println(p.toString());
		model.addAttribute("plan", p);
		return "planner";
	}
	
	@PostMapping("/editPlan/{id}") 
	public String updatePlanner(@RequestParam("id") long plannerId, Model model, @PathVariable("id") long id, @RequestParam(name="action") String action) {
		model.addAttribute("id", id);
		if(action.equals("Edit")) {
			List<Hotel> h = hotelRepo.findAll(); //TODO hotels aren't separated by account
			model.addAttribute("plan", repo.getById(plannerId));
			model.addAttribute("hotels", h);
			return "planner";
		}else {
			repo.deleteById(plannerId);
			model.addAttribute("plans", repo.findByAccountId(id));
			return "home";
		}
		
	}
	
	@PostMapping("/update/{id}")
	public String revisePlanner(Planner p, Model model, @PathVariable("id") long id) {
		repo.save(p);
		return viewAllPlans(model, id);
	}
	@PostMapping("/editName/{id}") 
	public String editName(@RequestParam("id") long plannerId, @RequestParam("name") String name, Model model, @PathVariable("id") long id) {
		Planner p = repo.getById(plannerId);
		p.setName(name);
		repo.save(p);
		model.addAttribute("plan", p);
		model.addAttribute("id", id);
		return "planner";
		
	}
}
