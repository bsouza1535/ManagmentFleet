package com.managment.fleet.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "maintenances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Maintenance {
    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(nullable = false, updatable = false)
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDate date;

    @ElementCollection(targetClass = AutoPart.class)
    @CollectionTable(name = "maintenance_parts", joinColumns = @JoinColumn(name = "maintenance_id"))
    @Column(name = "part_name")
    @Enumerated(EnumType.STRING)
    private List<AutoPart> partsAuto = new ArrayList<>();

    @Column(name = "auto_repair", nullable = true, updatable = true)
    private String autorepair;

    @Column(name = "price_of_maintanence", nullable = true, updatable = false)
    private Double priceOfMaintanence;

    @Column(name = "next_maintenance_mileage", nullable = false, updatable = true)
    private Double next_maintenance_mileage;

    @Column(name = "kilometers_of_maintanence", nullable = false, updatable = false)
    private Double KilometersOfMaintenance;

    //@Column(nullable = false, updatable = false)
    //private String vehicle_plate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, updatable = true)
    private MaintenanceType typeOfMaintenance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private MaintenanceStatus status = MaintenanceStatus.SCHEDULED;

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
        if (this.status == null) {
            this.status = MaintenanceStatus.SCHEDULED;
        }
    }

    public enum MaintenanceType {
        REVISAO, PREVENCAO, CORRECAO


    }

    public enum AutoPart {
        OLEO_MOTOR("Óleo do motor"),
        FILTRO_OLEO("Filtro de óleo"),
        FILTRO_AR("Filtro de ar"),
        FILTRO_COMBUSTIVEL("Filtro de combustível"),
        FILTRO_CABINE("Filtro de cabine"),
        VELAS_IGNICAO("Velas de ignição"),
        BATERIA("Bateria"),
        PNEUS("Pneus"),
        PASTILHAS_FREIO("Pastilhas de freio"),
        DISCOS_FREIO("Discos de freio"),
        SAPATAS_FREIO("Sapatas de freio"),
        FLUIDO_FREIO("Fluido de freio"),
        CORREIA_DENTADA("Correia dentada"),
        CORREIA_ALTERNADOR("Correia do alternador"),
        CORREIA_AUXILIAR("Correia auxiliar"),
        AMORTECEDORES("Amortecedores"),
        MOLAS_SUSPENSAO("Molas de suspensão"),
        PIVO_SUSPENSAO("Pivô de suspensão"),
        BARRA_DIRECAO("Barra de direção"),
        TERMINAL_DIRECAO("Terminal de direção"),
        COXIM_MOTOR("Coxim do motor"),
        RADIADOR("Radiador"),
        BOMBA_AGUA("Bomba d'água"),
        TERMOSTATO("Termostato"),
        LIQUIDO_ARREFECIMENTO("Líquido de arrefecimento"),
        EMBREAGEM("Embreagem"),
        PLATO_EMBREAGEM("Platô da embreagem"),
        ROLAMENTO_EMBREAGEM("Rolamento da embreagem"),
        LAMPADAS("Lâmpadas"),
        FAROIS("Faróis"),
        LANTERNAS("Lanternas"),
        ESCAPAMENTO("Escapamento"),
        CATALISADOR("Catalisador"),
        SENSOR_OXIGENIO("Sensor de oxigênio"),
        BOBINA_IGNICAO("Bobina de ignição"),
        ALTERNADOR("Alternador"),
        MOTOR_ARRANQUE("Motor de arranque"),
        JUNTA_CABECOTE("Junta do cabeçote"),
        RETENTORES("Retentores"),
        JUNTAS_DIVERSAS("Juntas diversas"),
        PARA_BRISA("Para-brisa"),
        VIDROS("Vidros"),
        RETROVISORES("Retrovisores"),
        MACANETAS("Maçanetas"),
        FECHADURAS("Fechaduras"),
        AC_COMPRESSOR("Ar condicionado - Compressor"),
        AC_CONDENSADOR("Ar condicionado - Condensador"),
        AC_EVAPORADOR("Ar condicionado - Evaporador"),
        AC_GAS("Ar condicionado - Gás refrigerante");

        private final String description;

        AutoPart(String description) {
            this.description = description;
        }

        @JsonValue
        public String getDescription() {
            return description;
        }

        @JsonCreator
        public static AutoPart fromString(String value) {
            for (AutoPart part : AutoPart.values()) {
                if (part.description.equalsIgnoreCase(value)) {
                    return part;
                }
            }
            throw new IllegalArgumentException("Peça inválida: " + value);
        }
    }
}
