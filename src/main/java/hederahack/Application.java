package hederahack;
import java.util.Collections;

import org.rendersnake.ext.spring.RenderableViewResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "8080"));
        app.run(args);
    }

    /*
     * RenderSnake view resolver, allow Spring to instantiate
     * RenderSnake page.
     */
    @Bean
    public RenderableViewResolver viewResolver() {
        final RenderableViewResolver viewResolver = new RenderableViewResolver();
        return viewResolver;
    }
}