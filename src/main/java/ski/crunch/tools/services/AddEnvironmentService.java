package ski.crunch.tools.services;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;
import ski.crunch.tools.io.EnvConfigLoader;
import ski.crunch.tools.model.AddEnvironmentOptions;
import ski.crunch.tools.model.EnvironmentConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class AddEnvironmentService {

    private File repoDirectory;
    private File terraformDirectory;

    private final GitService gitService;

    public AddEnvironmentService(GitService gitService) throws IOException {

        this.gitService = gitService;
        this.repoDirectory = Files.createTempDirectory("envrepo").toFile();
        this.terraformDirectory = Files.createTempDirectory("terraform").toFile();
    }

    public void addEnvironment(AddEnvironmentOptions options) throws IOException, GitAPIException {


        // clone the environment repository to a temp directory
        gitService.cloneRepo(options.getRepositoryUrl(), repoDirectory.getAbsolutePath());

        // parse the config file
        File sourceDir = new File(repoDirectory, "src");
        File configFile = new File(sourceDir, "envs.yml");
        EnvConfigLoader envConfigLoader = new EnvConfigLoader();
        EnvironmentConfig config = envConfigLoader.load(configFile);

        // generate the terraform source




    }


}
