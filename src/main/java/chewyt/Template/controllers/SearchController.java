package chewyt.Template.controllers;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
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

import chewyt.Template.models.Book;
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

        String title = form.getFirst("title");
        logger.info("Query search: %s".formatted(title));

        if (title.equals("")) {
            model.addAttribute("error", "Empty field. Please try again.");
            return "index";
        }

        // Check if resource is available in Redis cache
        // Initialize optional object

        Optional<List<Book>> opt = cacheService.getArray(title);
        List<Book> searches = Collections.emptyList();

        if (opt.isPresent()) {
            logger.info("Cache hit for %s".formatted(title));
            // Loading from cache service
            searches = opt.get();
        } else {
            try {
                
                logger.info(">>>>>Running try catch for mainService");
                searches = service.search(title);
                logger.info("Is Searches List empty after main Service: %s".formatted(searches.isEmpty()));
                if (searches.size() > 0) {
                    // Saving to cache
                    cacheService.save(title, searches);
                }
            } catch (Exception e) {
                logger.warning("Warning: %s".formatted(e.getMessage()));
                model.addAttribute("error", "Search term found no results. Please try again.");
                return "results";
            }
        }

        // String cityNameforDisplay = weather.get(0).getCityNameInSentenceCase();
        // String countryNameforDisplay = weather.get(0).getCountryName();
        // String countryCodeforDisplay = weather.get(0).getCountryCode();

        logger.log(Level.INFO, "Data: %s".formatted(searches));
        model.addAttribute("query", "Search result for '%s'".formatted(title));
        
        
        if (searches.size()<1) {  
            model.addAttribute("error", "Search term found no results. Please try again.");
        }        
        if (searches.size()==20) {  
            model.addAttribute("more", "<More results, not shown>");
        }        
        model.addAttribute("searches", searches);
        // searches= searches.subList(0, 20); //Standard index search
        // model.addAttribute("countryCode", countryCodeforDisplay);
        // model.addAttribute("data", weather);
        return "results";
    }

}
