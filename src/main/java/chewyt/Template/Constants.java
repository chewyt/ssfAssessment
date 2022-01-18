package chewyt.Template;

public class Constants {
    
    //API URLS
    public static final String  URL_API_SEARCH = "http://openlibrary.org/search.json";
    public static final String URL_API_WORKS = "https://openlibrary.org/works/";
    public static final String REDIS_KEY = "ASSESMENT_API_";


    //Storing in Config VARS of Heroku
    // public static final String ENV_REDISCLOUD = System.getenv("ENV_REDISCLOUD");
    // public static final String ENV_APIKEY_PUBLIC = System.getenv("APIKEY_PUBLIC"); 
    // public static final String ENV_APIKEY_PRIVATE = System.getenv("APIKEY_PRIVATE");
    
    
    
    
    //Storing in ENV path locally: RESTART PC after local ENV changes
    public static final String ENV_REDISCLOUD = System.getenv("REDIS_PW");
    


}
