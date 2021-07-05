package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.AdminService;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class AdminResource
{
    private final static AdminResource INSTANCE = new AdminResource();
    private AdminResource(){}
    public static AdminResource getInstance()
    {
        return INSTANCE;
    }

    private final CustomerService customerService = CustomerService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();
    private final AdminService adminService = AdminService.getInstance();

    public Customer getCustomer(String email)
    {
        return customerService.getCustomer(email);
    }

    public void addRoom(IRoom room)
    {
        reservationService.addRoom(room);
    }

    public Collection<IRoom> getAllRooms()
    {
        return reservationService.getAllRooms();
    }

    public Collection<Customer> getAllCustomers()
    {
        return customerService.getAllCustomer();
    }

    public Collection<Reservation> getAllReservations()
    {
        return reservationService.getAllReservation();
    }

    public void adminDefault()
    {
        adminService.resetAdminPassword();
    }

    public void adminChangePassword()
    {
        adminService.adminNewPassword();
    }

    public boolean adminLogin()
    {
        if (adminService.adminAuth(adminService.adminPassword()))
        {
            return true;
        }
        else
            return false;
    }
}
