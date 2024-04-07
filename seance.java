package cinema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class seance {

    public static void AfficherSeance(Connection connection) throws SQLException {
        Scanner sc = new Scanner(System.in);
        boolean idValide = false;
        System.out.println("Veuillez choisir l'ID du film que vous souhaitez regarder : ");
        while (!idValide) {
            int choixFilm;
            if (sc.hasNextInt()) {
                choixFilm = sc.nextInt();
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM film WHERE FILM_ID = ?");
                preparedStatement.setInt(1, choixFilm);
                ResultSet filmResultSet = preparedStatement.executeQuery();
                if (filmResultSet.next()) {
                    idValide = true;
                    String titre = filmResultSet.getString("Titre");
                    // Assurez-vous d'importer java.sql.Time pour utiliser Time
                    java.sql.Time duree = filmResultSet.getTime("Durée_film");
                    String type = filmResultSet.getString("type");
                    String description = filmResultSet.getString("Description");
                    System.out.println("Titre du film  : " + titre);
                    System.out.println("Durée du film  : " + duree);
                    System.out.println("Type du film  : " + type);
                    System.out.println("Description du film  : " + description);
                    PreparedStatement seanceStatement = connection.prepareStatement("SELECT Séance_ID,Film_ID,Salle_ID,Durée_seance,Heure,Jour_Semaine FROM séance WHERE Film_ID = ?");
                    seanceStatement.setInt(1, choixFilm);
                    ResultSet seanceResultSet = seanceStatement.executeQuery();
                    System.out.println("Séances disponibles pour ce film :");
                    System.out.println("+-----------+------------+------------+-----------------+-----------------------+");
                    System.out.println("|   Salle   | Type Salle |  Heure     | ID de la séance | Jour de la séance     |");
                    System.out.println("+-----------+------------+------------+-----------------+-----------------------+");
                    while (seanceResultSet.next()) {
                        int seanceID = seanceResultSet.getInt("Séance_ID");
                        int Salle_ID = seanceResultSet.getInt("Salle_ID");
                        java.sql.Time Heure = seanceResultSet.getTime("Heure");
                        String Jour_Semaine = seanceResultSet.getString("Jour_Semaine");
                        String Type_Salle = null;
                        PreparedStatement salleStatement = connection.prepareStatement("SELECT Type_Salle FROM salle WHERE numero_salle = ?");
                        salleStatement.setInt(1, Salle_ID);
                        ResultSet salleResultSet = salleStatement.executeQuery();
                        while (salleResultSet.next()) {
                            Type_Salle = salleResultSet.getString("Type_Salle");
                        }
                        System.out.printf("| Salle %-3s | %-10s | %-10s |\t\t  %-9s |\t\t %-14s |\n", Salle_ID, Type_Salle, Heure, seanceID, Jour_Semaine);
                    }
                    System.out.println("+-----------+------------+------------+-----------------+-----------------------+");
                    seanceResultSet.close();
                    seanceStatement.close();
                } else {
                    System.out.println("ID de film invalide. Veuillez choisir un ID valide.");
                }
                filmResultSet.close();
                preparedStatement.close();
            } else {
                System.out.println("ID de film invalide. Veuillez choisir un ID valide.");
                sc.next();
            }
        }
    }
}
