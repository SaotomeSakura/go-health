package org.example.cli;

import org.example.dto.request.CreateTicketRequest;
import org.example.dto.request.UpdateTicketRequest;
import org.example.dto.response.TicketResponse;
import org.example.enums.TicketStatus;
import org.example.facade.TicketFacade;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TicketCliRunner implements CommandLineRunner {

    private final TicketFacade ticketFacade;

    public TicketCliRunner(TicketFacade ticketService) {
        this.ticketFacade = ticketService;
    }

    @Override
    public void run(String... args) {
        if (args.length == 0 || args[0].equals("--help")) {
            System.out.println("Usage:");
            System.out.println("--create --description \"...\" [--parentId ID]");
            System.out.println("--update --id ID --status STATUS");
            System.out.println("--list --status STATUS");
            return;
        }

        try {
            switch (args[0]) {
                case "--create" -> {
                    String description = getArg(args, "--description");
                    String parentId = getOptionalArg(args, "--parentId");

                    CreateTicketRequest request = new CreateTicketRequest();
                    request.setDescription(description);
                    if (parentId != null) {
                        request.setParentId(parentId);
                    }

                    ticketFacade.createTicket(request);
                    System.out.println("Ticket created.");
                }
                case "--update" -> {
                    String id = getArg(args, "--id");
                    String status = getArg(args, "--status");

                    UpdateTicketRequest request = new UpdateTicketRequest();
                    request.setStatus(TicketStatus.valueOf(status));

                    ticketFacade.updateTicket(id, request);
                    System.out.println("Ticket updated.");
                }
                case "--list" -> {
                    String status = getArg(args, "--status");
                    var tickets = ticketFacade.getTicketsByStatus(TicketStatus.valueOf(status));
                    formatTicketsForOutput(tickets);
                }
                default -> System.out.println("Unknown command. Use --help.");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private String getArg(String[] args, String key) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals(key)) return args[i + 1];
        }
        throw new IllegalArgumentException("Missing argument: " + key);
    }

    private String getOptionalArg(String[] args, String key) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals(key)) return args[i + 1];
        }
        return null;
    }

    private void formatTicketsForOutput(List<TicketResponse> tickets) {
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

