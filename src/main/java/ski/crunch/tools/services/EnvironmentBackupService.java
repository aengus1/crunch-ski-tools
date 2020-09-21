package ski.crunch.tools.services;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ski.crunch.tools.ThymeleafConfig;
import ski.crunch.tools.io.BackupConfigLoader;
import ski.crunch.tools.model.BackupConfig;
import ski.crunch.tools.model.BackupOptions;

import java.io.IOException;

@Service
@Slf4j
public class EnvironmentBackupService {

    private final TemplateEngine backupConfigEngine;

    @Getter
    private final ThymeleafConfig thymeleafConfig;


    public EnvironmentBackupService(ApplicationContext applicationContext) {
            thymeleafConfig = new ThymeleafConfig();
            thymeleafConfig.setApplicationContext(applicationContext);
            this.backupConfigEngine = thymeleafConfig.backupConfigTemplateEngine();
    }

    public void backupEnvironment(BackupOptions options) throws IOException {

        Context context = buildThymeleafContext(options);
        BackupConfigLoader loader = new BackupConfigLoader();
        BackupConfig config = loader.load(backupConfigEngine, context);




    }

    private Context buildThymeleafContext(BackupOptions options) {
            Context context = new Context();
            context.setVariable("env", options.getEnvName());
            context.setVariable("projectName", options.getProjectName());
            context.setVariable("stage", options.getStage());
            return context;
    }
}
