import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/myconstruction";
    private static final String USER = "root"; // Cambia por tu usuario
    private static final String PASSWORD = "0023"; // Cambia por tu contraseña

    static {
        try {
            // Cargar el driver JDBC de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("MySQL JDBC Driver no encontrado.", e);
        }
    }

    // Método para probar la conexión
    public static boolean testConnection() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            return true; // Si la conexión es exitosa
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Si ocurre un error, la conexión falla
        }
    }

    // Método para validar las credenciales del usuario
    public static boolean validateUser(String email, String password) {
        if (!testConnection()) { // Verifica la conexión antes de proceder
            System.out.println("Error: No se pudo conectar a la base de datos.");
            return false; // Si no hay conexión, se detiene la ejecución
        }

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return true; // Si hay un resultado, las credenciales son válidas
            } else {
                System.out.println("No se encontraron coincidencias.");
                return false; // Si no hay coincidencia, las credenciales son incorrectas
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Si ocurre un error en la consulta, devuelve falso
        }
    }

    // Método para insertar un nuevo usuario en la base de datos
    public static boolean insertUser(String email, String password) {
        if (!testConnection()) { // Verifica la conexión antes de proceder
            System.out.println("Error: No se pudo conectar a la base de datos.");
            return false; // Si no hay conexión, se detiene la ejecución
        }

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "INSERT INTO users (email, password) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Si la inserción es exitosa, se devuelve true
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Si ocurre un error, la inserción falla
        }
    }
}

