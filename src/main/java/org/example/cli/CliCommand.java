package org.example.cli;

/**
 * Represents a CLI command that can be executed with a set of arguments.
 * Implementations encapsulate specific command logic such as creating, updating, or listing tickets.
 */
public interface CliCommand {
    void execute(String[] args);
}
