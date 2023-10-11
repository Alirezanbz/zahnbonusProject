package com.example.zahnbonusproject.Entity;
import jakarta.persistence.*;
import java.util.Date;
import lombok.*;

@Entity
@Table(name = "request")
@Getter
@Setter
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "status")
    private String status;

    @Column(name = "creation_date", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate = new Date();

}
