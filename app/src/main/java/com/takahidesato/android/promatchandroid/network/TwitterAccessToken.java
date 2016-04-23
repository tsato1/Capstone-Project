package com.takahidesato.android.promatchandroid.network;

/**
 * Created by tsato on 4/22/16.
 */
public class TwitterAccessToken {
    private String access_token;
    private String token_type;

    public String getAccessToken() {
        return access_token;
    }

    public String getTokentype() {
        // OAuth requires uppercase Authorization HTTP header value for token type
        if (!Character.isUpperCase(token_type.charAt(0))) {
            token_type = Character.toString(token_type.charAt(0)).toUpperCase() + token_type.substring(1);
        }

        return token_type;
    }
}
