package dataBase;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;

public class DataBase {

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost/profiles?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String user = "root", pass = "";
        return DriverManager.getConnection(url, user, pass);
    }

    public static void printUsers() throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        String query = "select * from user";
        assert connection != null;
        var statement = connection.prepareStatement(query);
        ResultSet r = statement.executeQuery();
        System.out.printf("%s\t%-15s%-35s%-10s%-10s\n", "ID", "NAME", "EMAIL", "PASS", "GENDER");

        while (r.next()) {
            int id = r.getInt("id");
            String name = r.getString("name");
            String email = r.getString("email");
            String pass = r.getString("pass");
            boolean gender = r.getBoolean("gender");
            System.out.printf("%s\t%-15s%-35s%-10s%-10s\n", id, name, email, pass, gender);
        }
    }

    public void deleteUsers(int id) throws SQLException, ClassNotFoundException {
        PreparedStatement ps = getConnection().prepareStatement("DELETE FROM user " + "WHERE id = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    public boolean getUser(String name, String pass) throws SQLException, ClassNotFoundException {
        PreparedStatement ps;
        ResultSet resultSet;
        String query = "SELECT * FROM `user` WHERE `name` =? AND `pass` =?";
        ps = getConnection().prepareStatement(query);
        ps.setString(1, name);
        ps.setString(2, pass);
        resultSet = ps.executeQuery();

        return resultSet.next();
    }

    public void addProfilePicture(FileInputStream imagePath, int id) throws SQLException, ClassNotFoundException {
        PreparedStatement ps;
        String query = "SELECT * FROM `user` WHERE `id` =?";
        ps = getConnection().prepareStatement(query);
        ps.setInt(1, id);
        ps.setBinaryStream(5, imagePath);
    }

    public static boolean searchUsername(String name, String email) throws SQLException, ClassNotFoundException {
        ObservableList<Profile> profiles = getProfiles();
        for (Profile user : profiles) {
            if (user.getName().equals(name) || user.getEmail().equals(email)){
                return true;
            }
        }
        return false;
    }

    public static ObservableList<Profile> getProfiles() throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        String query = "select * from user";
        assert connection != null;
        var statement = connection.prepareStatement(query);
        ResultSet r = statement.executeQuery();

        ObservableList<Profile> profiles = FXCollections.observableArrayList();
        while (r.next()) {
            int id = r.getInt("id");
            String name = r.getString("name");
            String email = r.getString("email");
            String pass = r.getString("pass");
            boolean gender = r.getBoolean("gender");
            boolean admin = r.getBoolean("admin");
            profiles.add(new Profile(id, name, email, pass, gender, admin));
        }
        return profiles;
    }

    public boolean addUser(String name, String email, String pass, boolean gender) throws SQLException, ClassNotFoundException, IOException {
        if (searchUsername(name, email)) {
            return true;
        } else {
            PreparedStatement ps;
            //INSERT INTO `user`(`id`, `name`, `email`, `pass`, `gender`, `admin`) VALUES (?,?,?,?,?,?)
            ps = getConnection().prepareStatement("INSERT INTO `user`(`name`, `email`, `pass`, `gender`, `admin`) VALUES (?,?,?,?,?)");

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, pass);
            ps.setBoolean(4, gender);
            ps.setBoolean(5, false);
            ps.executeUpdate();
            return false;
        }
    }
}

    /**/
