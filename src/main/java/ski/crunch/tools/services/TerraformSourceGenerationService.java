package ski.crunch.tools.services;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ski.crunch.tools.ThymeleafConfig;
import ski.crunch.tools.model.EnvironmentConfig;
import ski.crunch.utils.FileUtils;

import java.io.File;
import java.io.IOException;

@Service
public class TerraformSourceGenerationService {

    private final ThymeleafConfig thymeleafConfig;
    private final TemplateEngine tfTemplateEngine;
    private final TemplateEngine jsonTemplateEngine;


    public TerraformSourceGenerationService(ApplicationContext applicationContext) {
        this.thymeleafConfig = new ThymeleafConfig();
        thymeleafConfig.setApplicationContext(applicationContext);
        this.tfTemplateEngine = thymeleafConfig.terraformTemplateEngine();
        this.jsonTemplateEngine = thymeleafConfig.jsonTemplateEngine();
    }

    public void generateTerraformSource(EnvironmentConfig config, File sourceDirectory) throws IOException {

        if (!sourceDirectory.exists() && !sourceDirectory.mkdir()) {
            throw new IOException("Failed to create directory: " + sourceDirectory.getAbsolutePath());
        }

        for (String s : config.getEnvironmentDefs().keySet()) {

            File environmentTerraformRoot = buildTerraformEnvironmentSourceDirectory(sourceDirectory, s, config.getEnvironmentDefs().get(s).isCI());
            Context context = mapEnvironmentConfigToThymeleafContext(config, s);

            writeModuleSource("api", s, context, environmentTerraformRoot);
            writeModuleSource("data", s, context, environmentTerraformRoot);
            writeModuleSource("frontend", s, context, environmentTerraformRoot);

            if( config.getEnvironmentDefs().get(s).isCI()) {
                writeModuleSource("webclient-cd", s, context, environmentTerraformRoot);
            }
        }
    }

    private void writeModuleSource(String moduleName, String envName, Context context, File envTfRoot) throws  IOException {

        String moduleTf = tfTemplateEngine.process(moduleName.concat("/").concat(moduleName).concat(".env"), context);
        String moduleJson = jsonTemplateEngine.process(moduleName.concat("/terraform.tfvars"), context);
        File moduleDir = new File(envTfRoot, moduleName);
        FileUtils.writeStringToFile(moduleTf, new File(moduleDir, envName.concat(".").concat(moduleName).concat(".env.tf")));
        FileUtils.writeStringToFile(moduleJson, new File(moduleDir, envName.concat(".terraform.tfvars.json")));
    }

    private Context mapEnvironmentConfigToThymeleafContext(EnvironmentConfig config, String environmentName) {
        Context context = new Context();
        EnvironmentConfig.EnvironmentDefinition environmentDefinition = config.getEnvironmentDefs().get(environmentName);

        context.setVariable("project_name", config.getProject_name());
        context.setVariable("domain_name", environmentDefinition.getDomain_name());
        context.setVariable("primary_region", config.getPrimary_region());
        context.setVariable("stage", environmentName);
        context.setVariable("secondary_region", config.getSecondary_region());
        context.setVariable("profile", environmentDefinition.getProfile());
        context.setVariable("user_table_read_capacity", environmentDefinition.getUser_table_read_capacity());
        context.setVariable("user_table_write_capacity", environmentDefinition.getUser_table_write_capacity());
        context.setVariable("encrypt_user_table", environmentDefinition.isEncrypt_user_table());
        context.setVariable("user_table_billing_mode", environmentDefinition.getUser_table_billing_mode());
        context.setVariable("user_table_point_in_time_recovery", environmentDefinition.isUser_table_point_in_time_recovery());
        context.setVariable("activity_table_read_capacity", environmentDefinition.getActivity_table_read_capacity());
        context.setVariable("activity_table_write_capacity", environmentDefinition.getActivity_table_write_capacity());
        context.setVariable("encrypt_activity_table", environmentDefinition.isEncrypt_activity_table());
        context.setVariable("activity_table_billing_mode", environmentDefinition.getActivity_table_billing_mode());
        context.setVariable("activity_table_point_in_time_recovery", environmentDefinition.isActivity_table_point_in_time_recovery());
        context.setVariable("app_alias", environmentDefinition.getApp_alias());
        context.setVariable("cognito_sub_domain", environmentDefinition.getCognito_sub_domain());
        context.setVariable("ws_sub_domain", environmentDefinition.getWs_sub_domain());
        context.setVariable("api_sub_domain", environmentDefinition.getApi_sub_domain());
        context.setVariable("infra_branch", environmentDefinition.getInfraBranch());

        return context;
    }

    private File buildTerraformEnvironmentSourceDirectory(File sourceDirectory, String environmentName, boolean isCi) throws IOException {
        File root = makeModuleDir(sourceDirectory, environmentName);
        makeModuleDir(root, "api");
        makeModuleDir(root, "data");
        makeModuleDir(root, "frontend");
        if (isCi) {
            makeModuleDir(root, "webclient-cd");
        }

        return root;
    }

    private File makeModuleDir(File root, String module) throws IOException {
        File moduleDir = new File(root, module);
        if (!moduleDir.mkdir()) {
            throw new IOException("Failed to create directory " + moduleDir.getAbsolutePath());
        }
        return moduleDir;
    }
}
