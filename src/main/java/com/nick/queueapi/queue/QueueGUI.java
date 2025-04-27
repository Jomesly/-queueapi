/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nick.queueapi.queue;

/**
 *
 * @author User
 */
// QueueGUI.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueueGUI {
    private final QueueManager queueManager;
    private JFrame mainFrame;
    private JTabbedPane tabbedPane;
    private JPanel customerPanel;
    private JPanel adminPanel;
    
    // Admin view components
    private JTable registrarTable;
    private JTable enrollmentTable;
    private JTable cashierTable;
    
    // Customer view components
    private JComboBox<ServiceType> serviceTypeComboBox;
    private JTextField nameField;
    private JTextField idField;
    private JTextField phoneField;
    private JPanel ticketPanel;
    private JLabel ticketNumberLabel;
    private JLabel positionLabel;
    private JLabel waitTimeLabel;
    
    private Map<ServiceType, JLabel[]> queueStatusLabels = new HashMap<>();
    private Map<ServiceType, JLabel> currentlyServingLabels = new HashMap<>();
    
    public QueueGUI(QueueManager queueManager) {
        this.queueManager = queueManager;
    }
    
    public void initialize() {
        mainFrame = new JFrame("Campus Queue Management System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);
        
        tabbedPane = new JTabbedPane();
        
        createCustomerPanel();
        createAdminPanel();
        
        tabbedPane.addTab("Customer View", customerPanel);
        tabbedPane.addTab("Admin View", adminPanel);
        
        mainFrame.add(tabbedPane);
        
        // Start the notification timer
        startNotificationTimer();
        
        // Start the queue update timer
        startQueueUpdateTimer();
        
        mainFrame.setVisible(true);
    }
    
    private void createCustomerPanel() {
        customerPanel = new JPanel(new BorderLayout());
        
        // Status panel showing current queue states
        JPanel statusPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        
        for (ServiceType type : ServiceType.values()) {
            JPanel queuePanel = createQueueStatusPanel(type);
            statusPanel.add(queuePanel);
        }
        
        // Form panel for joining queue
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createTitledBorder("Join Queue"));
        
        JPanel formFieldsPanel = new JPanel(new GridLayout(5, 2, 5, 10));
        
        formFieldsPanel.add(new JLabel("Name:"));
        nameField = new JTextField(20);
        formFieldsPanel.add(nameField);
        
        formFieldsPanel.add(new JLabel("Student ID:"));
        idField = new JTextField(20);
        formFieldsPanel.add(idField);
        
        formFieldsPanel.add(new JLabel("Phone Number:"));
        phoneField = new JTextField(20);
        formFieldsPanel.add(phoneField);
        
        formFieldsPanel.add(new JLabel("Service Type:"));
        serviceTypeComboBox = new JComboBox<>(ServiceType.values());
        formFieldsPanel.add(serviceTypeComboBox);
        
        JButton joinButton = new JButton("Join Queue");
        joinButton.addActionListener(e -> joinQueue());
        formFieldsPanel.add(new JLabel(""));
        formFieldsPanel.add(joinButton);
        
        formPanel.add(formFieldsPanel);
        
        // Ticket panel for showing ticket details
        ticketPanel = new JPanel();
        ticketPanel.setLayout(new BoxLayout(ticketPanel, BoxLayout.Y_AXIS));
        ticketPanel.setBorder(BorderFactory.createTitledBorder("Your Ticket"));
        ticketPanel.setVisible(false);
        
        JPanel ticketDetailsPanel = new JPanel(new GridLayout(4, 1));
        
        ticketNumberLabel = new JLabel("Ticket Number: ");
        positionLabel = new JLabel("Position in Queue: ");
        waitTimeLabel = new JLabel("Estimated Wait Time: ");
        JLabel notificationLabel = new JLabel("You will receive an SMS when your turn is approaching.");
        
        ticketDetailsPanel.add(ticketNumberLabel);
        ticketDetailsPanel.add(positionLabel);
        ticketDetailsPanel.add(waitTimeLabel);
        ticketDetailsPanel.add(notificationLabel);
        
        ticketPanel.add(ticketDetailsPanel);
        
        // Add components to main panel
        customerPanel.add(statusPanel, BorderLayout.NORTH);
        customerPanel.add(formPanel, BorderLayout.CENTER);
        customerPanel.add(ticketPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createQueueStatusPanel(ServiceType type) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(type.toString()));
        
        JLabel nowServingLabel = new JLabel("Now Serving: " + type.getPrefix() + "-0");
        JLabel waitingLabel = new JLabel("Waiting: 0 people");
        JLabel waitTimeLabel = new JLabel("Est. Wait: 0 mins");
        
        panel.add(nowServingLabel);
        panel.add(waitingLabel);
        panel.add(waitTimeLabel);
        
        // Store labels in a map for updating later
        JLabel[] labels = {nowServingLabel, waitingLabel, waitTimeLabel};
        queueStatusLabels.put(type, labels);
        
        return panel;
    }
    
    private void createAdminPanel() {
        adminPanel = new JPanel(new BorderLayout());
        
        JTabbedPane adminTabs = new JTabbedPane();
        
        for (ServiceType type : ServiceType.values()) {
            JPanel servicePanel = createServiceAdminPanel(type);
            adminTabs.addTab(type.toString(), servicePanel);
        }
        
        adminPanel.add(adminTabs, BorderLayout.CENTER);
    }
    
    private JPanel createServiceAdminPanel(ServiceType type) {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Table to show waiting customers
        String[] columnNames = {"Ticket #", "Name", "ID", "Phone", "Wait Time"};
        Object[][] data = {};
        
        JTable table = new JTable(new QueueTableModel(columnNames, data));
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Store reference to the table for updating
        switch (type) {
            case REGISTRAR:
                registrarTable = table;
                break;
            case ENROLLMENT:
                enrollmentTable = table;
                break;
            case CASHIER:
                cashierTable = table;
                break;
        }
        
        // Control panel
        JPanel controlPanel = new JPanel();
        
        JButton nextButton = new JButton("Serve Next");
        nextButton.addActionListener(e -> {
            queueManager.serveNext(type);
            updateQueueDisplays();
        });
        
        JLabel currentlyServingLabel = new JLabel("Currently Serving: None");
        
        controlPanel.add(nextButton);
        controlPanel.add(currentlyServingLabel);
        
        // Store reference to the label
        currentlyServingLabels.put(type, currentlyServingLabel);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void joinQueue() {
        String name = nameField.getText().trim();
        String id = idField.getText().trim();
        String phone = phoneField.getText().trim();
        ServiceType type = (ServiceType) serviceTypeComboBox.getSelectedItem();
        
        if (name.isEmpty() || id.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Ticket ticket = queueManager.generateTicket(name, id, phone, type);
        
        // Update and show ticket panel
        ticketNumberLabel.setText("Ticket Number: " + ticket.getTicketNumber());
        int position = queueManager.getTicketPosition(ticket.getTicketNumber());
        positionLabel.setText("Position in Queue: " + position);
        int waitTime = queueManager.getEstimatedWaitTime(type) / Math.max(1, position);
        waitTimeLabel.setText("Estimated Wait Time: ~" + waitTime + " minutes");
        
        ticketPanel.setVisible(true);
        
        // Clear form
        nameField.setText("");
        idField.setText("");
        phoneField.setText("");
        
        // Update queue displays
        updateQueueDisplays();
    }
    
    private void updateQueueDisplays() {
        // Update status labels
        for (ServiceType type : ServiceType.values()) {
            JLabel[] labels = queueStatusLabels.get(type);
            
            Ticket current = queueManager.getCurrentlyServing(type);
            if (current != null) {
                labels[0].setText("Now Serving: " + current.getTicketNumber());
            } else {
                labels[0].setText("Now Serving: None");
            }
            
            int waiting = queueManager.getQueueLength(type);
            labels[1].setText("Waiting: " + waiting + " people");
            
            int waitTime = queueManager.getEstimatedWaitTime(type);
            labels[2].setText("Est. Wait: ~" + waitTime + " mins");
            
            // Update admin "Currently Serving" labels
            JLabel currentLabel = currentlyServingLabels.get(type);
            if (current != null) {
                currentLabel.setText("Currently Serving: " + current.getTicketNumber() + " - " + current.getStudentName());
            } else {
                currentLabel.setText("Currently Serving: None");
            }
            
            // Update admin tables
            JTable table = null;
            switch (type) {
                case REGISTRAR:
                    table = registrarTable;
                    break;
                case ENROLLMENT:
                    table = enrollmentTable;
                    break;
                case CASHIER:
                    table = cashierTable;
                    break;
            }
            
            if (table != null) {
                QueueTableModel model = (QueueTableModel) table.getModel();
                List<Ticket> tickets = queueManager.getAllTicketsForService(type);
                model.setData(tickets);
            }
        }
        
        // If we have a visible ticket, update its position and wait time
        if (ticketPanel.isVisible()) {
            String ticketNumber = ticketNumberLabel.getText().replace("Ticket Number: ", "");
            int position = queueManager.getTicketPosition(ticketNumber);
            
            if (position > 0) {
                positionLabel.setText("Position in Queue: " + position);
                
                // Get service type from ticket number prefix
                ServiceType type = null;
                if (ticketNumber.startsWith("R-")) {
                    type = ServiceType.REGISTRAR;
                } else if (ticketNumber.startsWith("E-")) {
                    type = ServiceType.ENROLLMENT;
                } else if (ticketNumber.startsWith("C-")) {
                    type = ServiceType.CASHIER;
                }
                
                if (type != null) {
                    int totalWait = queueManager.getEstimatedWaitTime(type);
                    int waitPerPerson = totalWait / Math.max(1, queueManager.getQueueLength(type));
                    int personalWait = position * waitPerPerson;
                    waitTimeLabel.setText("Estimated Wait Time: ~" + personalWait + " minutes");
                }
            } else {
                // Ticket no longer in queue (being served or already served)
                ticketPanel.setVisible(false);
            }
        }
    }
    
    private void startNotificationTimer() {
        Timer timer = new Timer(30000, e -> {
            queueManager.notifyUpcomingTurn(3); // Notify when in position 3 or closer
        });
        timer.start();
    }
    
    private void startQueueUpdateTimer() {
        Timer timer = new Timer(5000, e -> {
            updateQueueDisplays();
        });
        timer.start();
    }
}