package org.yearup;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class LedgerApp
{
    static Scanner scanner = new Scanner(System.in);

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
                    // Make payment
                    break;
                case "L":
                    // Display Ledger screen
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

    public void addDeposit()
    {
        // Prompt user for description, vendor & amount
        System.out.println("\n----------ADD-DEPOSIT----------\n");
        System.out.print("Enter a description: ");
        String description = scanner.nextLine().strip();
        System.out.print("Enter vendor name: ");
        String vendor = scanner.nextLine().strip();
        System.out.print("Enter amount: $");
        double amount = Double.parseDouble(scanner.nextLine().strip());

        // Set date & time to current
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        // Make new transaction w/ these values
        Transaction t = new Transaction(date, time, description, vendor, amount);

        // Write to file
        writeTransaction(t.getDate(), t.getFormattedTime(), t.getDescription(), t.getVendor(), t.getAmount());
    }

    // Write transaction to transactions.csv
    public void writeTransaction(LocalDate date, String time, String description, String vendor, double amount)
    {
        String fileName = "transactions.csv";
        FileWriter writer = null;

        try
        {
            // Append to file
            writer = new FileWriter(fileName, true);
            writer.write("\n" + date.toString() + "|" + time + "|" + description + "|" + vendor + "|" + String.format("%.2f", amount));
            System.out.println("\nDeposit added.");
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
        displayHome();
    }
}
