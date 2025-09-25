package org.example.cli;

import lombok.RequiredArgsConstructor;
import org.example.enums.TicketStatus;
import org.example.facade.TicketFacade;
import org.springframework.stereotype.Component;

/**
 * CLI command for listing tickets filtered by status.
 * Retrieves matching tickets and formats them for CLI output.
 *
 * Expected arguments:
 * <ul>
 *     <li><code>--status</code> (mandatory): Status to filter tickets by</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class ListTicketsCommand implements CliCommand {

    private final TicketFacade ticketFacade;

    private final CliCommandUtils utils;

    /**
     * Executes the ticket listing command.
     * Validates input arguments, fetches matching tickets, and prints them to the console.
     *
     * @param args the CLI arguments passed to the command
     */
    @Override
    public void execute(String[] args) {
        try {
            String status = utils.getArg(args, "--status");

            TicketStatus ticketStatus = null;
            try {
                ticketStatus = TicketStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.err.println("Error: Invalid status. Use OPEN, IN_PROGRESS, or CLOSED.");
                System.exit(1);
            }

            var tickets = ticketFacade.getTicketsByStatus(ticketStatus);
            utils.formatTicketsForOutput(tickets);
        } catch (Exception e) {
            System.err.println("Failed to fetch tickets: " + e.getMessage());
            System.exit(1);
        }
    }
}
