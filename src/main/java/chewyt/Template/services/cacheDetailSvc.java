package chewyt.Template.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
// import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
// import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chewyt.Template.models.*;
import chewyt.Template.repositories.BookDetailsRepository;
import jakarta.json.Json;
// import jakarta.json.JsonArray;
// import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
// import static chewyt.Template.Constants.*;


@Service
public class cacheDetailSvc {
    
    @Autowired
    BookDetailsRepository repo;

    private final Logger logger = Logger.getLogger(cacheDetailSvc.class.getName());
   
    public Optional<BookDetails> getObj(String searchKey) {
        Optional<String> opt  = repo.get(searchKey);
        if (opt.isEmpty()) {
            logger.info(">>>>>>>>>>> Running cacheDetailSvc (JSONObjects)");
            logger.info("Search term \" %s \"is not found in Redis DB".formatted(searchKey));
            return Optional.empty();
        }
        else{
            JsonObject jsonObject = parseJsonObject(opt.get());
            return Optional.of(BookDetails.createfromRedis(jsonObject));
        }
    }

    // private JsonArray parseJsonArray(String searchKey){
    //     try (InputStream is = new ByteArrayInputStream(searchKey.getBytes())) {
    //         JsonReader reader = Json.createReader(is);
    //         return reader.readArray();            
    //     } catch (Exception e) {
    //         //Log errors
    //     }
    //     return Json.createArrayBuilder().build();
    // }
    
    private JsonObject parseJsonObject(String searchKey){
        try (InputStream is = new ByteArrayInputStream(searchKey.getBytes("UTF-8"))) {
            JsonReader reader = Json.createReader(is);
            return reader.readObject();            
        } catch (Exception e) {
            //Log errors
        }
        return Json.createObjectBuilder().build();
    }

    // List Model convert to Json array and stringify to Json string
    public void save(String searchKey, BookDetails book) {
        
        repo.save(searchKey, book.toString());
            
    }

    

}
