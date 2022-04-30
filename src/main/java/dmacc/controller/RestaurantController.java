package dmacc.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dmacc.beans.Planner;
import dmacc.beans.Reservation;
import dmacc.beans.Restaurant;
import dmacc.repository.PlannerRepository;
import dmacc.repository.ReservationRepository;
import dmacc.repository.RestaurantRepository;

@Controller
public class RestaurantController {
	@Autowired
	RestaurantRepository repo;
	@Autowired
	PlannerRepository plannerRepo;
	@Autowired
	ReservationRepository rRepo;
	
	@GetMapping("/addRestaurant/{id}")
	public String addNewRestaurant(Model model, @PathVariable("id") long id) {
		Restaurant r = new Restaurant();
		model.addAttribute("newRestaurant", r);
		model.addAttribute("id", id);
		return "addRestaurant";
	}

	@PostMapping("/inputRestaurant/{id}")
	public String addNewRestaurant(Restaurant r, Model model, @PathVariable("id") long id) {
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
			return "addRestaurant";
		} else {
			List<Reservation> reservations = rRepo.getByRestaurant(r);
			for(Reservation reservation : reservations) {
				rRepo.delete(reservation);
			}
			repo.delete(r);
			return viewAllRestaurants(model, id);
		}
	}
	
	
	@PostMapping("/findRestaurants/{id}")
	public String findRestaurants(Model model, @RequestParam(name = "id") String planId, @PathVariable("id") long id, @RequestParam(name = "state") String state, @RequestParam(name = "startTime") String startTime, @RequestParam(name = "endTime") String endTime, @RequestParam(name = "numberOfPeople") String numberOfPeople, @RequestParam(name = "date") String date) {
		List<Restaurant> restaurants = repo.findByAddressStateOrderByAddressCity(state);
		List<String> cities = new ArrayList<String>();
		for (Restaurant r : restaurants) {
			if (!cities.contains(r.getAddress().getCity())) {
				cities.add(r.getAddress().getCity());
			}
		}
		model.addAttribute("planId", planId);
		model.addAttribute("id", id);
		model.addAttribute("cities", cities);
		model.addAttribute("state", state);
		model.addAttribute("restaurants", restaurants);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("numberOfPeople", numberOfPeople);
		model.addAttribute("date", date);
		return "selectRestaurant";
	}

	@PostMapping("/selectRestaurant/{id}")
	public String selectRestaurant(Model model, @PathVariable("id") long id, @RequestParam(name = "id") String restaurantId, @RequestParam(name = "planId") String planId, @RequestParam(name = "startTime") String startTime, @RequestParam(name = "endTime") String endTime, @RequestParam(name = "numberOfPeople") String numberOfPeople, @RequestParam(name = "date") String date) {
		Restaurant restaurant = repo.getById(Long.parseLong(restaurantId));
		Planner plan = plannerRepo.getById(Long.parseLong(planId));
		Reservation reservation = new Reservation();
		reservation.setRestaurant(restaurant);
		reservation.setStartTime(startTime);
		reservation.setEndTime(endTime);
		reservation.setNumberOfPeople(Integer.parseInt(numberOfPeople));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate formatedDate = LocalDate.parse(date, formatter);
		reservation.setDate(formatedDate);
		rRepo.save(reservation);
		List<Reservation> reservations = plan.getReservations();
		reservations.add(reservation);
		plan.setReservations(reservations);
		plannerRepo.save(plan);
		model.addAttribute("plan", plan);
		return "planner";
	}

	@PostMapping("/findRestaurantsByState/{id}")
	public String findRestaurantByState(Model model, @RequestParam(name = "state") String state, @PathVariable("id") long id) {
		List<Restaurant> r = repo.findByAddressStateOrderByAddressCity(state);
		model.addAttribute("restaurants", r);
		model.addAttribute("id", id);
		return "viewAllRestaurants";
	}

	@PostMapping("/findRestaurantByCity/{id}")
	public String findRestaurantByCity(Model model, @PathVariable("id") long id, @RequestParam(name = "city") String city,
			@RequestParam(name = "planId") String planId, @RequestParam(name = "state") String state,
			@RequestParam(name = "startTime") String startTime, @RequestParam(name = "endTime") String endTime,
			@RequestParam(name = "numberOfPeople") String numberOfPeople, @RequestParam(name = "date") String date) {
		List<String> cities = new ArrayList<String>();
		if (!city.equals("All Cities")) {
			List<Restaurant> r = repo.findByAddressCityOrderByName(city);
			List<Restaurant> stateRestaurants = repo.findByAddressStateOrderByAddressCity(state);
			for (Restaurant restaurant : stateRestaurants) {
				if (!cities.contains(restaurant.getAddress().getCity())) {
					cities.add(restaurant.getAddress().getCity());
				}
			}
			model.addAttribute("planId", planId);
			model.addAttribute("restaurants", r);
			model.addAttribute("id", id);
			model.addAttribute("cities", cities);
			model.addAttribute("state", state);
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
			model.addAttribute("numberOfPeople", numberOfPeople);
			model.addAttribute("date", date);
			return "selectRestaurant";
		} else {
			return findRestaurants(model, planId, id, state, startTime, endTime, numberOfPeople, date);
		}
	}
	
	@PostMapping("/cancelReservation/{id}")
	public String cancelReservation(Model model, @RequestParam(name = "reservationId") String reservationId, @RequestParam(name = "planId") String planId, @PathVariable("id") long id) {
		Reservation reservation = rRepo.getById(Long.parseLong(reservationId));
		Planner plan = plannerRepo.getById(Long.parseLong(planId));
		List<Reservation> reservations = plan.getReservations();
		reservations.remove(reservation);
		plan.setReservations(reservations);
		plannerRepo.save(plan);
		rRepo.delete(reservation);
		model.addAttribute("id", id);
		model.addAttribute("plan", plan);
		return "planner";
	}
	
	
	
}
