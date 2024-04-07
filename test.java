import java.sql.*;
import java.util.Objects;
import java.util.Scanner;

public class test {
    private static final String URL = "jdbc:mysql://localhost:3306/cinema";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM film");



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
            Scanner sc = new Scanner(System.in);
            boolean idValide = false;
            int choixFilm =6;

            do{

                System.out.println("Veuillez choisir l'ID du film que vous souhaitez regarder : ");

                // Pour vérifier si l'entrée de l'utilisateur est un entier et si l'ID de film est valide
                if (sc.hasNextInt()) {
                    choixFilm = sc.nextInt();
                    sc.nextLine();
                    if (choixFilm <=7) {

                        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM film WHERE FILM_ID = ?");
                        preparedStatement.setInt(1, choixFilm);
                        ResultSet filmResultSet = preparedStatement.executeQuery();


                        if (filmResultSet.next()) {
                            idValide = true; // Mettre idValide à true car l'ID est valide
                            String titre = filmResultSet.getString("Titre");
                            Time duree = filmResultSet.getTime("Durée_film");
                            String type = filmResultSet.getString("type");
                            String description = filmResultSet.getString("Description");
                            System.out.println("Titre du film  : " + titre);
                            System.out.println("Durée du film  : " + duree);
                            System.out.println("Type du film  : " + type);
                            System.out.println("Description du film  : " + description);

                            // Afficher les séances disponibles pour ce film
                            PreparedStatement seanceStatement = connection.prepareStatement("SELECT Séance_ID,Film_ID,Salle_ID,Durée_seance,Heure,Jour_Semaine FROM séance WHERE Film_ID = ?");
                            seanceStatement.setInt(1, choixFilm);
                            ResultSet seanceResultSet = seanceStatement.executeQuery();

                            //
                            System.out.println("Séances disponibles pour ce film :");
                            System.out.println("+-----------+------------+------------+-----------+-----------------+--------------------+-------------------+");
                            System.out.println("|   Salle   | Type Salle | ID du Film |   Heure   | ID de la séance | Durée de la séance | Jour de la séance |");
                            System.out.println("+-----------+------------+------------+-----------+-----------------+--------------------+-------------------+");

                            while (seanceResultSet.next()) {
                                int seanceID = seanceResultSet.getInt("Séance_ID");
                                int Film_ID = seanceResultSet.getInt("Film_ID");
                                int Salle_ID = seanceResultSet.getInt("Salle_ID");
                                Time Heure = seanceResultSet.getTime("Heure");
                                Time dureSeance = seanceResultSet.getTime("Durée_seance");
                                String Jour_Semaine = seanceResultSet.getString("Jour_Semaine");
                                String Type_Salle = null;

                                PreparedStatement salleStatement = connection.prepareStatement("SELECT Type_Salle FROM salle WHERE numero_salle = ?");
                                salleStatement.setInt(1, Salle_ID);
                                ResultSet salleResultSet = salleStatement.executeQuery();

                                // Vérifier si un résultat est retourné
                                while (salleResultSet.next()) {
                                    Type_Salle = salleResultSet.getString("Type_Salle");
                                }

                                System.out.printf("| Salle %-3s | %-10s |\t   %-5s  | %-9s |\t\t  %-9s |\t   %-13s |\t\t %-11s |\n", Salle_ID, Type_Salle, Film_ID, Heure, seanceID, dureSeance, Jour_Semaine );
                            }
                            System.out.println("+-----------+------------+------------+-----------+-----------------+--------------------+-------------------+");

                        }else {
                            while(choixFilm <=1 || choixFilm>=7) {
                                System.out.println("ID de film invalide. Veuillez saisir un ID entre 1 et 7.");
                                if (sc.hasNextInt()) {
                                    choixFilm = sc.nextInt();
                                    sc.next();
                                    if (choixFilm >=1 || choixFilm<=7)
                                        idValide = true;
                                }
                            }
                            sc.next();
                        }
                    }


                }else{
                    idValide = false;
                    while(idValide == false) {
                        System.out.println("ID de film invalide. Veuillez saisir un entier.");
                        if (sc.hasNextInt()) {
                            choixFilm = sc.nextInt();
                            sc.next();
                            idValide = true;
                        }
                        sc.next();
                    }
                }

                String answer;
                int nombreBillets;

                System.out.println("Voulez vous acheter un Billet ? (O/N)");
                answer = sc.next();
                sc.nextLine();
                if(Objects.equals(answer, "O") || Objects.equals(answer, "o")){
                    System.out.println("Très bien, Combien de billets souhaitez vous acheter ?");
                    nombreBillets = sc.nextInt();
                    sc.nextLine();
                }else{
                    System.out.println("FIN");
                }

            }while (idValide == true);

            resultSet.close(); // Fermer le ResultSet après utilisation
            statement.close(); // Fermer le Statement après utilisation
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
            connection = test.getConnection();


            // Ajoutez ici les opérations pour le film choisi

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            test.closeConnection(connection);
        }
    }
}