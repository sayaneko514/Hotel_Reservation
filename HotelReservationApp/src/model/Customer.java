package model;
import java.util.regex.Pattern;

public class Customer
{
    private String firstName, lastName, email, emailRegex = "^(.+)@(.+).(.+)$";

    public Customer(String firstName, String lastName, String email)
    {
        Pattern emailPattern = Pattern.compile(emailRegex);
        if (!firstName.matches("^[a-zA-Z]*$"))
        {
            throw new IllegalArgumentException("First name should only contains letters");
        }
        else if (!lastName.matches("^[a-zA-Z]*$"))
        {
            throw new IllegalArgumentException("Last name should only contains letters");
        }
        else if(!emailPattern.matcher(email).matches())
        {
            throw new IllegalArgumentException("Please enter a email address with similar format to'name@domain.com'");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmailRegex()
    {
        return emailRegex;
    }

    public void setEmailRegex(String emailRegex)
    {
        this.emailRegex = emailRegex;
    }

    @Override
    public String toString()
    {
        return "Customer_Info{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
