package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.*;
import java.util.stream.Collectors;

public class ReservationService
{
    private final static ReservationService INSTANCE  = new ReservationService();
    private ReservationService (){}
    public static ReservationService getInstance()
    {
        return INSTANCE;
    }

    private final Map<String,IRoom> roomMap = new HashMap<String, IRoom>();

    private final Map<String, Collection<Reservation>> reservationMap = new HashMap<String, Collection<Reservation>>();

    public void addRoom(IRoom room)
    {
        roomMap.put(room.getRoomNumber(),room);
    }

    public IRoom getARoom (String roomID)
    {
        return roomMap.get(roomID);
    }

    public Collection<IRoom> getAllRooms()
    {
        return roomMap.values();
    }

    public Collection<Reservation> getCustomerReservation(Customer customer)
    {
        return reservationMap.get(customer.getEmail());
    }

    public Collection<IRoom> findRoom (Date checkIn, Date checkOut) {
        Collection<IRoom> bookedRooms = getAllBookedRooms(checkIn, checkOut);
        Collection<IRoom> vacantRooms = new LinkedList<IRoom>();

        for (IRoom room : getAllRooms())
        {
            if (!bookedRooms.contains(room))
            {
                vacantRooms.add(room);
            }
        }
        return vacantRooms;
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate)
    {
       if (vacancyCheck(room, checkInDate, checkOutDate))
        {
            return null;
        }
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
       Collection<Reservation> customerReservations = getCustomerReservation(customer);
       if (customerReservations == null)
       {
           customerReservations = new LinkedList<>();
       }
       customerReservations.add(reservation);
       reservationMap.put(customer.getEmail(), customerReservations);
       return reservation;
    }

    public Collection<Reservation> getAllReservation()
    {
        Collection<Reservation> reservationCollection = new LinkedList<Reservation>();

        for (Collection<Reservation> reservations: reservationMap.values())
        {
            reservationCollection.addAll(reservations);
        }

        return reservationCollection;
    }

    private static boolean vacancyCheck (IRoom room, Date checkIn, Date checkOut)
    {
       Collection<IRoom> bookedRooms = getAllBookedRooms(checkIn,checkOut);
       if (bookedRooms.contains(room))
       {
           return true;
       }
        return false;
    }

    private static Collection<IRoom> getAllBookedRooms(Date checkInDate, Date checkOutDate)
    {
        Collection<IRoom> booked = new LinkedList<>();
        for (Reservation reservation : getInstance().getAllReservation())
        {
            if (reservation.vacancyChecker(checkInDate, checkOutDate))
            {
                booked.add(reservation.getRoom());
            }
        }
        return booked;
    }
}
