import api.AdminResource;
import api.HotelResource;
import model.*;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class AdminMenu
{
    private final static HotelResource hotelResource = HotelResource.getInstance();
    private final static AdminResource adminResource = AdminResource.getInstance();

    private static ArrayList<Customer> customerList = new ArrayList<Customer>();
    private static ArrayList<IRoom> roomList = new ArrayList<IRoom>();

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
                "=============|Administration Panel|=============\n"+
                "================================================\n");
    }

    public static void menuMain()
    {
        System.out.print(
                " [*] Please select one of the following services\n"+
                        "   |\n"+
                        "   |--[1] View all customers\n"+
                        "   |\n"+
                        "   |--[2] View all rooms\n"+
                        "   |\n"+
                        "   |--[3] View all reservations\n"+
                        "   |\n"+
                        "   |--[4] Add a new room\n"+
                        "   |\n"+
                        "   |--[5] Change admin password\n"+
                        "\n"+
                        "[6] Return to the main menu\n"+
                        "================================================\n"+
                        "Please input the option and press ENTER: ");

        Scanner input = new Scanner(System.in);
        String s = input.nextLine();
        if (isOptionValid(s))
        {
            switch (Integer.parseInt(s))
            {
                case 1:
                    getAllCustomers();
                    break;
                case 2:
                    getAllRooms();
                    break;
                case 3:
                    getAllReservations();
                    break;
                case 4:
                    addRoom();
                    break;
                case 5:
                    changePassowrd();
                    break;
                case 6:
                    returnToMain();
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

    public static void getAllCustomers()
    {
        Collection<Customer> allCustomers = adminResource.getAllCustomers();
        if (allCustomers.isEmpty())
        {
            System.out.println("There's no customer at the moment.");
        }
        else
        {
            System.out.println("================================================\n|");
            for (Customer customer : allCustomers)
            {
                customerList.add(customer);
                int index = customerList.indexOf(customer)+1;
                System.out.println("|--[" + index + "] " + customer.toString()+"\n|");
            }
        }
        System.out.println("================================================");
        goBackOption();
    }

    public static void getAllRooms()
    {
        Collection<IRoom> allRooms = adminResource.getAllRooms();
        if (allRooms.isEmpty())
        {
            System.out.println("There's no room at the moment.");
        }
        else
        {
            System.out.println("================================================\n|");
            for (IRoom room : allRooms)
            {
                roomList.add(room);
                int index = roomList.indexOf(room)+1;
                System.out.println("|--[" + index + "] " + room.toString()+"\n|");
            }
        }
        System.out.println("================================================");
        goBackOption();
    }

    public static void getAllReservations()
    {
        Collection<Reservation> allReservations = adminResource.getAllReservations();
        if (allReservations.isEmpty())
        {
            System.out.println("There's no reservation at the moment.");
        }
        else
        {
            System.out.println("================================================\n|");
            for (Reservation reservation : allReservations)
            {
                System.out.println("|-- " + reservation.toString()+"\n");
            }
        }
        System.out.println("================================================");
        goBackOption();
    }

    public static void addRoom()
    {
        String roomNumber = null;
        Double price = 0.00;
        RoomType roomType = null;
        boolean roomLoop, priceLoop, typeLoop;

        Scanner input = new Scanner(System.in);
        roomLoop = false;
        while (!roomLoop)
        {
            System.out.print("Please enter the room number: ");
            roomNumber = input.nextLine();
            IRoom roomDup = hotelResource.getRoom(roomNumber);
            if (roomDup == null)
            {
                try {
                    int numCheck = Integer.parseInt(roomNumber);
                    roomLoop = true;
                } catch (NumberFormatException ex) {
                    System.out.println("Please enter a valid number\n================================================");
                    roomLoop = false;
                }
            }
            else
            {
                System.out.println("The room number you entered has already been assigned\n================================================");
                roomLoop = false;
            }

        }

        priceLoop = false;
        while(!priceLoop)
        {
            try
            {
                System.out.print("Please enter the room price: $");
                price = input.nextDouble();
                if (price < 0)
                {
                    System.out.println("Please enter a valid price\n================================================");
                }
                else
                {
                    priceLoop = true;
                }
            } catch (Exception ex) {
                System.out.println("Please enter a valid price\n================================================");
                priceLoop = false;
            }
        }

        typeLoop = false;
        while(!typeLoop)
        {
            try {
                System.out.print("Select from the following room type:\n" +
                                "|\n" +
                                "|--[1] Single bed room\n" +
                                "|\n" +
                                "|--[2] Double bed room\n" +
                                "|\n" +
                                "===============================================\n" +
                        "Please enter your selection: ");
                int typeCheck = input.nextInt();
                if (typeCheck > 2 || typeCheck < 1) {
                    System.out.println("Please make a valid selection\n================================================");
                } else
                {
                    if (typeCheck == 1)
                    {
                        roomType = RoomType.SINGLE;
                    }
                    else if (typeCheck == 2)
                    {
                        roomType = RoomType.DOUBLE;
                    }
                    typeLoop = true;
                }
            }catch (Exception ex)
            {
                System.out.println("Please make a valid selection\n================================================");
                typeLoop = false;
            }
        }

        Room newRoom = new Room (roomNumber,price,roomType);
        adminResource.addRoom(newRoom);
        System.out.println("Room added, " + newRoom);
        System.out.println("================================================\nContinue to add more room? Y/N");
        Scanner choice = new Scanner(System.in);
        String option = choice.nextLine();
        if (option.equalsIgnoreCase("y") || option.equalsIgnoreCase("yes"))
        {
            addRoom();
        }
        else if (option.equalsIgnoreCase("n") || option.equalsIgnoreCase("no"))
        {
            goBackOption();
        }
        else
        {
            System.out.println("Invalid responses detected, returning to main menu...");
            clearConsoleScreen();
            mainMenu();
        }

    }

    public static boolean isOptionValid(String string)
    {
        try
        {
            if(Integer.parseInt(string) > 6 || Integer.parseInt(string) < 0)
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

    private static void returnToMain()
    {
        MainMenu.mainMenu();
    }

    private static void changePassowrd()
    {
        if (adminResource.adminLogin())
        {
            System.out.print("Please select the following options:\n" +
                               "|--[1] Change the password\n"+
                               "|\n"+
                               "|--[2] Reset password to default\n"+
                               "================================================\n"+
                               "Please input the option and press ENTER: ");
            Scanner input = new Scanner(System.in);
            String s = input.nextLine();
            if(isOptionValid(s))
            {
                switch (Integer.parseInt(s))
                {
                    case 1:
                        adminResource.adminChangePassword();
                        System.out.println("Password changed!");
                        mainMenu();
                        break;
                    case 2:
                        adminResource.adminDefault();
                        System.out.println("Password changed!");
                        mainMenu();
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
        else
        {
            System.out.print("The password you entered is incorrect\n================================================\n");
            clearConsoleScreen();
            menuMain();
        }

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

    private static void clearConsoleScreen()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
