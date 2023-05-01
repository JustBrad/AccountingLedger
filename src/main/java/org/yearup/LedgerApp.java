package org.yearup;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class LedgerApp
{
    static Scanner scanner = new Scanner(System.in);
    static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

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
        String formattedTime = time.format(timeFormatter);

        System.out.println(date);
        System.out.println(formattedTime);
        System.out.println(description);
        System.out.println(vendor);
        System.out.println(amount);
    }

    public void run()
    {
        displayHome();
    }
}
