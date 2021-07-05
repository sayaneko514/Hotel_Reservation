package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Date;

public class HotelResource
{
    private final static HotelResource INSTANCE = new HotelResource();
    private HotelResource() {}
    public static HotelResource getInstance()
    {
        return INSTANCE;
    }

    private final CustomerService customerService = CustomerService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();

    public Customer getCustomer(String email)
    {
        return customerService.getCustomer(email);
    }

    public IRoom getRoom (String roomNumber)
    {
        return reservationService.getARoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail, IRoom room, Date checkIn, Date checkOut)
    {
        return reservationService.reserveARoom(getCustomer(customerEmail),room,checkIn,checkOut);
    }

    public Collection<IRoom> findARoom (Date checkIn, Date checkOut)
    {
        return reservationService.findRoom(checkIn,checkOut);
    }

    public void createACustomer(String email, String firstName, String lastName)
    {
        customerService.addCustomer(email,firstName,lastName);
    }

    public Collection<Reservation> getCustomerReservations(String customerEmail)
    {
        return reservationService.getCustomerReservation(getCustomer(customerEmail));
    }


}
