/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nick.queueapi.queue;

/**
 *
 * @author User
 */
// ServiceQueue.java
import java.util.*;

public class ServiceQueue {
    private Queue<Ticket> waitingTickets;
    private Ticket currentlyServing;
    private ServiceType serviceType;
    private int ticketCounter;
    
    public ServiceQueue(ServiceType serviceType) {
        this.serviceType = serviceType;
        this.waitingTickets = new LinkedList<>();
        this.ticketCounter = 1;
    }
    
    public Ticket createTicket(String studentName, String studentId, String phoneNumber) {
        String ticketNumber = serviceType.getPrefix() + "-" + ticketCounter++;
        Ticket ticket = new Ticket(ticketNumber, studentName, studentId, phoneNumber, serviceType);
        waitingTickets.add(ticket);
        return ticket;
    }
    
    public void serveNextCustomer() {
        if (!waitingTickets.isEmpty()) {
            currentlyServing = waitingTickets.poll();
        }
    }
    
    public Ticket getCurrentlyServing() {
        return currentlyServing;
    }
    
    public int getQueueLength() {
        return waitingTickets.size();
    }
    
    public List<Ticket> getNextInQueue(int count) {
        List<Ticket> nextTickets = new ArrayList<>();
        Iterator<Ticket> iterator = waitingTickets.iterator();
        int counter = 0;
        
        while (iterator.hasNext() && counter < count) {
            nextTickets.add(iterator.next());
            counter++;
        }
        
        return nextTickets;
    }
    
    public int getTicketPosition(String ticketNumber) {
        int position = 1;
        for (Ticket ticket : waitingTickets) {
            if (ticket.getTicketNumber().equals(ticketNumber)) {
                return position;
            }
            position++;
        }
        return -1; // Ticket not found in this queue
    }
    
    public List<Ticket> getAllTickets() {
        return new ArrayList<>(waitingTickets);
    }
}