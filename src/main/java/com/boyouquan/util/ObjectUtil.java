package com.boyouquan.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

public class ObjectUtil {

    private static final Logger logger = LoggerFactory.getLogger(ObjectUtil.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T jsonToObject(String json, Class<T> objectType) {
        try {
            return mapper.readValue(json, objectType);
        } catch (JacksonException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T jsonToList(String json, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(json, typeReference);
        } catch (JacksonException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}