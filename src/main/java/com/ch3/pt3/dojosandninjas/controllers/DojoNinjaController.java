package com.ch3.pt3.dojosandninjas.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ch3.pt3.dojosandninjas.models.Dojo;
import com.ch3.pt3.dojosandninjas.models.Ninja;
import com.ch3.pt3.dojosandninjas.services.DojoService;
import com.ch3.pt3.dojosandninjas.services.NinjaService;

@Controller
public class DojoNinjaController {
	
	private final DojoService dojoService;
	private final NinjaService ninjaService;
	
	public DojoNinjaController (DojoService dojoService, NinjaService ninjaService) {
		this.dojoService = dojoService;
		this.ninjaService = ninjaService;
	}
	
	@GetMapping("/dojos/new")
	public String newDojo(@ModelAttribute("dojo") Dojo dojo) {
		return "newDojo.jsp";
	}
	
	@PostMapping("/dojos/new")
	public String createDojo(@Valid @ModelAttribute("dojo") Dojo dojo, BindingResult result) {
		if(result.hasErrors()) {
			return "newDojo.jsp";
		}
		dojoService.createDojo(dojo);
		return "redirect:/ninjas/new";
	}
	
	@GetMapping("/ninjas/new")
	public String newNinja(@ModelAttribute("ninja") Ninja ninja, Model model) {
		List<Dojo> dojos = dojoService.allDojos();
		model.addAttribute("dojos", dojos);
		return "newNinja.jsp";
	}
	
	@PostMapping("/ninjas/new")
	public String createNinja(@Valid @ModelAttribute("ninja") Ninja ninja, BindingResult result, RedirectAttributes redirectAttrs) {
		if (result.hasErrors()) {
			return "/ninjas/new";
		}
		ninjaService.createNinja(ninja);
		Long id = ninja.getDojo().getId();
//		System.out.println(id);
//		dojo.addAttribute("id", id);
//		System.out.println(dojo.getAttribute("id"));
		
		redirectAttrs.addAttribute("id", id).addFlashAttribute("message", "Ninja created!");
		
		return "redirect:/dojos/{id}";
	}
	
	@GetMapping("/dojos/{id}")
	public String dojoDetail(@PathVariable("id") Long id, Model model) {
		Dojo dojo = dojoService.getDojo(id);
		model.addAttribute("dojo", dojo);
		return "dojoDetail.jsp";
	}
	
}
