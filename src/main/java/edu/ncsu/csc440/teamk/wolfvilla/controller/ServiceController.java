package edu.ncsu.csc440.teamk.wolfvilla.controller;

import edu.ncsu.csc440.teamk.wolfvilla.dao.HotelDAO;
import edu.ncsu.csc440.teamk.wolfvilla.dao.ServicesDAO;
import edu.ncsu.csc440.teamk.wolfvilla.model.Hotel;
import edu.ncsu.csc440.teamk.wolfvilla.model.Service;
import edu.ncsu.csc440.teamk.wolfvilla.model.Staff;
import edu.ncsu.csc440.teamk.wolfvilla.util.FlashMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

import static edu.ncsu.csc440.teamk.wolfvilla.util.FlashMessage.MESSAGE;

/**
 * Created by Joshua on 11/6/2016.
 */
@Controller
@RequestMapping("/services")
public class ServiceController {
    /**
     * @return a page with a list of all services.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index() throws SQLException, ClassNotFoundException {
        List<Service> services = ServicesDAO.getAllServices();
        return new ModelAndView("services/listservices", "services", services);
    }

    /**
     * @param request contains the id of the currently logged in user, defualts the staff who performed service.
     * @return a page a form for adding a new service.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/new")
    public ModelAndView requestAddService(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        Staff user = (Staff)request.getSession().getAttribute("user");
        return new ModelAndView("services/addservice", "service", new Service(-1, "", 0, user.getId(), -1));
    }

    /**
     * Adds a new service with the given data.
     * @param redir used to display a success signal, and the id of added service.
     * @param description the description of the new service
     * @param price the price of the new service
     * @param staffId the staff who performed the new service
     * @param checkinID For which checkin the new service was for.
     * @return redirects to the index.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
    @RequestMapping(method = RequestMethod.POST, value = "/new")
    public ModelAndView addService(RedirectAttributes redir,
                                 @RequestParam("description") String description,
                                 @RequestParam("price") Double price,
                                 @RequestParam("staffId") Long staffId,
                                 @RequestParam("checkinID") Long checkinID) throws SQLException, ClassNotFoundException {
        Service service = new Service(-1, description, price, staffId, checkinID);
        long id = ServicesDAO.addService(service);
        ModelAndView mv = new ModelAndView("redirect:/services");
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Added Service (ID=%d)", id)));
        return mv;
    }

    /**
     * @param id the id of the service to edit.
     * @return a form to edit the service with the given id.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public ModelAndView getServiceById(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        Service service = ServicesDAO.getService(id);
        return new ModelAndView("services/editservice", "service", service);
    }

    /**
     * Edits the service with the given id to the new information.
     * @param redir used to display a success message.
     * @param id the id of the service to edit.
     * @param description the new description for the service.
     * @param price the new price for the service.
     * @param staffId the new id of the staff who did the service.
     * @param checkinID the new id of the checkin for the service.
     * @return redirects to index
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
    @RequestMapping(method = RequestMethod.POST, value = "edit/{id}")
    public ModelAndView editServiceById(RedirectAttributes redir,
                                      @PathVariable("id") Long id,
                                      @RequestParam("description") String description,
                                      @RequestParam("price") Double price,
                                      @RequestParam("staffId") Long staffId,
                                      @RequestParam("checkinID") Long checkinID) throws SQLException, ClassNotFoundException {
        Service service = new Service(id, description, price, staffId, checkinID);
        ServicesDAO.updateService(service);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Edited Service (ID=%d)", service.getId())));
        return new ModelAndView("redirect:/services");
    }

    /**
     * @param id the id of the service to delete
     * @return a confirmation form for deleting the service with the given id.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
    @RequestMapping(method = RequestMethod.GET, value = "delete/{id}")
    public ModelAndView requestDeleteServiceById(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        Service service = ServicesDAO.getService(id);
        return new ModelAndView("services/deleteservice", "service", service);
    }

    /**
     *  Deletes the service with the given id.
     * @param redir used to display a success message.
     * @param id the id of the service to delete.
     * @return redirect to index.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
    @RequestMapping(method = RequestMethod.POST, value = "delete/{id}")
    public ModelAndView deleteServiceById(RedirectAttributes redir, @PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        ServicesDAO.deleteService(id);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Deleted Service (ID=%d)", id)));
        return new ModelAndView("redirect:/services");
    }
}
