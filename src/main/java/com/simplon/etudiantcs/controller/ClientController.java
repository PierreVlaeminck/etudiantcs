package com.simplon.etudiantcs.controller;

import com.simplon.etudiantcs.entity.Etudiant;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class ClientController {
    private RestTemplate restTemplate;

    @GetMapping("/")
    public String index(Model model) {
        this.restTemplate = new RestTemplate();
        String url =  "http://localhost:8080/rest/etudiants";
        ResponseEntity<List<Etudiant>> response =
                restTemplate.exchange(url, HttpMethod.GET, null, new
                        ParameterizedTypeReference<List<Etudiant>>() {
                        });
        List<Etudiant> etudiants = response.getBody();

        model.addAttribute("etudiants", etudiants);
        return "index";
    }

    @GetMapping("/etudiants/{id}")
    public String getEtudiant(Model model, @PathVariable long id){
        this.restTemplate = new RestTemplate();
        String url = "http://localhost:8080/rest/etudiants/{id}";
        ResponseEntity<Etudiant> response = restTemplate.getForEntity(url, Etudiant.class, id);
        Etudiant etudiant = response.getBody();

        model.addAttribute("etudiants", etudiant);
        return "etudiant";
    }

    @GetMapping("/etudiants/form/add")
    public String formEtudiant(Model model) {
        Etudiant etudiant = new Etudiant();
        model.addAttribute("etudiant", etudiant);
        return "form";
    }

    @GetMapping("/etudiants/maj/{id}")
    public String majEtudiant(Model model, @PathVariable long id){
        this.restTemplate = new RestTemplate();
        String url = "http://localhost:8080/rest/etudiants/{id}";
        ResponseEntity<Etudiant> response = restTemplate.getForEntity(url, Etudiant.class, id);
        Etudiant etudiant = response.getBody();

        model.addAttribute("etudiant", etudiant);
        return "form";
    }

    @GetMapping("etudiants/del/{id}")
    public String delEtudiant(Model model, @PathVariable long id){
        this.restTemplate = new RestTemplate();

        String url = "http://localhost:8080/rest/etudiants/{id}";
        restTemplate.delete(url, id);

        return "redirect:/";
    }

    @PostMapping("/etudiants/maj/{id}")
    public String updateEtudiant(@ModelAttribute("etudiant") Etudiant etudiant, @PathVariable long id){

        String url = "http://localhost:8080/rest/etudiants/{id}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Etudiant> request = new HttpEntity<>(etudiant, headers);
        ResponseEntity<Etudiant> response = restTemplate.exchange(url, HttpMethod.PUT, request, Etudiant.class, id);

        return "redirect:/";
    }

    @PostMapping("/etudiants/form/add")
    public String addEtudiant(@ModelAttribute("etudiant") Etudiant etudiant){
        this.restTemplate = new RestTemplate();

        String url = "http://localhost:8080/rest/etudiants";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Etudiant> request = new HttpEntity<>(etudiant, headers);
        ResponseEntity<Etudiant> response = restTemplate.postForEntity(url, request, Etudiant.class);

        return "redirect:/";
    }
}
