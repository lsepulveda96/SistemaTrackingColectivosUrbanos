package com.stcu.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Mapper {
    
    private static ObjectMapper mapper = new ObjectMapper(); 
    
    public static <T> String getResponseAsJson( Response<T> response ) {
        try {
            return mapper.writeValueAsString( response );
        }
        catch(JsonProcessingException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
