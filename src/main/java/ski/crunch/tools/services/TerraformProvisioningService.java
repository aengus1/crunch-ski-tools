package ski.crunch.tools.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ski.crunch.tools.model.AddEnvironmentOptions;
import ski.crunch.tools.model.EnvironmentConfig;
import ski.crunch.utils.FileUtils;
import ski.crunch.utils.process.ProcessRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

@Slf4j
@Service
public class TerraformProvisioningService {


    private final ProcessRunner processRunner;
    private final String initializeEnvPath;
    private final String destroyEnvPath;

    public TerraformProvisioningService() {
        processRunner = new ProcessRunner();

        initializeEnvPath = System.getProperty("user.dir")+"/build/scripts/init_environment.sh";
        destroyEnvPath = System.getProperty("user.dir")+"/build/scripts/destroy_environment.sh";
    }



    public boolean provisionEnv(EnvironmentConfig config, AddEnvironmentOptions options, String envName, File sourceDir) throws IOException {

        boolean alreadyProvisioned = false;

        // is this stage already provisioned?  If so.. take a data backup
        if ( config.getEnvironmentState().containsKey(options.getStage()) ) {
            alreadyProvisioned = true;
            //todo -> backup the existing environment for this stage
        }

        //provision
        String[] cmdArray = new String[] {initializeEnvPath, envName};
        int tfProvision = setPermissionsAndRun(cmdArray, sourceDir);

        //exit on tf failure
        if (tfProvision != 0) {
            log.error("Environment " + envName + " terraform provisioning failed");
            return false;
        }
        File tfLog = new File(sourceDir, "terraform.log");
        String logStr = FileUtils.readFileToString(tfLog);
        log.info(logStr);

        // 4. load data
        //5. update the env repo

        return true;
    }


    public boolean deProvisionEnv(EnvironmentConfig config, AddEnvironmentOptions options, String envName, File sourceDir) throws IOException {
        String[] cmdArray = new String[] {destroyEnvPath, envName};
        int tfProvision = setPermissionsAndRun(cmdArray, sourceDir);

        //exit on tf failure
        if (tfProvision != 0) {
            log.error("Environment " + envName + " terraform de-provisioning failed");
            return false;
        }
        File tfLog = new File(sourceDir, "terraform_destroy.log");
        String logStr = FileUtils.readFileToString(tfLog);
        log.info(logStr);
        return true;
    }


    private int setPermissionsAndRun(@org.jetbrains.annotations.NotNull String[] cmdArray, File sourceDir) throws IOException{
        Files.setPosixFilePermissions(
                Path.of(cmdArray[0]),
                Set.of(
                        PosixFilePermission.OWNER_READ,
                        PosixFilePermission.OTHERS_READ,
                        PosixFilePermission.OTHERS_EXECUTE,
                        PosixFilePermission.OWNER_EXECUTE
                )
        );


        int tfProvision = processRunner.startProcess(cmdArray, sourceDir, true);
        log.info("Provision: ", processRunner.getInputStream());
        log.error("Provision: ", processRunner.getErrorStream());

        return tfProvision;
    }


}
