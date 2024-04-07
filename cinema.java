package cinema;
import java.sql.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat;

public class cinema {
    private static final String URL = "jdbc:mysql://localhost:3306/cinema";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);





//
//


            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
            throw new SQLException("Erreur lors de la connexion à la base de données", e);
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connexion à la base de données fermée avec succès.");
            } catch (SQLException e) {
                System.out.println("Erreur lors de la fermeture de la connexion à la base de données : " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = cinema.getConnection();

            //Appel à la méthode pour afficher le menu
            Menu.afficherMenu(connection);
           //Appel à la méthode pour afficher les séances disponibles pour le film choisi
            seance.AfficherSeance(connection);

            // Appel à la méthode pour reserver un Billet
            reserver.reserverBillet(connection);


            // Ajoutez ici les opérations pour le film choisi

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cinema.closeConnection(connection);
        }
    }
}

//
//
//    // Méthode pour vérifier si l'entrée de l'utilisateur est un entier
//