package ski.crunch.tools.io;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.yaml.snakeyaml.Yaml;
import ski.crunch.tools.model.BackupConfig;

import java.util.Map;

public class BackupConfigLoader {


    /**
     * Loads the backup-config.yml file from resources folder
     *
     * @param templateEngine   Thymeleaf Template Engine
     * @param thymeleafContext Thymeleaf Context
     * @return BackupConfig model
     */
    public BackupConfig load(TemplateEngine templateEngine, Context thymeleafContext) {
        Yaml yaml = new Yaml();
        String mappedConfig = templateEngine.process("backup-config", thymeleafContext);
        Map<String, Object> configYaml = yaml.load(mappedConfig);
        return readBackupConfig(configYaml);

    }

    private BackupConfig readBackupConfig(Map<String, Object> configYml) {
        BackupConfig config = new BackupConfig();
        Map<String, Object> root = (Map<String, Object>) configYml.get("sources");


        Map<String, String> s3Map = (Map<String, String>) root.get("s3");
        config.setS3Buckets(s3Map);

        Map<String, String> dynamodbMap = (Map<String, String>) root.get("dynamodb");
        config.setDynamoDbTables(dynamodbMap);

        Map<String, String> ssmMap = (Map<String, String>) root.get("ssm");
        config.setSsmParameters(ssmMap);

        return config;
    }
}
