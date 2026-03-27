# Sandtris

## À propos du projet

Sandtris est un jeu de type Tetris revisité, développé en Java avec la bibliothèque JavaFX. Le jeu intègre des mécaniques de jeu classiques ainsi que des fonctionnalités additionnelles comme un classement des meilleurs scores et une ambiance sonore.

Ce projet a été réalisé dans le cadre du cours PRO3600 (Projet de Programmation par Objet) à Télécom SudParis.

## Fonctionnalités

*   Système de jeu Tetris classique
*   Rotation et déplacement des pièces
*   Gestion des lignes complétées et augmentation du score
*   Affichage de la pièce suivante
*   Menu principal, écran de pause et de fin de partie
*   Classement des meilleurs scores (`leaderboard`)
*   Effets sonores et musique d'ambiance

## Captures d'écran

*(Vous pouvez ajouter ici des captures d'écran du jeu)*

## Prérequis

*   Java (version 11 ou supérieure)
*   Apache Maven

## Comment compiler et lancer le jeu

1.  Clonez le dépôt ou téléchargez les sources.
2.  Ouvrez un terminal et naviguez jusqu'au répertoire du projet.
3.  Placez-vous dans le dossier `dev/` :
    ```sh
    cd dev
    ```
4.  Compilez le projet avec Maven :
    ```sh
    mvn install
    ```
5.  Lancez l'application :
    ```sh
    mvn javafx:run
    ```

## Structure du projet

Le projet est un projet Maven standard. Le code source se trouve dans `dev/src/main/java/`.

*   `controls/`: Contient la logique du jeu (contrôleur principal, gestion des pièces).
*   `models/`: Définit les objets du jeu (blocs, formes, zone de jeu).
*   `views/`: Contient les contrôleurs pour les différentes vues FXML (menus, écran de jeu, etc.).
*   `resources/`: Contient les fichiers FXML pour l'interface, les images, les polices et les sons.

## Équipe

Ce projet a été réalisé par un groupe d'étudiants de Télécom SudParis.
Deleporte Hugo
Prugnat Aurélien
Benois Loup
Jean-Marie Matisse 
Moi même
