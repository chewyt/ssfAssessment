package chewyt.Template.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import chewyt.Template.models.ObjModel;
import chewyt.Template.services.*;
// import static chewyt.Template.Constants.*;

@Controller
@RequestMapping(path = "/search")
public class SearchController {

    private final Logger logger = Logger.getLogger(SearchController.class.getName());

    @Autowired
    BookService service;

    @Autowired
    cacheService cacheService;

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String getSomething(@RequestBody MultiValueMap<String, String> form, Model model) {

        String variable = form.getFirst("variable");
        logger.info("Variable: %s".formatted(variable));

        if (variable.equals("")) {
            model.addAttribute("error", "Empty field. Please try again.");
            return "index";
        }

        // Check if resource is available in Redis cache
        // Initialize optional object

        Optional<List<ObjModel>> opt = cacheService.getArray(variable);
        List<ObjModel> thatList = Collections.emptyList();

        if (opt.isPresent()) {
            logger.info("Cache hit for %s".formatted(variable));
            // Loading from cache service
            thatList = opt.get();
        } else {
            try {
                
                logger.info(">>>>>Running try catch for mainService");
                thatList = service.getModelList(variable);
                logger.info("Is Weather List empty after main Service: %s".formatted(thatList.isEmpty()));
                if (thatList.size() > 0) {
                    // Saving to cache
                    cacheService.save(variable, thatList);
                }
            } catch (Exception e) {
                logger.warning("Warning: %s".formatted(e.getMessage()));
                model.addAttribute("error", "Search term found no results. Please try again.");
                return "index";
            }
        }

        // String cityNameforDisplay = weather.get(0).getCityNameInSentenceCase();
        // String countryNameforDisplay = weather.get(0).getCountryName();
        // String countryCodeforDisplay = weather.get(0).getCountryCode();

        logger.log(Level.INFO, "Data: %s".formatted(thatList));
        // model.addAttribute("city", cityNameforDisplay);
        // model.addAttribute("country", countryNameforDisplay);
        // model.addAttribute("countryCode", countryCodeforDisplay);
        // model.addAttribute("data", weather);
        return "results";
    }

}
