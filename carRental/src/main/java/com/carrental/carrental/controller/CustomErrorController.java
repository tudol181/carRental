package com.carrental.carrental.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        // Get the HTTP error code
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");

        // Return different views for specific error codes
        if (statusCode != null) {
            if (statusCode == 404) {
                return "404";  // Redirect to 404.html
            } else if (statusCode == 500) {
                return "error"; // Redirect to error.html for server errors
            }
        }

        // Default error page
        return "error-page";
    }

    public String getErrorPath() {
        return "/error";
    }
}
