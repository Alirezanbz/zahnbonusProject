package com.example.zahnbonusproject.Service;

import com.example.zahnbonusproject.Entity.User;
import com.example.zahnbonusproject.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }
    public User registerUser(User user) {
        user.setRole("kunde");
        return userRepository.save(user);
    }

    public String getIban(Long userId){
        return userRepository.findById(userId)
                .map(User::getIban)
                .orElse(null);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public int calculateAge(Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null || user.getDob() == null) {
            throw new IllegalArgumentException("User or date of birth not found");
        }

        LocalDate dob = ((java.sql.Date) user.getDob()).toLocalDate();
        LocalDate currentDate = LocalDate.now();

        return Period.between(dob, currentDate).getYears();
    }



}
