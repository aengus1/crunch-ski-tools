package ski.crunch.tools.commands;

import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@Component
@CommandLine.Command(
        name = "crunch",
        description = "Administrative CLI tool for provisioning and data migration in Crunch.Ski backend",
        version="1.0.1",
        mixinStandardHelpOptions = true,
        subcommands = {
//                BackupCommand.class,
//                RestoreCommand.class,
                ConfigCommand.class,
//                WipeCommand.class,
//                StatusCommand.class,
//                UpCommand.class,
//                DownCommand.class,
//                SettingsCommand.class
        }
)
public class MainCommand implements Callable<Integer> {

    @CommandLine.Option(names = {"-p", "--profile"},
            paramLabel = "PROFILE",
            description = "AWS credentials profile to use"
    )
    private String awsProfile;


    @CommandLine.Option(names = {"-n", "--project-name"},
            paramLabel = "PROJECT_NAME",
            description = "name of project instance"
    )
    private String projectName;

    @CommandLine.Option(names = {"-r", "--data-region"},
            paramLabel = "DATA_REGION",
            description = "aws region of project's data stack"
    )
    private String dataRegion;

    @CommandLine.Option(names = {"-d", "--source-dir"},
            paramLabel = "PROJECT_SOURCE_DIR",
            description = "path to source directory")
    private String projectSourceDir;


    @CommandLine.Option(names = {"-v", "--verbose"})
    private boolean verbose;

    @CommandLine.Option(names = {"--stack-trace"})
    private boolean stackTrace;

    @Override
    public Integer call() throws Exception {
        System.out.print("Please choose one of the subcommands: backup, restore, wipe, configure, status, up, down, settings");
        return 0;
    }

    public String getAwsProfile() {
        return awsProfile;
    }
    public String getProjectName() {
        return projectName;
    }

    public String getProjectSourceDir() {
        return this.projectSourceDir;
    }
    public String getDataRegion() {
        return dataRegion;
    }
    void setAwsProfile(String profile) {
        this.awsProfile = profile;
    }
    void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    void setDataRegion(String dataRegion) {
        this.dataRegion = dataRegion;
    }
    public boolean isVerbose() {
        return verbose;
    }
    public boolean isStackTrace() {
        return stackTrace;
    }
}
