package com.tokobuku.nitnot.controller;

import com.tokobuku.nitnot.dto.EmployeeRequest;
import com.tokobuku.nitnot.model.Role;
import com.tokobuku.nitnot.model.User;
import com.tokobuku.nitnot.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hrd/api/employees")
public class HrdApiController {

    private final UserService userService;

    public HrdApiController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> addEmployee(@RequestBody EmployeeRequest request) {
        try {
            User newUser = userService.addEmployee(request.getUsername(), request.getPassword(), request.getRole());
            return ResponseEntity.ok(newUser);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/accept/{userId}")
    public ResponseEntity<?> acceptUser(@PathVariable Long userId) {
        return userService.acceptUser(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/reject/{userId}")
    public ResponseEntity<?> rejectUser(@PathVariable Long userId) {
        userService.rejectUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/cut/{userId}")
    public ResponseEntity<?> cutEmployee(@PathVariable Long userId) {
        userService.cutEmployee(userId);
        return ResponseEntity.noContent().build();
    }

    // Helper class for error response
    static class ErrorResponse {
        private String message;
        public ErrorResponse(String message) { this.message = message; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
