package ski.crunch.tools.io;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ski.crunch.tools.model.EnvironmentConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class EnvConfigLoaderTest {


    private final EnvConfigLoader envConfigLoader = new EnvConfigLoader();

    @Test
    public void testLoadConfig() throws IOException {
        File envConfigFile = new File(getClass().getClassLoader().getResource("env.yml").getFile());

        EnvironmentConfig config = envConfigLoader.load(envConfigFile);

        assertEquals("ca-central-1", config.getPrimary_region());
        assertEquals("crunch-ski",config.getProject_name());
        assertEquals("us-east-1", config.getSecondary_region());

        assertEquals(4, config.getEnvironments().keySet().size());

        EnvironmentConfig.Environment prodEnv = config.getEnvironments().get("prod");

        assertEquals("prod", prodEnv.getProfile());
        assertEquals("crunch.ski", prodEnv.getDomain_name());
        assertEquals("master", prodEnv.getDefault_branch());
        assertTrue(prodEnv.isCI());
        assertNull(prodEnv.getServicesVersion());
        assertEquals("1.2.3", prodEnv.getInfraVersion());

        assertEquals("master", prodEnv.getInfraBranch());
        assertEquals(1, prodEnv.getUser_table_read_capacity());
        assertEquals(1, prodEnv.getUser_table_write_capacity());
        assertFalse(prodEnv.isEncrypt_user_table());

        assertTrue(prodEnv.isUser_table_point_in_time_recovery());
        assertEquals("PAY_PER_REQUEST", prodEnv.getUser_table_billing_mode());
        assertEquals(1, prodEnv.getActivity_table_read_capacity());
        assertEquals(1, prodEnv.getActivity_table_write_capacity());
        assertFalse(prodEnv.isEncrypt_activity_table());
        assertEquals("PAY_PER_REQUEST", prodEnv.getActivity_table_billing_mode());
        assertTrue(prodEnv.isActivity_table_point_in_time_recovery());

        assertEquals("app", prodEnv.getApp_alias());
        assertEquals("auth", prodEnv.getCognito_sub_domain());
        assertEquals("ws", prodEnv.getWs_sub_domain());
        assertEquals("api", prodEnv.getApi_sub_domain());
    }


}