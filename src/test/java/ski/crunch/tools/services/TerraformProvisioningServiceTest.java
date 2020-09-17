package ski.crunch.tools.services;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ski.crunch.tools.model.AddEnvironmentOptions;
import ski.crunch.tools.model.EnvironmentConfig;
import ski.crunch.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class TerraformProvisioningServiceTest {

    @Autowired
    private TerraformProvisioningService terraformProvisioningService;
    private File tempDir;

    @Mock
    private EnvironmentConfig config;

    @Mock
    private AddEnvironmentOptions options;

    /**
     * Generate terraform source code and write to a temporary directory
     */
    @BeforeAll()
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        try {

            System.out.println(System.getProperty("java.io.tmpdir"));
            tempDir = Files.createTempDirectory("tf_src").toFile();
            System.out.println(tempDir.getAbsolutePath());
            File testTfSrc = new File(getClass().getClassLoader().getResource("tf_source").getFile());
            FileUtils.copyDirectory(testTfSrc.getAbsolutePath(), tempDir.getAbsolutePath());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testProvision() throws IOException {
        Map<String, EnvironmentConfig.EnvironmentState> envState = new HashMap<>();
        envState.put("staging", null);
        when(config.getEnvironmentState()).thenReturn(envState);
        when(options.getStage()).thenReturn("staging");
        String envName = "namedenv";

       boolean success =  terraformProvisioningService.provisionEnv(config, options, envName, tempDir);

       assertTrue(success);
    }

//    @Test
//    public void testDeProvision() throws IOException {
//        Map<String, EnvironmentConfig.EnvironmentState> envState = new HashMap<>();
//        envState.put("staging", null);
//        when(config.getEnvironmentState()).thenReturn(envState);
//        when(options.getStage()).thenReturn("staging");
//        String envName = "namedenv";
//
//        boolean success =  terraformProvisioningService.deProvision(config, options, envName, tempDir);
//
//        assertTrue(success);
//    }


    @AfterAll
    public void tearDown() {

        // tempDir.deleteOnExit();
    }
}