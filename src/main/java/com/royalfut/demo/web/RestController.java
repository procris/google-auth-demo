package com.royalfut.demo.web;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    public static final String CLIENT_ID = "754137399246-qabm6770ckoo0puec4pibpbsh79pvi44.apps.googleusercontent.com";
    public static final String CLIENT_SECRET = "bsNTqgIBZ--F5EOfpuXee0k0";
    public static final String REDIRECT_URI = "https://google-auth-royalfut.herokuapp.com/";

    @PostMapping(path = "/storeauthcode")
    public Result storeAuthCode(HttpServletRequest request, @RequestBody String authCode) throws IOException {
        System.out.println("Got requested authCode " + authCode);
        if (request.getHeader("X-Requested-With") == null) {
            // Without the `X-Requested-With` header, this request could be forged. Aborts.
        }

        GoogleTokenResponse tokenResponse =
                new GoogleAuthorizationCodeTokenRequest(
                        new NetHttpTransport(),
                        JacksonFactory.getDefaultInstance(),
                        "https://oauth2.googleapis.com/token",
                        CLIENT_ID,
                        CLIENT_SECRET,
                        authCode,
                        REDIRECT_URI)
                        .execute();

        String accessToken = tokenResponse.getAccessToken();

        System.out.println("Got access token " + accessToken);

        GoogleIdToken idToken = tokenResponse.parseIdToken();
        GoogleIdToken.Payload payload = idToken.getPayload();
        String userId = payload.getSubject();
        String email = payload.getEmail();
        boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
        String name = (String) payload.get("name");
        String pictureUrl = (String) payload.get("picture");
        String locale = (String) payload.get("locale");
        String familyName = (String) payload.get("family_name");
        String givenName = (String) payload.get("given_name");

        System.out.println("Got profile email " + email + " and name " + name + " and givenName " + givenName);

        Result result = new Result(email, name);
        return result;
    }

    public class Result {
        public String email;
        public String name;

        public Result(String email, String name) {
            this.email = email;
            this.name = name;
        }
    }
}
