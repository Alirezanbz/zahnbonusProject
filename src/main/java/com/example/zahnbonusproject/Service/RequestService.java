package com.example.zahnbonusproject.Service;


import com.example.zahnbonusproject.Entity.Request;
import com.example.zahnbonusproject.Entity.User;
import com.example.zahnbonusproject.Repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class RequestService {

    private final String UPLOAD_DIR = "./uploads/";

    @Autowired
    private RequestRepository requestRepository;

    public Request createRequest(MultipartFile document, User user) {
        try {

            Files.createDirectories(Paths.get(UPLOAD_DIR));

            byte[] bytes = document.getBytes();
            Path path = Paths.get(UPLOAD_DIR + document.getOriginalFilename());
            Files.write(path, bytes);

            Request request = new Request();
            request.setPath(path.toString());
            request.setStatus("In Bearbeitung");
            request.setUser(user);

            return requestRepository.save(request);

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }


    public List<Request> findPendingRequests() {
        return requestRepository.findByStatus("In Bearbeitung");
    }

    public byte[] getDocumentByRequestId(Long requestId) {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null) {
            return null;
        }

        Path path = Paths.get(request.getPath());
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file.", e);
        }
    }
    public Request acceptRequest(Long requestId) {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null) {
            throw new RuntimeException("Failed: " + requestId);
        }
        request.setStatus("Ausgezahlt");
        return requestRepository.save(request);
    }

    public Request denyRequest(Long requestId) {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null) {
            throw new RuntimeException("Failed: " + requestId);
        }
        request.setStatus("Abgelehnt");
        return requestRepository.save(request);
    }

    public String getRequestStatus(Long requestId) {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null) {
            throw new RuntimeException("Failed: " + requestId);
        }
        return request.getStatus();
    }
}
