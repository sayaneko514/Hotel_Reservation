package service;

import java.util.Scanner;

public class AdminService
{
    private String password = "admin";

    private final static AdminService INSTANCE = new AdminService();
    private AdminService(){}
    public static AdminService getInstance()
    {
        return INSTANCE;
    }

    public final String resetAdminPassword()
    {
        return password = "admin";
    }

    public String adminNewPassword()
    {
        Scanner input = new Scanner(System.in);
        System.out.print("Please enter the new admin password: ");
        return password = input.nextLine();
    }

    public String adminPassword()
    {
        Scanner input = new Scanner(System.in);
        System.out.print("Please enter the admin password: ");
        String inputPassword = input.nextLine();
        return inputPassword;
    }

    public boolean adminAuth (String inputPassword)
    {
        if (inputPassword.equals(password))
        {
            return true;
        }
        else
            return false;
    }
}
