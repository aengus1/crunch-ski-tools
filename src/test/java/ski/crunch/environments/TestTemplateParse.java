package ski.crunch.environments;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ski.crunch.tools.CoreTestConfiguration;

import static org.junit.Assert.assertTrue;

@SpringBootTest(classes= CoreTestConfiguration.class)
public class TestTemplateParse {

    @Autowired
    ApplicationContext applicationContext;


    @Test
    public void testTemplateParse() {

        ThymeleafConfig config = new ThymeleafConfig();
        config.setApplicationContext(applicationContext);

        TemplateEngine templateEngine =config.templateEngine();


        Context context = new Context();
        context.setVariable("envname", "myenvironment");
        context.setVariable("infra_branch", "feature-123");

        String result =  templateEngine.process("test", context);

        assertTrue(result.contains("    bucket = \"myenvironment-crunch-ski-tf-backend-store\""));
        assertTrue(result.contains("source = \"git::ssh://aengus123@bitbucket.org/mcculloughsolutions/ski-analytics-infrastructure.git?ref=feature-123/stacks/api\""));

    }
}
