package edu.ncsu.csc440.teamk.wolfvilla.controller;

import edu.ncsu.csc440.teamk.wolfvilla.dao.HotelDAO;
import edu.ncsu.csc440.teamk.wolfvilla.dao.ServicesDAO;
import edu.ncsu.csc440.teamk.wolfvilla.model.Hotel;
import edu.ncsu.csc440.teamk.wolfvilla.model.Service;
import edu.ncsu.csc440.teamk.wolfvilla.util.FlashMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.List;

import static edu.ncsu.csc440.teamk.wolfvilla.util.FlashMessage.MESSAGE;

/**
 * Created by Joshua on 11/6/2016.
 */
@Controller
@RequestMapping("/services")
public class ServiceController {
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index() throws SQLException, ClassNotFoundException {
        List<Service> services = ServicesDAO.getAllServices();
        return new ModelAndView("services/listservices", "services", services);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/new")
    public ModelAndView requestAddService() throws SQLException, ClassNotFoundException {
        return new ModelAndView("services/addservice", "service", new Service());
    }

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

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public ModelAndView getServiceById(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        Service service = ServicesDAO.getService(id);
        return new ModelAndView("services/editservice", "service", service);
    }

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

    @RequestMapping(method = RequestMethod.GET, value = "delete/{id}")
    public ModelAndView requestDeleteServiceById(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        Service service = ServicesDAO.getService(id);
        return new ModelAndView("services/deleteservice", "service", service);
    }

    @RequestMapping(method = RequestMethod.POST, value = "delete/{id}")
    public ModelAndView deleteServiceById(RedirectAttributes redir, @PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        HotelDAO.deleteHotel(id);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Deleted Service (ID=%d)", id)));
        return new ModelAndView("redirect:/services");
    }
}