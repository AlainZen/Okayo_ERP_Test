package fr.okayo.facturationapi.controller;

import fr.okayo.facturationapi.model.*;
import fr.okayo.facturationapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/factures")
@RequiredArgsConstructor
public class FactureController {

    private final FactureRepository factureRepository;
    private final ClientRepository clientRepository;
    private final PrestationRepository prestationRepository;
    private final LigneFactureRepository ligneFactureRepository;

    @PostMapping
    public ResponseEntity<Facture> creerFacture(@RequestBody FactureRequest request) {
        Client client = clientRepository.findById(request.getClientId()).orElseThrow();

        List<LigneFacture> lignes = new ArrayList<>();
        BigDecimal totalHT = BigDecimal.ZERO;
        BigDecimal totalTTC = BigDecimal.ZERO;

        for (FactureRequest.Ligne ligne : request.getLignes()) {
            Prestation prestation = prestationRepository.findById(ligne.getPrestationId()).orElseThrow();

            BigDecimal prixHT = prestation.getPrixUnitaireHT().multiply(BigDecimal.valueOf(ligne.getQuantite()));
            BigDecimal prixTTC = prixHT.add(prixHT.multiply(prestation.getTauxTVA().divide(BigDecimal.valueOf(100))));

            LigneFacture lf = LigneFacture.builder()
                    .nom(prestation.getNom())
                    .quantite(ligne.getQuantite())
                    .prixUnitaireHT(prestation.getPrixUnitaireHT())
                    .tauxTVA(prestation.getTauxTVA())
                    .facture(null) // sera set après
                    .build();

            lignes.add(lf);
            totalHT = totalHT.add(prixHT);
            totalTTC = totalTTC.add(prixTTC);
        }

        Facture facture = Facture.builder()
                .reference(request.getReference())
                .dateFacturation(LocalDate.now())
                .client(client)
                .totalHT(totalHT)
                .totalTTC(totalTTC)
                .build();

        facture = factureRepository.save(facture);

        for (LigneFacture lf : lignes) {
            lf.setFacture(facture);
            ligneFactureRepository.save(lf);
        }

        facture.setLignes(ligneFactureRepository.findByFacture(facture));
        return ResponseEntity.ok(facture);
    }

    @GetMapping
    public List<Facture> getAll() {
        List<Facture> factures = factureRepository.findAll();
        for (Facture f : factures) {
            f.setLignes(ligneFactureRepository.findByFacture(f));
        }
        return factures;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Facture> getOne(@PathVariable Long id) {
        return factureRepository.findById(id)
                .map(facture -> {
                    facture.setLignes(ligneFactureRepository.findByFacture(facture));
                    return ResponseEntity.ok(facture);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Facture> update(@PathVariable Long id, @RequestBody Facture updatedFacture) {
        return factureRepository.findById(id)
                .map(facture -> {
                    facture.setReference(updatedFacture.getReference());
                    facture.setDateFacturation(updatedFacture.getDateFacturation());
                    facture.setClient(updatedFacture.getClient());
                    facture.setTotalHT(updatedFacture.getTotalHT());
                    facture.setTotalTTC(updatedFacture.getTotalTTC());
                    return ResponseEntity.ok(factureRepository.save(facture));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return factureRepository.findById(id)
                .map(facture -> {
                    factureRepository.delete(facture);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
