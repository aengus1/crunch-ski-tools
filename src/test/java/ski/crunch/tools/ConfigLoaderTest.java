package ski.crunch.tools;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.context.SpringBootTest;
import ski.crunch.tools.model.ConfigurationProperty;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Map;

import static junit.framework.TestCase.*;


public class ConfigLoaderTest {

    private final InputStream originalIn = System.in;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;


    private ConfigLoader configLoader;
    private File storageDir = new File(System.getProperty("java.io.tmpdir"), ".crunch");

    @Before
    public void setUp() throws Exception {
        System.setOut(new PrintStream(outContent));

        if(storageDir.exists()) {
            storageDir.delete();
        }
        this.configLoader = new ConfigLoader();
    }

    @Test
    void testCreateConfigIfNotExists() throws IOException {
        this.configLoader = new ConfigLoader();
        configLoader.setHomeDir(System.getProperty("java.io.tmpdir"));
        configLoader.setStorageDir(storageDir);
        configLoader.setConfigFile(new File(storageDir, "config"));

        configLoader.createConfigIfNotExists();

//        assertEquals("No configuration directory detected"
//                +System.lineSeparator()+"No configuration file detected", outContent.toString());

        // assert that the config directory has been created
        File configDir = new File(System.getProperty("java.io.tmpdir"), ".crunch");
        assertTrue(configDir.exists());

        // assert that the config file is not empty if it does exist
        File configFile = new File(configDir, "config");
        if(configFile.exists()) {
            FileReader fr = new FileReader(configFile);
            assertTrue(fr.read()!=-1);
        }

    }


    @Test
    void testReadConfiguration() throws IOException {
        ConfigLoader configLoader = new ConfigLoader();
        configLoader.setConfigFile(new File(getClass().getClassLoader().getResource("testConfig.properties").getFile()));

        configLoader.readConfiguration();
        Map<ConfigurationProperty, String> result = configLoader.getValues();
        assertTrue(result.keySet().contains(ConfigurationProperty.PROJECT_NAME));
        assertEquals("crunch.ski", result.get(ConfigurationProperty.PROJECT_NAME));

        assertTrue(result.keySet().contains(ConfigurationProperty.SECONDARY_REGION));
        assertEquals("us-west-2", result.get(ConfigurationProperty.SECONDARY_REGION));

    }

    @Test
    void testWriteConfiguration() throws IOException {
        ConfigLoader configLoader = new ConfigLoader();
        configLoader.setConfigFile(new File(getClass().getClassLoader().getResource("testConfig.properties").getFile()));

        configLoader.readConfiguration();
        Map<ConfigurationProperty, String> result = configLoader.getValues();

        configLoader.setConfigFile(new File(System.getProperty("java.io.tmpdir"),"testconfigwrite.txt"));
        configLoader.writeConfig();

        configLoader.readConfiguration();
        Map<ConfigurationProperty, String> secondResult = configLoader.getValues();
        assertEquals(result.get(ConfigurationProperty.PROJECT_NAME), secondResult.get(ConfigurationProperty.PROJECT_NAME));

    }

//    @Test
//    public void testReadPropertyFromUserInput() throws Exception{
//        configLoader = new ConfigLoader();
//        configLoader.setHomeDir(System.getProperty("java.io.tmpdir"));
//        configLoader.setStorageDir(storageDir);
//        configLoader.setConfigFile(new File(storageDir, "config"));
//
//        configLoader.readPropertiesFromUserInput();
//        String data = "test.ski" + System.lineSeparator() + "myprofile\r\nus-east-1\r\ntest.ca\r\nyes\r\nus-west-2";
//        System.setIn(new ByteArrayInputStream(data.getBytes()));
//
//        configLoader.readConfiguration();
//        Map<ConfigurationProperty, String> result = configLoader.getValues();
//        assertTrue(result.keySet().contains(ConfigurationProperty.PROJECT_NAME));
//        assertEquals("test.ski", result.get(ConfigurationProperty.PROJECT_NAME));
//
//    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @After
    public void tearDownConfigDir() {
        storageDir.delete();
    }

    public static InputStream readFromResourcesDirectory(Class clazz, String fileName) throws IOException {
        return clazz.getResourceAsStream(fileName);
    }
}

