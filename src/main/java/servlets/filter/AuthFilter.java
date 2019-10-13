package servlets.filter;

import Utils.DBMananger;
import model.User;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static java.util.Objects.nonNull;

public class AuthFilter implements Filter {
    DBMananger dbMananger;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
         dbMananger=DBMananger.getInstance();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) servletRequest;
        final HttpServletResponse resp= (HttpServletResponse) servletResponse;

        final String login = req.getParameter("login");
        final String password = req.getParameter("password");

        final HttpSession session = req.getSession();
        if (nonNull(session)&&nonNull(session.getAttribute("login"))&&nonNull(session.getAttribute("password"))){
            final User.ROLE role =(User.ROLE) session.getAttribute("role");
            moveToMenu(req,resp,role);
        }else if(nonNull(login)&& nonNull(password) && dbMananger.getUserByLoginAndPassword(login,password)!=null){
            User user= dbMananger.getUserByLoginAndPassword(login,password);
            session.setAttribute("login",user.getLogin());
            session.setAttribute("password",user.getPassword());
            session.setAttribute("role",user.getRole());
            moveToMenu(req,resp,user.getRole());
        }else{
            moveToMenu(req,resp, User.ROLE.UNKNOWN);
        }
    }

    private void moveToMenu(HttpServletRequest req, HttpServletResponse resp, User.ROLE role) throws ServletException, IOException {
        if(role.equals(User.ROLE.ADMIN)){

            req.getRequestDispatcher("/WEB-INF/JSP_Pages/Admin.jsp").forward(req,resp);
        }else if(role.equals(User.ROLE.USER)){

            req.getRequestDispatcher("/WEB-INF/JSP_Pages/User.jsp").forward(req,resp);
        }else {

            req.getRequestDispatcher("/WEB-INF/JSP_Pages/Login.jsp").forward(req,resp);
        }
    }

    @Override
    public void destroy() {
        DBMananger.closeConnection();
    }
}
