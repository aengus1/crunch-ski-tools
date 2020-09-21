package ski.crunch.tools.io;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ski.crunch.tools.CoreTestConfiguration;
import ski.crunch.tools.ThymeleafConfig;

import static org.junit.Assert.assertTrue;

@SpringBootTest(classes= CoreTestConfiguration.class)
public class TestTemplateParse {

    @Autowired
    ApplicationContext applicationContext;


    @Test
    public void testTemplateParse() {

        ThymeleafConfig config = new ThymeleafConfig();
        config.setApplicationContext(applicationContext);

        TemplateEngine templateEngine = config.terraformTemplateEngine();


        Context context = new Context();
        context.setVariable("envname", "myenvironment");
        context.setVariable("infra_ref", "feature-123");

        String result =  templateEngine.process("test", context);
        assertTrue(result.contains("    bucket = \"myenvironment-crunch-ski-tf-backend-store\""));
        assertTrue(result.contains("source = \"git::ssh://aengus123@bitbucket.org/mcculloughsolutions/ski-analytics-infrastructure.git//stacks/api?ref=feature-123\""));

    }
}
