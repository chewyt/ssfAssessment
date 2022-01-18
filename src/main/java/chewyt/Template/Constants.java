package chewyt.Template;

public class Constants {
    
    //API URLS
    public static final String URL_API = "https://api.openweathermap.org/data/2.5/weather";
    public static final String URL_WEATHER = "https://api.openweathermap.org/data/2.5/weather";
    public static final String URL_COUNTRY = "https://flagcdn.com/en/codes.json";
    public static final String REDIS_KEY = "ASSESMENT_API_";


    //Storing in Config VARS of Heroku
    // public static final String ENV_REDISCLOUD = System.getenv("ENV_REDISCLOUD");
    // public static final String ENV_APIKEY_PUBLIC = System.getenv("APIKEY_PUBLIC"); 
    // public static final String ENV_APIKEY_PRIVATE = System.getenv("APIKEY_PRIVATE");
    
    
    
    
    //Storing in ENV path locally: RESTART PC after local ENV changes
    public static final String ENV_REDISCLOUD = System.getenv("REDIS_PW");
    public static final String ENV_APIKEY_PUBLIC = System.getenv("APIKEY_PUBLIC"); 
    public static final String ENV_APIKEY_PRIVATE = System.getenv("APIKEY_PRIVATE");


}
