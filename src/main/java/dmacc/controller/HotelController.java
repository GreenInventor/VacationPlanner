package dmacc.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dmacc.beans.Hotel;
import dmacc.beans.HotelRental;
import dmacc.beans.Planner;
import dmacc.repository.HotelRentalRepository;
import dmacc.repository.HotelRepository;
import dmacc.repository.PlannerRepository;

@Controller
public class HotelController {
	@Autowired
	HotelRepository repo;
	@Autowired
	PlannerRepository plannerRepo;
	@Autowired
	HotelRentalRepository rentalRepo;

	@GetMapping("/inputHotel/{id}")
	public String addNewHotel(Model model, @PathVariable("id") long id) {
		Hotel h = new Hotel();
		model.addAttribute("newHotel", h);
		model.addAttribute("id", id);
		return "hotel";
	}

	@PostMapping("/inputHotel/{id}")
	public String addNewHotel(@ModelAttribute Hotel h, Model model, @PathVariable("id") long id) {
		repo.save(h);
		return viewAllHotels(model, id);
	}

	@GetMapping("/viewAllHotels/{id}")
	public String viewAllHotels(Model model, @PathVariable("id") long id) {
		if (repo.findAll().isEmpty()) {
			return addNewHotel(model, id);
		}
		model.addAttribute("hotels", repo.findAll());
		model.addAttribute("id", id);
		return "viewAllHotels";
	}

	@PostMapping("/findHotelByState/{id}")
	public String findHotelByState(Model model, @RequestParam(name = "state") String state,
			@PathVariable("id") long id) {
		List<Hotel> h = repo.findByAddressStateOrderByAddressCity(state);
		model.addAttribute("hotels", h);
		model.addAttribute("id", id);
		return "viewAllHotels";
	}

	@PostMapping("/addHotel/{id}")
	public String addHotel(Model model, @PathVariable("id") long id, @RequestParam(name = "startDate") String startDate,
			@RequestParam(name = "endDate") String endDate, @RequestParam(name = "state") String state,
			@RequestParam(name = "id") String planId) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate formatedStartDate = LocalDate.parse(startDate, formatter);
		LocalDate formatedEndDate = LocalDate.parse(endDate, formatter);
		List<LocalDate> listOfDates = formatedStartDate.datesUntil(formatedEndDate.plusDays(1)).collect(Collectors.toList());

		for (LocalDate date : listOfDates) {
			System.out.println(date);
		}

		List<Hotel> hotels = repo.findByAddressStateOrderByAddressCity(state);
		List<Hotel> avalibleHotels = new ArrayList<Hotel>();
		List<String> cities = new ArrayList<String>();
		for (Hotel hotel : hotels) {
			String city = hotel.getAddress().getCity();
			try {
				if (!hotel.getDaysRented().stream().anyMatch(listOfDates::contains)) {
					avalibleHotels.add(hotel);
					if (!cities.contains(city)) {
						cities.add(city);
					}
				}
			} catch (Exception e) {
				avalibleHotels.add(hotel);
				if (!cities.contains(city)) {
					cities.add(city);
				}
			}
		}
		for (Hotel hotel : avalibleHotels) {
			System.out.println(hotel.toString());
		}

