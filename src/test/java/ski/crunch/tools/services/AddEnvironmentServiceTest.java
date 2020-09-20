package ski.crunch.tools.services;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ski.crunch.tools.model.AddEnvironmentOptions;

import static org.junit.Assert.assertFalse;


@SpringBootTest
public class AddEnvironmentServiceTest {

    @Autowired
    private AddEnvironmentService addEnvironmentService;

    private AddEnvironmentOptions options = new AddEnvironmentOptions();

    @Test
    @Disabled
    public void testAddEnvironment() throws  Exception{
        options.setRepositoryUrl("https://aengus123@bitbucket.org/mcculloughsolutions/ski-analytics-environments.git");
        options.setStage("staging");


        addEnvironmentService.addEnvironment(options);
        assertFalse(true);
    }
}