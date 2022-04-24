package dmacc.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dmacc.beans.Event;
import dmacc.beans.EventTicket;
import dmacc.beans.Planner;
import dmacc.repository.EventRepository;
import dmacc.repository.EventTicketRepository;
import dmacc.repository.PlannerRepository;

@Controller
public class EventController {
	@Autowired
	EventRepository eventRepo;
	@Autowired
	PlannerRepository plannerRepo;
	@Autowired
	EventTicketRepository etRepo;
	
	@GetMapping("/addEvent/{id}")
	public String addEvent(Model model, @PathVariable("id") long id) {
		Event e = new Event();
		model.addAttribute("newEvent", e);
		model.addAttribute("id", id);
		return "event";
	}
	

	@PostMapping("/addEvent/{id}")
	public String addEvent(Event e, Model model, @PathVariable("id") long id) {
		e.setStartTime(LocalTime.parse(e.getStartTime() , DateTimeFormatter.ofPattern( "HH:mm" , Locale.US)).format( DateTimeFormatter.ofPattern("h:mm a")));
		e.setEndTime(LocalTime.parse(e.getEndTime() , DateTimeFormatter.ofPattern( "HH:mm" , Locale.US)).format( DateTimeFormatter.ofPattern("h:mm a")));
		SimpleDateFormat myFormat = new SimpleDateFormat("M/d/yyyy");
		SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
		try {
			e.setDate( myFormat.format(fromUser.parse(e.getDate())));
		} catch (ParseException e1) {}
		eventRepo.save(e);
		return viewAllEvents(model, id);
	}
	
	@GetMapping("/viewAllEvents/{id}")
	public String viewAllEvents(Model model, @PathVariable("id") long id) {
		if(eventRepo.findAll().isEmpty()) {
			return addEvent(model, id);
		}
		model.addAttribute("events", eventRepo.findAll());
		model.addAttribute("id", id);
		return "viewAllEvents";
	}
	@PostMapping("/editEvent/{id}") 
	public String editEvent(Model model, @RequestParam(name="id") String eventId, @RequestParam(name="action") String action, @PathVariable("id") long id) {
		Event e = eventRepo.getById(Long.parseLong(eventId));
		if(action.equals("Edit")) {
			model.addAttribute("newEvent", e);
			model.addAttribute("id", id);
			return "event";
		}else {
			eventRepo.delete(e);
			return viewAllEvents(model, id);
		}	
	}
	@PostMapping("/planEvent/{id}") 
	public String planEvent(Model model, @RequestParam(name="id") String planId, @PathVariable("id") long id, @RequestParam(name="state") String state, @RequestParam(name="startDate") String startDate, @RequestParam(name="endDate") String endDate, @RequestParam(name="numberOfPeople") String numberOfPeople) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate formatedStartDate = LocalDate.parse(startDate, formatter);
		LocalDate formatedEndDate = LocalDate.parse(endDate, formatter);
		List<LocalDate> listOfDates = formatedStartDate.datesUntil(formatedEndDate.plusDays(1)).collect(Collectors.toList());
		List<Event> events = eventRepo.findByAddressStateAndAvalibleTicketsGreaterThanAndDateInOrderByAddressCity(state, Integer.parseInt(numberOfPeople) - 1, listOfDates);
		List<String> cities = new ArrayList<String>();
		for(Event e : events) {
				if(!cities.contains(e.getAddress().getCity())) {
				cities.add(e.getAddress().getCity());
			}
			}
		model.addAttribute("planId", planId);
		model.addAttribute("id", id);
		model.addAttribute("cities", cities);
		model.addAttribute("state", state);
		model.addAttribute("events", events);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
			return "selectEvent";
				}
	@PostMapping("/selectEvent/{id}")
	public String selectEvent(Model model, @PathVariable("id") long id, @RequestParam(name="id") String eventId, @RequestParam(name="planId") String planId, @RequestParam(name="numberOfPeople") String numberOfPeople) {
		Event event = eventRepo.getById(Long.parseLong(eventId));
		Planner plan = plannerRepo.getById(Long.parseLong(planId));
		EventTicket eventInfo = new EventTicket();
		eventInfo.setEvent(event);
		eventInfo.setNumberOfTickets(Integer.parseInt(numberOfPeople));
		eventInfo.setTotalPrice(Integer.parseInt(numberOfPeople) * event.getTicketPrice());
		etRepo.save(eventInfo);
		List<EventTicket> events = plan.getEvents();
		events.add(eventInfo);
		plan.setEvents(events);
		plannerRepo.save(plan);
		model.addAttribute("plan", plan);
		return "planner";
	}
	@PostMapping("/findEventByCity/{id}")
	public String findEventByCity(Model model, @PathVariable("id") long id, @RequestParam(name="city") String city, @RequestParam(name="planId") String planId, @RequestParam(name="state") String state, @RequestParam(name="startDate") String startDate, @RequestParam(name="endDate") String endDate, @RequestParam(name="numberOfPeople") String numberOfPeople) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate formatedStartDate = LocalDate.parse(startDate, formatter);
		LocalDate formatedEndDate = LocalDate.parse(endDate, formatter);
		List<LocalDate> listOfDates = formatedStartDate.datesUntil(formatedEndDate.plusDays(1)).collect(Collectors.toList());
		List<String> cities = new ArrayList<String>();
		System.out.println(city);
		if(!city.equals("All Cities")) {
			List<Event> e = eventRepo.findByAddressCityAndAvalibleTicketsGreaterThanAndDateInOrderByName(city, Integer.parseInt(numberOfPeople) - 1, listOfDates);	
			List<Event> stateEvents = eventRepo.findByAddressStateAndAvalibleTicketsGreaterThanAndDateInOrderByAddressCity(state, Integer.parseInt(numberOfPeople) - 1, listOfDates);
			for(Event event : stateEvents) {
					if(!cities.contains(event.getAddress().getCity())) {
						cities.add(event.getAddress().getCity());
				}
			}
				model.addAttribute("planId", planId);
				model.addAttribute("events", e);
				model.addAttribute("id", id);
				model.addAttribute("cities", cities);
				model.addAttribute("state", state);
				model.addAttribute("startDate", startDate);
				model.addAttribute("endDate", endDate);
			return "selectEvent";
		}else {
			return planEvent(model, planId, id, state, startDate, endDate, numberOfPeople);
		}
	}
}
