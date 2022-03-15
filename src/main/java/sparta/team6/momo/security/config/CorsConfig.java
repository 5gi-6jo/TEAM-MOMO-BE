package sparta.team6.momo.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
<<<<<<< HEAD
        config.addAllowedOrigin("*");
=======
        config.addAllowedOriginPattern("*");
        config.addAllowedOrigin("http://localhost:3000");
>>>>>>> c21917a62e5965167092f0c6a9ed03478b376c0d
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/users/**", config);
        source.registerCorsConfiguration("/plans/**", config);
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }

}
