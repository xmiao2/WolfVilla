package edu.ncsu.csc440.teamk.wolfvilla.controller;

import edu.ncsu.csc440.teamk.wolfvilla.dao.HotelDAO;
import edu.ncsu.csc440.teamk.wolfvilla.dao.ReportDAO;
import edu.ncsu.csc440.teamk.wolfvilla.dao.RoomDAO;
import edu.ncsu.csc440.teamk.wolfvilla.model.Hotel;
import edu.ncsu.csc440.teamk.wolfvilla.model.Room;
import edu.ncsu.csc440.teamk.wolfvilla.util.FlashMessage;
import edu.ncsu.csc440.teamk.wolfvilla.util.SQLTypeTranslater;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import static edu.ncsu.csc440.teamk.wolfvilla.util.FlashMessage.MESSAGE;

/**
 * Controller for Hotel related pages.
 */
@Controller
@RequestMapping("/hotels")
public class HotelController {
    /**
     * @return view containing list of hotels
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index() throws SQLException, ClassNotFoundException {
        List<Hotel> hotels = HotelDAO.getHotels();
        return new ModelAndView("hotels/listhotels", "hotels", hotels);
    }

    /**
     * @return view containing options to add hotel
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/new")
    public ModelAndView requestAddHotel() throws SQLException, ClassNotFoundException {
        return new ModelAndView("hotels/addhotel", "hotel", new Hotel());
    }

    /**
     * @param id id of hotel
     * @return view containing options to assign manager to hotel
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, value = "editmanager/{id}")
    public ModelAndView getChangeManager(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        return new ModelAndView("hotels/editmanager", "id", id);
    }

    /**
     * Assign a new manager to a given hotel.
     *
     * @param redir redirect attribute in Spring framework
     * @param hotelId hotel id
     * @param managerId manager id to be assigned to the hotel
     * @return redirected view containing list of hotels once manager is assigned
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.POST, value = "editmanager/{hotelId}")
    public ModelAndView changeManager(RedirectAttributes redir,
                                      @PathVariable("hotelId") Long hotelId,
                                      @RequestParam("manager") Long managerId) throws SQLException, ClassNotFoundException {
        HotelDAO.assignHotelManager(hotelId, managerId);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Edited Hotel (ID=%d)", hotelId)));
        return new ModelAndView("redirect:/hotels");
    }

    /**
     * Add a given hotel to database.
     *
     * @param redir redirect attribute in Spring framework
     * @param name name of hotel
     * @param phoneNumber phone number of hotel
     * @param address address of hotel
     * @return redirected view of list of hotels
     * @throws SQLException
     * @throws ClassNotFoundException
     */
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

