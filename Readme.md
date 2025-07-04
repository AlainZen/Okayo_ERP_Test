# 💼 Facturation API – Okayo

Une API REST développée en **Spring Boot** pour gérer la facturation d'un panel de prestations proposées par la société fictive **Okayo**.

Ce projet répond à l'exercice technique suivant : créer un modèle de données et une API permettant de gérer les clients, les prestations, les factures, et d'assurer que les montants (HT, TTC) restent figés dans le temps, indépendamment des évolutions futures du catalogue ou de la TVA.

---

## ✨ Fonctionnalités

- 🔹 **Gestion des clients** (Créer, Lire, Modifier, Supprimer)
- 🔹 **Création de factures** avec plusieurs lignes (désignations)
- 🔹 **Calcul automatique** du total HT et TTC (TVA variable ligne par ligne)
- 🔹 **Consultation, modification et suppression** des factures
- 🔹 **Swagger** pour documenter et tester facilement l'API
- 🔹 Données persistées en **base H2 in-memory**

---

## 🧱 Modèle de données

### `Client`
- `id` (auto)
- `codeClient` (unique, ex : CU2025-0001)
- `nom`
- `adresse`
- `email`

### `Facture`
- `id`
- `reference` (ex : 2025-0001)
- `dateFacturation`
- `client` (lien vers l'objet client)
- `totalHT` (somme HT des lignes)
- `totalTTC` (somme TTC des lignes)
- `lignes` (liste des prestations facturées)

### `LigneFacture`
- `id`
- `nom` de la prestation (copié depuis un catalogue éventuel)
- `quantite`
- `prixUnitaireHT`
- `tauxTVA` (ex : 0.20)
- ❗ **figé à la date de création de la facture**

---

## 🧪 Exemple d'appel API

### ➕ POST `/api/clients`

```json
{
  "codeClient": "CU2025-0001",
  "nom": "Alain Sliman",
  "adresse": "90 Av du Président Pompidou",
  "email": "Alain.sliman@gmail.com"
}
```

### ➕ POST `/api/factures`
```json
{
  "reference": "2025-0001",
  "clientId": 1,
  "lignes": [
    {
      "nom": "Création de site vitrine",
      "quantite": 2,
      "prixUnitaireHT": 1200.00,
      "tauxTVA": 0.20
    },
    {
      "nom": "Maintenance annuelle",
      "quantite": 1,
      "prixUnitaireHT": 1200.00,
      "tauxTVA": 0.20
    }
  ]
}
```

#### ✅ Calcul automatique :

Total HT : 2×1200 + 1×1200 = 3600

Total TTC : 3600 × 1.20 = 4320.00

### 🔁 PUT `/api/factures/{id}`
Permet de modifier une facture (client et lignes). Le calcul HT/TTC est automatiquement mis à jour.

### ❌ DELETE `/api/factures/{id}`
Supprime une facture ainsi que ses lignes.

---

## 🚀 Lancer le projet

Depuis le terminal :
```bash
./mvnw spring-boot:run
```

---

## 🌐 Swagger

#### 📍 Accès :
http://localhost:8080/swagger-ui/index.html

---

## 📦 Tech Stack

- Java 17
- Spring Boot 3
- Spring Data JPA
- H2 (in-memory)
- Lombok
- Maven
- Swagger / SpringDoc OpenAPI

---

## 🧠 Hypothèses métiers

- Le catalogue de prestations peut évoluer, mais les prestations d'une facture sont figées à la date de facturation.
- Chaque ligne de facture copie les infos du catalogue (nom, prix, TVA), ce qui permet de garantir que la facture reste fidèle dans le temps.
- La TVA est définie au niveau de chaque prestation pour gérer des taux différents.

---

## 💡 Idées d'améliorations

- Ajouter une vraie entité Prestation (catalogue)
- Historique de TVA par période
- Génération de PDF (En cours de production)
- Sécurité : authentification JWT
- Interface frontend (En cours de production)

---

## 📁 Structure du projet

```bash
src/
├── controller/          ← REST Controllers
├── model/               ← Entités JPA
├── repository/          ← Interfaces JPA
├── dto/                 ← Objets de transfert (factures)
├── FacturationApiApplication.java
```

---

## 🙌 Auteur

Projet réalisé par Alain Sliman, dans le cadre d'un test technique pour OKAYO