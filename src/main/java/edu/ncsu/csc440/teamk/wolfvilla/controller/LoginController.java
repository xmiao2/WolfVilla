package edu.ncsu.csc440.teamk.wolfvilla.controller;

import edu.ncsu.csc440.teamk.wolfvilla.dao.StaffDAO;
import edu.ncsu.csc440.teamk.wolfvilla.model.Staff;
import edu.ncsu.csc440.teamk.wolfvilla.util.FlashMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

import static edu.ncsu.csc440.teamk.wolfvilla.util.FlashMessage.MESSAGE;

/**
 * Controller for loging that handles routing requests.
 */
@Controller
@RequestMapping("/")
public class LoginController {
    /**
     * Index page of login.
     *
     * @return view containing a list of users and links for user to assume role
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index() throws SQLException, ClassNotFoundException {
        return new ModelAndView("index", "users", StaffDAO.retrieveAllStaff());
    }

    /**
     * @param request Spring HTTP request attribute to retrieve session variable
     * @param redirectAttributes Spring redirect attribute
     * @param id Staff id to login
     * @return redirected view to login index page
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, value = "login/{id}")
    public ModelAndView login(HttpServletRequest request,
                              RedirectAttributes redirectAttributes,
                              @PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        Staff user = StaffDAO.retrieveStaff(id);
        request.getSession().setAttribute("user", user);
        redirectAttributes.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Logged in as %s", user.getName())));
        return new ModelAndView("redirect:/");
    }

    /**
     * Logs a user out from the system.
     *
     * @param request Spring HTTP request attribute to retrieve session variable
     * @param redirectAttributes redirect attribute of Spring
     * @return redirected view to login index page
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, value = "logout")
    public ModelAndView logout(HttpServletRequest request,
                              RedirectAttributes redirectAttributes) throws SQLException, ClassNotFoundException {
        request.getSession().removeAttribute("user");
        redirectAttributes.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, "Logged out"));
        return new ModelAndView("redirect:/");
    }
}
