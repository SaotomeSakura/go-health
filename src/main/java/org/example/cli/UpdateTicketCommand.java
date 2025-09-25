package org.example.cli;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.UpdateTicketRequest;
import org.example.enums.TicketStatus;
import org.example.facade.TicketFacade;
import org.springframework.stereotype.Component;

/**
 * CLI command for updating the status of an existing ticket.
 * Requires both the ticket ID and the new status value.
 *
 * Expected arguments:
 * <ul>
 *     <li><code>--id</code> (mandatory): ID of the ticket to update</li>
 *     <li><code>--status</code> (required): New status value (e.g. OPEN, IN_PROGRESS, CLOSED)</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class UpdateTicketCommand implements CliCommand {
    private final TicketFacade ticketFacade;

    private final CliCommandUtils utils;

    /**
     * Executes the ticket update command.
     * Validates input arguments and delegates the update to the {@link TicketFacade}.
     *
     * @param args the CLI arguments passed to the command
     */
    @Override
    public void execute(String[] args) {
        try {
            String id = utils.getArg(args, "--id");
            String status = utils.getArg(args, "--status");

            if (id == null || id.isBlank()) {
                System.err.println("Error: --id is required.");
                System.exit(1);
            }

            if (status == null || status.isBlank()) {
                System.err.println("Error: --status is required.");
                System.exit(1);
            }

            TicketStatus ticketStatus = null;
            try {
                ticketStatus = TicketStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.err.println("Error: Invalid status. Use OPEN, IN_PROGRESS, or CLOSED.");
                System.exit(1);
            }

            UpdateTicketRequest request = new UpdateTicketRequest();
            request.setStatus(ticketStatus);

            ticketFacade.updateTicket(id, request);
            System.out.println("Ticket updated.");
        } catch (Exception e) {
            System.err.println("Failed to update ticket: " + e.getMessage());
            System.exit(1);
        }
    }

}
