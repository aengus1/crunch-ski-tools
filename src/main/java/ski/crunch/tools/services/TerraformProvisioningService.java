package ski.crunch.tools.services;

import org.springframework.stereotype.Service;
import ski.crunch.tools.model.AddEnvironmentOptions;
import ski.crunch.tools.model.EnvironmentConfig;
import ski.crunch.utils.StreamUtils;
import ski.crunch.utils.process.ProcessRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

@Service
public class TerraformProvisioningService {


    private final ProcessRunner processRunner;
    public final String initializeEnvPath;


    public TerraformProvisioningService() {
        processRunner = new ProcessRunner();
        System.out.println(System.getProperty("user.dir")+"/build/scripts/init_environment.sh");
        initializeEnvPath = System.getProperty("user.dir")+"/build/scripts/init_environment.sh";
    }

    public boolean provision(EnvironmentConfig config, AddEnvironmentOptions options, String envName, File sourceDir) throws IOException {

        boolean alreadyProvisioned = false;

        // is this stage already provisioned?  If so.. take a data backup
        if ( config.getEnvironmentState().containsKey(options.getStage()) ) {
            alreadyProvisioned = true;
            //todo -> backup the existing environment for this stage
        }

        //provision
        String[] cmdArray = new String[] {initializeEnvPath, envName};

        Files.setPosixFilePermissions(
                Path.of(initializeEnvPath),
                Set.of(
                            PosixFilePermission.OWNER_READ,
                            PosixFilePermission.OTHERS_READ,
                            PosixFilePermission.OTHERS_EXECUTE,
                            PosixFilePermission.OWNER_EXECUTE
                    )
            );


        int tfProvision = processRunner.startProcess(cmdArray, sourceDir, true);
        System.out.println(StreamUtils.convertStreamToString(processRunner.getInputStream()));
        System.err.println(StreamUtils.convertStreamToString(processRunner.getErrorStream()));

        //exit on tf failure
        if (tfProvision != 0) {
            System.err.println("Environment " + envName + " terraform provisioning failed");
            return false;
        }


        //2. create a new admin module for the soon to be created environment
        // 3. switch to this stage's directory in the terraformSource dir and provision
        // 4. load data
        //5. update the env repo

        return true;
    }
}
