package app.albums.giphy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class SlackSlashCommandConverterConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        SlackSlashCommandConverter slackSlashCommandConverter = new SlackSlashCommandConverter();
        SlackButtonCommandConverter slackButtonCommandConverter = new SlackButtonCommandConverter();
        MediaType mediaType = new MediaType("application", "x-www-form-urlencoded", StandardCharsets.UTF_8);
        slackSlashCommandConverter.setSupportedMediaTypes(List.of(mediaType));
        slackButtonCommandConverter.setSupportedMediaTypes(List.of(mediaType));
        converters.add(slackSlashCommandConverter);
        converters.add(slackButtonCommandConverter);
        super.configureMessageConverters(converters);
    }
}