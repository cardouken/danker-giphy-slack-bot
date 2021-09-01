package app.albums.giphy.config;

import app.albums.giphy.controller.api.SlackButtonEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.util.Map;

public class SlackButtonCommandConverter extends AbstractHttpMessageConverter<SlackButtonEvent> {

    private static final FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected boolean supports(Class<?> clazz) {
        return (SlackButtonEvent.class == clazz);
    }

    @Override
    protected SlackButtonEvent readInternal(Class<? extends SlackButtonEvent> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        Map<String, String> vals = formHttpMessageConverter.read(null, inputMessage).toSingleValueMap();

        return mapper.convertValue(vals, SlackButtonEvent.class);
    }

    @Override
    protected void writeInternal(SlackButtonEvent slackButtonCommand, HttpOutputMessage outputMessage) throws HttpMessageNotWritableException {

    }
}