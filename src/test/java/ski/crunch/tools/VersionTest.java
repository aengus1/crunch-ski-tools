package ski.crunch.tools;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static com.github.stefanbirkner.systemlambda.SystemLambda.*;
import java.io.File;
import java.io.FileWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class VersionTest {


    static {
        File version = new File("/tmp/version.txt");
        try (FileWriter fw = new FileWriter(version)) {
            fw.write("Irrelevant info: not important" + System.lineSeparator());
            fw.write("Version: 1.0.0" + System.lineSeparator());
            fw.write("Build info: abc123");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Test
    void getVersion() throws Exception {
        withEnvironmentVariable("PWD", "/tmp").execute(() -> {
        System.out.println(System.getenv().get("PWD"));
        Version version = new Version();
        String[] versionRes = version.getVersion();
        assertNotNull(versionRes[0]);
        assertEquals(1, versionRes.length);
        assertEquals("1.0.0", versionRes[0].trim());
        });
    }
}
