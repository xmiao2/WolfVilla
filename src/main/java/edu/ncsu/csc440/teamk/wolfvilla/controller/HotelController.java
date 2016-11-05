package edu.ncsu.csc440.teamk.wolfvilla.controller;

import edu.ncsu.csc440.teamk.wolfvilla.dao.HotelDAO;
import edu.ncsu.csc440.teamk.wolfvilla.model.Hotel;
import edu.ncsu.csc440.teamk.wolfvilla.util.FlashMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static edu.ncsu.csc440.teamk.wolfvilla.util.FlashMessage.MESSAGE;

@Controller
@RequestMapping("/hotels")
public class HotelController {
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index() throws SQLException, ClassNotFoundException {
        List<Hotel> hotels = HotelDAO.getHotels();
        return new ModelAndView("hotels/listhotels", "hotels", hotels);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/new")
    public ModelAndView requestAddHotel() throws SQLException, ClassNotFoundException {
        return new ModelAndView("hotels/addhotel", "hotel", new Hotel());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/new")
    public ModelAndView addHotel(RedirectAttributes redir,
                                 @RequestParam("name") String name,
                                 @RequestParam("phoneNumber") String phoneNumber,
                                 @RequestParam("address") String address) throws SQLException, ClassNotFoundException {
        Hotel hotel = new Hotel(address, name, phoneNumber);
        long id = HotelDAO.createHotel(hotel);
        ModelAndView mv = new ModelAndView("redirect:/hotels");
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Added Hotel (ID=%d)", id)));
        return mv;
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public ModelAndView getHotelById(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        Hotel hotel = HotelDAO.getHotelById(id);
        return new ModelAndView("hotels/edithotel", "hotel", hotel);
    }

    @RequestMapping(method = RequestMethod.POST, value = "edit/{id}")
    public ModelAndView editHotelById(RedirectAttributes redir,
                                      @PathVariable("id") Long id,
                                      @RequestParam("name") String name,
                                      @RequestParam("phoneNumber") String phoneNumber,
                                      @RequestParam("address") String address) throws SQLException, ClassNotFoundException {
        Hotel hotel = new Hotel(id, address, name, phoneNumber);
        HotelDAO.updateHotel(hotel);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Edited Hotel (ID=%d)", hotel.getPrimaryKey())));
        return new ModelAndView("redirect:/hotels");
    }

    @RequestMapping(method = RequestMethod.GET, value = "delete/{id}")
    public ModelAndView requestDeleteHotelById(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        Hotel hotel = HotelDAO.getHotelById(id);
        return new ModelAndView("hotels/deletehotel", "hotel", hotel);
    }

    @RequestMapping(method = RequestMethod.POST, value = "delete/{id}")
    public ModelAndView deleteHotelById(RedirectAttributes redir, @PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        HotelDAO.deleteHotel(id);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Deleted Hotel (ID=%d)", id)));
        return new ModelAndView("redirect:/hotels");
    }
}
