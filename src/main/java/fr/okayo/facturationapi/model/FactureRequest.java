package fr.okayo.facturationapi.model;

import lombok.Data;
import java.util.List;

@Data
public class FactureRequest {
    private Long clientId;
    private List<Ligne> lignes;

    @Data
    public static class Ligne {
        private Long prestationId;
        private int quantite;
    }
}
