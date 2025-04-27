/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nick.queueapi.queue;

/**
 *
 * @author User
 */
// Ticket.java
import java.time.LocalDateTime;

public class Ticket {
    private String ticketNumber;
    private String studentName;
    private String studentId;
    private String phoneNumber;
    private ServiceType serviceType;
    private LocalDateTime issueTime;
    private boolean notified;
    
    public Ticket(String ticketNumber, String studentName, String studentId, String phoneNumber, ServiceType serviceType) {
        this.ticketNumber = ticketNumber;
        this.studentName = studentName;
        this.studentId = studentId;
        this.phoneNumber = phoneNumber;
        this.serviceType = serviceType;
        this.issueTime = LocalDateTime.now();
        this.notified = false;
    }
    
    public String getTicketNumber() {
        return ticketNumber;
    }
    
    public String getStudentName() {
        return studentName;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public ServiceType getServiceType() {
        return serviceType;
    }
    
    public LocalDateTime getIssueTime() {
        return issueTime;
    }
    
    public boolean isNotified() {
        return notified;
    }
    
    public void setNotified(boolean notified) {
        this.notified = notified;
    }
    
    @Override
    public String toString() {
        return ticketNumber + " - " + studentName;
    }
}