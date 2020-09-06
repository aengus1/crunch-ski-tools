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

        for (String s : config.getEnvironments().keySet()) {

            File environmentTerraformRoot = buildTerraformEnvironmentSourceDirectory(sourceDirectory, s, config.getEnvironments().get(s).isCI());
            Context context = mapEnvironmentConfigToThymeleafContext(config, s);

            writeModuleSource("api", s, context, environmentTerraformRoot);
            writeModuleSource("data", s, context, environmentTerraformRoot);
            writeModuleSource("frontend", s, context, environmentTerraformRoot);

            if( config.getEnvironments().get(s).isCI()) {
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
        EnvironmentConfig.Environment environment = config.getEnvironments().get(environmentName);

        context.setVariable("project_name", config.getProject_name());
        context.setVariable("domain_name", environment.getDomain_name());
        context.setVariable("primary_region", config.getPrimary_region());
        context.setVariable("stage", environmentName);
        context.setVariable("secondary_region", config.getSecondary_region());
        context.setVariable("profile", environment.getProfile());
        context.setVariable("user_table_read_capacity", environment.getUser_table_read_capacity());
        context.setVariable("user_table_write_capacity", environment.getUser_table_write_capacity());
        context.setVariable("encrypt_user_table", environment.isEncrypt_user_table());
        context.setVariable("user_table_billing_mode", environment.getUser_table_billing_mode());
        context.setVariable("user_table_point_in_time_recovery", environment.isUser_table_point_in_time_recovery());
        context.setVariable("activity_table_read_capacity", environment.getActivity_table_read_capacity());
        context.setVariable("activity_table_write_capacity", environment.getActivity_table_write_capacity());
        context.setVariable("encrypt_activity_table", environment.isEncrypt_activity_table());
        context.setVariable("activity_table_billing_mode", environment.getActivity_table_billing_mode());
        context.setVariable("activity_table_point_in_time_recovery", environment.isActivity_table_point_in_time_recovery());
        context.setVariable("app_alias", environment.getApp_alias());
        context.setVariable("cognito_sub_domain", environment.getCognito_sub_domain());
        context.setVariable("ws_sub_domain", environment.getWs_sub_domain());
        context.setVariable("api_sub_domain", environment.getApi_sub_domain());
        context.setVariable("infra_branch", environment.getInfraBranch());

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
