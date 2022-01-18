package chewyt.Template.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import chewyt.Template.models.*;
import static chewyt.Template.Constants.*;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class BookService {
    //API Service

    Logger logger = Logger.getLogger(BookService.class.getName());

    public List<Book> search(String variable) {

        logger.info(">>>>>>>>>Linking external API");

        String url = UriComponentsBuilder
                .fromUriString(URL_API)
                .queryParam("nameStartsWith", variable.replace(" ", "+"))
                .queryParam("ts", "1")  //optional
                .queryParam("apikey", ENV_APIKEY_PRIVATE)  //check all query keys and value pairs
                .queryParam("hash", ENV_APIKEY_PUBLIC)
                .toUriString();

        final RequestEntity<Void> req = RequestEntity.get(url).build();
        final RestTemplate template = new RestTemplate();
        final ResponseEntity<String> resp = template.exchange(req, String.class);

        logger.log(Level.INFO, resp.getStatusCode().toString());
        logger.log(Level.INFO, resp.getHeaders().toString());
        // logger.log(Level.INFO, resp.getBody().toString());

        try (InputStream is = new ByteArrayInputStream(resp.getBody().getBytes())) {
            JsonReader reader = Json.createReader(is);
            JsonObject result = reader.readObject();
            final JsonArray results = result.getJsonObject("data").getJsonArray("results");
            return results.stream()
                    .map(v -> (JsonObject) v)
                    .map(Book::create)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.severe("GG: %s".formatted(e.getMessage()));
        }

        return null;
    }
    
}
