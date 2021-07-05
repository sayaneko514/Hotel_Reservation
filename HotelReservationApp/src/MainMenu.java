import api.AdminResource;
import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class MainMenu
{
    private final static HotelResource hotelResource = HotelResource.getInstance();
    private final static AdminResource adminResource = AdminResource.getInstance();
    private static ArrayList<Reservation> reservationsList = new ArrayList<Reservation>();

    public static void mainMenu()
    {
        clearConsoleScreen();
        menuHead();
        menuMain();

    }

    public static void menuHead()
    {
        System.out.println(
                "================================================\n"+
                "=========|Hotel Reservation System 1.0|=========\n"+
                "================================================\n");
    }

    public static void menuMain()
    {
        System.out.print(
                " [*] Please select one of the following services\n"+
                "   |\n"+
                "   |--[1] Find and reserve a room\n"+
                "   |\n"+
                "   |--[2] See my reservations\n"+
                "   |\n"+
                "   |--[3] Create an account\n"+
                " \n"+
                " [4] Admin\n"+
                "\n"+
                " [5] Exit\n"+
                "================================================\n"+
                "Please input the option and press ENTER: ");

        Scanner input = new Scanner(System.in);
        String s = input.nextLine();
        if (isOptionValid(s))
        {
            switch (Integer.parseInt(s))
            {
                case 1:
                    getRoom();
                    break;
                case 2:
                    getUserReservation();
                    break;
                case 3:
                    getAccount();
                    break;
                case 4:
                    adminPortal();
                    break;
                case 5:
                    terminateOption();
                    break;
                default:
                    System.out.println("==================Invalid input==================");
                    clearConsoleScreen();
                    menuMain();
                    break;
                }
            }
        else
        {
            System.out.println("==================Invalid input==================");
            clearConsoleScreen();
            menuMain();
        }
    }

    public static boolean isOptionValid(String string)
    {
        try
        {
            if(Integer.parseInt(string) > 5 || Integer.parseInt(string) < 0)
            {
                return false;
            }
            else
                return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }

    private static void getRoom()
    {
        Date checkInDate = getCheckIn();
        Date checkOutDate = getCheckOut(checkInDate);
        Collection<IRoom> vacantRooms = hotelResource.findARoom(checkInDate,checkOutDate);
        boolean continueBooking = false;
        if (vacantRooms.isEmpty())
        {
            Date recommendCheckInDate = getNewDate(checkInDate);
            Date recommendCheckOutDate = getNewDate(checkOutDate);
            vacantRooms = hotelResource.findARoom(recommendCheckInDate, recommendCheckOutDate);
            if (!vacantRooms.isEmpty())
            {
                System.out.println("There are no vacant rooms that fit your schedule, do you want to try the date that is a week after?");
                continueBooking = listVacantRooms(vacantRooms);
                checkInDate = recommendCheckInDate;
                checkOutDate = recommendCheckOutDate;
            }
            else
            {
                System.out.println("There's no vacant room available for booking");
            }
        }
        else
        {
            continueBooking = listVacantRooms(vacantRooms);
        }

        if(!continueBooking)
        {
            System.out.println("Returning to main menu...");
            mainMenu();
        }

        Customer customer = getCurrentCustomer();

        IRoom room = getReservation(vacantRooms);
        Reservation reservation = hotelResource.bookARoom(customer.getEmail(),room,checkInDate,checkOutDate);
        if (reservation == null)
        {
            System.out.println("Sorry, the room you requested is already booked\nDo you want to book a new room? Y/N");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes"))
            {
                getRoom();
            }
            else
            {
                System.out.println("Returning to main menu...");
                mainMenu();
            }
        }
        else
        {
            System.out.println("Booking successful!\nYour "+reservation+"\nReturning to main menu...");
            mainMenu();

        }
    }

    private static boolean listVacantRooms(Collection<IRoom> vacantRoom)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("================================================");
        for (IRoom room : vacantRoom)
        {
            System.out.println("|-- " + room.toString()+"\n|");
        }
        System.out.println("================================================\nContinue with booking? Y/N");
        String userChoice = scanner.nextLine();
        if (userChoice.equalsIgnoreCase("y") || userChoice.equalsIgnoreCase("yes"))
        {
            return true;
        }
        else
            return false;
    }

    private static Customer getCurrentCustomer()
    {
        Scanner scanner = new Scanner(System.in);
        String email = "";
        System.out.println("Do you have an account with us? Y/N");
        String input = scanner.nextLine();
        if(input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("Yes"))
        {
            System.out.print("Please enter your Email: ");
            email = scanner.nextLine();
            Customer customer = hotelResource.getCustomer(email.toLowerCase());
            if (customer == null)
            {
                System.out.print("No matching email found, do you want to register an account with us first? Enter y/yes to register: ");
                String choice = scanner.nextLine();
                if (choice.equalsIgnoreCase("Y") || choice.equalsIgnoreCase("Yes"))
                {
                    getAccount();
                }
                else
                {
                    System.out.println("Returning to main menu...");
                    mainMenu();
                }
            }
        }
        else
        {
            System.out.print("Do you want to register an account with us first? Enter y/yes to register: ");
            String choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("Y") || choice.equalsIgnoreCase("Yes"))
            {
                getAccount();
            }
            else
            {
                System.out.println("Returning to main menu...");
                mainMenu();
            }
        }
        return hotelResource.getCustomer(email);
    }

    private static IRoom getReservation(Collection<IRoom> vacantRoom)
    {
        Scanner scanner = new Scanner(System.in);
        IRoom room = null;
        String roomNumber ="";
        boolean roomNumChecker = false;
        while(!roomNumChecker)
        {
            System.out.println("Please enter the room number you wish to book");
            roomNumber = scanner.nextLine();
            room = hotelResource.getRoom(roomNumber);
            try
            {
                if (room == null)
                {
                    System.out.println("Please enter a valid room number");
                }
                else
                {
                    if (!vacantRoom.contains(room))
                    {
                        System.out.println("The room is booked, please enter another room");
                    }
                    else
                        roomNumChecker = true;
                }
            }catch (Exception ex)
            {
                System.out.println("Hi, it's working at this line.");
            }

        }
        return room;
    }

    private static Date getCheckIn()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM/dd/yyyy");
        Date checkInDate = null;
        boolean isCheckInValid = false;
        while(!isCheckInValid)
        {
            System.out.print("Enter the date you wished to check in: ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            try
            {
                checkInDate = simpleDateFormat.parse(input);
                Date today = new Date();
                if (checkInDate.before(today))
                {
                    System.out.println("The earliest time you can check in is the day after "+ today);
                }
                else
                {
                    isCheckInValid = true;
                }
            }catch (ParseException ex)
            {
                System.out.println("Please enter the date as example 'JAN/01/2021'");
            }
        }
        return checkInDate;
    }

    private static Date getCheckOut(Date checkInDate)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM/dd/yyyy");
        Date checkOutDate = null;
        boolean isCheckOutValid = false;
        while(!isCheckOutValid)
        {
            System.out.print("Enter the date you wished to check out: ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            try
            {
                checkOutDate = simpleDateFormat.parse(input);
                if (checkOutDate.before(checkInDate))
                {
                    System.out.println("Invalid date selection, you must check in before you can check out.");
                }
                else
                {
                    isCheckOutValid = true;
                }
            }catch(ParseException ex)
            {
                System.out.println("Please enter the date as example 'JAN/01/2021'");
            }
        }
        return checkOutDate;
    }

    private static Date getNewDate(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 7);
        return calendar.getTime();
    }

    private static void getUserReservation()
    {
        String emailRegex = "^(.+)@(.+).(.+)$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        Scanner input = new Scanner(System.in);
        System.out.print("Please Enter your email: ");
        String userEmail = input.nextLine().trim();
        if(!emailPattern.matcher(userEmail).matches())
        {
            System.out.println("Please enter a email address with similar format to'name@domain.com'");
            goBackOption();
        }
        else
        {
            Customer customer = hotelResource.getCustomer(userEmail.toLowerCase());
            if (customer == null)
            {
                System.out.println("No matching email found, returning to main menu...\n");
                mainMenu();
            }
            else
            {
                Collection<Reservation> reservations = hotelResource.getCustomerReservations(customer.getEmail());
                if (reservations == null)
                {
                    System.out.println("You currently have no room reserved under the account.");
                }
                else
                {
                    System.out.println("================================================\n|");
                    for (Reservation reservation : reservations)
                    {
                        reservationsList.add(reservation);
                        int index = reservationsList.indexOf(reservation)+1;
                        System.out.println("|--[" + index + "] " + reservation.toString()+"\n");
                    }
                }
                System.out.println("================================================");
                goBackOption();
            }
        }
    }

    private static void getAccount()
    {
        Scanner input = new Scanner(System.in);
        String email = null;
        boolean statusCheck = false;
        while (!statusCheck)
        {
            try
            {
                System.out.print("Please enter your first name: ");
                String firstName = input.nextLine();
                System.out.print("Please enter your last name: ");
                String lastName = input.nextLine();
                System.out.print("Please enter your e-Mail: ");
                email = input.nextLine().toLowerCase();
                hotelResource.createACustomer(email,firstName,lastName);
                System.out.println("Account created, " + hotelResource.getCustomer(email)+"\nReturning to main menu...");
                statusCheck = true;
            }catch (IllegalArgumentException ex)
            {
                System.out.println(ex.getLocalizedMessage());
            }
        }
        clearConsoleScreen();
        mainMenu();
    }

    private static void goBackOption()
    {
        System.out.println("Enter 'B' to return to the main menu, anything else to terminate the program");
        Scanner option = new Scanner(System.in);
        String userOption = option.nextLine();
        if (userOption.equals("B"))
        {
            System.out.println("Returning to main menu...");
            clearConsoleScreen();
            mainMenu();
        }
        else
        {
            terminateOption();
        }
    }

    private static void terminateOption()
    {
        System.out.println("Exiting program...");
        System.exit(0);
    }

    private static void adminPortal()
    {
        if (adminResource.adminLogin())
        {
            AdminMenu.mainMenu();
        }
        else
        {
            System.out.println("The password you entered is incorrect, returning to main menu...\n");
            MainMenu.mainMenu();
        }
    }

    private static void clearConsoleScreen()
    {
        //doesn't seem to work with Intellij IDE
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
