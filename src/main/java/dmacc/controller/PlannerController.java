package dmacc.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dmacc.beans.Account;
import dmacc.beans.Car;
import dmacc.beans.CarRental;
import dmacc.beans.Event;
import dmacc.beans.EventTicket;
import dmacc.beans.Hotel;
import dmacc.beans.HotelRental;
import dmacc.beans.Planner;
import dmacc.repository.AccountRepository;
import dmacc.repository.CarRentalRepository;
import dmacc.repository.CarRepository;
import dmacc.repository.EventRepository;
import dmacc.repository.EventTicketRepository;
import dmacc.repository.HotelRentalRepository;
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
	@Autowired
	CarRepository carRepo;
	@Autowired
	CarRentalRepository carRentalRepo;
	@Autowired
	HotelRentalRepository hotelRentalRepo;
	@Autowired
	EventTicketRepository etRepo;
	@Autowired
	EventRepository eventRepo;

	@GetMapping("/adminHome/{id}")
	public String viewAllPlans(Model model, @PathVariable("id") long id) {
		model.addAttribute("plans", repo.findAll());
		model.addAttribute("id", id);
		model.addAttribute("users", aRepo.findByAccountType("user"));
		model.addAttribute("admins", aRepo.findByAccountType("admin"));
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
	public String updatePlanner(@RequestParam("id") long plannerId, Model model, @PathVariable("id") long id, @RequestParam(name = "action") String action) {
		model.addAttribute("id", id);
		if (action.equals("Edit")) {
			model.addAttribute("plan", repo.getById(plannerId));
			return "planner";
		} else {
			Planner p = repo.getById(plannerId);
			List<CarRental> rentals = p.getCarRentals();
			List<HotelRental> hRentals = p.getHotelRentals();
			List<EventTicket> eventTickets = p.getEvents();
			for (CarRental r : rentals) {
				LocalDate startDate = r.getRentalStartDate();
				LocalDate endDate = r.getRentalEndDate();
				List<LocalDate> listOfDates = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());
				Car c = r.getCar();
				ArrayList<LocalDate> dates = c.getDaysRented();
				for (LocalDate date : listOfDates) {
					dates.remove(date);
				}
				c.setDaysRented(dates);
				carRepo.save(c);
				carRentalRepo.delete(r);
			}
			for (HotelRental hr : hRentals) {
				LocalDate startDate = hr.getRentalStartDate();
				LocalDate endDate = hr.getRentalEndDate();
				List<LocalDate> listOfDates = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());
				Hotel h = hr.getHotel();
				ArrayList<LocalDate> dates = h.getDaysRented();
				for (LocalDate date : listOfDates) {
					dates.remove(date);
				}
				h.setDaysRented(dates);
				hotelRepo.save(h);
				hotelRentalRepo.delete(hr);
			}
			for (EventTicket et : eventTickets) {
				Event event = et.getEvent();
				event.setAvalibleTickets(event.getAvalibleTickets() + et.getNumberOfTickets());
				eventRepo.save(event);
				etRepo.delete(et);
			}
			repo.delete(p);
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
	@PostMapping("/viewPlanDetails/{id}")
	public String viewPlanDetails(Model model, @PathVariable("id") long id, @RequestParam("id") Long planId) {
	model.addAttribute("plan", repo.getById(planId));
		return "planDetails";
	}
}
