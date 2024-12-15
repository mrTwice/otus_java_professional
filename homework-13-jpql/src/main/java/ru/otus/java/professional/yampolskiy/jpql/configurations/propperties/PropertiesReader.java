package ru.otus.java.professional.yampolskiy.jpql.configurations.propperties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;


import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;

public class PropertiesReader {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final Properties properties = new Properties();
    private final Map<Class<? extends PropertyKey>, Map<? extends PropertyKey, String>> propertyCache = new EnumMap<>(Class.class);

    public PropertiesReader(String propertiesFilePath) throws IOException {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(propertiesFilePath)) {
            if (input == null) {
                throw new IllegalArgumentException("Файл : " + propertiesFilePath + "не найден");
            }
            properties.load(input);
        } catch (IOException e) {
            logger.error("Ошибка загрузки файла настроек.", e);
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Enum<T> & PropertyKey> String getProperty(T propertyKey) {
        propertyCache.computeIfAbsent(propertyKey.getDeclaringClass(), key -> loadPropertiesForEnum((Class<T>) key));
        return ((Map<T, String>) propertyCache.get(propertyKey.getDeclaringClass())).get(propertyKey);
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

