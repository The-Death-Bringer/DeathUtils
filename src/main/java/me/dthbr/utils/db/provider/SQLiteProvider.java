package me.dthbr.utils.db.provider;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteProvider implements ConnectionProvider {

    private Connection connection;
    private final File dbFile;

    public SQLiteProvider(File dbFile) {
        this.dbFile = dbFile;
    }

    @Override
    public Connection connection() {
        try {
            if (connection != null && !connection.isClosed())
                return connection;

            Class.forName("org.sqlite.JDBC");
            return connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


}
