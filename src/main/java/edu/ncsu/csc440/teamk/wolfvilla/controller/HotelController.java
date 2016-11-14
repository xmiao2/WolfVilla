package edu.ncsu.csc440.teamk.wolfvilla.controller;

import edu.ncsu.csc440.teamk.wolfvilla.dao.HotelDAO;
import edu.ncsu.csc440.teamk.wolfvilla.dao.ReportDAO;
import edu.ncsu.csc440.teamk.wolfvilla.dao.RoomDAO;
import edu.ncsu.csc440.teamk.wolfvilla.model.Hotel;
import edu.ncsu.csc440.teamk.wolfvilla.model.Room;
import edu.ncsu.csc440.teamk.wolfvilla.util.FlashMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
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


    @RequestMapping(method = RequestMethod.GET, value = "editmanager/{id}")
    public ModelAndView getChangeManager(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        return new ModelAndView("hotels/editmanager", "id", id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "editmanager/{hotelId}")
    public ModelAndView changeManager(RedirectAttributes redir,
                                      @PathVariable("hotelId") Long hotelId,
                                      @RequestParam("manager") Long managerId) throws SQLException, ClassNotFoundException {
        HotelDAO.assignHotelManager(hotelId, managerId);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Edited Hotel (ID=%d)", hotelId)));
        return new ModelAndView("redirect:/hotels");
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
        Hotel hotel = new Hotel(id, null, address, name, phoneNumber);
        HotelDAO.updateHotel(hotel);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Edited Hotel (ID=%d)", hotel.getId())));
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
    @RequestMapping(method = RequestMethod.GET, value = "{id}/rooms")
    public ModelAndView listRoomsById(ModelAndView mv, @PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        List<Room> rooms = RoomDAO.listRooms(id);
        mv.setViewName("rooms/listrooms");
        mv.addObject("rooms", rooms);
        mv.addObject("hotelId", id);
        return mv;
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}/rooms/new")
    public ModelAndView addRoom(@PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        Room room = new Room();
        room.setHotelId(id);
        return new ModelAndView("rooms/addroom", "room", room);
    }

    @RequestMapping(method = RequestMethod.POST, value = "{id}/rooms/new")
    public ModelAndView addRoom(RedirectAttributes redir, @PathVariable("id") Long id,
                                     @ModelAttribute("room") Room room) throws SQLException, ClassNotFoundException {
        room.setHotelId(id);
        RoomDAO.createRoom(room);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Added Room (Room Number=%d)", room.getRoomNumber())));
        return new ModelAndView(String.format("redirect:/hotels/%d/rooms", id));
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}/rooms/{roomNumber}")
    public ModelAndView editRoom(@PathVariable("id") Long id, @PathVariable("roomNumber") Integer roomNumber) throws SQLException, ClassNotFoundException {
        Room room = RoomDAO.getRoom(id, roomNumber);
        return new ModelAndView("rooms/editroom", "room", room);
    }

    @RequestMapping(method = RequestMethod.POST, value = "{id}/rooms/edit/{roomNumber}")
    public ModelAndView editRoomById(RedirectAttributes redir, @PathVariable("id") Long id,
                                     @PathVariable("roomNumber") Integer roomNumber, @ModelAttribute("room") Room room) throws SQLException, ClassNotFoundException {
        room.setHotelId(id);
        RoomDAO.updateRoom(room);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Edited Room (Room Number=%d)", roomNumber)));
        return new ModelAndView(String.format("redirect:/hotels/%d/rooms", id));
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}/rooms/delete/{roomNumber}")
    public ModelAndView deleteRoom(@PathVariable("id") Long id,
                                     @PathVariable("roomNumber") Integer roomNumber) throws SQLException, ClassNotFoundException {
        Room room = RoomDAO.getRoom(id, roomNumber);
        return new ModelAndView("rooms/deleteroom", "room", room);
    }

    @RequestMapping(method = RequestMethod.POST, value = "{id}/rooms/delete/{roomNumber}")
    public ModelAndView deleteRoom(RedirectAttributes redir, @PathVariable("id") Long id,
                                   @PathVariable("roomNumber") Integer roomNumber) throws SQLException, ClassNotFoundException {
        RoomDAO.deleteRoom(id, roomNumber);
        redir.addFlashAttribute(MESSAGE, new FlashMessage(FlashMessage.MessageType.SUCCESS, String.format("Deleted Room (Room Number=%d)", roomNumber)));
        return new ModelAndView(String.format("redirect:/hotels/%d/rooms", id));
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}/rooms/all_available")
    public ModelAndView getAllAvailablerooms(ModelAndView mv, @PathVariable("id") Long id) throws SQLException, ClassNotFoundException {
        List<Room> rooms = ReportDAO.reportAllAvailable(id);
        mv.setViewName("rooms/all_available");
        mv.addObject("hotelId", id);
        mv.addObject("rooms", rooms);
        return mv;
    }
}
