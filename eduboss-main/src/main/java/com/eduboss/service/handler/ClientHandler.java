package com.eduboss.service.handler;

import org.springframework.stereotype.Component;

@Component
public interface ClientHandler {

    boolean updateStudentContact(String studentId, String contact);
    
}
