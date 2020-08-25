package ski.crunch.tools;

import picocli.CommandLine;

import java.io.*;

public class Version implements CommandLine.IVersionProvider {

    public static String get() throws IOException{
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
