import java.util.Scanner;
import java.util.ArrayList;
import java.util.*;
/**
 * Write a description of class PrimeEvent here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class PrimeEvent
{
    private User logedInUser;
    private ListOfUser users;
    private ListOfHall halls;
    
    public PrimeEvent()
    {
        logedInUser = new User();
        users = new ListOfUser();
        halls = new ListOfHall();
    }
    
    public PrimeEvent(User newUser,ListOfHall newHallList,ListOfUser newUserList)
    {
        logedInUser = newUser;
        users = newUserList;
        halls = newHallList;
    }

    public void startSystem()
    {
        UserInterface boundary = new UserInterface();
        Scanner sc = new Scanner(System.in);
        int index = 0;
        boolean bool = false;
        boundary.displayWelcomeMessage();
        while(bool == false)
        {
            boundary.displayMainMenu();
            index = Integer.parseInt(sc.nextLine());
            if(index == 1)
            {
                customerLogin();
                bool = true;
            }
            else if(index == 2)
            {
                System.out.println("Register function will coming soon...");
                bool = false;
            }
            else
                System.exit(0);
        }
        UserInterface.displayCustomerMainMenu();
    }

    public void customerLogin()
    {
        Scanner scan = new Scanner(System.in);
        String inputEmail;
        String inputPasswd;
        UserInterface boundary = new UserInterface();
        boolean bool = false;
        int index = 0;
        while(bool == false)
        {
            boundary.displayLoginMenu();
            inputEmail = scan.nextLine();
            bool = users.checkAUser(inputEmail);
            if(bool == true)
            {
                for(int i = 0; i < users.getListOfUser().size(); i++)
                {
                    if(users.getListOfUser().get(i).getEmailAddr().equals(inputEmail))
                        index = i;
                }
            }
            else
                System.out.println("Please input a valid user account.");
        }
        boolean bol = false;
        while(bol == false)
        {
            boundary.askForpasswd();
            logedInUser = users.getListOfUser().get(index);
            inputPasswd = scan.nextLine();
            if (inputPasswd.equals(logedInUser.getPassword()))
            {
                System.out.println("Welcome back!" + logedInUser.getFname());
                System.out.println("Press Enter to Continue!");
                scan.nextLine();
                customerMenu();
                bol = true;
            }
            else
            {
                System.out.println("Please input correct password!");
                bol = false;
            }
        }
    }

    public void customerMenu()
    {
        Scanner scan = new Scanner(System.in);
        UserInterface boundary = new UserInterface();
        UserInterface.displayCustomerMainMenu();
        String choice = scan.nextLine();
        switch(choice)
        {
            case "1": selectViewAllHall(); break;
            //case "2": selectViewQuotations(); break;
            //case "3": selectViewBookings(); break;
            //case "4": displayReceipt();break;
            case "5": break;
            default: boundary.displayInvalidInput();
        }

    }

    public void selectViewAllHall()
    {
        Scanner scan = new Scanner(System.in);
        UserInterface boundary = new UserInterface();
        ArrayList<Hall> availaHalls = halls.getListOfAvailableHalls();
        UserInterface.displayAllHallInfo(halls.getListOfAvailableHalls());
        String choice = scan.nextLine();
        int hallindexNo = 0;
        while(true)
        {
            if (choice.equals("q"))
            {
                customerMenu();
                break;
            }
            try
            {
                hallindexNo = Integer.parseInt(choice);
            }
            catch(Exception e)
            {
                boundary.displayInvalidInput();
                scan.nextLine();
            }
            if (hallindexNo > 0 && hallindexNo < availaHalls.size()+1)
            {
                int index = hallindexNo -1;
                Hall selectedHall = availaHalls.get(index);
                //selectHall(selectedHall);
                UserInterface.displayHallDetails(selectedHall);
                while(true)
                {

                    UserInterface.askForRFQ();
                    String choic = scan.nextLine();
                    if (choic.equals("y"))
                    {
                        setQuotation(selectedHall);
                        break;
                    }
                    if (choic.equals("n"))
                    {
                        //viewAllHalls();
                        break;
                    }
                    System.out.println("Invalid Input!");
                }
            }
        }

    }

    public void selectHall(Hall hall)
    {
        UserInterface.displayHallDetails(hall);
    }

    public float calculatePrice(Quotation quotation)
    {
        if(quotation.getIsCateringPreferred() == false)
            return quotation.getNoOfPeopleAttending() * quotation.getHall().getHallBasePrice();
        else
            return quotation.getNoOfPeopleAttending() * (quotation.getHall().getHallBasePrice() + 20);
    }

    public void setQuotation(Hall hall)
    {
        Scanner scan = new Scanner(System.in);
        UserInterface boundary = new UserInterface();
        boundary.displayQuotationMenu();
        Quotation quotation = new Quotation();
        String noOfPeople = scan.nextLine();
        int noOfpeople = Integer.parseInt(noOfPeople);
        quotation.setNoOfPeopleAttending(noOfpeople);
        quotation.setHall(hall);
        //quotation.setEstimatedPrice(calculatePrice(quotation));
        //boundary.displayEstimatedPrice(quotation.getEstimatedPrice());
        boundary.askForCaterPrefer();
        String caterpref = scan.nextLine();
        switch(caterpref)
        {
            case"y":
            quotation.setIscateringPreferred(true);
            break;
            case"n":
            quotation.setIscateringPreferred(false);
            break;
            default:
            boundary.displayInvalidInput();
            break;
        }
        quotation.setEstimatedPrice(calculatePrice(quotation));
        System.out.println("Your quotation information has been saved.");
        boundary.displayEstimatedPrice(quotation.getEstimatedPrice());
        boundary.displayQuotation(quotation);
    }

    public void MakeBooking(Quotation quotation)
    {
        Scanner scan = new Scanner(System.in);
        Booking booking = new Booking();
        float price = 0;
        UserInterface boundary = new UserInterface();
        booking.setUser(logedInUser);
        booking.setQuotation(quotation);
        booking.setBookingDate(new Date());
        System.out.println("Your quotation is now approved by the owner!");
        System.out.println("Process to Booking?(y/n)");
        String input = scan.nextLine();
        if(input.equals("y"))
        {
            System.out.println("These are the quotation details.");
            boundary.displayQuotation(quotation);
            //System.out.println("This hall provides a " + )
            //booking.setDiscountPercent();
            System.out.println("If you want to take this discount, Please input discount keyword: ");
            String key = scan.nextLine();
            if(key.equals("PRIME"))
            {
                price = quotation.getEstimatedPrice() * booking.getDiscountPercent();
                System.out.println("The total price for this booking is: " + price);
            }
            else
            {
                price = quotation.getEstimatedPrice();
                System.out.println("The total price for this booking is: " + price);
            }
            System.out.println("Press enter to continue.");
            scan.nextLine();
        }
        System.out.println("The deposit for this booking is: " + price * quotation.getHall().getDeposPercen());
        String str = "n";
        while(!str.equals("y"))
        {
            System.out.println("Deposit Confirm?(y/n)");
            str = scan.nextLine();
        }
        System.out.println("Your booking has been confirmed! Here is your booking details: ");
        booking.setBookingStatus(true);
        boundary.displayBookingDetail(booking);
    }
}
