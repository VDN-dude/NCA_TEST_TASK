package com.nca.config.properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.Map;

/**
 * {@code AppProperties} intended to initialize properties from .yaml files.
 */

@Data
@Component
@AllArgsConstructor
public class AppProperties {

    protected String profile;
    private Datasource datasource;
    private Hibernate hibernate;

    public AppProperties() {

        //        HERE IS LOADING PROFILE WHICH SHOULD BE USED

        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("config/application.yaml");
        Map<String, Object> properties = yaml.load(inputStream);
        this.profile = (String) properties.get("profile");

        //        HERE IS LOADING CONFIGURATION PROPERTIES WHICH DEPENDED ON PROFILE

        yaml = new Yaml(new Constructor(new LoaderOptions()));
        inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("config/application-" + profile + ".yaml");

        //        HERE IS PROCESS OF INITIALIZATION PROPERTIES TO NESTED CLASSES

        Map<String, Object> load = yaml.load(inputStream);
        ObjectMapper objectMapper = new ObjectMapper();
        this.datasource = objectMapper.convertValue(load.get("datasource"), Datasource.class);
        this.hibernate = objectMapper.convertValue(load.get("hibernate"), Hibernate.class);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Datasource {

        private String url;
        private String driver;
        private String username;
        private String password;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Hibernate {

        private String hbm2ddlAuto;
        private String dialect;
        private String showSql;
        private String defaultSchema;
    }
}
