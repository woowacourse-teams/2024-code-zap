package codezap.global;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.Cookie;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import codezap.global.cors.CorsProperties;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableConfigurationProperties(CorsProperties.class)
public class IntegrationTest {

    protected MockMvc mvc;
    protected Cookies cookies;

    private static class Cookies {
        List<Cookie> cookies = new ArrayList<>();

        private void addAll(Cookie... newCookies) {
            Arrays.stream(newCookies)
                    .filter(cookies::contains)
                    .forEach(cookies::add);
        }

        private Cookie[] getCookies() {
            return cookies.toArray(Cookie[]::new);
        }

        private void setCookie(CookieValue cookieValue) {
            if (!containsNameOf(cookieValue.name)) {
                cookies.add(cookieValue.toCookie());
                return;
            }

            Cookie existsCookie = cookies.stream()
                    .filter(cookie -> cookie.getName().equals(cookieValue.name))
                    .findFirst()
                    .get();
            cookieValue.apply(existsCookie);
        }

        private boolean containsNameOf(String name) {
            return cookies.stream().anyMatch(cookie -> cookie.getName().equals(name));
        }

        public boolean isEmpty() {
            return cookies.isEmpty();
        }
    }

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
        cookies = new Cookies();
    }

    protected ResultActions request(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        requestBuilder = requestBuilder
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        if (!cookies.isEmpty()) {
            requestBuilder = requestBuilder.cookie(cookies.getCookies());
        }

        ResultActions perform = mvc.perform(requestBuilder);

        MockHttpServletResponse response = perform.andReturn().getResponse();
        cookies.addAll(response.getCookies());

        if (response.containsHeader(HttpHeaders.SET_COOKIE)) {
            CookieValue cookieValue = new CookieValue(response.getHeader(HttpHeaders.SET_COOKIE));
            cookies.setCookie(cookieValue);
        }
        return perform;
    }

    private static class CookieValue {
        private static final List<String> NO_VALUE_ATTRIBUTES = List.of("Secure", "HttpOnly");
        private final String name;
        private final String value;
        private final int maxAge;

        public CookieValue(String header) {
            String[] split = header.split(";");
            String[] nameAndValue = split[0].split("=", 2);

            Map<String, Object> attributes = new HashMap<>();
            for (int i = 1; i < split.length; i++) {
                String token = split[i].strip();
                if (NO_VALUE_ATTRIBUTES.contains(token)) {
                    attributes.put(token, Boolean.TRUE);
                    continue;
                }
                String[] attributesValue = token.split("=");
                attributes.put(attributesValue[0], attributesValue[1]);
            }

            this.name = nameAndValue[0];
            this.value = nameAndValue[1];
            this.maxAge = getExpires(attributes);
        }

        private int getExpires(Map<String, Object> attributes) {
            if (attributes.containsKey("Max-Age")) {
                String maxAge = (String) attributes.get("Max-Age");
                return Integer.parseInt(maxAge);
            }
            return -1;
        }

        public void apply(Cookie cookie) {
            cookie.setMaxAge(maxAge);
        }

        public Cookie toCookie() {
            Cookie cookie = new Cookie(this.name, this.value);
            cookie.setMaxAge(this.maxAge);
            return cookie;
        }
    }
}
