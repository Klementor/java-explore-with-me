package ru.practicum.ewm.hit.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;

@Configuration
public class DateTimeFormatConfig implements WebMvcConfigurer {
    private final String date;
    private final String dateTime;

    public DateTimeFormatConfig(
            @Value("${default-date-format}") String date,
            @Value("${default-date-time-format}") String dateTime
    ) {
        this.date = date;
        this.dateTime = dateTime;
    }

    @Override
    public void addFormatters(@NonNull FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateFormatter(DateTimeFormatter.ofPattern(date));
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern(dateTime));
        registrar.registerFormatters(registry);
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.simpleDateFormat(dateTime);
            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(date)));
            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTime)));
            builder.deserializers(new LocalDateDeserializer(DateTimeFormatter.ofPattern(date)));
            builder.deserializers(new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateTime)));
        };
    }
}

