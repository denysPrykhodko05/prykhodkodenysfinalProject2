package servlets.servlet;

import Utils.DBMananger;
import Utils.UserDAO;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class CreateUserPageServlet extends HttpServlet {
    private DBMananger dbMananger;
    List<User> users;
    @Override
    public void init() throws ServletException {
        dbMananger=DBMananger.getInstance();
        users=dbMananger.getAllUsers();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("users",users);
        req.getRequestDispatcher("/WEB-INF/JSP_Pages/AdminChangeUser.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String role = req.getParameter("role");
        dbMananger.addUser(login,password,role,req,resp);
        doGet(req,resp);
    }

    @Override
    public void destroy() {

        DBMananger.closeConnection();
    }
}



