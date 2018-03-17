package br.com.lucas.baseapp.controller;

import br.com.lucas.baseapp.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PageController {

    @Autowired
    FileService fileService;

    /**
     * @return index page
     */
    @GetMapping({"files","/"})
    public String index(Model model) {
        model.addAttribute("files", fileService.getAll());
        return "files";
    }

    /**
     * @return solr page
     */
    @GetMapping({"solrsearch"})
    public String solrsearch() {
        return "solr";
    }
}
