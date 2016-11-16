package edu.ncsu.csc440.teamk.wolfvilla.controller;

import edu.ncsu.csc440.teamk.wolfvilla.dao.CheckInDAO;
import edu.ncsu.csc440.teamk.wolfvilla.dao.CustomerDAO;
import edu.ncsu.csc440.teamk.wolfvilla.model.CheckInInformation;
import edu.ncsu.csc440.teamk.wolfvilla.dao.ReportDAO;
import edu.ncsu.csc440.teamk.wolfvilla.model.Customer;
import edu.ncsu.csc440.teamk.wolfvilla.util.FlashMessage;
import edu.ncsu.csc440.teamk.wolfvilla.model.Staff;
import edu.ncsu.csc440.teamk.wolfvilla.util.SQLTypeTranslater;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import static edu.ncsu.csc440.teamk.wolfvilla.util.FlashMessage.MESSAGE;

/**
 * Created by Adam on 11/7/2016.
 * <p>
 * Controller for the customers, used to update, delete and add customer information.
 */

@Controller
@RequestMapping("/customers")
public class CustomerController {
    /**
     * @param request gets the current user information for defaults.
     * @return a page with the list of all customers.
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        Staff user = (Staff) request.getSession().getAttribute("user");
        List<Customer> customers = CustomerDAO.listCustomers(user.getHotelId());
        return new ModelAndView("customers/listcustomers", "customers", customers);
    }

    /**
     * @return a form to enter parameters for a new customer.
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/new")
    public ModelAndView requestAddCustomer() throws SQLException, ClassNotFoundException {
        return new ModelAndView("customers/addcustomer", "customer", new Customer(0, "", 'M', "", "", ""));
    }

    /**
     * Makes a new customer
     * @param redir used to give a success message and return the new id.
     * @param name the name of the new customer.
     * @param gender the gender of the new customer
     * @param phoneNumber the phone number of the new customer
     * @param email the email of the new customer
     * @param address the address of the new customer
     * @return redirects to index
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/new")
    public ModelAndView addCustomer(RedirectAttributes redir,
                                    @RequestParam("name") String name,
                                    @RequestParam("gender") Character gender,
                                    @RequestParam("phoneNumber") String phoneNumber,
                                    @RequestParam("email") String email,
                                    @RequestParam("address") String address) throws SQLException, ClassNotFoundException {
        Customer customer = new Customer(0, name, gender, phoneNumber, email, address);
        long id = CustomerDAO.createCustomer(customer);
        ModelAndView mv = new ModelAndView("redirect:/customers");
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Added Customer (ID=%d)", id)));
        return mv;
    }

    /**
     * @param id the id of the customer to edit
     * @return a form with the fields to change the customers to
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, value = "edit/{id}")
    public ModelAndView getCustomer(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        Customer customer = CustomerDAO.getCustomer(id);
        return new ModelAndView("customers/editcustomer", "customer", customer);
    }

    /**
     * Updates the customer with the given id to the new values
     * @param redir used to give a success message.
     * @param id the id of the customer to udpdate
     * @param name the new name for the customer
     * @param gender the new gender of the customer.
     * @param phoneNumber the new phone number for the customer
     * @param email the new email for the customer
     * @param address the new address for the customer
     * @return redirects to index
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.POST, value = "edit/{id}")
    public ModelAndView editCustomerById(RedirectAttributes redir,
                                         @PathVariable("id") Long id,
                                         @RequestParam("name") String name,
                                         @RequestParam("gender") Character gender,
                                         @RequestParam("phoneNumber") String phoneNumber,
                                         @RequestParam("email") String email,
                                         @RequestParam("address") String address) throws SQLException, ClassNotFoundException {
        Customer customer = new Customer(id, name, gender, phoneNumber, email, address);
        CustomerDAO.updateCustomer(customer);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Edited Customer (ID=%d)", id)));
        return new ModelAndView("redirect:/customers");
    }

    /**
     * @param id the id of the customer to delete
     * @return the form confirming delete.
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, value = "delete/{id}")
    public ModelAndView requestDeleteCustomerById(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        Customer customer = CustomerDAO.getCustomer(id);
        return new ModelAndView("customers/deletecustomer", "customer", customer);
    }

    /**
     * @param redir used to send a success signal.
     * @param id the id of the customer to delete.
     * @return redirects to index.
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.POST, value = "delete/{id}")
    public ModelAndView deleteCustomerById(RedirectAttributes redir, @PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        CustomerDAO.deleteCustomer(id);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Deleted Customer (ID=%d)", id)));
        return new ModelAndView("redirect:/customers");
    }

    /**
     * @param mv used to redirect the view
     * @param startDate the first date to look for occupants in
     * @param endDate the last date to look for occupants in
     * @param id the hotel to look for occupants in
     * @return a page with the list of all customers in a given hotel in the given range.
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/all_occupants")
    public ModelAndView getAllOccupants(ModelAndView mv, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("hotelId") Long id) throws SQLException, ClassNotFoundException {
        List<Customer> customers = null;
        try {
            customers = ReportDAO.reportOccupants(SQLTypeTranslater.stringToDate(startDate), SQLTypeTranslater.stringToDate(endDate), id);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mv.setViewName("customers/all_occupants");
        mv.addObject("hotelId", id);
        mv.addObject("customers", customers);
        return mv;
    }

    /**
     * @return a form for the dates and hotel to look for occupants in.
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/occupants")
    public ModelAndView reportOccupied() throws SQLException, ClassNotFoundException {
        ModelAndView mv = new ModelAndView("customers/occupants");
        return mv;
    }
}
