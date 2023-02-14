package me.dthbr.utils.db.provider;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider {

    Connection connection();

    default void close() {
        try {
            if (connection() != null && !connection().isClosed())
                connection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
