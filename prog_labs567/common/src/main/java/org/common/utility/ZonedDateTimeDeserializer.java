package org.common.utility;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Кастомный Десерилиазатор для типа ZonedDateTime
 */
public class ZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> implements Serializable {
    @Override
    public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDate localDate = LocalDate.parse(p.getText(), dateTimeFormatter);
        return localDate.atStartOfDay(ZoneOffset.UTC);
        //return localDate.atStartOfDay().atZone(ZoneId.of("UTC"));
    }
}