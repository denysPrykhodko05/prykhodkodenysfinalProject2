//Дописать получение роли пологину и закончить получение юзера по логину и паролю
package Utils;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DBMananger {
    private static final String SQL_GET_USER_BY_LOGIN_PASSWORD = "SELECT * FROM users WHERE login=(?) and password=(?)";
    private static final String SQL_GET_ROLE_BY_ID = "SELECT role FROM role WHERE id=(?)";
    private static final String SQL_ADD_ADMIN = "INSERT INTO users(login, password, role) values('Denys', '1',1)";
    private static final String SQL_ADD_USER = "INSERT INTO users(login, password, role) VALUES (?,?,?)";
    private static final String SQL_GET_ID_BY_ROLE = "SELECT id FROM role WHERE role=(?)";
    private static final String SQL_GET_ALL_USERS = "SELECT * FROM users";


    private final static String login = "root";
    private final static String password = "";
    private final static String url = "jdbc:mysql://localhost:3306/mysql?useSSL=false";

    private static Connection connection;
    private static DBMananger instance;
    Statement statement;

    DBMananger(String login, String password, String url) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, login, password);
            statement = connection.prepareStatement("USE  app");
            ((PreparedStatement) statement).execute();
            addAmin();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static DBMananger getInstance() {
        if (instance == null) {
            instance = new DBMananger(login, password, url);

        }
        return instance;
    }

    public void addUser(String login, String password, String role, HttpServletRequest req, HttpServletResponse resp) {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            req.setAttribute("unLogin", true);
            req.setAttribute("unPassword", true);
            req.setAttribute("unRole", true);
            preparedStatement = connection.prepareStatement(SQL_ADD_USER);
            if (UserDAO.userLoginAndPasswordValidation(login)) {
                preparedStatement.setString(1, login);

            } else {
                req.setAttribute("error","Uncorrect login");
                req.setAttribute("login", login);
                req.setAttribute("unLogin", false);
                req.getRequestDispatcher("/WEB-INF/JSP_Pages/CreateUserError.jsp").forward(req, resp);

            }
            if (UserDAO.userLoginAndPasswordValidation(password)) {
                preparedStatement.setString(2, password);
            } else {
                req.setAttribute("error","Uncorrect password");
                req.setAttribute("password", password);
                req.setAttribute("unPassword", false);
                req.getRequestDispatcher("/WEB-INF/JSP_Pages/CreateUserError.jsp").forward(req, resp);
            }

            if (User.ROLE.ADMIN.equals(User.ROLE.valueOf(role)) || User.ROLE.USER.equals(User.ROLE.valueOf(role))){
                preparedStatement.setString(3, getIdByRole(role));
            }else {
                req.setAttribute("error","Choose role");
                req.setAttribute("unRole", false);
                req.getRequestDispatcher("/WEB-INF/JSP_Pages/CreateUserError.jsp").forward(req, resp);
            }
            try {
                if (preparedStatement.executeUpdate() == 1) {
                    req.getRequestDispatcher("/WEB-INF/JSP_Pages/Admin.jsp").forward(req, resp);
                }
            }catch (MySQLIntegrityConstraintViolationException e){
                req.setAttribute("error", "Change login");
                req.getRequestDispatcher("/WEB-INF/JSP_Pages/CreateUserError.jsp").forward(req, resp);
            }

        } catch (SQLException | ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        List<User> users = new CopyOnWriteArrayList<>();
        int id;
        String login;
        String password;
        User.ROLE role;
        try {
            preparedStatement = connection.prepareStatement(SQL_GET_ALL_USERS);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt(1);
                login = resultSet.getString(2);
                password = resultSet.getString(3);
                role = getRoleById(resultSet.getInt(4));
                if (!role.equals(User.ROLE.ADMIN)) {
                    users.add(new User(id, login, password, role));
                }
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User.ROLE getRoleById(int id) {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            preparedStatement = connection.prepareStatement(SQL_GET_ROLE_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return User.ROLE.valueOf(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return User.ROLE.UNKNOWN;
    }

    public String getIdByRole(String role) {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            preparedStatement = connection.prepareStatement(SQL_GET_ID_BY_ROLE);
            preparedStatement.setString(1, role);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addAmin() {
        try {
            statement = connection.prepareStatement(SQL_ADD_ADMIN);
            ((PreparedStatement) statement).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserByLoginAndPassword(String login, String password) {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            preparedStatement = connection.prepareStatement(SQL_GET_USER_BY_LOGIN_PASSWORD);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                String login_temp = resultSet.getString(2);
                String password_temp = resultSet.getString(3);
                int role = resultSet.getInt(4);
                preparedStatement = connection.prepareStatement(SQL_GET_ROLE_BY_ID);
                preparedStatement.setInt(1, role);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String role_temp = resultSet.getString(1);
                    return new User(id, login_temp, password_temp, User.ROLE.valueOf(role_temp));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
