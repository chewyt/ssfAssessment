package chewyt.Template.models;

import java.io.Serializable;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class ObjModel implements Serializable {
   
    private String name;
    private String imgUrl;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImgUrl() {
        return imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return this.toJson().toString();
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("name", name)
                .add("imgUrl", imgUrl)
                .build();
    }

    public static ObjModel create(JsonObject o) {
        ObjModel om = new ObjModel();
        om.setName(o.getString("name"));
        om.setImgUrl(o.getJsonObject("thumbnail").getString("path"));
        return om;
    }
    
    public static ObjModel createfromRedis(JsonObject o) {
        ObjModel om = new ObjModel();
        om.setName(o.getString("name"));
        om.setImgUrl(o.getString("imgUrl"));
        return om;
    }

    public static ObjModel create(String jsonString) {
        try (InputStream is = new ByteArrayInputStream(jsonString.getBytes())) {
            final JsonReader reader = Json.createReader(is);
            return create(reader.readObject());

        } catch (Exception e) {
            return new ObjModel();
        }
    }

}