package ski.crunch.tools.services;


import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ski.crunch.tools.io.EnvConfigLoader;
import ski.crunch.tools.model.EnvironmentConfig;
import ski.crunch.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import static org.junit.Assert.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class TerraformSourceGenerationServiceTest {

    @Autowired
    private TerraformSourceGenerationService terraformSourceGenerationService;

    private final EnvConfigLoader envConfigLoader = new EnvConfigLoader();
    private File tempSourceDir;
    //private List<String> envNames;
    private Map<String, String> stageEnvNameMap;

    /**
     * Generate terraform source code and write to a temporary directory
     */
    @BeforeAll()
    public void setUp() {
        try {

            System.out.println(System.getProperty("java.io.tmpdir"));
            tempSourceDir = Files.createTempDirectory("terraform").toFile();
            System.out.println(tempSourceDir.getAbsolutePath());
            File envConfigFile = new File(getClass().getClassLoader().getResource("env.yml").getFile());
            EnvironmentConfig config = envConfigLoader.load(envConfigFile);
            stageEnvNameMap = terraformSourceGenerationService.generateTerraformSource(config, tempSourceDir);

//            envNames = config.getEnvironmentDefs().keySet().stream()
//                    .map(x -> config.getEnvironmentDefs().get(x).getEnvName())
//                    .collect(Collectors.toList());
//            for (String envName : envNames) {
//                System.out.println(envName);
//            }
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Compare generated source code with expected
     * @throws IOException
     */

    @Disabled  // going to remove API stack - it isn't adding much value right now
    @Test
    public void testApiTemplateGeneration() throws IOException {
        String envName = stageEnvNameMap.get("prod");
        File devApiTf = new File (getClass().getClassLoader().getResource("prod2.api.env.tf").getFile());
        File generatedDevApiTf = new File (tempSourceDir, "/"+envName+"/api/"+envName+".api.env.tf");
        compareGeneratedSourceWithExpected(devApiTf, generatedDevApiTf, "prod", envName);

        File devApiJson = new File (getClass().getClassLoader().getResource("prod2.terraform.tfvars.json").getFile());
        File generatedDevApiJson = new File(tempSourceDir, "/"+envName + "/api/"+envName+".terraform.tfvars.json");
        compareGeneratedSourceWithExpected(devApiJson, generatedDevApiJson, "prod", envName);

    }

    @Test
    public void testDataTemplateGeneration() throws IOException {
        String envName = stageEnvNameMap.get("prod");
        File prodDataTf = new File(getClass().getClassLoader().getResource("prod.data.env.tf").getFile());
        File generatedProdDataTf = new File(tempSourceDir, "/"+envName+"/data/"+envName+".data.env.tf");
        compareGeneratedSourceWithExpected(prodDataTf, generatedProdDataTf, "prod", envName);

        File prodDataJson = new File(getClass().getClassLoader().getResource("prod.terraform.tfvars.json").getFile());
        File generatedProdDataJson = new File(tempSourceDir, "/"+envName+"/data/"+envName+".terraform.tfvars.json");
        compareGeneratedSourceWithExpected(prodDataJson, generatedProdDataJson, "prod", envName);

    }

    @Test
    public void testFrontEndTemplateGeneration() throws IOException {
        String envName = stageEnvNameMap.get("staging");
        File stagingFrontEndTf = new File(getClass().getClassLoader().getResource("staging.frontend.env.tf").getFile());
        File generatedStagingFrontEndTf = new File(tempSourceDir, "/"+envName+"/frontend/"+envName+".frontend.env.tf");
        compareGeneratedSourceWithExpected(stagingFrontEndTf, generatedStagingFrontEndTf, "staging", envName);

        File stagingFrontendJson = new File(getClass().getClassLoader().getResource("staging.terraform.tfvars.json").getFile());
        File generatedStagingFrontEndJson = new File(tempSourceDir, "/"+envName+"/frontend/"+envName+".terraform.tfvars.json");
        compareGeneratedSourceWithExpected(stagingFrontendJson, generatedStagingFrontEndJson, "staging", envName);

    }

    @Test
    public void testWebclientCdTemplateGeneration() throws IOException {
        String envName = stageEnvNameMap.get("int-test");
        File intTestWebClientTf = new File(getClass().getClassLoader().getResource("int-test.webclient-cd.env.tf").getFile());
        File generatedIntTestWebClientTf = new File(tempSourceDir, "/"+envName+"/webclient-cd/"+envName+".webclient-cd.env.tf");
        compareGeneratedSourceWithExpected(intTestWebClientTf, generatedIntTestWebClientTf, "int-test", envName);

        File intTestWebclientJson = new File(getClass().getClassLoader().getResource("int-test.terraform.tfvars.json").getFile());
        File generatedIntTestWebclientJson = new File(tempSourceDir, "/"+envName+"/webclient-cd/"+envName+".terraform.tfvars.json");
        compareGeneratedSourceWithExpected(intTestWebclientJson, generatedIntTestWebclientJson, "int-test", envName);

    }

    @AfterAll()
    public void tearDown() {

        //FileSystemUtils.deleteRecursively(tempSourceDir);
    }

    private void compareGeneratedSourceWithExpected(File expected, File generated, String envName, String generatedEnvName) throws IOException {

        String expectedStr = FileUtils.readFileToString(expected)
                .replaceAll(" ", "")
                .replaceAll(envName, generatedEnvName)
                .replaceAll("envrep", generatedEnvName)
                .replaceAll("\"stage\":\""+generatedEnvName+"\"","\"stage\":\""+envName+"\"")
                .replaceAll("\"profile\":\""+generatedEnvName+"\"","\"profile\":\""+envName+"\"");

        String generatedStr = FileUtils.readFileToString(generated)
                .replaceAll(" ", "");
        assertEquals(expectedStr, generatedStr);
    }
}