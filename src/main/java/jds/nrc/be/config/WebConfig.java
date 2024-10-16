package jds.nrc.be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    private final static String[] ALLOWED_ORIGINS = {
            "http://localhost:3000",
    };

    @Bean
    public WebMvcConfigurer corsConfigure() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // You can customize your CORS configuration here
                registry.addMapping("/api/v1/**") // Apply CORS to all endpoints
                        .allowedOrigins(ALLOWED_ORIGINS)
                        .allowCredentials(true)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*"); // Allow all headers
            }
        };
    }
}
