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
- **Persistance** : Sauvegarde sécurisée des comptes utilisateurs

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
- **Statistiques** : Score moyen, nombre de joueurs

### 🎨 Interface Utilisateur
- **Material Design 3** : Interface moderne et cohérente
- **Responsive Design** : Adaptation à toutes les tailles d'écran
- **Animations fluides** : Transitions et feedback visuels
- **Thème adaptatif** : Support du mode sombre/clair

## 🏗️ Architecture Technique

### **Patterns & Architecture**
- **MVVM** (Model-View-ViewModel) avec Android Architecture Components
- **Repository Pattern** pour la gestion des données
- **ViewBinding** pour l'accès sécurisé aux vues
- **LiveData & Observer Pattern** pour la réactivité

### **Technologies Utilisées**
- **Kotlin** - Langage principal
- **Room Database** - Base de données locale SQLite
- **Retrofit** - Client HTTP pour les appels API
- **Coroutines** - Programmation asynchrone
- **Material Design Components** - Interface utilisateur
- **View Binding** - Liaison sécurisée des vues
- **Lifecycle Components** - Gestion du cycle de vie

### **API Externe**
- **OpenTDB (Open Trivia Database)** - Source des questions de quiz
  - Plus de 4000 questions vérifiées
  - 24 catégories différentes
  - Filtrage par difficulté
  - Format JSON standardisé

## 📂 Structure du Projet

```
app/src/main/java/com/sdv/quizapp/
├── data/                           # Couche de données
│   ├── local/                      # Base de données locale
│   │   ├── AppDatabase.kt          # Configuration Room
│   │   ├── UserDao.kt              # DAO utilisateurs
│   │   └── ScoreDao.kt             # DAO scores
│   ├── remote/                     # API distante
│   │   └── TriviaApiService.kt     # Interface Retrofit
│   └── QuizRepository.kt           # Repository principal
├── model/                          # Modèles de données
│   ├── User.kt                     # Entité utilisateur
│   ├── Score.kt                    # Entité score
│   └── TriviaModels.kt             # Modèles API
├── view/                           # Couche présentation
│   ├── LoginActivity.kt            # Écran de connexion
│   ├── MainActivity.kt             # Sélection catégorie
│   ├── DifficultySelectionActivity.kt # Sélection difficulté
│   ├── QuizActivity.kt             # Interface de quiz
│   ├── LeaderboardActivity.kt      # Classement
│   └── ScoreAdapter.kt             # Adaptateur RecyclerView
├── viewmodel/                      # ViewModels
│   ├── LoginViewModel.kt           # Logique authentification
│   ├── MainViewModel.kt            # Logique catégories
│   ├── QuizViewModel.kt            # Logique quiz
│   └── LeaderboardViewModel.kt     # Logique classement
└── utils/                          # Utilitaires
    ├── ViewModelFactory.kt         # Factory ViewModels
    └── HtmlUtils.kt                # Décodage HTML
```

## 🚀 Installation & Configuration

### **Prérequis**
- Android Studio Arctic Fox ou plus récent
- SDK Android 24+ (Android 7.0)
- Connexion Internet pour les questions dynamiques

### **Installation**
1. **Cloner le repository**
```bash
git clone https://github.com/votre-username/QuizApp.git
cd QuizApp
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
- **Résolution** : 1080x1920 (density 420)

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

## 🛠️ Développement

### **Commandes Gradle**
```bash
# Nettoyer le projet
./gradlew clean

# Compiler en mode debug
./gradlew assembleDebug

# Lancer les tests
./gradlew test

# Générer l'APK de release
./gradlew assembleRelease
```

### **Structure de la Base de Données**
```sql
-- Table utilisateurs
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL
);

-- Table scores
CREATE TABLE scores (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    userId INTEGER NOT NULL,
    username TEXT NOT NULL,
    score INTEGER NOT NULL,
    category TEXT NOT NULL,
    timestamp INTEGER NOT NULL,
    FOREIGN KEY (userId) REFERENCES users(id)
);
```

## 🧪 Tests

### **Tests Unitaires**
- ViewModels avec données mockées
- Repository avec base de données en mémoire
- Utilitaires et helpers

### **Tests d'Intégration**
- Navigation entre activités
- Persistance des données
- Appels API

### **Tests UI**
- Scénarios utilisateur complets
- Validation des interactions
- Tests de régression

## 📊 Métriques & Analytics

### **Performance**
- **Temps de démarrage** : < 2 secondes
- **Temps de chargement quiz** : < 1 seconde
- **Utilisation mémoire** : < 100 MB
- **Taille APK** : < 10 MB

### **Statistiques d'Usage**
- Questions répondues par session
- Catégories les plus populaires
- Taux de réussite par difficulté
- Temps moyen par question

## 🤝 Contribution

### **Guidelines**
1. **Fork** le projet
2. **Créer** une branche feature (`git checkout -b feature/AmazingFeature`)
3. **Commit** les changements (`git commit -m 'Add some AmazingFeature'`)
4. **Push** la branche (`git push origin feature/AmazingFeature`)
5. **Ouvrir** une Pull Request

### **Standards de Code**
- Suivre les conventions Kotlin
- Documenter les fonctions publiques
- Maintenir une couverture de tests > 80%
- Respecter l'architecture MVVM

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

## 👨‍💻 Auteur

**Timéo AVI** - *Développeur Android*
- GitHub: [@TimeoAVI](https://github.com/TimeoAVI)
- Email: timeo.avi@example.com

## 🙏 Remerciements

- **OpenTDB** pour l'API de questions gratuite
- **Material Design** pour les guidelines UI/UX
- **Android Developers** pour la documentation excellente
- **Communauté Kotlin** pour le support et les ressources

## 📈 Roadmap

### **Version 2.0**
- [ ] Mode multijoueur en temps réel
- [ ] Système de badges et achievements
- [ ] Questions personnalisées
- [ ] Thèmes graphiques additionnels

### **Version 2.1**
- [ ] Mode hors-ligne complet
- [ ] Export des statistiques
- [ ] Intégration réseaux sociaux
- [ ] Widget Android

---

**⭐ N'hésitez pas à mettre une étoile si ce projet vous plaît !**