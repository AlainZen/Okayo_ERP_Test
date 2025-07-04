package fr.okayo.facturationapi.model;

import lombok.Data;

import java.util.List;

@Data
public class FactureRequest { // Représente la requête pour créer une facture
    private String reference;
    private Long clientId;
    private List<Ligne> lignes;

    @Data
    public static class Ligne { // Représente une ligne de facture
        private Long prestationId;
        private int quantite;
    }
}
