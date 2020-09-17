package ski.crunch.tools.services;

import com.maximeroussy.invitrode.WordGenerator;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;
import ski.crunch.tools.io.EnvConfigLoader;
import ski.crunch.tools.model.AddEnvironmentOptions;
import ski.crunch.tools.model.EnvironmentConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Service
public class AddEnvironmentService {

    private File repoDirectory;
    private File terraformDirectory;

    private final GitService gitService;
    private final TerraformSourceGenerationService sourceGenerationService;
    private final TerraformProvisioningService provisioningService;

    public AddEnvironmentService(GitService gitService, TerraformSourceGenerationService sourceGenerationService, TerraformProvisioningService provisioningService) throws IOException {

        this.gitService = gitService;
        this.sourceGenerationService = sourceGenerationService;
        this.provisioningService = provisioningService;

        this.repoDirectory = Files.createTempDirectory("envrepo").toFile();
        this.terraformDirectory = Files.createTempDirectory("terraform").toFile();
    }

    public void addEnvironment(AddEnvironmentOptions options) throws IOException, GitAPIException {

        WordGenerator generator = new WordGenerator();
        String envName = generator.newWord(8).toLowerCase();
        System.out.println("Env Name = " + envName);

        // clone the environment repository to a temp directory
        gitService.cloneRepo(options.getRepositoryUrl(), repoDirectory.getAbsolutePath());

        // parse the config file
        File sourceDir = new File(repoDirectory, "src");
        File configFile = new File(sourceDir, "envs.yml");
        EnvConfigLoader envConfigLoader = new EnvConfigLoader();
        EnvironmentConfig config = envConfigLoader.load(configFile);

        // generate the terraform source
        Map<String, String> envStageMap = sourceGenerationService.generateTerraformSource( config, sourceDir);

        boolean provisionSuccess = provisioningService.provisionEnv(config, options, envName, sourceDir);






    }


}
