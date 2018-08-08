package com.challenge.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;

import java.io.IOException;

public class JsonStringConverter {

    private ObjectMapper mapper;

    /**
     * Json string - DTO converter.
     */
    public JsonStringConverter() {
        this.mapper = new ObjectMapper();
    }

    public JsonStringConverter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Converts object to json string.
     *
     * @param data the object to convert
     * @return [String] the converted json string
     * @throws RuntimeJsonMappingException when conversion fails.
     */
    public String convertDataToJsonString(Object data) throws RuntimeJsonMappingException {
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException ex) {
            throw new RuntimeJsonMappingException("Can not convert object to json string. " + ex.getMessage());
        }
    }

    /**
     * Converts json string to object.
     * Use like: jsonStringConverter.convertJsonStringToData(responseJsonData, MyDto.class);
     *
     * @param jsonString the json string to convert
     * @param valueType the type of the return object
     * @return [T] the converted object
     * @throws RuntimeJsonMappingException when conversion fails.
     */
    public <T> T convertJsonStringToData(String jsonString, Class<T> valueType) throws RuntimeJsonMappingException {
        try {
            return mapper.readValue(jsonString, valueType);
        } catch (IOException ex) {
            throw new RuntimeJsonMappingException("IO error. Can not convert json string to object. " + ex.getMessage());
        }
    }

    /**
     * Converts json string to object collections.
     * Use like: jsonStringConverter.convertJsonStringToData(responseJsonData, new TypeReference<Map<String,MyDto>>(){});
     *
     * @param jsonString the json string to convert
     * @param valueTypeRef the type reference of the collection.
     * @return [T] the converted object
     * @throws RuntimeJsonMappingException when conversion fails.
     */
    public <T> T convertJsonStringToData(String jsonString, TypeReference valueTypeRef) throws RuntimeJsonMappingException {
        try {
            return mapper.readValue(jsonString, valueTypeRef);
        } catch (IOException ex) {
            throw new RuntimeJsonMappingException("IO error. Can not convert json string to object. " + ex.getMessage());
        }
    }
}
