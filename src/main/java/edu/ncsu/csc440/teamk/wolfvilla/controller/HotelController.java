package edu.ncsu.csc440.teamk.wolfvilla.controller;

import edu.ncsu.csc440.teamk.wolfvilla.dao.HotelDAO;
import edu.ncsu.csc440.teamk.wolfvilla.model.Hotel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public ModelAndView addHotel(@RequestParam("name") String name,
                                 @RequestParam("phoneNumber") String phoneNumber,
                                 @RequestParam("address") String address) throws SQLException, ClassNotFoundException {
        Hotel hotel = new Hotel(address, name, phoneNumber);
        HotelDAO.createHotel(hotel);
        //TODO: print message
        return index();
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public ModelAndView getHotelById(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        Hotel hotel = HotelDAO.getHotelById(id);
        return new ModelAndView("hotels/edithotel", "hotel", hotel);
    }

    @RequestMapping(method = RequestMethod.POST, value = "edit/{id}")
    public ModelAndView editHotelById(@PathVariable("id") Long id,
                                      @RequestParam("name") String name,
                                      @RequestParam("phoneNumber") String phoneNumber,
                                      @RequestParam("address") String address) throws SQLException, ClassNotFoundException {
        Hotel hotel = new Hotel(id, address, name, phoneNumber);
        HotelDAO.updateHotel(hotel);
        // TODO: Print message
        return index();
    }

    @RequestMapping(method = RequestMethod.GET, value = "delete/{id}")
    public ModelAndView requestDeleteHotelById(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        Hotel hotel = HotelDAO.getHotelById(id);
        return new ModelAndView("hotels/deletehotel", "hotel", hotel);
    }

    @RequestMapping(method = RequestMethod.POST, value = "delete/{id}")
    public ModelAndView deleteHotelById(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        HotelDAO.deleteHotel(id);
        // TODO: Print message
        return index();
    }
}
