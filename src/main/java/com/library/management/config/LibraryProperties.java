package com.library.management.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Reads fine amount per day from application.properties.
 */
@Configuration
@ConfigurationProperties(prefix = "library")
@Getter
@Setter
public class LibraryProperties {

    private Fine fine = new Fine();

    @Getter
    @Setter
    public static class Fine {
        private int perDay = 10;
    }
}
