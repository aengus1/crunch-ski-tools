package ski.crunch.tools.io;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ski.crunch.tools.ThymeleafConfig;
import ski.crunch.tools.model.BackupConfig;
import ski.crunch.tools.services.EnvironmentBackupService;
import ski.crunch.tools.services.TerraformProvisioningService;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BackupConfigLoaderTest {

    private final BackupConfigLoader backupConfigLoader = new BackupConfigLoader();

    @Autowired
    private EnvironmentBackupService environmentBackupService;

    @Test
    public void testLoadConfig() throws IOException {

        Context context = new Context();
        context.setVariable("env", "mytestenv");
        context.setVariable("projectName", "crunch-ski");
        context.setVariable("stage", "prod");

        ThymeleafConfig thymeleafConfig = environmentBackupService.getThymeleafConfig();
        BackupConfig config = backupConfigLoader.load(thymeleafConfig.backupConfigTemplateEngine(), context);

        assertTrue(config.getS3Buckets().containsKey("activity"));
        assertEquals("mytestenv-activity-crunch-ski", config.getS3Buckets().get("activity"));

        assertTrue(config.getS3Buckets().containsKey("rawActivity"));
        assertEquals("mytestenv-raw-activity-crunch-ski", config.getS3Buckets().get("rawActivity"));

        assertTrue(config.getDynamoDbTables().containsKey("user"));
        assertEquals("mytestenv-crunch-ski-userTable", config.getDynamoDbTables().get("user"));

        assertTrue(config.getDynamoDbTables().containsKey("activity"));
        assertEquals("mytestenv-crunch-ski-Activity", config.getDynamoDbTables().get("activity"));

        assertTrue(config.getSsmParameters().containsKey("weather"));
        assertEquals("crunch-ski-weather-api-key", config.getSsmParameters().get("weather"));

        assertTrue(config.getSsmParameters().containsKey("location"));
        assertEquals("crunch-ski-location-api-key", config.getSsmParameters().get("location"));

        assertTrue(config.getSsmParameters().containsKey("rockset"));
        assertEquals("crunch-ski-rockset-api-key", config.getSsmParameters().get("rockset"));


    }
}