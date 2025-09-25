package org.example.cli;

import org.example.dto.response.TicketResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Utility class for CLI command implementations.
 * Provides helper methods for parsing command-line arguments and formatting ticket output.
 * Intended for internal use by {@link CliCommand} implementations.
 */
@Component
public class CliCommandUtils {

    /**
     * Retrieves the value of a required argument from the CLI input.
     *
     * @param args the array of CLI arguments
     * @param key the argument key to search for (ex. "--description")
     * @return the value associated with the key
     * @throws IllegalArgumentException if the key is missing or not followed by a value
     */
    protected String getArg(String[] args, String key) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals(key)) return args[i + 1];
        }
        throw new IllegalArgumentException("Missing argument: " + key);
    }

    /**
     * Retrieves the value of an optional argument from the CLI input.
     *
     * @param args the array of CLI arguments
     * @param key the argument key to search for (ex. "--parentId")
     * @return the value associated with the key, or {@code null} if not present
     */
    protected String getOptionalArg(String[] args, String key) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals(key)) return args[i + 1];
        }
        return null;
    }

    /**
     * Formats a list of {@link TicketResponse} objects for CLI output.
     * Prints each ticket's details in a structured layout.
     *
     * @param tickets the list of tickets to format and print
     */
    protected static void formatTicketsForOutput(List<TicketResponse> tickets) {
        tickets.forEach(ticket -> {
            System.out.println("--------------------------------------------------");
            System.out.printf("ID         : %s%n", ticket.getId());
            System.out.printf("Description: %s%n", ticket.getDescription());
            System.out.printf("Status     : %s%n", ticket.getStatus());
            if (ticket.getParentId() != null) {
                System.out.printf("Parent ID  : %s%n", ticket.getParentId());
            }
            System.out.printf("Created At : %s%n", ticket.getCreatedAt());
            if (ticket.getUpdatedAt() != null) {
                System.out.printf("Updated At : %s%n", ticket.getUpdatedAt());
            }
        });
        System.out.println("--------------------------------------------------");
    }

}
