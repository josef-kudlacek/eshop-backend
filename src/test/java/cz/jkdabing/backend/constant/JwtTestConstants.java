package cz.jkdabing.backend.constant;

import cz.jkdabing.backend.constants.ResponseConstants;

public class JwtTestConstants {

    public static final String VALID_JWT_TOKEN =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0VXNlciIsImlhdCI6MTUxNjIzOTAyMn0.DHHj5nHbANZVSkS6x5zD98i86G8sfY3_XYmzps_Ro8Q";
    public static final String INVALID_JWT_TOKEN =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJpbnZhbGlkVXNlciIsImlhdCI6MTUxNjIzOTAyMiwiZXhwIjoxNTE2MjM5MDIyfQ.invalid_signature";

    public static final int EXPIRATION_TIME = 1000 * 60 * 2;

    public static final String COOKIE_ACCESS_TOKEN = ResponseConstants.COOKIE_ACCESS_TOKEN;
}
