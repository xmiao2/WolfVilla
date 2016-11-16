package edu.ncsu.csc440.teamk.wolfvilla.controller;

import edu.ncsu.csc440.teamk.wolfvilla.dao.ReportDAO;
import edu.ncsu.csc440.teamk.wolfvilla.dao.StaffDAO;
import edu.ncsu.csc440.teamk.wolfvilla.model.Customer;
import edu.ncsu.csc440.teamk.wolfvilla.model.Staff;
import edu.ncsu.csc440.teamk.wolfvilla.model.TitleDepartment;
import edu.ncsu.csc440.teamk.wolfvilla.util.FlashMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

import static edu.ncsu.csc440.teamk.wolfvilla.util.FlashMessage.MESSAGE;

/**
 * Staff controller class that handles routing requests
 */
@Controller
@RequestMapping("/staff")
public class StaffController {
    /**
     * @return view containing list of staff
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index() throws SQLException, ClassNotFoundException {
        List<Staff> staff = StaffDAO.retrieveAllStaff();
        return new ModelAndView("staff/liststaff", "staff", staff);
    }

    /**
     * @return the form to select title and hotel to retrieve by.
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, value = "retrievebytitle")
    public ModelAndView selectTitleToRetrieveBy() throws SQLException, ClassNotFoundException {
        return new ModelAndView("staff/requestbytitle", "titledepartment", new TitleDepartment("Front Desk representative", null));
    }

    /**
     * @param title the job title of the staff to retrieve
     * @param hotel the hotel of staff to retrieve, all hotels if null.
     * @return all staff in the given hotel (or any if hotel = null), with given job title.
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.POST, value = "retrievebytitle")
    public ModelAndView retrieveByTitle(@RequestParam("title") String title,
                                        @RequestParam("hotel") Long hotel)
            throws SQLException, ClassNotFoundException {
        List<Staff> tastyStaff = null;
        if (hotel == null) {
            tastyStaff = ReportDAO.getStaffByRole(title);
        }  else {
            tastyStaff = ReportDAO.getHotelStaffByRole(title, hotel);
        }
        return new ModelAndView("staff/bytitle", "staff", tastyStaff);
    }

    /**
     * @return view containing option to create a new staff
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/new")
    public ModelAndView requestAddStaff() throws SQLException, ClassNotFoundException {
        return new ModelAndView("staff/addstaff", "staff", new Staff());
    }

    /**
     * Handle a request to create new staff.
     *
     * @param redir redirect attribute of Spring
     * @param staff staff to create
     * @return redirected view to index page
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/new")
    public ModelAndView addStaff(RedirectAttributes redir,
                                 @ModelAttribute Staff staff) throws SQLException, ClassNotFoundException {
        long id = StaffDAO.addStaff(staff);
        ModelAndView mv = new ModelAndView("redirect:/staff");
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Added Staff (ID=%d)", id)));
        return mv;
    }

    /**
     * @param id id of staff
     * @return view containing options to edit staff
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public ModelAndView getStaffById(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        Staff staff = StaffDAO.retrieveStaff(id);
        return new ModelAndView("staff/editstaff", "staff", staff);
    }

    /**
     * Handles a request to edit a staff.
     *
     * @param redir redirect attribute of Spring
     * @param staff staff to edit
     * @return redirected view to index page
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.POST, value = "edit/{id}")
    public ModelAndView editStaffById(RedirectAttributes redir, @ModelAttribute Staff staff) throws SQLException, ClassNotFoundException {
        StaffDAO.updateStaff(staff);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Edited Staff (ID=%d)", staff.getId())));
        return new ModelAndView("redirect:/staff");
    }

    /**
     * @param id id of the hotel to delete
     * @return view containing option to delete a stff
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, value = "delete/{id}")
    public ModelAndView requestDeleteStaffById(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        Staff staff = StaffDAO.retrieveStaff(id);
        return new ModelAndView("staff/deletestaff", "staff", staff);
    }

    /**
     * Handles a request to delete a staff.
     *
     * @param redir redirect attribute of Spring
     * @param id id of staff
     * @return redirected view to index page
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.POST, value = "delete/{id}")
    public ModelAndView deleteStaffById(RedirectAttributes redir, @PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        StaffDAO.deleteStaff(id);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Deleted Staff (ID=%d)", id)));
        return new ModelAndView("redirect:/staff");
    }

    /**
     * @param id id of staff
     * @return a list of customers the staff is serving
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, value = "{id}/customers")
    public ModelAndView getCustomerByStaff(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        List<Customer> customers = ReportDAO.getCustomersOfStaff(id);
        return new ModelAndView("staff/viewcustomers", "customers", customers);
    }
}