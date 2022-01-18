package chewyt.Template.models;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import jakarta.json.Json;
// import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class BookDetails implements Serializable {

    private String key;
    private String title;
    private String description;
    private List<String> excerpts;
    private boolean cache;
    private int coverNum;

    public int getCoverNum() {
        return coverNum;
    }

    public void setCoverNum(int coverNum) {
        this.coverNum = coverNum;
    }

    public BookDetails(String key, String title, String description, List<String> excerpts, int coverNum) {
        this.key = key;
        this.title = title;
        this.description = description;
        this.excerpts = excerpts;
        this.coverNum = coverNum;
        cache = false;
    }

    public BookDetails() {
        cache = false;
    }

    

    public BookDetails(String key, String title, String description, int coverNum) {
        this.key = key;
        this.title = title;
        this.description = description;
        this.coverNum = coverNum;
        excerpts=Collections.emptyList();
        cache=false;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getExcerpts() {
        return excerpts;
    }

    public void setExcerpts(List<String> excerpts) {
        this.excerpts = excerpts;
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return this.toJson().toString();
    }

    public JsonObject toJson() {

        JsonArrayBuilder array = Json.createArrayBuilder(); 
            excerpts.forEach(array::add);

        return Json.createObjectBuilder()
                .add("key", key)
                .add("title", title)
                .add("description", description)
                .add("coverNum", coverNum)
                .add("excerpts", array)
                .build();
    }

    public static String convertExcerpt(JsonObject o) {

        return o.getString("excerpt");
    }

    public static BookDetails create(JsonObject o) {
        BookDetails b = new BookDetails();
        b.setKey(o.getString("key"));
        b.setTitle(o.getString("title"));
        return b;
    }

    public static BookDetails createfromRedis(JsonObject o) {
        BookDetails b = new BookDetails();
        b.setKey(o.getString("key"));
        b.setTitle(o.getString("title"));
        b.setDescription(o.getString("description"));
        b.setCoverNum(o.getJsonNumber("coverNum").intValue());
        b.setExcerpts(o.getJsonArray("excerpts").stream()
                        .map(v->v.toString())
                        .collect(Collectors.toList())
        );
        b.setCache(true);
        return b;
    }

    public static BookDetails create(String jsonString) {
        try (InputStream is = new ByteArrayInputStream(jsonString.getBytes())) {
            final JsonReader reader = Json.createReader(is);
            return create(reader.readObject());

        } catch (Exception e) {
            return new BookDetails();
        }
    }

    

}
