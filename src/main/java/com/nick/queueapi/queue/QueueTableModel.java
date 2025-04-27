/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nick.queueapi.queue;

/**
 *
 * @author User
 */
// QueueTableModel.java
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

class QueueTableModel extends AbstractTableModel {
    private String[] columnNames;
    private List<Object[]> data;
    
    public QueueTableModel(String[] columnNames, Object[][] data) {
        this.columnNames = columnNames;
        this.data = new ArrayList<>();
        for (Object[] row : data) {
            this.data.add(row);
        }
    }
    
    public void setData(List<Ticket> tickets) {
        data.clear();
        int position = 1;
        
        for (Ticket ticket : tickets) {
            Object[] row = new Object[5];
            row[0] = ticket.getTicketNumber();
            row[1] = ticket.getStudentName();
            row[2] = ticket.getStudentId();
            row[3] = ticket.getPhoneNumber();
            
            // Calculate wait time based on position
            ServiceType type = ticket.getServiceType();
            int waitTimePerPerson = 0;
            switch (type) {
                case REGISTRAR:
                    waitTimePerPerson = 5;
                    break;
                case ENROLLMENT:
                    waitTimePerPerson = 7;
                    break;
                case CASHIER:
                    waitTimePerPerson = 3;
                    break;
            }
            row[4] = position * waitTimePerPerson + " mins";
            
            data.add(row);
            position++;
        }
        
        fireTableDataChanged();
    }
    
    @Override
    public int getRowCount() {
        return data.size();
    }
    
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    @Override
    public Object getValueAt(int row, int column) {
        return data.get(row)[column];
    }
}
