package chewyt.Template.models;

import java.io.Serializable;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Book implements Serializable {
   
    private String key;
    private String title;

    
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
        return Json.createObjectBuilder()
                .add("key", key)
                .add("title", title)
                .build();
    }

    public static Book create(JsonObject o) {
        Book om = new Book();
        om.setKey(o.getString("key"));
        om.setTitle(o.getString("title"));
        return om;
    }
    
    public static Book createfromRedis(JsonObject o) {
        Book om = new Book();
        om.setKey(o.getString("key"));
        om.setTitle(o.getString("title"));
        return om;
    }

    public static Book create(String jsonString) {
        try (InputStream is = new ByteArrayInputStream(jsonString.getBytes())) {
            final JsonReader reader = Json.createReader(is);
            return create(reader.readObject());

        } catch (Exception e) {
            return new Book();
        }
    }

}
