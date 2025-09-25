package org.example.cli;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.CreateTicketRequest;
import org.example.facade.TicketFacade;
import org.springframework.stereotype.Component;

/**
 * CLI command for creating a new ticket.
 * Parses required and optional arguments from the command line and delegates creation to the {@link TicketFacade}.
 *
 * Expected arguments:
 * <ul>
 *     <li><code>--description</code> (mandatory): Description of the ticket</li>
 *     <li><code>--parentId</code> (optional): ID of the parent ticket, if applicable</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class CreateTicketCommand implements CliCommand {
    private final TicketFacade ticketFacade;

    private final CliCommandUtils utils;

    /**
     * Executes the ticket creation command.
     * Validates input arguments and invokes the ticket creation logic.
     *
     * @param args the CLI arguments passed to the command
     */
    @Override
    public void execute(String[] args) {
        try {
            String description = utils.getArg(args, "--description");
            String parentId = utils.getOptionalArg(args, "--parentId");

            if (description == null || description.isBlank()) {
                System.err.println("Error: Description is required.");
                System.exit(1);
            }

            CreateTicketRequest request = new CreateTicketRequest();
            request.setDescription(description);
            if (parentId != null) request.setParentId(parentId);

            ticketFacade.createTicket(request);
            System.out.println("Ticket created.");
        } catch (Exception e) {
            System.err.println("Failed to create ticket: " + e.getMessage());
            System.exit(1);
        }
    }

}
