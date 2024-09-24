package com.nca.config;

import com.nca.config.initializer.DataInitializer;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * {@code StartupApplicationListener} intended to provide necessary logic after application initialization.
 */

@Component
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * {@code onApplicationEvent} method provides logic to run data initialization.
     * For more info about data initialization go to {@link DataInitializer}
     */
    @SneakyThrows
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        DataInitializer dataInitializer = (DataInitializer)
                contextRefreshedEvent.getApplicationContext().getBean("dataInitializer");
        dataInitializer.initialize();
    }
}
