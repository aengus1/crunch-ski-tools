package ski.crunch.tools.services;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import ski.crunch.tools.model.AddEnvironmentOptions;
import ski.crunch.tools.model.EnvironmentConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AddEnvironmentService {

    private File repoDirectory;

    private final GitService gitService;

    public AddEnvironmentService(GitService gitService) throws IOException {

        this.gitService = gitService;
        this.repoDirectory = Files.createTempDirectory("envrepo").toFile();
    }

    public void addEnvironment(AddEnvironmentOptions options) throws IOException, GitAPIException {

        // clone the environment repository to a temp directory
        gitService.cloneRepo(options.getRepositoryUrl(), repoDirectory.getAbsolutePath());

        // parse the config file
        EnvironmentConfig config = parseConfigFromRepo();
        System.out.println(config.getPrimary_region());


    }


    public EnvironmentConfig parseConfigFromRepo() throws IOException {
        File sourceDir = new File(repoDirectory, "src");
        File config = new File(sourceDir, "envs.yml");
        Yaml yaml = new Yaml();
        EnvironmentConfig envConfig = null;
        try (FileInputStream fis = new FileInputStream(config) ) {
            Map<String, Object> configYaml = yaml.load(fis);
            envConfig =  readEnvironmentConfig(configYaml);
        }
        return envConfig;
    }

    public static EnvironmentConfig readEnvironmentConfig(Map<String, Object> conf) {
        EnvironmentConfig result = new EnvironmentConfig();
        Map<String, Object> domain = (Map<String, Object>) conf.get("domain");
        result.setPrimary_region((String) domain.get("primary_region"));
        result.setSecondary_region((String) domain.get("secondary_region"));
        result.setProject_name(((String) domain.get("project_name")));

        List<Map<String, Object>> environments = (ArrayList<Map<String, Object>>) conf.get("environments");
        result.setEnvironments(new HashMap<>());
        for (Map<String, Object> environment : environments) {
            for (String s : environment.keySet()) {
                EnvironmentConfig.Environment env = new EnvironmentConfig.Environment();
                Map<String, Object> properties = (Map<String, Object>) environment.get(s);
                env.setDefault_branch((String) properties.get("default_branch"));
                env.setCI((boolean) properties.get("ci"));
                env.setDomain_name((String) properties.get("domain_name"));
                Map<String, String> version = (Map<String, String>) properties.get("version");
                env.setServicesVersion(version.get("services"));
                env.setInfraVersion(version.get("infra"));
                Map<String, Object> tfProps = (Map<String, Object>) properties.get("properties");
                env.setInfraBranch((String) tfProps.get("infra_branch"));
                env.setUser_table_read_capacity((int) tfProps.get("user_table_read_capacity"));
                env.setUser_table_write_capacity((int) tfProps.get("user_table_write_capacity"));
                env.setEncrypt_user_table((boolean) tfProps.get("encrypt_user_table"));
                env.setUser_table_billing_mode((String) tfProps.get("user_table_billing_mode"));
                env.setUser_table_point_in_time_recovery((boolean) tfProps.get("user_table_point_in_time_recovery"));
                env.setActivity_table_read_capacity((int) tfProps.get("activity_table_read_capacity"));
                env.setActivity_table_write_capacity((int) tfProps.get("activity_table_write_capacity"));
                env.setEncrypt_activity_table((boolean) tfProps.get("encrypt_activity_table"));
                env.setActivity_table_billing_mode((String) tfProps.get("activity_table_billing_mode"));
                env.setActivity_table_point_in_time_recovery((boolean) tfProps.get("activity_table_point_in_time_recovery"));
                result.getEnvironments().put(s, env);
            }

        }


        return result;
    }
}
