package ski.crunch.tools;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import ski.crunch.tools.commands.MainCommand;


@Component
public class CliRunner implements CommandLineRunner, ExitCodeGenerator {

    private final MainCommand command;
    private CommandLine.IFactory factory;
    private int exitCode;

    public CliRunner(MainCommand command, CommandLine.IFactory factory) {
        this.command = command;
        this.factory = factory;
    }

    @Override
    public void run(String... args) throws Exception {
        exitCode = new CommandLine(command, factory).execute(args);
    }

    @Override
    public int getExitCode() {
        return 0;
    }
}
