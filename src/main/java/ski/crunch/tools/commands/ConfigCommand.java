package ski.crunch.tools.commands;

import org.springframework.stereotype.Component;
import picocli.CommandLine;
import ski.crunch.tools.ConfigLoader;

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

    private ConfigLoader configLoader;

    public ConfigCommand() {
        configLoader = new ConfigLoader();
    }


    @Override
    public Integer call() throws Exception {
        try {
            configLoader.createConfigIfNotExists();

            if (configLoader.getConfigFile().exists()) {
                configLoader.updatePropertiesFromUserInput();
            } else {
                configLoader.readPropertiesFromUserInput();
            }
            configLoader.writeConfig();
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

                File storageDir = configLoader.getStorageDir();
                if (!storageDir.exists()) {
                    System.out.println("No configuration directory detected");
                    return 0;
                }

                File configFile = configLoader.getConfigFile();
                if (!configFile.exists()) {
                    System.out.println("No configuration file detected");
                    return 0;
                }

                configLoader.printConfig();
                return 0;

            } catch (Exception ex) {
                ex.printStackTrace();
                return 1;
            }
        }
    }

}
