package org.yearup;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class LedgerApp
{
    static String fileName = "transactions.csv";
    static Scanner scanner = new Scanner(System.in);

    // Returns negative value of given number
    public double makeNegative(double num)
    {
        return 0 - num;
    }

    // Display Home screen
    public void displayHome()
    {
        while (true)
        {
            System.out.println("\n----------HOME-SCREEN----------\n");
            System.out.println("What would you like to do?\n");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Display Ledger Screen");
            System.out.println("X) Exit\n");

            System.out.print("Enter an option: ");
            String option = scanner.nextLine().toUpperCase();

            switch (option)
            {
                case "D":
                    addDeposit();
                    break;
                case "P":
                    makePayment();
                    break;
                case "L":
                    displayLedger();
                    break;
                case "X":
                    System.out.println("\nExiting...");
                    return;
                default:
                    System.out.println("\nInvalid option.");
                    break;
            }
        }
    }

    // Display Ledger screen
    public void displayLedger()
    {
        while (true)
        {
            System.out.println("\n----------LEDGER----------\n");
            System.out.println("What would you like to display?\n");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home\n");

            System.out.print("Enter an option: ");
            String option = scanner.nextLine().toUpperCase();

            switch (option)
            {
                case "A":
                    //All
                    break;
                case "D":
                    //Deposits
                    break;
                case "P":
                    //Payments
                    break;
                case "R":
                    //Reports
                    break;
                case "H":
                    return;
                default:
                    System.out.println("\nInvalid option.");
                    break;
            }
        }
    }

    // Append a deposit to csv
    public void addDeposit()
    {
        // Prompt user for description, vendor & amount
        System.out.println("\n----------ADD-DEPOSIT----------\n");
        System.out.print("Enter a description: ");
        String description = scanner.nextLine().strip();
        System.out.print("Enter vendor name: ");
        String vendor = scanner.nextLine().strip();
        double amount;

        while(true)
        {
            System.out.print("Enter amount: $");
            amount = Double.parseDouble(scanner.nextLine().strip());

            // Handle 0 or negative values
            if(amount > 0)
            {
                break;
            }
            else if(amount < 0)
            {
                System.out.println("\nThe amount cannot be negative.\n");
            }
            else
            {
                System.out.println("The amount cannot be zero.\n");
            }
        }

        // Set date & time to current
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        // Make new transaction w/ these values
        Transaction t = new Transaction(date, time, description, vendor, amount);

        // Write to file
        writeTransaction(t.getDate(), t.getFormattedTime(), t.getDescription(), t.getVendor(), t.getAmount());
    }

    // Append a payment to csv
    public void makePayment()
    {
        // Prompt user for description, vendor & amount
        System.out.println("\n----------MAKE-PAYMENT----------\n");
        System.out.print("Enter a description: ");
        String description = scanner.nextLine().strip();
        System.out.print("Enter vendor name: ");
        String vendor = scanner.nextLine().strip();
        double amount;

        while(true)
        {
            System.out.print("Enter amount: $");
            amount = Double.parseDouble(scanner.nextLine().strip());

            // Handle 0 or negative values
            if(amount > 0)
            {
                amount = makeNegative(amount);
                break;
            }
            else if(amount < 0)
            {
                System.out.println("\nThe amount cannot be negative.\n");
            }
            else
            {
                System.out.println("The amount cannot be zero.\n");
            }
        }

        // Set date & time to current
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        // Make new transaction w/ these values
        Transaction t = new Transaction(date, time, description, vendor, amount);

        // Write to file
        writeTransaction(t.getDate(), t.getFormattedTime(), t.getDescription(), t.getVendor(), t.getAmount());
    }

    // Overwrite csv and add first line
    public void resetCsv()
    {
        FileWriter writer = null;

        try
        {
            // Append to file
            writer = new FileWriter(fileName);
            writer.write("date|time|description|vendor|amount");
            System.out.println("\n'transactions.csv' has been reset.");
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(writer != null)
                {
                    writer.close();
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    // Write transaction to transactions.csv
    public void writeTransaction(LocalDate date, String time, String description, String vendor, double amount)
    {
        FileWriter writer = null;

        try
        {
            // Append to file
            writer = new FileWriter(fileName, true);
            writer.write("\n" + date.toString() + "|" + time + "|" + description + "|" + vendor + "|" + String.format("%.2f", amount));
            if(amount > 0)
            {
                System.out.printf("\nDeposit added. (+$%.2f)\n", amount);
            }
            else
            {
                System.out.printf("\nPayment added. (-$%.2f)\n", makeNegative(amount));
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(writer != null)
                {
                    writer.close();
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void run()
    {
        resetCsv();
        displayHome();
    }
}
