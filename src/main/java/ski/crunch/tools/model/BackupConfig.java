package ski.crunch.tools.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class BackupConfig {

    private Map<String, String> s3Buckets;
    private Map<String, String> dynamoDbTables;
    private Map<String, String> ssmParameters;

    public BackupConfig() {
        this.s3Buckets = new HashMap<>();
        this.dynamoDbTables = new HashMap<>();
        this.ssmParameters = new HashMap<>();
    }
}
