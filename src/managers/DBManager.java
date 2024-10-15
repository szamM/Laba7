package managers;

import data.Coordinates;
import data.Flat;
import data.House;
import data.View;
import system.Request;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DBManager {

    private static Connection conn;
    private static Map<String, Integer> userIdsByLogin = new HashMap<>();

    public static void connect() {
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/studs", "s395243", "GDHNj1y5Gf5ibJlY");
            System.out.println("Подключение к PostgreSQL успешно выполнено");
        } catch (SQLException e) {
            System.out.println("Ошибка в подключении к БД: " + e.getMessage());
            System.exit(-1);
        }
    }

    public static void ReadDB() throws SQLException {
        CollectionManager.clear();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM flats");

        while (resultSet.next()) {
            Flat flat = new Flat();
            flat.setId(resultSet.getLong("id"));
            flat.setOwnerId(resultSet.getInt("owner_id"));
            flat.setName(resultSet.getString("name"));

            Coordinates coordinates = new Coordinates(0D, 0L);
            coordinates.setX(resultSet.getDouble("coordinates_x"));
            coordinates.setY(resultSet.getLong("coordinates_y"));
            flat.setCoordinates(coordinates);

            flat.setArea(resultSet.getLong("area"));
            flat.setNumberOfRooms(resultSet.getInt("number_of_rooms"));
            flat.setLivingSpace(resultSet.getInt("living_space"));
            flat.setNumberOfBathrooms(resultSet.getLong("number_of_bathrooms"));
            flat.setView(View.valueOf(resultSet.getString("view")));

            House house = new House(null, 0, 0L, 0L);
            house.setName(resultSet.getString("house_name"));
            house.setYear(resultSet.getInt("house_year"));
            house.setNumberOfFloors(resultSet.getLong("house_number_of_floors"));
            house.setNumberOfFlatsOnFloor(resultSet.getLong("house_number_of_flats_on_floor"));
            flat.setHouse(house);

            CollectionManager.add(flat);
        }

    }


    public static int getUserId(String login) throws SQLException {
        if (userIdsByLogin.containsKey(login)) {
            return userIdsByLogin.get(login);
        }
        String SQL = "SELECT id FROM users WHERE login = ?";
        int userId = -1;
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, login);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getInt("id");
                    userIdsByLogin.put(login, userId);
                }
            }
        }
        return userId;
    }



    public static boolean canEditFlat(int humanId, int ownerId) throws SQLException {
        String SQL = "SELECT owner_id FROM flats WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, humanId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("owner_id");
                    return userId == ownerId;
                }
            }
        }
        return false;
    }

    public static String updateFlat(Request request) throws SQLException {
        Flat flat = request.getFlat();
        long id = Long.parseLong(request.getPassword());
        if (flat == null) {
            throw new NullPointerException("Flat is null");
        }
        if (canEditFlat(Math.toIntExact(id), flat.getOwnerId())) {
            throw new SecurityException("Пользователь не может редактировать эту квартиру");
        }
        if(!CollectionManager.isIdExist(id)) {
            return "нет квартиры с таким id";
        }else {
            String SQL = "UPDATE flats SET name = ?, coordinates_x = ?, coordinates_y = ?, area = ?, number_of_rooms = ?, living_space = ?, number_of_bathrooms = ?, view = ?, house_name = ?, house_year = ?, house_number_of_floors = ?, house_number_of_flats_on_floor = ? WHERE id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
                pstmt.setString(1, flat.getName());
                pstmt.setDouble(2, flat.getCoordinates().getX());
                pstmt.setLong(3, flat.getCoordinates().getY());
                pstmt.setLong(4, flat.getArea());
                pstmt.setInt(5, flat.getNumberOfRooms());
                pstmt.setInt(6, flat.getLivingSpace());
                pstmt.setLong(7, flat.getNumberOfBathrooms());
                pstmt.setString(8, flat.getView().toString());
                pstmt.setString(9, flat.getHouse().getName());
                pstmt.setInt(10, flat.getHouse().getYear());
                pstmt.setLong(11, flat.getHouse().getNumberOfFloors());
                pstmt.setLong(12, flat.getHouse().getNumberOfFlatsOnFloor());
                pstmt.setLong(13, id);
                pstmt.executeUpdate();
            }
            return "квартира обновлен";
        }
    }

    public static void addFlat(Flat flat, Request request) throws SQLException {
        String SQL = "INSERT INTO flats (id, name, coordinates_x, coordinates_y, creation_date, area, number_of_rooms, living_space, number_of_bathrooms, view, house_name, house_year, house_number_of_floors, house_number_of_flats_on_floor, owner_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, flat.getId());
            pstmt.setString(2, flat.getName());
            pstmt.setDouble(3, flat.getCoordinates().getX());
            pstmt.setLong(4, flat.getCoordinates().getY());
            pstmt.setString(5, flat.getCreationDate().toString());
            pstmt.setLong(6, flat.getArea());
            pstmt.setLong(7, flat.getNumberOfRooms());
            pstmt.setLong(8, flat.getLivingSpace());
            pstmt.setLong(9, flat.getNumberOfBathrooms());
            pstmt.setString(10, flat.getView().toString());
            pstmt.setString(11, flat.getHouse().getName());
            pstmt.setInt(12, flat.getHouse().getYear());
            pstmt.setLong(13, flat.getHouse().getNumberOfFloors());
            pstmt.setLong(14, flat.getHouse().getNumberOfFlatsOnFloor());
            pstmt.setLong(15, request.getUserId());
            pstmt.executeUpdate();

            ReadDB();
        }
    }

    public static void addUser(String username, String password) throws SQLException {
        String SQL = "INSERT INTO users(login, password) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
        }
    }


    public static String removeFlat(Request request) throws SQLException {
        int count = CollectionManager.getArrayDeque().size();
        String SQL = "DELETE FROM flats WHERE id = ? AND owner_id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {
            preparedStatement.setLong(1, Long.parseLong(request.getPassword()));
            preparedStatement.setInt(2, request.getUserId());
            preparedStatement.executeUpdate();
            CollectionManager.clear();
            ReadDB();
            if (count != CollectionManager.getArrayDeque().size()) return "Квартира удален";
            else return "Нет подходящих квартир или квартира с таким id принадлежит другому пользователю";
        } catch (Exception e){
            return "Возникла ошибка " + e.getMessage();
        }
    }

    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Подключение закрыто");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка закрытия подключения: " + e.getMessage());
        }
    }

    public static void clearUserCollection(Request request) throws SQLException {
        int id = request.getUserId();
        String SQL = "DELETE FROM flats WHERE owner_id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            CollectionManager.clear();
            ReadDB();
        }
    }


}
