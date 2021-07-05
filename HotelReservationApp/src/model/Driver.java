package model;

public class Driver
{
    public static void main(String[] args)
    {
        try
        {
            Customer customer = new Customer("Rui","2134sad@!d","hi");
            System.out.println(customer);
        }catch(IllegalArgumentException ex)
        {
            System.out.println(ex.getLocalizedMessage());
        }
    }
}
