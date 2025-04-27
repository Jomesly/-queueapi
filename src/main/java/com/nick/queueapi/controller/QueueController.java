/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nick.queueapi.controller;

import com.nick.queueapi.queue.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/queue")
public class QueueController {

    private final QueueManager queueManager = new QueueManager(); // already working!

    // Example: http://localhost:8080/api/queue/current?service=registrar
    @GetMapping("/current")
    public Map<String, String> getCurrent(@RequestParam String service) {
        ServiceType type = ServiceType.valueOf(service.toUpperCase());
        Ticket ticket = queueManager.getCurrentlyServing(type);

        Map<String, String> response = new HashMap<>();
        response.put("ticketNumber", ticket != null ? ticket.getTicketNumber() : "None");
        return response;
    }

    // Optional: Generate a new ticket from a web request
    @PostMapping("/generate")
    public Map<String, String> generateTicket(
            @RequestParam String studentName,
            @RequestParam String studentId,
            @RequestParam String phoneNumber,
            @RequestParam String service) {

        ServiceType type = ServiceType.valueOf(service.toUpperCase());
        Ticket ticket = queueManager.generateTicket(studentName, studentId, phoneNumber, type);

        Map<String, String> response = new HashMap<>();
        response.put("ticketNumber", ticket.getTicketNumber());
        return response;
    }
}
