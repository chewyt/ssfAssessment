package chewyt.Template.controllers;

import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestMapping;

import chewyt.Template.models.BookDetails;
import chewyt.Template.services.BookService;
import chewyt.Template.services.*;



@Controller
public class BookController {
    
    
    private final Logger logger = Logger.getLogger(BookController.class.getName());

    @Autowired
    BookService service;

    @Autowired
    cacheDetailSvc cacheDetailSvc;

    @GetMapping(path="/works/{id}")
    public String getBook(@PathVariable String id, Model model){
        
        Optional<BookDetails> opt = cacheDetailSvc.getObj(id);
        BookDetails book;

        if (opt.isPresent()) {
            logger.info("Cache hit for %s".formatted(id));
            // Loading from cache service
            book = opt.get();
            logger.info(book.toString());
        } else {
            try {
                
                logger.info(">>>>>Running try catch for mainService");
                book = service.load(id);
                logger.info("Is Book details empty: %s".formatted(book.getKey().equals("")));
                if (!book.getKey().equals("")) {
                    // Saving to cache
                    cacheDetailSvc.save(id, book);
                }
            } catch (Exception e) {
                logger.warning("Warning: %s".formatted(e.getMessage()));
                model.addAttribute("error", "No resource found for this work ID.");
                return "book";
            }
        }

        model.addAttribute("book", book);
        model.addAttribute("img", book.getCoverNum());
        model.addAttribute("excerpts", book.getExcerpts());
        
        if(book.getExcerpts().size()==0)
            model.addAttribute("no_excerpt", "<No excerpt found>");
        return "book";
    }
}
