package ski.crunch.tools.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BackupOptions {


    private boolean isVerbose;
    private String stage;
    private String envName;
    private String projectName;
}
