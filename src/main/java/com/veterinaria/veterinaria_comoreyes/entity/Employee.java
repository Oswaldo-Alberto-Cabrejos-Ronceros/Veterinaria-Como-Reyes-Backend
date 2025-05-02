package com.veterinaria.veterinaria_comoreyes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @Column(length = 8,unique = true, nullable = false)
    private String dni;

    @Column(length = 5)
    private String cmvp;

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

    private String mainRole;

    @ManyToOne
    @JoinColumn(name = "id_headquarter")
    private Headquarter headquarter;

    @OneToOne
    @JoinColumn(name = "id_user")
    private User user;

    @Column(nullable = false, columnDefinition = "NUMBER(1,0)")
    private Byte status;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "employee_role", joinColumns = @JoinColumn(name = "id_employee", referencedColumnName = "employeeId"),
            inverseJoinColumns = @JoinColumn(name = "id_role",referencedColumnName = "roleId")
    )
    private List<Role> roles;

}
