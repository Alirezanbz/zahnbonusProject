package com.example.zahnbonusproject.Repository;

import com.example.zahnbonusproject.Entity.Request;
import com.example.zahnbonusproject.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByStatus(String status);

    Request findTopByUserOrderByCreationDateDesc(User user);


}
