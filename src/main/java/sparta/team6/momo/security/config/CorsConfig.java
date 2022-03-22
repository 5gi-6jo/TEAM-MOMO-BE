package sparta.team6.momo.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
//        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader("Authorization");
        config.addExposedHeader("Set-Cookie");

        source.registerCorsConfiguration("/users/**", config);
        source.registerCorsConfiguration("/**", config);
        source.registerCorsConfiguration("/kakao/**", config);
        source.registerCorsConfiguration("/plans/**", config);
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }

}
