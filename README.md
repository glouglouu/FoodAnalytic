# FoodAnalytic - Prototype Android

FoodAnalytic est une application Android moderne permettant d'analyser les produits alimentaires en scannant simplement leur code-barres. Ce prototype démontre l'intégration entre une base de données locale (Room), une API externe (Open Food Facts) et un système de scan performant.

## 🚀 Fonctionnalités Clés

- **Authentification Locale** : Système de création de compte et de connexion stocké en base de données locale (Room).
- **Scan de Code-barres** : Intégration du scanner Google (ML Kit) pour une détection rapide et sans configuration de permissions complexe.
- **Intégration Open Food Facts** : Récupération en temps réel des données nutritionnelles via l'API v2 d'Open Food Facts.
- **Détails Nutritifs Complets** : Affichage des calories, protéines, glucides, graisses, sel et sucres pour 100g.
- **Carrousel d'Images** : Visualisation des différentes photos du produit (face, ingrédients, nutrition, packaging) via Coil.
- **Historique Persistant** : Tous les produits scannés sont sauvegardés localement et consultables même hors ligne.

## 🛠 Technologies Utilisées

- **Langage** : Kotlin
- **UI** : Jetpack Compose (Material 3)
- **Base de données** : Room (SQLite)
- **Réseau** : Retrofit 2 & GSON
- **Chargement d'images** : Coil
- **Scan** : Google Code Scanner (Play Services)
- **Architecture** : MVVM (Model-View-ViewModel) simplifié pour le prototype

## 🔄 Workflow de l'Application

1. **Connexion/Inscription** : L'utilisateur s'identifie pour accéder à son espace.
2. **Scan** : L'utilisateur clique sur le bouton "Scanner". Le scanner Google s'ouvre.
3. **Récupération API** : Une fois le code EAN récupéré, l'app appelle `world.openfoodfacts.org`.
4. **Mapping & Stockage** : Les données JSON complexes sont transformées en une entité `Product` simple et enregistrées dans Room.
5. **Affichage** : La liste d'accueil se met à jour instantanément via un `Flow`. Un clic sur un produit ouvre l'écran de détails avec le carrousel d'images.

## 📦 Installation

1. Cloner le projet.
2. Ouvrir avec **Android Studio Ladybug** (ou version supérieure).
3. Synchroniser les fichiers **Gradle**.
4. Lancer sur un appareil physique ou un émulateur disposant du **Google Play Store**.

---
*Développé comme prototype d'analyse nutritionnelle moderne.*
