/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.nick.queueapi.queue;

/**
 *
 * @author User
 */
// QueueManagementSystem.java
public class QueueManagementSystem {
    public static void main(String[] args) {
        QueueManager queueManager = new QueueManager();
        
        // Create the GUI components and start the system
        QueueGUI gui = new QueueGUI(queueManager);
        gui.initialize();
    }
}

