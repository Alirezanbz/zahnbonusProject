package com.example.zahnbonusproject.Controller;

import com.example.zahnbonusproject.Entity.Request;
import com.example.zahnbonusproject.Entity.User;
import com.example.zahnbonusproject.Service.RequestService;
import com.example.zahnbonusproject.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class RequestController {
    private static final Logger logger = LoggerFactory.getLogger(RequestController.class);

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserService userService;

    @PostMapping("/upload")
    public ResponseEntity<?> submitRequest(@RequestParam("file") MultipartFile document,
                                           @RequestParam("email") String userEmail) {
        User currentUser = userService.findByEmail(userEmail);
        if (currentUser == null) {
            return ResponseEntity.badRequest().body("User not found!");
        }


        if (requestService.userHasExistingRequest(currentUser)) {
            return ResponseEntity.badRequest().body("You've already uploaded a document!");
        }

        Request request = requestService.createRequest(document, currentUser);
        return ResponseEntity.ok(request);
    }

    @GetMapping("/pending")
    public ResponseEntity<?> getPendingRequests() {

        List<Request> requests = requestService.findPendingRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadDocument(@RequestParam Long requestId) {
        byte[] documentData = requestService.getDocumentByRequestId(requestId);
        if (documentData == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("inline")
                .filename("document.pdf")
                .build());

        return new ResponseEntity<>(documentData, headers, HttpStatus.OK);
    }

    @PostMapping("/accept")
    public ResponseEntity<Request> acceptRequest(@RequestBody Long requestId) {
        Request updatedRequest = requestService.acceptRequest(requestId);
        return ResponseEntity.ok(updatedRequest);
    }


    @PostMapping("/deny")
    public ResponseEntity<Request> denyRequest(@RequestBody Long requestId) {
        Request updatedRequest = requestService.denyRequest(requestId);
        return ResponseEntity.ok(updatedRequest);
    }


    @GetMapping("/statusByEmail")
    public ResponseEntity<String> getStatusByEmail(@RequestParam String email) {
        logger.info("Received request for status by email: " + email);
        String status = requestService.getRequestStatusByEmail(email);
        return ResponseEntity.ok(status);
    }




    @GetMapping("/iban")
    public ResponseEntity<String> getIban(@RequestParam Long requestId){
        String iban = userService.getIban(requestId);
        return ResponseEntity.ok(iban);
    }

    @GetMapping("/age")
    public ResponseEntity<?> isUserOver16(@RequestParam("email") String userEmail) {
        User user = userService.findByEmail(userEmail);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found!");
        }

        int age = userService.calculateAge(user.getUserId());
        return ResponseEntity.ok().body(age);
    }



}
