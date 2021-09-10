package app.albums.giphy.config;

import app.albums.giphy.enums.EventType;
import org.springframework.core.convert.converter.Converter;

public class StringToEventTypeConverter implements Converter<String, EventType> {

    @Override
    public EventType convert(String source) {
        return EventType.valueOf(source.toUpperCase());
    }
}