package dmacc.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dmacc.beans.Activity;
import dmacc.beans.Planner;
import dmacc.repository.ActivityRepository;
import dmacc.repository.PlannerRepository;

@Controller
public class ActivityController {
	@Autowired
	ActivityRepository ar;
	@Autowired
	PlannerRepository plannerRepo;
	
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
	@PostMapping("/findActivityByState/{id}")
	public String findActivityByState(Model model, @RequestParam(name = "state") String state, @PathVariable("id") long id) {
		List<Activity> a = ar.findByAddressStateOrderByAddressCity(state);
		model.addAttribute("activities", a);
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
	@PostMapping("/planActivity/{id}") 
	public String planActivity(Model model, @RequestParam(name="id") String planId, @PathVariable("id") long id, @RequestParam(name="state") String state) {
		List<Activity> activities = ar.findByAddressStateOrderByAddressCity(state);;
		List<String> cities = new ArrayList<String>();
		List<Activity> avalibleActivities = new ArrayList<Activity>();
		Planner p = plannerRepo.getById(Long.parseLong(planId));
		for(Activity a : activities) {
			if(!p.getActivities().contains(a)) {
				avalibleActivities.add(a);
				if(!cities.contains(a.getAddress().getCity())) {
				cities.add(a.getAddress().getCity());
				}
			}
			}
			
			
		model.addAttribute("planId", planId);
		model.addAttribute("id", id);
		model.addAttribute("cities", cities);
		model.addAttribute("state", state);
		model.addAttribute("activities", avalibleActivities);
			return "selectActivity";
				}
	@PostMapping("/selectActivity/{id}")
	public String selectActivity(Model model, @PathVariable("id") long id, @RequestParam(name="id") String activityId, @RequestParam(name="planId") String planId) {
		Activity activity = ar.getById(Long.parseLong(activityId));
		Planner plan = plannerRepo.getById(Long.parseLong(planId));
		List<Activity> activities = plan.getActivities();
		activities.add(activity);
		plan.setActivities(activities);
		plannerRepo.save(plan);
		model.addAttribute("plan", plan);
		return "planner";
	}
	@PostMapping("/findActivityByCity/{id}")
	public String findActivityByCity(Model model, @PathVariable("id") long id, @RequestParam(name="city") String city, @RequestParam(name="planId") String planId, @RequestParam(name="state") String state) {
		List<String> cities = new ArrayList<String>();
		System.out.println(city);
		if(!city.equals("All Cities")) {
			List<Activity> a = ar.findByAddressCityOrderByActivityName(city);	
			List<Activity> avalibleActivities = new ArrayList<Activity>();
			Planner p = plannerRepo.getById(Long.parseLong(planId));
			List<Activity> stateActivities = ar.findByAddressStateOrderByAddressCity(state);
			for(Activity activity : stateActivities) {
				if(!p.getActivities().contains(activity)) {
					if(!cities.contains(activity.getAddress().getCity())) {
						cities.add(activity.getAddress().getCity());
					}
				}
			}
			for(Activity activity : a) {
				if(!p.getActivities().contains(activity)) {
					avalibleActivities.add(activity);
				}
			}
				model.addAttribute("planId", planId);
				model.addAttribute("activities", avalibleActivities);
				model.addAttribute("id", id);
				model.addAttribute("cities", cities);
				model.addAttribute("state", state);
			return "selectActivity";
		}else {
			return planActivity(model, planId, id, state);
		}
	}
	@PostMapping("/removeActivity/{id}") 
	public String removeActivity(Model model, @RequestParam(name="activityId") String activityId, @RequestParam(name="planId") String planId, @PathVariable("id") long id) {
		Activity a = ar.getById(Long.parseLong(activityId));
		Planner plan = plannerRepo.getById(Long.parseLong(planId));
		List<Activity> activities = plan.getActivities();
		activities.remove(a);
		plan.setActivities(activities);
		plannerRepo.save(plan);
		model.addAttribute("id", id);
		model.addAttribute("plan", plan);
		return "planner";
	}
}
