package ski.crunch.tools.commands;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "add",
        description = "create a new environment"
)
public class AddEnvCommand implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        return null;
    }
}
