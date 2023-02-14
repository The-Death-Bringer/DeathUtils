package me.dthbr.utils.db.provider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLProvider implements ConnectionProvider {

    private final MySQLSettings settings;
    private Connection connection;

    public MySQLProvider(MySQLSettings settings) {
        this.settings = settings;
    }

    @Override
    public Connection connection() {
        try {
            if (connection != null && !connection.isClosed())
                return connection;

            Class.forName("com.mysql.jdbc.Driver");
            return connection = DriverManager.getConnection(settings.asConnectionString(), settings.username(), settings.password());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public record MySQLSettings(String host, int port, String database, String username, String password) {

        public String asConnectionString() {
            return "jdbc:mysql://" + host + ":" + port + "/" + database;
        }

    }

}
