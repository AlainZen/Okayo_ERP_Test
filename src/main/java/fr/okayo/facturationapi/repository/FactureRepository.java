package fr.okayo.facturationapi.repository;

import fr.okayo.facturationapi.model.Facture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactureRepository extends JpaRepository<Facture, Long> {
}
