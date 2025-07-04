package fr.okayo.facturationapi.repository;

import fr.okayo.facturationapi.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
