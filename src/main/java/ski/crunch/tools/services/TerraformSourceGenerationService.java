package ski.crunch.tools.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ski.crunch.environments.ThymeleafConfig;
import ski.crunch.tools.model.EnvironmentConfig;

import java.io.File;

@Service
public class TerraformSourceGenerationService {

    private final ThymeleafConfig thymeleafConfig;
    private final TemplateEngine templateEngine;


    public TerraformSourceGenerationService(ApplicationContext applicationContext) {
        this.thymeleafConfig = new ThymeleafConfig();
        thymeleafConfig.setApplicationContext(applicationContext);
        this.templateEngine = thymeleafConfig.templateEngine();
    }

    public void generateTerraformSource(EnvironmentConfig config, File sourceDirectory) throws Exception {

        for (String s : config.getEnvironments().keySet()) {
            EnvironmentConfig.Environment environment = config.getEnvironments().get(s);
            Context context = new Context();
            context.setVariable("project_name", config.getProject_name());
            context.setVariable("domain_name", environment.getDomain_name());
            context.setVariable("primary_region", config.getPrimary_region());
            context.setVariable("stage", s);
            context.setVariable("secondary_region", config.getSecondary_region());
            context.setVariable("profile", environment.getProfile());

        }
    }
}