    /**
     * @param id id of hotel
     * @return view containing options to modify hotel attributes
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public ModelAndView getHotelById(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        Hotel hotel = HotelDAO.getHotelById(id);
        return new ModelAndView("hotels/edithotel", "hotel", hotel);
    }

    /**
     * Modifies hotel by the given values.
     *
     * @param redir redirect attribute in Spring framework
     * @param id id of hotel
     * @param name name of hotel
     * @param phoneNumber phone number of hotel
     * @param address address of hotel
     * @return redirected view of hotel
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.POST, value = "edit/{id}")
    public ModelAndView editHotelById(RedirectAttributes redir,
                                      @PathVariable("id") Long id,
                                      @RequestParam("name") String name,
                                      @RequestParam("phoneNumber") String phoneNumber,
                                      @RequestParam("address") String address) throws SQLException, ClassNotFoundException {
        Hotel hotel = new Hotel(id, null, address, name, phoneNumber);
        HotelDAO.updateHotel(hotel);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Edited Hotel (ID=%d)", hotel.getId())));
        return new ModelAndView("redirect:/hotels");
    }

    /**
     * @param id id of hotel
     * @return view containing confirmation to delete hotel
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, value = "delete/{id}")
    public ModelAndView requestDeleteHotelById(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        Hotel hotel = HotelDAO.getHotelById(id);
        return new ModelAndView("hotels/deletehotel", "hotel", hotel);
    }

    /**
     * Removes a hotel of a given id.
     *
     * @param redir redirect attribute in Spring framework
     * @param id id of hotel
     * @return redirected view containing a list of hotels
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.POST, value = "delete/{id}")
    public ModelAndView deleteHotelById(RedirectAttributes redir, @PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        HotelDAO.deleteHotel(id);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Deleted Hotel (ID=%d)", id)));
        return new ModelAndView("redirect:/hotels");
    }

    /**
     * @param mv ModelView object in Spring framework
     * @param id id of hotel
     * @return view containing a list of rooms by the given hotel
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, value = "{id}/rooms")
    public ModelAndView listRoomsById(ModelAndView mv, @PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        List<Room> rooms = RoomDAO.listRooms(id);
        mv.setViewName("rooms/listrooms");
        mv.addObject("rooms", rooms);
        mv.addObject("hotelId", id);
        return mv;
    }

    /**
     * @param id id of hotel
     * @return view containing adding a new room
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, value = "{id}/rooms/new")
    public ModelAndView addRoom(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        Room room = new Room();
        room.setHotelId(id);
        return new ModelAndView("rooms/addroom", "room", room);
    }

    /**
     * Adds a new room from given form attributes.
     *
     * @param redir redirect attribute in Spring framework
     * @param id id of hotel
     * @param room room object constructed from form attributes
     * @return view containing list of rooms contained in given hotel
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.POST, value = "{id}/rooms/new")
    public ModelAndView addRoom(RedirectAttributes redir, @PathVariable("id") Long id,
                                     @ModelAttribute("room") Room room) throws SQLException, ClassNotFoundException {
        room.setHotelId(id);
        RoomDAO.createRoom(room);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Added Room (Room Number=%d)", room.getRoomNumber())));
        return new ModelAndView(String.format("redirect:/hotels/%d/rooms", id));
    }

    /**
     * @param id id of hotel
     * @param roomNumber room number of room
     * @return view containing options to edit room
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, value = "{id}/rooms/{roomNumber}")
    public ModelAndView editRoom(@PathVariable("id") Long id, @PathVariable("roomNumber") Integer roomNumber) throws SQLException, ClassNotFoundException {
        Room room = RoomDAO.getRoom(id, roomNumber);
        return new ModelAndView("rooms/editroom", "room", room);
    }

    /**
     * Edit room from the given form attributes.
     *
     * @param redir redirect attribute in Spring framework
     * @param id id of hotel
     * @param roomNumber room number of room
     * @param room room object constructed from form attributes
     * @return redirected view containing list of rooms contained in given hotel
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.POST, value = "{id}/rooms/edit/{roomNumber}")
    public ModelAndView editRoomById(RedirectAttributes redir, @PathVariable("id") Long id,
                                     @PathVariable("roomNumber") Integer roomNumber, @ModelAttribute("room") Room room) throws SQLException, ClassNotFoundException {
        room.setHotelId(id);
        RoomDAO.updateRoom(room);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Edited Room (Room Number=%d)", roomNumber)));
        return new ModelAndView(String.format("redirect:/hotels/%d/rooms", id));
    }

    /**
     * @param id id of hotel
     * @param roomNumber room number of room
     * @return view containing options to delete room
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, value = "{id}/rooms/delete/{roomNumber}")
    public ModelAndView deleteRoom(@PathVariable("id") Long id,
                                     @PathVariable("roomNumber") Integer roomNumber) throws SQLException, ClassNotFoundException {
        Room room = RoomDAO.getRoom(id, roomNumber);
        return new ModelAndView("rooms/deleteroom", "room", room);
    }

    /**
     * Delete a room based on given hotel id and room number.
     *
     * @param redir redirect attribute in Spring framework
     * @param id id of hotel
     * @param roomNumber room number of room
     * @return redirected view containing list of rooms contained in given hotel
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.POST, value = "{id}/rooms/delete/{roomNumber}")
    public ModelAndView deleteRoom(RedirectAttributes redir, @PathVariable("id") Long id,
                                   @PathVariable("roomNumber") Integer roomNumber) throws SQLException, ClassNotFoundException {
        RoomDAO.deleteRoom(id, roomNumber);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Deleted Room (Room Number=%d)", roomNumber)));
        return new ModelAndView(String.format("redirect:/hotels/%d/rooms", id));
    }

    /**
     * @param mv ModelView object in Spring framework
     * @param id id of hotel
     * @return view containing all available rooms of the given hotel
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, value = "{id}/rooms/all_available")
    public ModelAndView getAllAvailablerooms(ModelAndView mv, @PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        List<Room> rooms = ReportDAO.reportAllAvailable(id);
        mv.setViewName("rooms/all_available");
        mv.addObject("hotelId", id);
        mv.addObject("rooms", rooms);
        return mv;
    }

    /**
     * @param mv the model view to return the view through
     * @param startDate the earliest a stay can be in a room to consider the room "occupied"
     * @param endDate the latest a stay can be in a room to consider the room "occupied"
     * @param id the id of the hotel to look for occupied rooms in
     * @return a page with the list of rooms occupied sometime in the given interval.
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.POST, value = "rooms/all_occupied")
    public ModelAndView getAllOccupiedrooms(ModelAndView mv, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("hotelId") Long id) throws SQLException, ClassNotFoundException {
        List<Room> rooms = null;
        String percent = "";
        try {
            rooms = ReportDAO.reportOccupied(SQLTypeTranslater.stringToDate(startDate), SQLTypeTranslater.stringToDate(endDate), id);
            percent = ReportDAO.percentOccupied(SQLTypeTranslater.stringToDate(startDate), SQLTypeTranslater.stringToDate(endDate), id);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mv.setViewName("rooms/all_occupied");
        mv.addObject("hotelId", id);
        mv.addObject("percent", percent);
        mv.addObject("rooms", rooms);
        return mv;
    }

    /**
     * @return a form for the hotel and interval to get occupation information from.
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/occupied")
    public ModelAndView reportOccupied() throws SQLException, ClassNotFoundException {
        ModelAndView mv = new ModelAndView("hotels/occupied");
        return mv;
    }

    /**
     * @param mv the model view to return the view through
     * @param startDate the earliest a stay can be in a room to consider the room "occupied"
     * @param endDate the latest a stay can be in a room to consider the room "occupied"
     * @param id the id of the hotel to look for occupied rooms in
     * @return a page with the list of rooms unoccupied for this entire interval.
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.POST, value = "rooms/all_unoccupied")
    public ModelAndView getAllUnoccupiedrooms(ModelAndView mv, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("hotelId") Long id) throws SQLException, ClassNotFoundException {
        List<Room> rooms = null;
        String percent = "";
        try {
            rooms = ReportDAO.reportUnoccupanied(SQLTypeTranslater.stringToDate(startDate), SQLTypeTranslater.stringToDate(endDate), id);
            percent = ReportDAO.percentOccupied(SQLTypeTranslater.stringToDate(startDate), SQLTypeTranslater.stringToDate(endDate), id);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mv.setViewName("rooms/all_unoccupied");
        mv.addObject("hotelId", id);
        mv.addObject("percent", percent);
        mv.addObject("rooms", rooms);
        return mv;
    }

    /**
     * @return a form for the hotel and interval to get unoccupation information from.
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/unoccupied")
    public ModelAndView reportUnoccupied() throws SQLException, ClassNotFoundException {
        ModelAndView mv = new ModelAndView("hotels/unoccupied");
        return mv;
    }

    /**
     * @param mv the model view to return the view through
     * @param startDate the earliest a stay can be in a room to consider the room "occupied"
     * @param endDate the latest a stay can be in a room to consider the room "occupied"
     * @param id the id of the hotel to look for occupied rooms in
     * @param category the category of rooms considered
     * @param occupancy the minimum occupancy for rooms considered.
     * @return a page with the list of rooms unoccupied for this entire interval with the given category
     *          and at least the given occupancy.
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.POST, value = "rooms/all_availablerooms")
    public ModelAndView getAllAvailablerooms(ModelAndView mv, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("hotelId") Long id, @RequestParam("category") String category, @RequestParam("occupancy") int occupancy) throws SQLException, ClassNotFoundException {
        List<Room> rooms = null;
        try {
            rooms = ReportDAO.reportAvailable(category, SQLTypeTranslater.stringToDate(startDate), SQLTypeTranslater.stringToDate(endDate), id, occupancy);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mv.setViewName("rooms/all_availablerooms");
        mv.addObject("hotelId", id);
        mv.addObject("rooms", rooms);
        return mv;
    }

    /**
     * @return a form to enter getAllAvailable room parameters (including date range, category, and max_occupancy).
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/available")
    public ModelAndView reportAvailable() throws SQLException, ClassNotFoundException {
        ModelAndView mv = new ModelAndView("hotels/available");
        return mv;
    }

}
