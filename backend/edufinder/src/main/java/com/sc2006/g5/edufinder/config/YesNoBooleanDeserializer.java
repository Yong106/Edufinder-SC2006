package com.sc2006.g5.edufinder.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Custom {@link JsonDeserializer} to parse "Yes/No" into {@code Boolean}.
 * <p>
 * If the text is "Yes", return {@code true}, otherwise, return {@code false}
 */
public class YesNoBooleanDeserializer extends JsonDeserializer<Boolean> {
    @Override
    public Boolean deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        String text = parser.getText();
        if (text == null) return false;
        return text.equalsIgnoreCase("Yes");
    }
}