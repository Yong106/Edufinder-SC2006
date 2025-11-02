package com.sc2006.g5.edufinder.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class YesNoBooleanDeserializer extends JsonDeserializer<Boolean> {
    @Override
    public Boolean deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        String text = parser.getText();
        if (text == null) return false;
        return text.equalsIgnoreCase("Yes");
    }
}