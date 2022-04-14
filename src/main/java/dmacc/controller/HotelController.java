package dmacc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import dmacc.beans.Hotel;
import dmacc.repository.HotelRepository;

@Controller
public class HotelController {
	@Autowired
	HotelRepository repo;

	@GetMapping("/inputHotel")
	public String addNewHotel(Model model) {
		Hotel h = new Hotel();
		model.addAttribute("newHotel", h);
		return "hotel";
	}

	@PostMapping("/inputHotel")
	public String addNewHotel(@ModelAttribute Hotel h, Model model) {
		repo.save(h);
		System.out.println("Hotel Created!");
		//System.out.println(h.toString());
		return viewAllHotels(model);
	}
	
	@GetMapping("/viewAllHotels")
	public String viewAllHotels(Model model) {
		if (repo.findAll().isEmpty()) {
			return addNewHotel(model);
		}
		model.addAttribute("hotels", repo.findAll());
		return "viewAllHotels";
	}
}
