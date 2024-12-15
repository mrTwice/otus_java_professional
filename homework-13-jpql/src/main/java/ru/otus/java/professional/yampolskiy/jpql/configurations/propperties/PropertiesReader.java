package ru.otus.java.professional.yampolskiy.jpql.configurations.propperties;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Properties;


import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;

@Getter
public class PropertiesReader {
    private final Logger LOGGER = LogManager.getLogger(PropertiesReader.class);
    private final String PROPERTIES_FILE_PATH = "application.properties";
    private final Properties properties = new Properties();
    private final Map<Class<? extends Enum<?>>, Map<? extends PropertyKey, String>> propertyCache = new HashMap<>();

    public PropertiesReader() throws IOException {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_PATH)) {
            if (input == null) {
                throw new IllegalArgumentException("Файл " + PROPERTIES_FILE_PATH + "не найден");
            }
            properties.load(input);
        } catch (IOException e) {
            LOGGER.error("Ошибка загрузки файла настроек.", e);
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Enum<T> & PropertyKey> String getProperty(T propertyKey) {
        propertyCache.computeIfAbsent(propertyKey.getDeclaringClass(), key -> loadPropertiesForEnum((Class<T>) key));
        return propertyCache.get(propertyKey.getDeclaringClass()).get(propertyKey);
    }



    private <T extends Enum<T> & PropertyKey> Map<T, String> loadPropertiesForEnum(Class<T> enumClass) {
        Map<T, String> enumPropertyMap = new EnumMap<>(enumClass);
        for (T enumConstant : enumClass.getEnumConstants()) {
            String value = properties.getProperty(enumConstant.getKey());
            if (value != null) {
                enumPropertyMap.put(enumConstant, value);
            }
        }
        return enumPropertyMap;
    }



}

