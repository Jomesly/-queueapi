/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nick.queueapi.queue;

/**
 *
 * @author User
 */
// QueueManager.java
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QueueManager {
    private Map<ServiceType, ServiceQueue> queues;
    private Map<String, Ticket> activeTickets;
    private Map<ServiceType, Integer> waitTimeEstimates; // in minutes per customer
    
    public QueueManager() {
        queues = new HashMap<>();
        queues.put(ServiceType.REGISTRAR, new ServiceQueue(ServiceType.REGISTRAR));
        queues.put(ServiceType.ENROLLMENT, new ServiceQueue(ServiceType.ENROLLMENT));
        queues.put(ServiceType.CASHIER, new ServiceQueue(ServiceType.CASHIER));
        
        activeTickets = new HashMap<>();
        
        waitTimeEstimates = new HashMap<>();
        waitTimeEstimates.put(ServiceType.REGISTRAR, 5);    // 5 minutes per customer
        waitTimeEstimates.put(ServiceType.ENROLLMENT, 7);   // 7 minutes per customer
        waitTimeEstimates.put(ServiceType.CASHIER, 3);      // 3 minutes per customer
    }
    
    public Ticket generateTicket(String studentName, String studentId, String phoneNumber, ServiceType type) {
        ServiceQueue queue = queues.get(type);
        Ticket ticket = queue.createTicket(studentName, studentId, phoneNumber);
        activeTickets.put(ticket.getTicketNumber(), ticket);
        return ticket;
    }
    
    public void serveNext(ServiceType type) {
        queues.get(type).serveNextCustomer();
    }
    
    public Ticket getCurrentlyServing(ServiceType type) {
        return queues.get(type).getCurrentlyServing();
    }
    
    public int getQueueLength(ServiceType type) {
        return queues.get(type).getQueueLength();
    }
    
    public List<Ticket> getNextInQueue(ServiceType type, int count) {
        return queues.get(type).getNextInQueue(count);
    }
    
    public int getEstimatedWaitTime(ServiceType type) {
        int queueLength = getQueueLength(type);
        int timePerCustomer = waitTimeEstimates.get(type);
        return queueLength * timePerCustomer;
    }
    
    public Ticket getTicketByNumber(String ticketNumber) {
        return activeTickets.get(ticketNumber);
    }
    
    public int getTicketPosition(String ticketNumber) {
        for (ServiceType type : ServiceType.values()) {
            int position = queues.get(type).getTicketPosition(ticketNumber);
            if (position > 0) {
                return position;
            }
        }
        return -1; // Ticket not found
    }
    
    public List<Ticket> getAllTicketsForService(ServiceType type) {
        return queues.get(type).getAllTickets();
    }
    
    public void notifyUpcomingTurn(int notificationThreshold) {
        for (ServiceType type : ServiceType.values()) {
            List<Ticket> upcoming = queues.get(type).getNextInQueue(notificationThreshold);
            for (Ticket ticket : upcoming) {
                if (!ticket.isNotified()) {
                    sendSMSNotification(ticket);
                    ticket.setNotified(true);
                }
            }
        }
    }
    
    private void sendSMSNotification(Ticket ticket) {
        // In a real implementation, this would connect to an SMS gateway
        // For now we'll just print to console
        System.out.println("SENDING SMS to " + ticket.getPhoneNumber() + ": " 
                + "Your ticket " + ticket.getTicketNumber() 
                + " will be called soon. Please proceed to the " 
                + ticket.getServiceType() + " area.");
    }
}