package managers;

import system.Request;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserManager {
    private static Connection cnn;

    public static Boolean isExist(String username) throws SQLException {

        cnn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/studs", "s395243", "GDHNj1y5Gf5ibJlY");
        PreparedStatement pstmt = cnn.prepareStatement("SELECT * FROM users WHERE login = ?");
        pstmt.setString(1, username);


        return pstmt.executeQuery().next();

    }

    public static String createUser(Request request) throws NoSuchAlgorithmException, SQLException {
        String password = request.getPassword();
        String username = request.getLogin();

        MessageDigest md = MessageDigest.getInstance("MD2");
        byte[] hashedPassword = md.digest(password.getBytes());

        StringBuilder sb = new StringBuilder();

        for (byte b : hashedPassword) {
            sb.append(String.format("%02x", b));
        }


        if (UserManager.isExist(request.getLogin())) {
            return "пользователь с таким именем уже существует";
        } else {
            DBManager.addUser(username, sb.toString());
            return "Пользователь создан";
        }

    }
    public static Boolean isAthCorrect(String username, String password) throws SQLException, NoSuchAlgorithmException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/studs", "s395243", "GDHNj1y5Gf5ibJlY");
        PreparedStatement pstmt = connection.prepareStatement("SELECT login, password FROM users WHERE login = ? AND password = ?");
        pstmt.setString(1, username);

        MessageDigest md = MessageDigest.getInstance("MD2");
        byte[] hashedPassword = md.digest(password.getBytes());

        StringBuilder sb = new StringBuilder();

        for (byte b : hashedPassword) {
            sb.append(String.format("%02x", b));
        }

        pstmt.setString(2, sb.toString());

        return pstmt.executeQuery().next();

    }

}