package ski.crunch.tools.services;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.FileSystemUtils;
import ski.crunch.tools.io.EnvConfigLoader;
import ski.crunch.tools.model.EnvironmentConfig;
import ski.crunch.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class TerraformSourceGenerationServiceTest {

    @Autowired
    private TerraformSourceGenerationService terraformSourceGenerationService;

    private final EnvConfigLoader envConfigLoader = new EnvConfigLoader();
    private File tempSourceDir;

    @BeforeAll()
    public void setUp() {
        try {
            System.out.println(System.getProperty("java.io.tmpdir"));
            tempSourceDir = Files.createTempDirectory("terraform").toFile();
            System.out.println(tempSourceDir.getAbsolutePath());
            File envConfigFile = new File(getClass().getClassLoader().getResource("env.yml").getFile());
            EnvironmentConfig config = envConfigLoader.load(envConfigFile);
            terraformSourceGenerationService.generateTerraformSource(config, tempSourceDir);
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testApiTemplateGeneration() throws IOException {

        File devApiTf = new File(getClass().getClassLoader().getResource("dev.api.env.tf").getFile());
        File generatedDevApiTf = new File(tempSourceDir, "/dev/api/dev.api.env.tf");
        compareGeneratedSourceWithExpected(devApiTf, generatedDevApiTf);

        File devApiJson = new File(getClass().getClassLoader().getResource("dev.terraform.tfvars.json").getFile());
        File generatedDevApiJson = new File(tempSourceDir, "/dev/api/dev.terraform.tfvars.json");
        compareGeneratedSourceWithExpected(devApiJson, generatedDevApiJson);

    }

    @Test
    public void testDataTemplateGeneration() throws IOException {

        File prodDataTf = new File(getClass().getClassLoader().getResource("prod.data.env.tf").getFile());
        File generatedProdDataTf = new File(tempSourceDir, "/prod/data/prod.data.env.tf");
        compareGeneratedSourceWithExpected(prodDataTf, generatedProdDataTf);

        File prodDataJson = new File(getClass().getClassLoader().getResource("prod.terraform.tfvars.json").getFile());
        File generatedProdDataJson = new File(tempSourceDir, "/prod/data/prod.terraform.tfvars.json");
        compareGeneratedSourceWithExpected(prodDataJson, generatedProdDataJson);

    }

    @Test
    public void testFrontEndTemplateGeneration() throws IOException {

        File stagingFrontEndTf = new File(getClass().getClassLoader().getResource("staging.frontend.env.tf").getFile());
        File generatedStagingFrontEndTf = new File(tempSourceDir, "/staging/frontend/staging.frontend.env.tf");
        compareGeneratedSourceWithExpected(stagingFrontEndTf, generatedStagingFrontEndTf);

        File stagingFrontendJson = new File(getClass().getClassLoader().getResource("staging.terraform.tfvars.json").getFile());
        File generatedStagingFrontEndJson = new File(tempSourceDir, "/staging/frontend/staging.terraform.tfvars.json");
        compareGeneratedSourceWithExpected(stagingFrontendJson, generatedStagingFrontEndJson);

    }

    @Test
    public void testWebclientCdTemplateGeneration() throws IOException {

        File intTestWebClientTf = new File(getClass().getClassLoader().getResource("int-test.webclient-cd.env.tf").getFile());
        File generatedIntTestWebClientTf = new File(tempSourceDir, "/int-test/webclient-cd/int-test.webclient-cd.env.tf");
        compareGeneratedSourceWithExpected(intTestWebClientTf, generatedIntTestWebClientTf);

        File intTestWebclientJson = new File(getClass().getClassLoader().getResource("int-test.terraform.tfvars.json").getFile());
        File generatedIntTestWebclientJson = new File(tempSourceDir, "/int-test/webclient-cd/int-test.terraform.tfvars.json");
        compareGeneratedSourceWithExpected(intTestWebclientJson, generatedIntTestWebclientJson);

    }

    @AfterAll()
    public void tearDown() {

        FileSystemUtils.deleteRecursively(tempSourceDir);
    }

    private void compareGeneratedSourceWithExpected(File expected, File generated) throws IOException {

        String expectedStr = FileUtils.readFileToString(expected).replaceAll(" ", "");
        String generatedStr = FileUtils.readFileToString(generated).replaceAll(" ", "");
        assertEquals(expectedStr, generatedStr);
    }
}