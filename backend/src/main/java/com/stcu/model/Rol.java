package com.stcu.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="roles")
@Getter @Setter
public class Rol {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(length=20)
    private ERole rol;

    public Rol() {
    }

    public Rol( ERole rol ) {
        this.rol = rol;
    }
}
