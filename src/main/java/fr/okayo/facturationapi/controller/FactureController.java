package fr.okayo.facturationapi.controller;

import fr.okayo.facturationapi.model.*;
import fr.okayo.facturationapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/factures")
public class FactureController {

    @Autowired
    private FactureRepository factureRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PrestationRepository prestationRepository;

    @Autowired
    private LigneFactureRepository ligneFactureRepository;

    @PostMapping
    public ResponseEntity<Facture> creerFacture(@RequestBody FactureRequest request) {
        Client client = clientRepository.findById(request.getClientId()).orElseThrow();

        List<LigneFacture> lignes = new ArrayList<>();
        BigDecimal totalHT = BigDecimal.ZERO;
        BigDecimal totalTVA = BigDecimal.ZERO;

        for (FactureRequest.Ligne ligne : request.getLignes()) {
            Prestation prestation = prestationRepository.findById(ligne.getPrestationId()).orElseThrow();

            BigDecimal montantHT = prestation.getPrixUnitaireHT().multiply(BigDecimal.valueOf(ligne.getQuantite()));
            BigDecimal montantTVA = montantHT.multiply(prestation.getTauxTVA());
            BigDecimal montantTTC = montantHT.add(montantTVA);

            LigneFacture lf = LigneFacture.builder()
                    .designation(prestation.getNom())
                    .quantite(ligne.getQuantite())
                    .prixUnitaireHT(prestation.getPrixUnitaireHT())
                    .montantHT(montantHT)
                    .montantTVA(montantTVA)
                    .montantTTC(montantTTC)
                    .build();

            lignes.add(lf);
            totalHT = totalHT.add(montantHT);
            totalTVA = totalTVA.add(montantTVA);
        }

        Facture facture = Facture.builder()
                .reference("FACT-" + System.currentTimeMillis())
                .dateFacturation(LocalDate.now())
                .client(client)
                .totalHT(totalHT)
                .totalTTC(totalHT.add(totalTVA))
                .lignes(new ArrayList<>())
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
    public List<Facture> getAllFactures() {
        return factureRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Facture> getFactureById(@PathVariable Long id) {
        return factureRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
