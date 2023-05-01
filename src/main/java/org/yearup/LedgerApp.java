package org.yearup;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class LedgerApp
{
    static String fileName = "transactions.csv";
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Transaction> transactions = new ArrayList<>();
    static ArrayList<Transaction> deposits = new ArrayList<>();
    static ArrayList<Transaction> payments = new ArrayList<>();

    // Returns negative value of given number
    public double makeNegative(double num)
    {
        return 0 - num;
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
                    showAll(transactions);
                    break;
                case "D":
                    showAll(deposits);
                    break;
                case "P":
                    showAll(payments);
                    break;
                case "R":
                    displayReports();
                    break;
                case "H":
                    return;
                default:
                    System.out.println("\nInvalid option.");
                    break;
            }
        }
    }

    // Display Reports screen
    public void displayReports()
    {
        while (true)
        {
            System.out.println("\n----------REPORTS----------\n");
            System.out.println("What would you like to do?\n");
            System.out.println("1) Generate Month to Date");
            System.out.println("2) Generate Previous Month");
            System.out.println("3) Generate Year to Date");
            System.out.println("4) Generate Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");
            System.out.println("H) Home\n");

            System.out.print("Enter an option: ");
            String option = scanner.nextLine().toUpperCase();

            switch (option)
            {
                case "1":
                    //month to date
                    break;
                case "2":
                    //prev month
                    break;
                case "3":
                    //year to date
                    break;
                case "4":
                    //prev year
                    break;
                case "5":
                    //search by vendor
                    break;
                case "0":
                    return;
                case "H":
                    displayHome();
                    break;
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
        System.out.print("Enter a date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter a time (HH:MM:SS): ");
        LocalTime time = LocalTime.parse(scanner.nextLine());
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

        // Make new transaction w/ these values
        Transaction t = new Transaction(date, time, description, vendor, amount);

        // Add to respective arraylists
        transactions.add(t);
        deposits.add(t);

        // Write to file
        writeTransaction(t.getDate(), t.getFormattedTime(), t.getDescription(), t.getVendor(), t.getAmount());
    }

    // Append a payment to csv
    public void makePayment()
    {
        // Prompt user for description, vendor & amount
        System.out.println("\n----------MAKE-PAYMENT----------\n");
        System.out.print("Enter a date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter a time (HH:MM:SS): ");
        LocalTime time = LocalTime.parse(scanner.nextLine());
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

        // Make new transaction w/ these values
        Transaction t = new Transaction(date, time, description, vendor, amount);

        // Add to respective lists
        transactions.add(t);
        payments.add(t);

        // Write to file
        writeTransaction(t.getDate(), t.getFormattedTime(), t.getDescription(), t.getVendor(), t.getAmount());
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

    // Read file & add entries to variables (ONLY FOR INITIAL RUN)
    public void readCsv()
    {
        FileInputStream fileStream = null;
        Scanner fileScanner = null;

        try
        {
            // Create file stream & scanner
            fileStream = new FileInputStream(fileName);
            fileScanner = new Scanner(fileStream);

            // Skip first line (columns)
            fileScanner.nextLine();

            // Scan through entire csv
            while(fileScanner.hasNextLine())
            {
                String line = fileScanner.nextLine();
                String[] columns = line.split("\\|");

                LocalDate date = LocalDate.parse(columns[0]);
                LocalTime time = LocalTime.parse(columns[1]);
                String description = columns[2];
                String vendor = columns[3];
                double amount = Double.parseDouble(columns[4]);

                // Create new transaction obj for each line
                Transaction t = new Transaction(date, time, description, vendor, amount);

                // Add entries to respective lists
                transactions.add(t);
                if(amount > 0)
                {
                    deposits.add(t);
                }
                else
                {
                    payments.add(t);
                }
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
                if(fileStream != null && fileScanner != null)
                {
                    fileStream.close();
                    fileScanner.close();
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    // Show all entries of specified ArrayList (transactions, deposits, payments)
    public void showAll(ArrayList<Transaction> list)
    {
        if(list == transactions) {System.out.println("\n----------SHOW-ALL----------\n");}
        else if(list == deposits) {System.out.println("\n----------DEPOSITS----------\n");}
        else if(list == payments) {System.out.println("\n----------PAYMENTS----------\n");}
        System.out.println("Date\t\t\tTime\t\t\tDescription\t\t\t\t\t\t\t\t Vendor\t\t\t\t\t  Amount");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        for(Transaction t : list)
        {
            System.out.printf("%-15s %-15s %-40s %-20s %10s\n", t.getDate(), t.getFormattedTime(), t.getDescription(), t.getVendor(), t.getAmount());
            System.out.println("--------------------------------------------------------------------------------------------------------");
        }
    }

    public void run()
    {
        // Read csv once to get existing data
        readCsv();
        displayHome();
    }
}
