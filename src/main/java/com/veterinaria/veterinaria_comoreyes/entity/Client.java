package com.veterinaria.veterinaria_comoreyes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Length;
import org.hibernate.annotations.Filter;

import java.time.LocalDate;
import java.util.Date;

@Entity

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Filter(name = "statusActive", condition = "status = :status")
public class Client extends EntityWithStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long clientId;

    @Column(length = 8,unique = true, nullable = false)
    private String dni;

    @Column(length = 60)
    private String name;

    @Column(length = 60)
    private String lastName;

    @Column(length = 255)
    private String address;

    @Column(unique = true, length = 9)
    private String phone;

    private LocalDate birthDate;

    @Column(length = 255)
    private String dirImage;

    @ManyToOne
    @JoinColumn(name = "id_headquarter")
    private Headquarter headquarter;

    @OneToOne
    @JoinColumn(name = "id_user")
    private User user;

    @Column(length = 100)
    private String blockNote;

    private LocalDate createDate;

}
