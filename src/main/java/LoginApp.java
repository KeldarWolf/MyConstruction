import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class LoginApp {
    public static void main(String[] args) {
        // Verificar la conexión a la base de datos
        if (!DatabaseConnector.testConnection()) {
            System.out.println("No se pudo conectar a la base de datos. Intente más tarde.");
            return; // Termina el programa si no se puede conectar
        } else {
            System.out.println("Conexión con la base de datos exitosa.");
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("Bienvenido al sistema de login.");
        System.out.print("¿Desea ingresar o registrarse? (ingresar/registrarse): ");
        String option = scanner.nextLine().toLowerCase();

        if ("registrarse".equals(option)) {
            // Registro de nuevo usuario
            System.out.print("Ingrese su email: ");
            String email = scanner.nextLine();

            System.out.print("Ingrese su contraseña: ");
            String password = scanner.nextLine();

            boolean isInserted = DatabaseConnector.insertUser(email, password);

            if (isInserted) {
                System.out.println("Registro exitoso. Ahora puede iniciar sesión.");
            } else {
                System.out.println("Hubo un error en el registro. Intente nuevamente.");
            }
        } else if ("ingresar".equals(option)) {
            // Login de usuario
            System.out.print("Ingrese su email: ");
            String email = scanner.nextLine();

            System.out.print("Ingrese su contraseña: ");
            String password = scanner.nextLine();

            boolean isValid = DatabaseConnector.validateUser(email, password);

            if (isValid) {
                System.out.println("Login exitoso. ¡Bienvenido!");
            } else {
                System.out.println("Credenciales incorrectas. Intente nuevamente.");
            }
        } else {
            System.out.println("Opción no válida. El programa terminará.");
        }

        scanner.close();
    }
}