		model.addAttribute("planId", planId);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("hotels", avalibleHotels);
		model.addAttribute("id", id);
		model.addAttribute("cities", cities);
		model.addAttribute("state", state);
		return "rentHotel";
	}

	@PostMapping("/rentHotel/{id}")
	public String rentHotel(Model model, @PathVariable("id") long id,
			@RequestParam(name = "startDate") String startDate, @RequestParam(name = "endDate") String endDate,
			@RequestParam(name = "id") String hotelId, @RequestParam(name = "planId") String planId) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate formatedStartDate = LocalDate.parse(startDate, formatter);
		LocalDate formatedEndDate = LocalDate.parse(endDate, formatter);
		List<LocalDate> listOfDates = formatedStartDate.datesUntil(formatedEndDate.plusDays(1)).collect(Collectors.toList());
		HotelRental rental = new HotelRental();
		Hotel hotel = repo.getById(Long.parseLong(hotelId));
		ArrayList<LocalDate> rentalDates = new ArrayList<LocalDate>();
		try {
			rentalDates.addAll(hotel.getDaysRented());
		} catch (Exception e) {
			// TODO
		}

		rentalDates.addAll(listOfDates);
		hotel.setDaysRented(rentalDates);
		repo.save(hotel);
		rental.setRentalStartDate(formatedStartDate);
		rental.setRentalEndDate(formatedEndDate);
		rental.setRentalTotal(hotel.getPricePerDay() * listOfDates.size());
		rental.setHotel(hotel);
		rentalRepo.save(rental);
		Planner plan = plannerRepo.getById(Long.parseLong(planId));
		List<HotelRental> rentals = plan.getHotelRentals();
		rentals.add(rental);
		plan.setHotelRentals(rentals);
		plannerRepo.save(plan);
		System.out.println(rentalDates);
		model.addAttribute("plan", plan);
		return "planner";
	}

	@PostMapping("/findHotelByCity/{id}")
	public String findHotelByCity(Model model, @PathVariable("id") long id,
			@RequestParam(name = "startDate") String startDate, @RequestParam(name = "endDate") String endDate,
			@RequestParam(name = "city") String city, @RequestParam(name = "planId") String planId,
			@RequestParam(name = "state") String state) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate formatedStartDate = LocalDate.parse(startDate, formatter);
		LocalDate formatedEndDate = LocalDate.parse(endDate, formatter);
		List<LocalDate> listOfDates = formatedStartDate.datesUntil(formatedEndDate).collect(Collectors.toList());
		List<Hotel> avalibleHotels = new ArrayList<Hotel>();
		List<String> cities = new ArrayList<String>();
		if (!city.equals("All Cities")) {
			List<Hotel> h = repo.findByAddressCityOrderByName(city);
			for (Hotel hotel : h) {
				try {
					if (hotel.getDaysRented().stream().anyMatch(listOfDates::contains)) {
						avalibleHotels.add(hotel);
					}
				} catch (Exception e) {
					avalibleHotels.add(hotel);
				}
			}
		} else {
			return addHotel(model, id, startDate, endDate, state, planId);
		}
		List<Hotel> stateHotels = repo.findByAddressStateOrderByAddressCity(state);
		for (Hotel hotel : stateHotels) {
			String testCity = hotel.getAddress().getCity();
			if (!cities.contains(testCity)) {
				cities.add(testCity);
			}
		}
		model.addAttribute("planId", planId);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("hotels", avalibleHotels);
		model.addAttribute("id", id);
		model.addAttribute("cities", cities);
		model.addAttribute("state", state);
		return "rentHotel";
	}

	@PostMapping("/cancelHotelRental/{id}")
	public String cancelHotelRental(Model model, @RequestParam(name = "rentalId") String rentalId, @RequestParam(name = "planId") String planId, @PathVariable("id") long id) {
		HotelRental rental = rentalRepo.getById(Long.parseLong(rentalId));
		LocalDate startDate = rental.getRentalStartDate();
		LocalDate endDate = rental.getRentalEndDate();
		List<LocalDate> listOfDates = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());
		Hotel h = rental.getHotel();
		ArrayList<LocalDate> dates = h.getDaysRented();
		for (LocalDate date : listOfDates) {
			dates.remove(date);
		}
		h.setDaysRented(dates);
		repo.save(h);
		Planner plan = plannerRepo.getById(Long.parseLong(planId));
		List<HotelRental> rentals = plan.getHotelRentals();
		rentals.remove(rental);
		plan.setHotelRentals(rentals);
		plannerRepo.save(plan);
		rentalRepo.delete(rental);
		model.addAttribute("id", id);
		model.addAttribute("plan", plan);
		return "planner";
	}
}
