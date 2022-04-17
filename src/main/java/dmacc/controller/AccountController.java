package dmacc.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import dmacc.beans.Account;
import dmacc.repository.AccountRepository;
import dmacc.repository.PlannerRepository;

@Controller
public class AccountController {
	@Autowired
	AccountRepository repo;
	@Autowired
	PlannerRepository pRepo;
	@GetMapping("/userHome/{id}")
	public String userHome(Model model, @PathVariable("id") long id) {
		model.addAttribute("id", id);
		model.addAttribute("plans", pRepo.findByAccountId(id));
		return "home";
	}

	@GetMapping("/register")
	public String addNewAccount(Model model) {
		Account a = new Account();
		model.addAttribute("newAccount", a);
		return "register";
	}

	@PostMapping("/register")
	public String addNewAccount(@ModelAttribute Account a, Model model) {
		try {
			a.setAccountType("user");
			if (!a.getPassword().equals(a.getConfirmPassword())) {
				model.addAttribute("newAccount", a);
				model.addAttribute("error", "Passwords Must match!");
				return "register";
			} else {
				repo.save(a);
				Account l = new Account();
				model.addAttribute("newAccount", l);
				return "index";
			}
		} catch (Exception e) {
			model.addAttribute("newAccount", a);
			model.addAttribute("error", "Email is already registered");
			return "register";
		}
	}

	@GetMapping("/")
	public String addLoginAccount(Model model) {
		Account a = new Account();
		model.addAttribute("newAccount", a);
		return "index";
	}

	@PostMapping("/login")
	public String loginAccount(@ModelAttribute Account a, Model model) {
		Account l = repo.findOneByEmail(a.getEmail());
		if (!Objects.isNull(l) && l.getPassword().equals(a.getPassword())) {
			System.out.println("Login Successfull");
			model.addAttribute("id", l.getId());
			if (l.getAccountType().equals("user")) {
				model.addAttribute("plans", pRepo.findByAccountId(l.getId()));
				return "home"; 
			} else {
				model.addAttribute("plans", pRepo.findAll());
				return "adminHome";
			}

		} else {
			model.addAttribute("newAccount", a);
			model.addAttribute("error", "Incorrect Email Or Password!");
			return "index";
		}
	}
}
