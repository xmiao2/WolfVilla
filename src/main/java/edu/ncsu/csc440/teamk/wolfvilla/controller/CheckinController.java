package edu.ncsu.csc440.teamk.wolfvilla.controller;

import edu.ncsu.csc440.teamk.wolfvilla.dao.BillingDAO;
import edu.ncsu.csc440.teamk.wolfvilla.dao.CheckInDAO;
import edu.ncsu.csc440.teamk.wolfvilla.dao.HotelDAO;
import edu.ncsu.csc440.teamk.wolfvilla.dao.ServicesDAO;
import edu.ncsu.csc440.teamk.wolfvilla.model.BillingInformation;
import edu.ncsu.csc440.teamk.wolfvilla.model.CheckInInformation;
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
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import static edu.ncsu.csc440.teamk.wolfvilla.util.FlashMessage.MESSAGE;

/**
 * Created by Joshua on 11/6/2016.
 */
@Controller
@RequestMapping("/checkin")
public class CheckinController {
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        Staff user = (Staff)request.getSession().getAttribute("user");
        List<CheckInInformation> checkins = CheckInDAO.listCheckIn(user.getHotelId());
        return new ModelAndView("checkin/listcheckin", "checkins", checkins);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/new")
    public ModelAndView requestAddCheckin() throws SQLException, ClassNotFoundException {
        ModelAndView mv = new ModelAndView("checkin/addcheckin");
        mv.addObject("checkin", new CheckInInformation());
        mv.addObject("billing", new BillingInformation());
        return mv;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/new")
    public ModelAndView addCheckin(RedirectAttributes redir,
                                   @RequestParam("currentOcupancy") Integer currentOcupancy,
                                   @RequestParam("checkinTime") Date checkinTime,
                                   @RequestParam(value = "checkoutTime", required = false) Date checkoutTime,
                                   @RequestParam("hotelId") long hotelId,
                                   @RequestParam("roomNumber") long roomNumber,
                                   @RequestParam("customerId") long customerId,
                                   @RequestParam(value = "cateringStaffId", required = false) Long cateringStaffId,
                                   @RequestParam(value = "roomServiceStaffId", required = false) Long roomServiceStaffId,
                                   @RequestParam("billingAddress") String billingAddress,
                                   @RequestParam("ssn") String ssn,
                                   @RequestParam("paymentMethod") String paymentMethod,
                                   @RequestParam("cardNumber") String cardNumber,
                                   @RequestParam("expirationDate") Date expirationDate)
            throws SQLException, ClassNotFoundException {
        CheckInInformation checkin = new CheckInInformation(-1, currentOcupancy, checkinTime, checkoutTime, -1,
                hotelId,roomNumber,customerId,cateringStaffId,roomServiceStaffId);
        BillingInformation billing = new BillingInformation(-1, billingAddress, ssn, paymentMethod, cardNumber, expirationDate);
        long id = CheckInDAO.addCheckIn(checkin, billing);
        ModelAndView mv = new ModelAndView("redirect:/checkin");
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Added Service (ID=%d)", id)));
        return mv;
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public ModelAndView getCheckinById(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        CheckInInformation checkin = CheckInDAO.getCheckIn(id);
        return new ModelAndView("checkin/editcheckin", "checkin", checkin);
    }

    @RequestMapping(method = RequestMethod.GET, value = "editbilling/{id}")
    public ModelAndView getBillingById(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        BillingInformation billing = BillingDAO.retrieveBillingInformation(id);
        ModelAndView mv = new ModelAndView("checkin/editbilling");
        mv.addObject("billing", billing);
        mv.addObject("checkinId", id);
        return mv;
    }

    @RequestMapping(method = RequestMethod.POST, value = "edit/{id}")
    public ModelAndView editCheckinById(RedirectAttributes redir,
                                        @PathVariable("id") Long id,
                                        @RequestParam("currentOcupancy") Integer currentOcupancy,
                                        @RequestParam("checkinTime") Date checkinTime,
                                        @RequestParam(value = "checkoutTime", required = false) Date checkoutTime,
                                        @RequestParam("hotelId") long hotelId,
                                        @RequestParam("roomNumber") long roomNumber,
                                        @RequestParam("customerId") long customerId,
                                        @RequestParam(value = "cateringStaffId", required = false) Long cateringStaffId,
                                        @RequestParam(value = "roomServiceStaffId", required = false) Long roomServiceStaffId) throws SQLException, ClassNotFoundException {
        CheckInInformation checkin = new CheckInInformation(id, currentOcupancy, checkinTime, checkoutTime, -1,
                hotelId,roomNumber,customerId,cateringStaffId,roomServiceStaffId);
        CheckInDAO.updateCheckIn(checkin);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Edited Service (ID=%d)", checkin.getId())));
        return new ModelAndView("redirect:/checkin");
    }

    @RequestMapping(method = RequestMethod.POST, value = "editbilling/{id}")
    public ModelAndView editBillingById(RedirectAttributes redir,
                                        @PathVariable("id") Long id,
                                        @RequestParam("billingAddress") String billingAddress,
                                        @RequestParam("ssn") String ssn,
                                        @RequestParam("paymentMethod") String paymentMethod,
                                        @RequestParam("cardNumber") String cardNumber,
                                        @RequestParam("expirationDate") Date expirationDate) throws SQLException, ClassNotFoundException {
        BillingInformation billing = new BillingInformation(-1, billingAddress, ssn, paymentMethod, cardNumber, expirationDate);
        BillingDAO.updateBillingInformation(id, billing);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Edited Service (ID=%d)", id)));
        return new ModelAndView("redirect:/checkin");
    }

    @RequestMapping(method = RequestMethod.GET, value = "delete/{id}")
    public ModelAndView requestDeleteServiceById(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        CheckInInformation checkin = CheckInDAO.getCheckIn(id);
        return new ModelAndView("checkin/deletecheckin", "checkin", checkin);
    }

    @RequestMapping(method = RequestMethod.POST, value = "delete/{id}")
    public ModelAndView deleteServiceById(RedirectAttributes redir, @PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        CheckInDAO.deleteCheckIn(id);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Deleted Checkin (ID=%d)", id)));
        return new ModelAndView("redirect:/checkin");
    }
}
