package ski.crunch.tools.io;

import lombok.Getter;
import lombok.Setter;
import ski.crunch.tools.model.ToolsConfigProperty;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Loads configuration from tools config file (~/.crunch/config)
 */
public class ToolsConfigLoader {

    @Getter @Setter
    private  String homeDir;

    @Getter @Setter
    private  File storageDir;

    @Getter @Setter
    private  File configFile;


    private Scanner scanner;
    @Getter
    private Map<ToolsConfigProperty, String> values;

    public ToolsConfigLoader() {
        scanner = new Scanner(System.in);
        values = new HashMap<>();
        setDefaults();
    }

    /**
     * Creates empty configuration directory
     * @throws IOException
     */
    public void createConfigIfNotExists() throws IOException {
        if (!storageDir.exists()) {
            if (storageDir.mkdir()) {
                System.out.println("No configuration directory detected");
            } else {
                throw new IOException("Error creating storage directory: " + storageDir.getAbsolutePath());
            }
        }

        if( !configFile.exists()) {
            System.out.println("No configuration file detected");
        } else {
            // delete the config file if it is empty
            try (FileReader fr = new FileReader(configFile)) {
                try (BufferedReader br = new BufferedReader(fr)) {
                    if (br.read() == -1) {
                        configFile.delete();
                    }
                }
            }
        }
    }

    /**
     * Reads configuration from user_dir/.crunch/config into a map of KVPs
     * @return Map<String, String> configuration properties
     * @throws IOException
     */
    public void readConfiguration() throws IOException {

        try (FileReader fileReader = new FileReader(configFile)) {
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] kvp = line.split("=");
                    values.put(ToolsConfigProperty.valueOf(kvp[0]), kvp[1]);
                }
            }
        }
    }

    /**
     * Prompts user to enter non-empty properties from commandline
     */
    public void readPropertiesFromUserInput() {
        for (ToolsConfigProperty property : ToolsConfigProperty.values()) {
            readPropertyFromUserInput(property);
        }
    }

    private void readPropertyFromUserInput(ToolsConfigProperty property) {
        System.out.println(property + " [Enter a value to continue]:");
        String input = scanner.nextLine();
        if (input.isEmpty()) {
            readPropertyFromUserInput(property);
        } else {
            values.put(property, input);
        }
    }

    /**
     * prints current configuration to the screen
     * @throws IOException
     */
    public void printConfig() throws IOException{
        readConfiguration();
        for (ToolsConfigProperty toolsConfigProperty : values.keySet()) {
            System.out.println(toolsConfigProperty.name() + " = " + values.get(toolsConfigProperty));
        }
    }
    /**
     * Writes current state of private property 'values' into the configuration file
     * @throws IOException
     */
    public void writeConfig() throws IOException {
        FileWriter fileWriter = new FileWriter(configFile);
        try {
            for (ToolsConfigProperty property : values.keySet()) {
                fileWriter.write(property.name() + "=" + values.get(property) + System.lineSeparator());
            }
        } finally {
            fileWriter.flush();
            fileWriter.close();
        }
    }

    private final void setDefaults() {
        this.homeDir = System.getProperty("user.home");
        this.storageDir = new File(homeDir, ".crunch");
        this.configFile =  new File(storageDir, "config");
    }

    public void updatePropertiesFromUserInput() throws IOException {
        try (FileReader fileReader = new FileReader(configFile)) {
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    String[] kvp = line.split("=");
                    String key = kvp[0];
                    for (ToolsConfigProperty property : ToolsConfigProperty.values()) {
                        if (property.name().equalsIgnoreCase(key)) {
                            values.put(property, kvp[1]);
                            System.out.println(property.name() + ":    " + kvp[1] + "  [Enter to accept]:");
                            String input = scanner.nextLine();
                            if (!"".equals(input)) {
                                values.put(property, input);
                            }
                        }
                    }
                }
            }
        }
    }
}
