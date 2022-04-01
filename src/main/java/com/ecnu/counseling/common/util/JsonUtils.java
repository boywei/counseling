package com.ecnu.counseling.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public class JsonUtils {

    public static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * 解析json 成 map结构
     *
     * @param json json
     * @param <K>  key
     * @param <V>  value
     * @return Map<K, V>
     */
    public static <K, V> Optional<Map<K, V>> decodeJsonToMap(String json) {
        if (StringUtils.isEmpty(json)) {
            return Optional.empty();
        }
        try {
            Map<K, V> obj = mapper.readValue(json, new TypeReference<Map<K, V>>() {
            });
            return Optional.of(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * 解析json 成 clazz
     *
     * @param json  json
     * @param clazz class
     * @param <T>   具体类型
     * @return Class<T>
     */
    public static <T> Optional<T> readValue(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json) || clazz == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(mapper.readValue(json, clazz));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
