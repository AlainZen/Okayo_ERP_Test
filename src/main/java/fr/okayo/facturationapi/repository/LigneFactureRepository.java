package fr.okayo.facturationapi.repository;

import fr.okayo.facturationapi.model.Facture;
import fr.okayo.facturationapi.model.LigneFacture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LigneFactureRepository extends JpaRepository<LigneFacture, Long> {
    List<LigneFacture> findByFacture(Facture facture);
}
