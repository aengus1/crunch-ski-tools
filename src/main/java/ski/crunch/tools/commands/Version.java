package ski.crunch.tools.commands;

import picocli.CommandLine;

import java.io.*;

/**
 * Class reads the current application version from <project_root_dir>/version.txt  (written by gradle during build)
 * and makes it available to the application through picocli --version
 */
public class Version implements CommandLine.IVersionProvider {

    private static String get() throws IOException {
        ClassLoader cl = Version.class.getClassLoader();
        File versionFile = new File(System.getenv().get("PWD")+"/version.txt");
        try (FileReader fr = new FileReader(versionFile)) {
            try (BufferedReader br = new BufferedReader(fr)) {
                String line = "";
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("Version:")) {
                        return line.split("Version:")[1];
                    }
                }
            }
        }
        return "";
    }


    @Override
    public String[] getVersion() throws Exception {
        return new String[]{get()};
    }

}
