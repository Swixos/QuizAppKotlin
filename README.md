# 🧠 QuizApp - Application de Quiz Interactive

Une application Android moderne de quiz développée en Kotlin, utilisant l'architecture MVVM et l'API OpenTDB pour des questions dynamiques.

## 📱 Aperçu

QuizApp est une application de quiz complète qui permet aux utilisateurs de :
- Se connecter ou s'inscrire avec un système d'authentification
- Choisir parmi différentes catégories de questions
- Sélectionner le niveau de difficulté (Facile, Moyen, Difficile, Mixte)
- Participer à des quiz de 10 questions avec un système de scoring
- Consulter un classement global des meilleurs scores

## ✨ Fonctionnalités

### 🔐 Authentification
- **Connexion/Inscription** : Système d'authentification local avec base de données Room
- **Validation** : Vérification des champs et gestion des erreurs

### 🎯 Système de Quiz
- **Catégories variées** : Culture générale, Sciences, Sports, Histoire, etc.
- **4 niveaux de difficulté** : 
  - 🟢 **Facile** : Questions simples
  - 🟡 **Moyenne** : Un bon défi
  - 🔴 **Difficile** : Pour les experts
  - 🎲 **Mixte** : Toutes difficultés mélangées
- **Questions dynamiques** : Récupération en temps réel depuis l'API OpenTDB
- **Interface intuitive** : Cartes interactives avec feedback visuel
- **Scoring en temps réel** : Suivi des bonnes et mauvaises réponses

### 🏆 Classement
- **Podium** : Affichage des 3 meilleurs scores
- **Classement complet** : Liste de tous les participants
- **Filtrage** : Par catégorie et utilisateur

## 🚀 Installation & Configuration

### **Prérequis**
- Android Studio Arctic Fox ou plus récent
- SDK Android 24+ (Android 7.0)
- Connexion Internet pour les questions dynamiques

### **Installation**
1. **Cloner le repository**
```bash
git clone https://github.com/Swixos/QuizAppKotlin.git
cd QuizAppKotlin
```

2. **Ouvrir dans Android Studio**
```bash
# Ouvrir Android Studio et sélectionner le dossier du projet
```

3. **Synchroniser le projet**
```bash
# File → Sync Project with Gradle Files
```

4. **Lancer l'application**
```bash
# Sélectionner un émulateur ou un appareil physique
# Cliquer sur Run ▶️
```

### **Configuration de l'émulateur**
- **API Level recommandé** : 33 ou 34
- **RAM** : 2048 MB minimum
- **Stockage** : 4 GB minimum

## 📱 Utilisation

### **1. Premier lancement**
- Créer un compte avec username/mot de passe
- Ou se connecter avec un compte existant

### **2. Sélection du quiz**
- Choisir une catégorie dans le menu déroulant
- Cliquer sur "Commencer le Quiz"
- Sélectionner la difficulté souhaitée

### **3. Participation au quiz**
- Répondre aux 10 questions en cliquant sur les cartes
- Voir le feedback immédiat (vert = correct, rouge = incorrect)
- Suivre la progression avec la barre en haut

### **4. Consultation des résultats**
- Voir son score final
- Consulter le classement global
- Comparer avec les autres joueurs

## 🎯 Fonctionnalités Avancées

### **Gestion des Erreurs**
- **Connexion réseau** : Fallback gracieux en cas de problème API
- **Validation des données** : Vérification côté client et serveur
- **Messages d'erreur** : Feedback utilisateur clair et actionnable

### **Performance**
- **Cache intelligent** : Réduction des appels API répétitifs
- **Chargement asynchrone** : Interface réactive pendant les opérations
- **Optimisation mémoire** : Gestion efficace des ressources

### **Sécurité**
- **Stockage local sécurisé** : Chiffrement des données sensibles
- **Validation des entrées** : Protection contre les injections
- **Gestion des sessions** : Timeout automatique
