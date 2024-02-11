package pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.DbUtil;

import java.sql.*;
import java.util.Arrays;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class UserDao {
    public static final String CREATE_USER =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";

    public static final String UPDATE_USER =
            "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";

    public static final String SHOW_USER =
            "SELECT * FROM users WHERE id = ?";

    public static final String DELETE_USER =
            "DELETE FROM users WHERE id = ?";

    public static final String ALL_USERS =
            "SELECT * FROM users";

    public String HASH_PASSWORD(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public User create(User user) throws SQLException {
        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(CREATE_USER, RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, HASH_PASSWORD(user.getPassword()));
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            return user;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public User read(int id) throws SQLException {
        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SHOW_USER);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(User user) throws SQLException {
        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_USER);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, this.HASH_PASSWORD(user.getPassword()));
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    public void delete(int id) throws SQLException {
        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DELETE_USER);
            statement.setInt(1, id);
            statement.executeUpdate();


        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    private User[] addToArray(User u, User[] users) {
        User[] tmpUsers = Arrays.copyOf(users, users.length + 1);
        tmpUsers[users.length] = u;
        return tmpUsers;
    }


    public User[] allUsers() {
        try (Connection connection = DbUtil.getConnection()) {
            User[] users = new User[0];
            PreparedStatement preparedStatement = connection.prepareStatement(ALL_USERS);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("ID"));
                user.setUserName(rs.getString("USERNAME"));
                user.setEmail(rs.getString("EMAIL"));
                user.setPassword(rs.getString("PASSWORD"));
                users = addToArray(user, users);
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

