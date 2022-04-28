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
		model.addAttribute("users", repo.findAll());
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
			repo.save(a);
			Account l = new Account();
			model.addAttribute("newAccount", l);
			return "index";
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
			model.addAttribute("id", l.getId());
			if (l.getAccountType().equals("user")) {
				model.addAttribute("plans", pRepo.findByAccountId(l.getId()));
				return "home";
			} else {
				model.addAttribute("plans", pRepo.findAll());
				model.addAttribute("users", repo.findByAccountType("user"));
				model.addAttribute("admins", repo.findByAccountType("admin"));
				return "adminHome";
			}
		} else {
			model.addAttribute("newAccount", a);
			model.addAttribute("error", "Incorrect Email Or Password!");
			return "index";
		}
	}

	@GetMapping("/accountSettings/{id}")
	public String accountSettings(Model model, @PathVariable("id") long id) {
		Account a = repo.getById(id);
		model.addAttribute("newAccount", a);
		model.addAttribute("accountType", a.getAccountType());
		return "editAccount";
	}

	@PostMapping("/accountSettings/{id}")
	public String accountSettings(Account a, Model model, @PathVariable("id") long id) {
		repo.save(a);
		if (a.getAccountType().equals("admin")) {
			model.addAttribute("plans", pRepo.findByAccountId(id));
			model.addAttribute("users", repo.findAll());
			return "adminHome";
		} else {
			model.addAttribute("plans", pRepo.findByAccountId(a.getId()));
			return "home";
		}
	}
}
