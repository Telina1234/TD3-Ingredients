// java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.io.IOException;

public class DBConnection {
    private static final String HOST = System.getenv().getOrDefault("DB_HOST", "localhost");
    private static final String PORT = System.getenv().getOrDefault("DB_PORT", "5432");
    private static final String DB   = System.getenv().getOrDefault("DB_NAME", "postgres");
    private static final String USER = System.getenv().getOrDefault("DB_USER", "postgres");
    private static final String PASS = System.getenv().getOrDefault("DB_PASS", "postgres");

    public Connection getConnection() {
        String url = String.format("jdbc:postgresql://%s:%s/%s", HOST, PORT, DB);

        // Vérification rapide que l'hôte:port répond avant d'essayer JDBC
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(HOST, Integer.parseInt(PORT)), 2000);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Impossible de joindre " + HOST + ":" + PORT + " — démarrer PostgreSQL ou vérifier le mappage de ports (Docker) / pare-feu.",
                    e
            );
        }

        try {
            return DriverManager.getConnection(url, USER, PASS);
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Impossible de se connecter à PostgreSQL: " + url +
                            " (utilisateur=" + USER + "). Vérifier que le serveur est démarré, les identifiants et les variables d'environnement DB_*.",
                    e
            );
        }
    }

    public void closeConnection(Connection conn) {
        if (conn != null) {
            try { conn.close(); } catch (SQLException ignored) {}
        }
    }
}
