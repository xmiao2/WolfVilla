package edu.ncsu.csc440.teamk.wolfvilla.controller;

import edu.ncsu.csc440.teamk.wolfvilla.dao.StaffDAO;
import edu.ncsu.csc440.teamk.wolfvilla.model.Staff;
import edu.ncsu.csc440.teamk.wolfvilla.util.FlashMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

import static edu.ncsu.csc440.teamk.wolfvilla.util.FlashMessage.MESSAGE;

@Controller
@RequestMapping("/staff")
public class StaffController {
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index() throws SQLException, ClassNotFoundException {
        List<Staff> staff = StaffDAO.retrieveAllStaff();
        return new ModelAndView("staff/liststaff", "staff", staff);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/new")
    public ModelAndView requestAddStaff() throws SQLException, ClassNotFoundException {
        return new ModelAndView("staff/addstaff", "staff", new Staff());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/new")
    public ModelAndView addStaff(HttpServletRequest request,
                                 RedirectAttributes redir,
                                 @ModelAttribute Staff staff) throws SQLException, ClassNotFoundException {
        long id = StaffDAO.addStaff(staff);
        ModelAndView mv = new ModelAndView("redirect:/staff");
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Added Staff (ID=%d)", id)));
        return mv;
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public ModelAndView getHotelById(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        Staff staff = StaffDAO.retrieveStaff(id);
        return new ModelAndView("staff/editstaff", "staff", staff);
    }

    @RequestMapping(method = RequestMethod.POST, value = "edit/{id}")
    public ModelAndView editHotelById(RedirectAttributes redir, @ModelAttribute Staff staff) throws SQLException, ClassNotFoundException {
        StaffDAO.updateStaff(staff);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Edited Staff (ID=%d)", staff.getId())));
        return new ModelAndView("redirect:/staff");
    }

    @RequestMapping(method = RequestMethod.GET, value = "delete/{id}")
    public ModelAndView requestDeleteHotelById(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        Staff staff = StaffDAO.retrieveStaff(id);
        return new ModelAndView("staff/deletestaff", "staff", staff);
    }

    @RequestMapping(method = RequestMethod.POST, value = "delete/{id}")
    public ModelAndView deleteHotelById(RedirectAttributes redir, @PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        StaffDAO.deleteStaff(id);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Deleted Staff (ID=%d)", id)));
        return new ModelAndView("redirect:/staff");
    }
}
