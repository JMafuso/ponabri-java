package com.fiap.ponabri.security;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final String EMAILS_URI = "https://api.github.com/user/emails";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        if ("github".equals(userRequest.getClientRegistration().getRegistrationId())) {
            String accessToken = userRequest.getAccessToken().getTokenValue();

            RestTemplate restTemplate = new RestTemplate();
            // Fetch emails from GitHub API with Authorization header
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);

            org.springframework.http.ResponseEntity<List> response = restTemplate.exchange(
                    EMAILS_URI,
                    org.springframework.http.HttpMethod.GET,
                    entity,
                    List.class);

            List<Map<String, Object>> emails = response.getBody();

            if (emails != null) {
                Optional<String> primaryEmail = emails.stream()
                        .filter(email -> Boolean.TRUE.equals(email.get("primary")))
                        .map(email -> (String) email.get("email"))
                        .findFirst();

                if (primaryEmail.isPresent()) {
                    Map<String, Object> attributes = new HashMap<>(oauth2User.getAttributes());
                    attributes.put("email", primaryEmail.get());

                    return new DefaultOAuth2User(
                            oauth2User.getAuthorities(),
                            attributes,
                            userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName());
                }
            }
        }

        return oauth2User;
    }
}
