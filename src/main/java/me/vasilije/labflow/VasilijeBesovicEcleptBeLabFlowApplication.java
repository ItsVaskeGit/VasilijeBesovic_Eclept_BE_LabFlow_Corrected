package me.vasilije.labflow;

import me.vasilije.labflow.config.RSAKeyConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RSAKeyConfiguration.class)
public class VasilijeBesovicEcleptBeLabFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(VasilijeBesovicEcleptBeLabFlowApplication.class, args);
    }

}
