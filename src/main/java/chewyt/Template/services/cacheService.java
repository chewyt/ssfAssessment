package chewyt.Template.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chewyt.Template.models.Book;
import chewyt.Template.repositories.BookRepository;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
// import static chewyt.Template.Constants.*;


@Service
public class cacheService {
    
    @Autowired
    BookRepository repo;

    private final Logger logger = Logger.getLogger(cacheService.class.getName());

    public Optional<List<Book>> getArray(String searchKey) {

        Optional<String> opt  = repo.get(searchKey);
        if (opt.isEmpty()) {
            logger.info(">>>>>>>>>>> Running cacheService (JSONArray)");
            logger.info("Search term \" %s \"is not found in Redis DB".formatted(searchKey));
            return Optional.empty();
        }
        else{
            JsonArray jsonarray  = parseJsonArray(opt.get());
            List<Book> thatList  = jsonarray.stream()
            .map(v->(JsonObject)v)     //cast as a stream of Json Objects
            .map(Book::createfromRedis) //cast as a stream of Weather objects
            .collect(Collectors.toList()); //collect as a Collection List of Weather Objects
            return Optional.of(thatList);
        }
    }
    
    public Optional<Book> getObj(String searchKey) {
        Optional<String> opt  = repo.get(searchKey);
        if (opt.isEmpty()) {
            logger.info(">>>>>>>>>>> Running cacheService (JSONObjects)");
            logger.info("Search term \" %s \"is not found in Redis DB".formatted(searchKey));
            return Optional.empty();
        }
        else{
            JsonObject jsonObject = parseJsonObject(opt.get());
            return Optional.of(Book.create(jsonObject));
        }
    }

    private JsonArray parseJsonArray(String searchKey){
        try (InputStream is = new ByteArrayInputStream(searchKey.getBytes())) {
            JsonReader reader = Json.createReader(is);
            return reader.readArray();            
        } catch (Exception e) {
            //Log errors
        }
        return Json.createArrayBuilder().build();
    }
    
    private JsonObject parseJsonObject(String searchKey){
        try (InputStream is = new ByteArrayInputStream(searchKey.getBytes())) {
            JsonReader reader = Json.createReader(is);
            return reader.readObject();            
        } catch (Exception e) {
            //Log errors
        }
        return Json.createObjectBuilder().build();
    }

    // List Model convert to Json array and stringify to Json string
    public void save(String searchKey, List<Book> listy) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        listy.stream()
            .forEach(v->arrayBuilder.add(v.toJson()));
        repo.save(searchKey, arrayBuilder.build().toString());
            
    }

    

}
