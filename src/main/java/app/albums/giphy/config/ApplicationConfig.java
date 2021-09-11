package app.albums.giphy.config;

import app.albums.giphy.config.util.JsonUtility;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@EnableWebMvc
@Configuration
public class ApplicationConfig implements WebMvcConfigurer {

    private static final String BOT_TOKEN = System.getenv("SLACK_BOT_TOKEN");

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter(JsonUtility.getObjectMapper()));
    }

    @Bean
    public MethodsClient slack() {
        return Slack.getInstance().methods(BOT_TOKEN);
    }

}
