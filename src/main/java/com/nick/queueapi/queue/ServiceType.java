/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nick.queueapi.queue;
/**
 *
 * @author User
 */
// ServiceType.java
public enum ServiceType {
    REGISTRAR("R"),
    ENROLLMENT("E"),
    CASHIER("C");
    
    private final String prefix;
    
    ServiceType(String prefix) {
        this.prefix = prefix;
    }
    
    public String getPrefix() {
        return prefix;
    }
}