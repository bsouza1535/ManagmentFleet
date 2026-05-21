package com.managment.fleet.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "maintenance_autoparts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartsAutoMaintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToOne
    @JoinColumn(name = "maintenance_id")
    private Maintenance manutencao;
}
