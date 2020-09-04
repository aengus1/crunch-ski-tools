package ski.crunch.tools.commands;

import org.springframework.stereotype.Component;
import picocli.CommandLine;
import ski.crunch.tools.ToolsConfigLoader;

import java.io.File;
import java.util.concurrent.Callable;

@Component
@CommandLine.Command(
        name = "config",
        description = "Configure CLI",
        subcommands = {
                ConfigCommand.ShowConfigSubCommand.class
        }
)
public class ConfigCommand implements Callable<Integer> {

    private ToolsConfigLoader toolsConfigLoader;

    public ConfigCommand() {
        toolsConfigLoader = new ToolsConfigLoader();
    }


    @Override
    public Integer call() throws Exception {
        try {
            toolsConfigLoader.createConfigIfNotExists();

            if (toolsConfigLoader.getConfigFile().exists()) {
                toolsConfigLoader.updatePropertiesFromUserInput();
            } else {
                toolsConfigLoader.readPropertiesFromUserInput();
            }
            toolsConfigLoader.writeConfig();
        } catch (Exception ex) {
            ex.printStackTrace();
            return 1;
        }
        return 0;
    }



    @CommandLine.Command(
            name = "show",
            description = "print configuration"
    )
     class ShowConfigSubCommand implements Callable<Integer> {


        @Override
        public Integer call() throws Exception {
            try {

                File storageDir = toolsConfigLoader.getStorageDir();
                if (!storageDir.exists()) {
                    System.out.println("No configuration directory detected");
                    return 0;
                }

                File configFile = toolsConfigLoader.getConfigFile();
                if (!configFile.exists()) {
                    System.out.println("No configuration file detected");
                    return 0;
                }

                toolsConfigLoader.printConfig();
                return 0;

            } catch (Exception ex) {
                ex.printStackTrace();
                return 1;
            }
        }
    }

}
