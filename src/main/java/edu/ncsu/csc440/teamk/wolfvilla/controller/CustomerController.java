package edu.ncsu.csc440.teamk.wolfvilla.controller;

import edu.ncsu.csc440.teamk.wolfvilla.dao.CheckInDAO;
import edu.ncsu.csc440.teamk.wolfvilla.dao.CustomerDAO;
import edu.ncsu.csc440.teamk.wolfvilla.model.CheckInInformation;
import edu.ncsu.csc440.teamk.wolfvilla.model.Customer;
import edu.ncsu.csc440.teamk.wolfvilla.util.FlashMessage;
import edu.ncsu.csc440.teamk.wolfvilla.model.Staff;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

import static edu.ncsu.csc440.teamk.wolfvilla.util.FlashMessage.MESSAGE;

/**
 * Created by Adac on 11/7/2016.
 *
 * Controller for the customers, used to update, delete and add customer information.
 */

@Controller
@RequestMapping("/customers")
public class CustomerController {
    /**
     * @param request
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        Staff user = (Staff)request.getSession().getAttribute("user");
        List<Customer> customers = CustomerDAO.listCustomers(user.getHotelId());
        return new ModelAndView("customers/listcustomers", "customers", customers);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/new")
    public ModelAndView requestAddCustomer() throws SQLException, ClassNotFoundException {
        return new ModelAndView("customers/addcustomer", "customer", new Customer(0, "", 'M', "", "", ""));
    }

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

    @RequestMapping(method = RequestMethod.GET, value = "edit/{id}")
    public ModelAndView getCustomer(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        Customer customer = CustomerDAO.getCustomer(id);
        return new ModelAndView("customers/editcustomer", "customer", customer);
    }

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

    @RequestMapping(method = RequestMethod.GET, value = "delete/{id}")
    public ModelAndView requestDeleteCustomerById(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        Customer customer = CustomerDAO.getCustomer(id);
        return new ModelAndView("customers/deletecustomer", "customer", customer);
    }

    @RequestMapping(method = RequestMethod.POST, value = "delete/{id}")
    public ModelAndView deleteCustomerById(RedirectAttributes redir, @PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        CustomerDAO.deleteCustomer(id);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Deleted Customer (ID=%d)", id)));
        return new ModelAndView("redirect:/customers");
    }
}
