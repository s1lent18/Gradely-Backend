package com.example.Gradely.intercepter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class RegistrationTimeInterceptor implements HandlerInterceptor {

    @Value("${course.registration.start}")
    private String registrationStartTimeStr;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        LocalDateTime registrationStartTime = LocalDateTime.parse(registrationStartTimeStr);

        if (LocalDateTime.now().isBefore(registrationStartTime)) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("Course registration is not open yet.");
            return false;
        }
        return true;
    }
}