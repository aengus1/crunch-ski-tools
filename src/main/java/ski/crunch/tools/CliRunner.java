package ski.crunch.tools;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import ski.crunch.tools.commands.MainCommand;

import java.util.List;
import java.util.concurrent.Callable;

@Component
public class CliRunner implements CommandLineRunner, ExitCodeGenerator {

    private final MainCommand command;
    private CommandLine.IFactory factory;

    public CliRunner(MainCommand command, CommandLine.IFactory factory) {
        this.command = command;
        this.factory = factory;
    }
    private int exitCode;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("running CLI runner");
        exitCode = new CommandLine(command, factory).execute(args);
    }

    @Override
    public int getExitCode() {
        return 0;
    }
}
