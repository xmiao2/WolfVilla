package edu.ncsu.csc440.teamk.wolfvilla.controller;

import edu.ncsu.csc440.teamk.wolfvilla.dao.HotelDAO;
import edu.ncsu.csc440.teamk.wolfvilla.model.Hotel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/hotels")
public class HotelController {
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index() throws SQLException, ClassNotFoundException {
//        List<Hotel> hotels = HotelDAO.getHotels();
        List<Hotel> hotels = new ArrayList<Hotel>() {{
            add(new Hotel(0, "123 test dr.", "wolfville", "9191231234"));
            add(new Hotel(1, "456 test dr.", "wolfville", "9191231234"));
            add(new Hotel(2, "789 test dr.", "wolfville", "9191231234"));
        }};
        return new ModelAndView("hotels/listhotels", "hotels", hotels);
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public ModelAndView getHotelById(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
//        Hotel hotel = HotelDAO.getHotelById(id);
        Hotel hotel = new Hotel(0, "123 test dr.", "wolfville", "9191231234");
        return new ModelAndView("hotels/edithotel", "hotel", hotel);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{id}")
    public ModelAndView deleteHotelById(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        return index();
    }
}
