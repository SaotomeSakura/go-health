package org.example.cli;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Entry point for the ticket CLI application.
 * Routes command-line arguments to the appropriate {@link CliCommand} implementation.
 * Supports commands for creating, updating, and listing tickets.
 *
 * <p>Usage:</p>
 * <pre>
 * --create --description "..." [--parentId ID]
 * --update --id ID --status STATUS
 * --list --status STATUS
 * </pre>
 */
@Component
public class TicketCliRunner implements CommandLineRunner {

    private  final Map<String, CliCommand> commandRegistry;

    public TicketCliRunner(
            CreateTicketCommand createCommand,
            UpdateTicketCommand updateCommand,
            ListTicketsCommand listCommand
    ) {
        this.commandRegistry = Map.of(
                "--create", createCommand,
                "--update", updateCommand,
                "--list", listCommand
        );
    }

    /**
     * Executes the CLI application logic.
     * Parses the first argument to determine the command and delegates execution.
     * Prints usage help if no arguments are provided or if the command is unknown.
     *
     * @param args the command-line arguments
     */
    @Override
    public void run(String... args) {
        if (args.length == 0 || args[0].equals("--help")) {
            printHelp();
            return;
        }

        CliCommand command = commandRegistry.get(args[0]);
        if (command == null) {
            System.err.println("Unknown command. Use --help.");
            System.exit(1);
        }
        command.execute(args);
    }

    private void printHelp() {
        System.out.println("Usage:");
        System.out.println("--create --description \"...\" [--parentId ID]");
        System.out.println("--update --id ID --status STATUS");
        System.out.println("--list --status STATUS");
    }

}

