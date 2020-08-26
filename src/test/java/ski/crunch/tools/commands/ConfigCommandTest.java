package ski.crunch.tools.commands;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import picocli.CommandLine;
import ski.crunch.tools.ToolsApplication;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest( classes = ToolsApplication.class)
public class ConfigCommandTest {

    @Autowired
    private CommandLine.IFactory factory;

    @Autowired
    private ConfigCommand configCommand;

    @Test
    public void testParsingCommandLineArgs() {
        CommandLine.ParseResult parseResult = new CommandLine(configCommand, factory)
                .parseArgs("show");

        assertEquals("config", parseResult.commandSpec().name());

        assertTrue(parseResult.hasSubcommand());
        CommandLine.ParseResult subResult = parseResult.subcommand();

    }


}
