package dmacc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dmacc.beans.Hotel;
import dmacc.repository.HotelRepository;

@Controller
public class HotelController {
	@Autowired
	HotelRepository repo;

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
		System.out.println("Hotel Created!");
		//System.out.println(h.toString());
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
	public String findHotelByState(Model model, @RequestParam(name="state") String state, @PathVariable("id") long id) {
		List<Hotel> h = repo.findByHotelAddressState(state);
		model.addAttribute("hotels", h);
		model.addAttribute("id", id);
		return "viewAllHotels";
	}
}
