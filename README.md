# Jeu d'aventure

Jeu d'aventure en ligne de commande dans lequel le joueur explore plusieurs salles,
résout des énigmes, collecte des objets et affronte éventuellement des monstres.
L'objectif est de découvrir le trésor caché dans le coffre-fort.

## Prérequis

- Java Development Kit (JDK) 17 ou supérieur ;
- Apache Maven 3.8 ou supérieur.

Vérifier l'installation :

```bash
java -version
mvn -version
```

## Installation et lancement

Depuis la racine du projet :

```bash
mvn clean package
java -cp target/classes fr.jeu.aventure.App
```

La première commande compile le projet et exécute les tests. La seconde démarre
le jeu dans le terminal.

Pour lancer uniquement les tests :

```bash
mvn test
```

## Commandes disponibles

Pendant la partie, les commandes suivantes sont disponibles :

| Commande | Action |
| --- | --- |
| `observer` | Observer la salle actuelle |
| `ramasser <objet>` | Ramasser un objet |
| `parler <pnj>` | Parler à un personnage |
| `enigme <réponse>` | Répondre à une énigme |
| `combattre` | Affronter un monstre |
| `avancer` | Aller à la salle suivante |
| `coffre` | Se rendre devant le coffre-fort |
| `ouvrir <phrase>` | Tenter d'ouvrir le coffre-fort |
| `inventaire` | Afficher son inventaire |
| `quitter` | Quitter la partie |

Les commandes ne sont pas sensibles à la casse. Les arguments (`<objet>`,
`<pnj>`, `<réponse>` et `<phrase>`) doivent être remplacés par la valeur
souhaitée.

## Structure du projet

```text
src/
├── main/java/fr/jeu/aventure/
│   ├── App.java                    # Point d'entrée
│   ├── controller/                 # Gestion des actions du joueur
│   ├── model/                     # Règles et état du jeu
│   └── view/                      # Affichage dans le terminal
└── test/java/                     # Tests unitaires
```
