package cinema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Menu {
    public static void afficherMenu(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM film");
        ResultSet resultSet = statement.executeQuery();

        System.out.println("BIENVENUE SUR NOTRE APPLICATION DE RESERVATION DE BILLET DE CINEMA EN LIGNE");
        System.out.println("Vous trouverez ci-après le menu des films disponibles pour cette semaine");
        System.out.println("+------+-------------------------------+--------------");
        System.out.println("| Num  |        FILM                   |    Durée     | ");
        System.out.println("+------+-------------------------------+---------------");

        while (resultSet.next()) {
            int num = resultSet.getInt(1);
            String film = resultSet.getString(2);
            String duree = resultSet.getString(3);
            System.out.printf("| %-4d | %-30s| %-12s |\n", num, film, duree);
        }

        System.out.println("+------+-------------------------------+--------------+\n");

        resultSet.close();
        statement.close();
    }
}
