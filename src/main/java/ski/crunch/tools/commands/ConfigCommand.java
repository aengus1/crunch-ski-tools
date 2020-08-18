package ski.crunch.tools.commands;

import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@Component
@CommandLine.Command(
        name = "config",
        description = "Configure CLI"
        //,
//        subcommands = {
//                ShowConfigSubCommand.class
//        }
)
public class ConfigCommand implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println("Config command called");
        return 0;
    }


}
