package ads_tracking.controller;

import ads_tracking.DAO.OracleDAO.DAOFactory;
import ads_tracking.DAO.UserDAO;
import ads_tracking.Entity.User;
import ads_tracking.Exception.DAOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private DAOFactory daoFactory;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLoginForm() {
        return "login";
    }

//    @RequestMapping(value = "/register", method = RequestMethod.GET)
//    public String showRegisterForm() {
//        return "register";
//    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            session.removeAttribute("user");
        }

        return "redirect:" + "/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String handleLoginForm(HttpServletRequest request, HttpServletResponse response) {
        try {
            UserDAO userDAO = daoFactory.getUserDAO();
            String login = request.getParameter("login");
            String password = request.getParameter("password");
            User user = userDAO.getUserByLogin(login);
            if (user != null) {
                if (user.getPassword().equals(password)) {
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    return "redirect:" + "/index";
                } else {
                    request.setAttribute("error", "Password error");
                    return "login";
                }
            } else {
                request.setAttribute("error", "User with specified login is not exist");
                return "login";
            }
        } catch (DAOException e) {
            request.setAttribute("error", "User with specified login is not exist");
            return "login";
        }
    }

//    @RequestMapping(value = "/register", method = RequestMethod.POST)
//    public String handleRegisterForm(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            UserDAO userDAO = daoFactory.getUserDAO();
//            String fullName = request.getParameter("fullName");
//            String login = request.getParameter("login");
//            String password = request.getParameter("password");
//            User user = new User();
//            user.setFullName(fullName);
//            user.setLogin(login);
//            user.setPassword(password);
//            userDAO.create(user);
//            setUserToSession(request, user);
//
//            return "redirect:" + "/show-items";
//        } catch (DAOException e) {
//            request.setAttribute("error", "Something went wrong");
//            return "register";
//        }
//    }

//    private void setUserToSession(HttpServletRequest request, User user) {
//        try {
//            HttpSession session = request.getSession();
//            User existUser = daoFactory.getUserDAO().getUserByLogin(user.getLogin());
//            session.setAttribute("user", existUser);
//        } catch (DAOException e) {
//            e.printStackTrace();
//        }
//    }

}
