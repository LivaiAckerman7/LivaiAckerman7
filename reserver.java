package cinema;

import java.sql.*;
import java.util.Scanner;
import java.util.Objects;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class reserver {

    public static void reserverBillet(Connection connection) throws SQLException {
        String answer;
        Scanner sc = new Scanner(System.in);
        System.out.println("Souhaitez vous réserver pour ce film ? (O/N)");
        answer = sc.next();
        sc.nextLine();
        if (Objects.equals(answer, "O") || Objects.equals(answer, "o")) {
            System.out.println("Veuillez saisir votre numéro de téléphone");
            int Num_tel = sc.nextInt();
            int nombreBillets;

            PreparedStatement userStatement = connection.prepareStatement("SELECT Nom,Prénom FROM user where Num_Téléphone=?");
            userStatement.setInt(1, Num_tel);
            ResultSet userResultSet = userStatement.executeQuery();
            String Nom = null;
            String Prenom = null;
            if (userResultSet.next()) {
                Nom = userResultSet.getString("Nom");
                Prenom = userResultSet.getString("Prénom");
                System.out.println("Bienvenue " + Nom + " " + Prenom);

            } else {

                System.out.println("Votre numéro de téléphone ne se trouve pas dans notre base de donnée voulez vous créer un compte ?");
                String secondAnswer = sc.next();
                if (Objects.equals(secondAnswer, "O") || Objects.equals(secondAnswer, "o")) {
                    creerCompte(connection, Nom, Prenom, Num_tel);
                }
            }
            System.out.println("Très bien, Combien de billets souhaitez vous acheter ?");
            nombreBillets = saisirEntier();

            System.out.println("Veuillez présenter cette facture pour profiter de votre film");
            obtenirBillet(nombreBillets, Nom, Prenom, Num_tel);

        } else {
            System.out.println("Fin des opérations");
        }
    }



    public static int saisirEntier() {
        Scanner scanner = new Scanner(System.in);
        int entier = 0;
        boolean saisieCorrecte = false;

        // Tant que l'utilisateur n'a pas saisi un entier valide dans la plage spécifiée, continuer à lui demander de saisir un entier
        while (!saisieCorrecte) {
            System.out.println("NB : Vous ne pouvez acheter au plus 5 billets");

            System.out.print("Veuillez saisir un entier entre 1 et 5 : ");
            if (scanner.hasNextInt()) {
                entier = scanner.nextInt();
                if (entier >= 1 && entier <= 5) { // Correction ici: utiliser && au lieu de ||
                    saisieCorrecte = true; // Si l'entier est dans la plage spécifiée, mettre saisieCorrecte à true
                } else {
                    System.out.println("Erreur : Veuillez saisir un entier valide entre 1 et 5.");
                }
            } else {
                System.out.println("Erreur : Veuillez saisir un entier valide.");
                scanner.next(); // Vider le buffer du scanner
            }
        }
        return entier;
    }


    public static void creerCompte(Connection connection, String Nom, String Prenom, int num_Telephone) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Veuillez saisir votre Nom :");
        Nom = sc.next();
        System.out.println("Veuillez saisir votre Prénom :");
        Prenom = sc.next();
        System.out.println("Veuillez saisir votre numéro de téléphone ");
        num_Telephone = sc.nextInt();

        PreparedStatement newUserStatement = connection.prepareStatement("INSERT INTO user (Nom, Prénom,Num_Téléphone) VALUES (?, ?, ?)");
        newUserStatement.setString(1, Nom);
        newUserStatement.setString(2, Prenom);
        newUserStatement.setInt(3, num_Telephone);
        newUserStatement.executeUpdate();
    }

    public static void obtenirBillet(int nombreBillet, String nomClient, String prenomClient, int numTelephone) {

        // Imprimer le cadre de la facture
        System.out.println("+---------------------------------------------------------+");
        System.out.println("|                      FACTURE                              |");
        System.out.println("+---------------------------------------------------------+");
        System.out.println("| Date: " + getDateDuJour() + "                                       |");
        System.out.println("| Client: " + prenomClient + " " + nomClient + "                                        |");
        System.out.println("| Téléphone: " + numTelephone + "                                       |");
        System.out.println("| Adresse: DSTI2C ESP, Dakar, Senegal                   |");
        System.out.println("+---------------------------------------------------------+");
        System.out.println("| Produit             | Quantité | Prix unitaire | Total    |");
        System.out.println("+---------------------+----------+---------------+----------+");
        System.out.println("| Billet             |    " + nombreBillet + "     |    cfa1500.00     |  " + 1500 * nombreBillet + "  |");
        System.out.println("+---------------------+----------+---------------+----------+");



    }

    // Méthode pour obtenir la date du jour au format "yyyy-MM-dd"
    public static String getDateDuJour() {
        // Obtenir la date actuelle
        Date date = new Date();
        LocalTime heureActuelle = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Formatage de l'heure actuelle selon le modèle spécifié
        String heureFormatee = heureActuelle.format(formatter);


        // Définir le format de date souhaité
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd à "+heureFormatee);

        // Formater la date au format spécifié et Retourner la date formatée
        return dateFormat.format(date);
    }



}
