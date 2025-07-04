package fr.okayo.facturationapi.repository;

import fr.okayo.facturationapi.model.Prestation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestationRepository extends JpaRepository<Prestation, Long> {
}
