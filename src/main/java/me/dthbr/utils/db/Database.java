package me.dthbr.utils.db;

import com.google.common.collect.Lists;
import me.dthbr.utils.db.provider.ConnectionProvider;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// SQL Database interface
public class Database {

    private final ConnectionProvider provider;

    public Database(ConnectionProvider connectionProvider) {
        this.provider = connectionProvider;
    }

    public void createTableIfNeeded() {
        String query = "CREATE TABLE IF NOT EXISTS chests (" +
                "who                VARCHAR(36) NOT NULL  PRIMARY KEY," +
                "crystal            TEXT," +
                "void               TEXTa," +
                "dragon             TEXT" +
                ");";
        update(query);
    }

    public ParsedData findFirst(String query, Object... args) {
        List<ParsedData> data = find(query, args);

        if (data.isEmpty())
            return null;

        return data.get(0);
    }

    public List<ParsedData> find(String query, Object... args) {
        Connection connection = provider.connection();

        if (connection == null)
            return null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < args.length; i++) {
                statement.setObject(i + 1, args[i]);
            }
            List<ParsedData> parsedDataList = Lists.newArrayList();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ParsedData parsedData = new ParsedData();

                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int numberOfColumns = resultSetMetaData.getColumnCount();

                for (int i = 1; i < numberOfColumns + 1; i++) {
                    String name = resultSetMetaData.getColumnLabel(i);
                    parsedData.put(name, resultSet.getObject(name));
                }
                parsedDataList.add(parsedData);
            }

            resultSet.close();
            return parsedDataList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int update(String query, Object... args) {
        Connection connection = provider.connection();

        if (connection == null)
            return -1;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < args.length; i++)
                statement.setObject(i + 1, args[i]);
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static class ParsedData {

        private final Map<String, Object> values = new HashMap<>();

        public void put(String value, Object object) {
            values.put(value, object);
        }

        public <T> T get(String key) {
            return (T) values.get(key);
        }

    }

}
