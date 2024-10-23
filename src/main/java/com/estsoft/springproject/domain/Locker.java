package com.estsoft.springproject.domain;

import jakarta.persistence.*;

@Entity
public class Locker {
    @Id
    @GeneratedValue
    @Column(name = "LOCKER_ID")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "locker")
    private Members members;
}
