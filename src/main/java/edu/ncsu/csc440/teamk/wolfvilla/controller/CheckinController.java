package edu.ncsu.csc440.teamk.wolfvilla.controller;

import edu.ncsu.csc440.teamk.wolfvilla.dao.BillingDAO;
import edu.ncsu.csc440.teamk.wolfvilla.dao.CheckInDAO;
import edu.ncsu.csc440.teamk.wolfvilla.model.BillingInformation;
import edu.ncsu.csc440.teamk.wolfvilla.model.CheckInInformation;
import edu.ncsu.csc440.teamk.wolfvilla.util.FlashMessage;
import edu.ncsu.csc440.teamk.wolfvilla.util.SQLTypeTranslater;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import static edu.ncsu.csc440.teamk.wolfvilla.util.FlashMessage.MESSAGE;

/**
 * Created by Joshua on 11/6/2016.
 *
 * The controller responsible for interfacing between the Front end and the server for
 * making, deleting, and updating checkins and billinginformation.
 */
@Controller
@RequestMapping("/checkin")
public class CheckinController {
    /**
     * @return the page that lists all checkins
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index() throws SQLException, ClassNotFoundException {
        List<CheckInInformation> checkins = CheckInDAO.listCheckIn();
        return new ModelAndView("checkin/listcheckin", "checkins", checkins);
    }

    /**
     * @return the page with the form for constructing a new checkin.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/new")
    public ModelAndView requestAddCheckin() {
        ModelAndView mv = new ModelAndView("checkin/addcheckin");
        mv.addObject("checkin", new CheckInInformation());
        mv.addObject("billing", new BillingInformation());
        return mv;
    }

    /**
     * @param id the id of the checkin being checked out.
     * @return the form for checking out.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/checkout/{id}")
    public ModelAndView getCheckOut(@PathVariable("id") Long id)  {
        ModelAndView mv = new ModelAndView("checkin/checkout");
        mv.addObject("id", id);
        mv.addObject("checkout", new java.sql.Date(new java.util.Date().getTime()));
        return mv;
    }

    /**
     * Checks out the given checkin with the given date.
     * @param redir used to display success.
     * @param id the id of the checkin to checkout
     * @param date the date the person checked out.
     * @return redirects to index
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     * @throws ParseException if a date is returned that cannot be parsed.
     */
    @RequestMapping(method = RequestMethod.POST, value = "/checkout/{id}")
    public ModelAndView checkOut(RedirectAttributes redir,
                                 @PathVariable("id") Long id,
                                 @RequestParam("checkout") String date)
            throws SQLException, ClassNotFoundException, ParseException {
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS,
                String.format("Checked Out (ID=%d), Price=$%.2f", id, CheckInDAO.checkOut(id, SQLTypeTranslater.stringToTimestamp(date)))));
        return new ModelAndView("redirect:/checkin");
    }

    /**
     * adds a checkin to the database, along with an associated billingInformation
     * @param redir used to display success.
     * @param currentOcupancy the occupancey to put in the new checkin
     * @param checkinTime the checkintime for the new checkin
     * @param checkoutTime the checkout time for the new checkin. Generally, should be null.
     * @param hotelId the id of the hotel the new checkin is in.
     * @param roomNumber the number of the room the checkin is in.
     * @param customerId the id of the customer in this checkin.
     * @param cateringStaffId the id of the catering staff assigned to this check in, ussually null.
     * @param roomServiceStaffId the id of the room service staff assigned to this check in.
     * @param billingAddress the billing address for the billing information associated with this check in.
     * @param ssn the ssn of this billing information
     * @param paymentMethod the payment method of this billing information. Should be "credit" or "debit".
     * @param cardNumber the number of car used for payment.
     * @param expirationDate the expiration date of the payment method.
     * @return redirects to index
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection cannot load connection.
     * @throws ParseException if a date is returned that cannot be parsed.
     */
    @RequestMapping(method = RequestMethod.POST, value = "/new")
    public ModelAndView addCheckin(RedirectAttributes redir,
                                   @RequestParam("currentOcupancy") Integer currentOcupancy,
                                   @RequestParam("checkinTime") String checkinTime,
                                   @RequestParam(value = "checkoutTime", required = false) String checkoutTime,
                                   @RequestParam("hotelId") long hotelId,
                                   @RequestParam("roomNumber") long roomNumber,
                                   @RequestParam("customerId") long customerId,
                                   @RequestParam(value = "cateringStaffId", required = false) Long cateringStaffId,
                                   @RequestParam(value = "roomServiceStaffId", required = false) Long roomServiceStaffId,
                                   @RequestParam("billingAddress") String billingAddress,
                                   @RequestParam("ssn") String ssn,
                                   @RequestParam("paymentMethod") String paymentMethod,
                                   @RequestParam("cardNumber") String cardNumber,
                                   @RequestParam("expirationDate") String expirationDate)
            throws SQLException, ClassNotFoundException, ParseException {
        CheckInInformation checkin = new CheckInInformation(-1, currentOcupancy, SQLTypeTranslater.stringToTimestamp(checkinTime),
                SQLTypeTranslater.stringToTimestamp(checkoutTime), -1,
                hotelId, roomNumber, customerId, cateringStaffId, roomServiceStaffId);
        BillingInformation billing = new BillingInformation(-1, billingAddress, ssn, paymentMethod, cardNumber,
                SQLTypeTranslater.stringToDate(expirationDate));
        long id = CheckInDAO.addCheckIn(checkin, billing);
        ModelAndView mv = new ModelAndView("redirect:/checkin");
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Added CheckIn (ID=%d)", id)));
        return mv;
    }

    /**
     * @param id the id of checkin to edit.
     * @return the form for editing the given checkin with the given id.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection cannot load connection.
     */
    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public ModelAndView getCheckinById(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        CheckInInformation checkin = CheckInDAO.getCheckIn(id);
        return new ModelAndView("checkin/editcheckin", "checkin", checkin);
    }

    /**
     * @param id the id of the checkin associated with the billing information to update
     * @return the form to edit the billing information assocated with the checkin having the given id.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection cannot load connection.
     */
    @RequestMapping(method = RequestMethod.GET, value = "editbilling/{id}")
    public ModelAndView getBillingById(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        BillingInformation billing = BillingDAO.retrieveBillingInformation(id);
        ModelAndView mv = new ModelAndView("checkin/editbilling");
        mv.addObject("billing", billing);
        mv.addObject("checkinId", id);
        return mv;
    }

    /**
     * Updates a checkin with the given id to the new given information.
     * @param redir used to display success.
     * @param currentOcupancy the occupancey to put in the new checkin
     * @param checkinTime the checkintime for the new checkin
     * @param checkoutTime the checkout time for the new checkin. Generally, should be null.
     * @param hotelId the id of the hotel the new checkin is in.
     * @param roomNumber the number of the room the checkin is in.
     * @param customerId the id of the customer in this checkin.
     * @param cateringStaffId the id of the catering staff assigned to this check in, ussually null.
     * @param roomServiceStaffId the id of the room service staff assigned to this check in.
     * @param id the id of the checkin to update
     * @return the index.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection cannot load connection.
     * @throws ParseException if a date is returned that cannot be parsed.
     */
    @RequestMapping(method = RequestMethod.POST, value = "edit/{id}")
    public ModelAndView editCheckinById(RedirectAttributes redir,
                                        @PathVariable("id") Long id,
                                        @RequestParam("currentOcupancy") Integer currentOcupancy,
                                        @RequestParam("checkinTime") String checkinTime,
                                        @RequestParam(value = "checkoutTime", required = false) String checkoutTime,
                                        @RequestParam("hotelId") long hotelId,
                                        @RequestParam("roomNumber") long roomNumber,
                                        @RequestParam("customerId") long customerId,
                                        @RequestParam(value = "cateringStaffId", required = false) Long cateringStaffId,
                                        @RequestParam(value = "roomServiceStaffId", required = false) Long roomServiceStaffId) throws SQLException, ClassNotFoundException, ParseException {
        CheckInInformation checkin = new CheckInInformation(id, currentOcupancy, SQLTypeTranslater.stringToTimestamp(checkinTime),
                SQLTypeTranslater.stringToTimestamp(checkoutTime), -1,
                hotelId,roomNumber,customerId,cateringStaffId,roomServiceStaffId);
        CheckInDAO.updateCheckIn(checkin);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Edited Service (ID=%d)", checkin.getId())));
        return new ModelAndView("redirect:/checkin");
    }

    /**
     * Updates a billing information with the given information.
     * @param redir used to display success.
     * @param id the id of the checkin associated with the billing information to update.
     * @param billingAddress the billing address for the billing information associated with this check in.
     * @param ssn the ssn of this billing information
     * @param paymentMethod the payment method of this billing information. Should be "credit" or "debit".
     * @param cardNumber the number of car used for payment.
     * @param expirationDate the expiration date of the payment method.
     * @return the index.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection cannot load connection.
     * @throws ParseException if a date is returned that cannot be parsed.
     */
    @RequestMapping(method = RequestMethod.POST, value = "editbilling/{id}")
    public ModelAndView editBillingById(RedirectAttributes redir,
                                        @PathVariable("id") Long id,
                                        @RequestParam("billingAddress") String billingAddress,
                                        @RequestParam("ssn") String ssn,
                                        @RequestParam("paymentMethod") String paymentMethod,
                                        @RequestParam("cardNumber") String cardNumber,
                                        @RequestParam("expirationDate") String expirationDate) throws SQLException, ClassNotFoundException, ParseException {
        BillingInformation billing = new BillingInformation(-1, billingAddress, ssn, paymentMethod, cardNumber,
                SQLTypeTranslater.stringToDate(expirationDate));
        BillingDAO.updateBillingInformation(id, billing);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Edited Service (ID=%d)", id)));
        return new ModelAndView("redirect:/checkin");
    }

    /**
     * @param id the id of checkin to delete.
     * @return A verifcation page whether the delete should be completed.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection cannot load connection.
     */
    @RequestMapping(method = RequestMethod.GET, value = "delete/{id}")
    public ModelAndView requestDeleteServiceById(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        CheckInInformation checkin = CheckInDAO.getCheckIn(id);
        return new ModelAndView("checkin/deletecheckin", "checkin", checkin);
    }

    /**
     * Deletes the checkin associated with the given id.
     * @param redir used to display success.
     * @param id the id of the checkin to delete.
     * @return the index.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection cannot load connection.
     */
    @RequestMapping(method = RequestMethod.POST, value = "delete/{id}")
    public ModelAndView deleteServiceById(RedirectAttributes redir, @PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        CheckInDAO.deleteCheckIn(id);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Deleted Checkin (ID=%d)", id)));
        return new ModelAndView("redirect:/checkin");
    }
}
