package chewyt.Template.controllers.rest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// import chewyt.Template.*;
import chewyt.Template.models.ObjModel;
import chewyt.Template.services.*;
// import static chewyt.Template.Constants.*;


import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

@RestController
@RequestMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestyController {

    @Autowired
    mainService service;

    @Autowired
    cacheService cacheService;

    private final Logger logger = Logger.getLogger(RestyController.class.getName());
    
    @GetMapping(path = "/{variable}")
    public ResponseEntity<String> getmodelListPath(@PathVariable String variable){
        
        logger.info(">>>>>>>>Use Get Mapping Path variable");
        logger.info("Variable: %s".formatted(variable));

        // >>>>>>>>>>>>Comment/ Uncomment either one
        Optional<List<ObjModel>> opt = cacheService.getArray(variable);
        // Optional<ObjModel> optObj = cacheService.getObj(variable);
        // >>>>>>>>>>>>Comment/ Uncomment either one

        List<ObjModel> modelList = Collections.emptyList();
        
        if (opt.isPresent()) {
            logger.info("Cache hit for %s".formatted(variable));
            modelList=opt.get();
        }
        else{
            try {
                logger.info(">>>>>>>>Try catch for using mainService");
                
                modelList=service.getModelList(variable);
                logger.info("Is modelList List empty: %s".formatted(modelList.isEmpty()));
                if (modelList.size()>0){
                    cacheService.save(variable, modelList);
                }
            } catch (Exception e) {
                logger.warning("Warning: %s".formatted(e.getMessage()));
                JsonObject object = Json.createObjectBuilder()
                    .add("error", e.getMessage()).build();
                return ResponseEntity.internalServerError().body(object.toString());
                
            } 
        }
        JsonArrayBuilder arrBuildr = Json.createArrayBuilder();
         modelList.stream()
                .forEach(v->arrBuildr.add(v.toJson()));
        
        return ResponseEntity.ok(arrBuildr.build().toString());
                
    }
}
