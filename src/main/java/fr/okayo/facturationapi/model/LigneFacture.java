package fr.okayo.facturationapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneFacture { // Représente une ligne de facture

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private int quantite;
    private BigDecimal prixUnitaireHT;
    private BigDecimal tauxTVA;

    @ManyToOne
    @JsonBackReference
    private Facture facture;
}
