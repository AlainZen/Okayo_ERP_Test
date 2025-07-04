package fr.okayo.facturationapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Prestation { // Représente une prestation

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, scale = 2)
    private BigDecimal prixUnitaireHT;

    @Column(nullable = false)
    private BigDecimal tauxTVA; // 0.20 = 20 % pour la rapidité

    private LocalDate dateDebutValidite;

    private LocalDate dateFinValidite;
}
