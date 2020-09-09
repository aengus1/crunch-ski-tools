package ski.crunch.tools.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import ski.crunch.tools.model.AddEnvironmentOptions;

import static org.junit.Assert.*;

@SpringBootTest
public class AddEnvironmentServiceTest {

    @Autowired
    private AddEnvironmentService addEnvironmentService;

    private AddEnvironmentOptions options = new AddEnvironmentOptions();

    @Test
    public void testAddEnvironment() throws  Exception{
    //options.setRepositoryUrl("ssh://aengus123@bitbucket.org/mcculloughsolutions/ski-analytics-environments.git");
        options.setRepositoryUrl("https://aengus123@bitbucket.org/mcculloughsolutions/ski-analytics-environments.git");

    addEnvironmentService.addEnvironment(options);
    }
}