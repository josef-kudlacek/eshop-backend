package cz.jkdabing.backend.constants;

public class ApiPathConstants {

    @SuppressWarnings("java:S1075")
    public static final String API_PATH = "/api";
    @SuppressWarnings("java:S1075")
    public static final String PRODUCT_PATH = "/products";

    public static final String ADMIN = API_PATH + "/admin";
    public static final String CUSTOMERS = API_PATH + "/customers";
    public static final String PRODUCTS = API_PATH + PRODUCT_PATH;
    public static final String USERS = API_PATH + "/users";

    public static final String ADMIN_PATH = ADMIN + "/**";
    public static final String AUDIO_PATHS = PRODUCT_PATH + "/audio/**";
    public static final String IMAGES_PATHS = PRODUCT_PATH + "/images/**";
    public static final String USERS_PATHS = USERS + "/**";

    private ApiPathConstants() {
    }
}
