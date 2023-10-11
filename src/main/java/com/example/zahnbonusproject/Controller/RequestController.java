package com.example.zahnbonusproject.Controller;

import com.example.zahnbonusproject.Entity.Request;
import com.example.zahnbonusproject.Entity.User;
import com.example.zahnbonusproject.Service.RequestService;
import com.example.zahnbonusproject.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class RequestController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserService userService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitRequest(@RequestParam MultipartFile document,
                                           @RequestParam String userEmail) {
        User currentUser = userService.findByEmail(userEmail);
        if (currentUser == null) {
            return ResponseEntity.badRequest().body("User not found!");
        }

        Request request = requestService.createRequest(document, currentUser);
        return ResponseEntity.ok(request);
    }

    @GetMapping("/pending")
    public ResponseEntity<?> getPendingRequests() {

        List<Request> requests = requestService.findPendingRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/download/{requestId}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long requestId) {
        byte[] documentData = requestService.getDocumentByRequestId(requestId);
        if (documentData == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("document.pdf")
                .build());

        return new ResponseEntity<>(documentData, headers, HttpStatus.OK);
    }

    @PostMapping("/accept/{requestId}")
    public ResponseEntity<Request> acceptRequest(@PathVariable Long requestId) {
        Request updatedRequest = requestService.acceptRequest(requestId);
        return ResponseEntity.ok(updatedRequest);
    }


    @PostMapping("/deny/{requestId}")
    public ResponseEntity<Request> denyRequest(@PathVariable Long requestId) {
        Request updatedRequest = requestService.denyRequest(requestId);
        return ResponseEntity.ok(updatedRequest);
    }


    @GetMapping("/status/{requestId}")
    public ResponseEntity<String> getStatus(@PathVariable Long requestId) {
        String status = requestService.getRequestStatus(requestId);
        return ResponseEntity.ok(status);
    }

}
