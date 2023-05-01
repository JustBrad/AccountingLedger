package org.yearup;

import java.util.Scanner;

public class LedgerApp
{
    static Scanner scanner = new Scanner(System.in);

    // Display Home screen
    public void displayHome()
    {
        System.out.println("\n----------HOME-SCREEN----------\n");
        System.out.println("What would you like to do?\n");
        System.out.println("D) Add Deposit");
        System.out.println("P) Make Payment (Debit)");
        System.out.println("L) Display Ledger Screen");
        System.out.println("X) Exit\n");

        System.out.print("Enter an option: ");
        String option = scanner.nextLine().toUpperCase();

        while (true)
        {
            switch (option)
            {
                case "D":
                    // Add deposit
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
            }
        }
    }

    public void addDeposit()
    {
        
    }

    public void run()
    {
        displayHome();
    }
}
