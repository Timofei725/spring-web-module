package ru.edu.springweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InfoController {

    @GetMapping("/author")
    public String getAuthorFullName(Model model) {
        model.addAttribute("author", "Киселев Тимофей");
        return "author";
    }

    @GetMapping("/hobby")
    public String getProfilePage(Model model) {
        model.addAttribute("hobby", "Программирование, инвестиции, спорт");
        return "hobby";
    }
}