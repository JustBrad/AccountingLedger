package org.yearup;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LedgerApp
{
    // Create input scanner & variable for CSV file
    static String fileName = "transactions.csv";
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Transaction> transactions = new ArrayList<>();
    static ArrayList<Transaction> deposits = new ArrayList<>();
    static ArrayList<Transaction> payments = new ArrayList<>();
    static HashMap<Transaction, String> vendorMap = new HashMap<>();

    // Returns negative value of given number
    public double makeNegative(double num)
    {
        return 0 - num;
    }

    // Print a formatted entry for Transaction t
    public void printEntry(Transaction t)
    {
        System.out.printf("%-15s %-15s %-40s %-20s %10s\n", t.getDate(), t.getFormattedTime(), t.getDescription(), t.getVendor(), String.format("%.2f", t.getAmount()));
        System.out.println("--------------------------------------------------------------------------------------------------------");
    }

    // Overwrite csv and add first line
    public void resetCsv()
    {
        FileWriter writer = null;

        try
        {
            // Append to file
            writer = new FileWriter(fileName);
            writer.write("DATE|TIME|DESCRIPTION|VENDOR|AMOUNT");
            System.out.println("\nThe 'transactions.csv' file has been reset.");
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
            String option = scanner.nextLine().toUpperCase().strip();

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
                    System.exit(0);
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
            String option = scanner.nextLine().toUpperCase().strip();

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
            System.out.println("6) Custom Search");
            System.out.println("0) Back");
            System.out.println("H) Home\n");

            System.out.print("Enter an option: ");
            String option = scanner.nextLine().toUpperCase().strip();

            switch (option)
            {
                case "1":
                    generateMonthToDate();
                    break;
                case "2":
                    generatePreviousMonth();
                    break;
                case "3":
                    generateYearToDate();
                    break;
                case "4":
                    generatePreviousYear();
                    break;
                case "5":
                    searchByVendor();
                    break;
                case "6":
                    customSearch();
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
        // Prompt user
        System.out.println("\n----------ADD-DEPOSIT----------\n");
        LocalDate date;
        LocalTime time;
        while(true)
        {
            try
            {
                System.out.print("Enter a date (YYYY-MM-DD): ");
                date = LocalDate.parse(scanner.nextLine().strip());
                System.out.print("Enter a time (HH:MM:SS): ");
                time = LocalTime.parse(scanner.nextLine().strip());
                break;
            }
            catch(Exception e)
            {
                System.out.println("\nInvalid date/time.\n");
            }
        }
        System.out.print("Enter a description: ");
        String description = scanner.nextLine().strip().toUpperCase();
        System.out.print("Enter vendor name: ");
        String vendor = scanner.nextLine().strip().toUpperCase();
        double amount;

        // Prevent negatives & zero
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

        // Add entries to vendor HashMap
        vendorMap.put(t, vendor.toUpperCase());

        // Write to file
        writeTransaction(t.getDate(), t.getFormattedTime(), t.getDescription(), t.getVendor(), t.getAmount());
    }

    // Append a payment to csv
    public void makePayment()
    {
        // Prompt user for description, vendor & amount
        System.out.println("\n----------MAKE-PAYMENT----------\n");
        LocalDate date;
        LocalTime time;
        while(true)
        {
            try
            {
                System.out.print("Enter a date (YYYY-MM-DD): ");
                date = LocalDate.parse(scanner.nextLine().strip());
                System.out.print("Enter a time (HH:MM:SS): ");
                time = LocalTime.parse(scanner.nextLine().strip());
                break;
            }
            catch(Exception e)
            {
                System.out.println("\nInvalid date/time.\n");
            }
        }
        System.out.print("Enter a description: ");
        String description = scanner.nextLine().strip().toUpperCase();
        System.out.print("Enter vendor name: ");
        String vendor = scanner.nextLine().strip().toUpperCase();
        double amount;

        // Prevent negatives & zero
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

        // Add entries to vendor HashMap
        vendorMap.put(t, vendor.toUpperCase());

        // Write to file
        writeTransaction(t.getDate(), t.getFormattedTime(), t.getDescription(), t.getVendor(), t.getAmount());
    }

    // Write transaction to transactions.csv
    public void writeTransaction(LocalDate date, String time, String description, String vendor, double amount)
    {
        // Initialize FileWriter
        FileWriter writer = null;

        // Catch errors
        try
        {
            // Open file in APPEND mode
            writer = new FileWriter(fileName, true);
            writer.write("\n" + date.toString() + "|" + time + "|" + description + "|" + vendor + "|" + String.format("%.2f", amount));

            // Decide if deposit or payment
            if(amount > 0)
            {
                System.out.printf("\nDeposit added. (+$%.2f)\n", amount);
            }
            else
            {
                // If payment, then make number negative
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
        // Initialize input stream & file scanner
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
                String line = fileScanner.nextLine().toUpperCase();
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

                // Add entries to vendor HashMap
                vendorMap.put(t, vendor.toUpperCase());
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
            printEntry(t);
        }
    }

    // Generates report for Month to Date (Prompted for current date)
    public void generateMonthToDate()
    {
        LocalDate currentDate;

        // Handle invalid date
        while(true)
        {
            try
            {
                System.out.print("Enter current date: ");
                currentDate = LocalDate.parse(scanner.nextLine().strip());
                break;
            }
            catch(Exception e)
            {
                System.out.println("\nInvalid date.\n");
            }
        }
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        double totalAmount = 0.0;

        System.out.println("\n----------MONTH-TO-DATE----------\n");
        System.out.println("CURRENT DATE: " + currentDate.getMonth() + " " + currentDate.getDayOfMonth() + ", " + currentDate.getYear() + "\n");

        System.out.println("Date\t\t\tTime\t\t\tDescription\t\t\t\t\t\t\t\t Vendor\t\t\t\t\t  Amount");
        System.out.println("--------------------------------------------------------------------------------------------------------");

        for(Transaction t : transactions)
        {
            // If entry is from start of month (including the 1st) to current date (including current date)
            if(t.getDate().isAfter(firstDayOfMonth.minusDays(1)) && t.getDate().isBefore(currentDate.plusDays(1)))
            {
                printEntry(t);
                totalAmount += t.getAmount();
            }
        }

        System.out.printf("\nTOTAL: $%.2f\n", totalAmount);
    }

    // Generates report for Previous Month (Prompted for current date)
    public void generatePreviousMonth()
    {
        LocalDate currentDate;

        // Handle invalid date
        while(true)
        {
            try
            {
                System.out.print("Enter current date: ");
                currentDate = LocalDate.parse(scanner.nextLine().strip());
                break;
            }
            catch(Exception e)
            {
                System.out.println("\nInvalid date.\n");
            }
        }
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        LocalDate firstDayOfPreviousMonth = firstDayOfMonth.minusMonths(1);
        double totalAmount = 0.0;

        System.out.println("\n----------PREVIOUS-MONTH----------\n");
        System.out.println("CURRENT DATE: " + currentDate.getMonth() + " " + currentDate.getDayOfMonth() + ", " + currentDate.getYear() + "\n");
        System.out.println("PREVIOUS MONTH: " + firstDayOfPreviousMonth.getMonth() + "\n");

        System.out.println("Date\t\t\tTime\t\t\tDescription\t\t\t\t\t\t\t\t Vendor\t\t\t\t\t  Amount");
        System.out.println("--------------------------------------------------------------------------------------------------------");

        for(Transaction t : transactions)
        {
            // If entry is from start of previous month (including the 1st) to end of previous month
            if(t.getDate().isAfter(firstDayOfPreviousMonth.minusDays(1)) && t.getDate().isBefore(firstDayOfMonth))
            {
                printEntry(t);
                totalAmount += t.getAmount();
            }
        }

        System.out.printf("\nTOTAL: $%.2f\n", totalAmount);
    }

    // Generates report for Year to Date (Prompted for current date)
    public void generateYearToDate()
    {
        LocalDate currentDate;

        // Handle invalid date
        while(true)
        {
            try
            {
                System.out.print("Enter current date: ");
                currentDate = LocalDate.parse(scanner.nextLine().strip());
                break;
            }
            catch(Exception e)
            {
                System.out.println("\nInvalid date.\n");
            }
        }
        LocalDate firstDayOfYear = currentDate.withDayOfYear(1);
        double totalAmount = 0.0;

        System.out.println("\n----------YEAR-TO-DATE----------\n");
        System.out.println("CURRENT DATE: " + currentDate.getMonth() + " " + currentDate.getDayOfMonth() + ", " + currentDate.getYear() + "\n");
        System.out.println("YEAR OF: " + currentDate.getYear() + "\n");

        System.out.println("Date\t\t\tTime\t\t\tDescription\t\t\t\t\t\t\t\t Vendor\t\t\t\t\t  Amount");
        System.out.println("--------------------------------------------------------------------------------------------------------");

        for(Transaction t : transactions)
        {
            // If entry is from start of year (including the 1st) to current date (including current date)
            if(t.getDate().isAfter(firstDayOfYear.minusDays(1)) && t.getDate().isBefore(currentDate.plusDays(1)))
            {
                printEntry(t);
                totalAmount += t.getAmount();
            }
        }

        System.out.printf("\nTOTAL: $%.2f\n", totalAmount);
    }

    // Generates report for Previous Year (Prompted for current date)
    public void generatePreviousYear()
    {
        LocalDate currentDate;

        // Handle invalid date
        while(true)
        {
            try
            {
                System.out.print("Enter current date: ");
                currentDate = LocalDate.parse(scanner.nextLine().strip());
                break;
            }
            catch(Exception e)
            {
                System.out.println("\nInvalid date.\n");
            }
        }
        LocalDate firstDayOfYear = currentDate.withDayOfYear(1);
        LocalDate firstDayOfPreviousYear = firstDayOfYear.minusYears(1);
        double totalAmount = 0.0;

        System.out.println("\n----------PREVIOUS-YEAR----------\n");
        System.out.println("CURRENT DATE: " + currentDate.getMonth() + " " + currentDate.getDayOfMonth() + ", " + currentDate.getYear() + "\n");
        System.out.println("YEAR OF: " + firstDayOfPreviousYear.getYear() + "\n");

        System.out.println("Date\t\t\tTime\t\t\tDescription\t\t\t\t\t\t\t\t Vendor\t\t\t\t\t  Amount");
        System.out.println("--------------------------------------------------------------------------------------------------------");

        for(Transaction t : transactions)
        {
            // If entry is from start of previous year (including the 1st) to end of previous year
            if(t.getDate().isAfter(firstDayOfPreviousYear.minusDays(1)) && t.getDate().isBefore(firstDayOfYear))
            {
                printEntry(t);
                totalAmount += t.getAmount();
            }
        }

        System.out.printf("\nTOTAL: $%.2f\n", totalAmount);
    }

    // Lists all entries for specified vendor
    public void searchByVendor()
    {
        // Prompt user for vendor
        System.out.println("\n----------SEARCH-BY-VENDOR----------\n");
        System.out.print("Enter vendor name (partial/full): ");
        String vendor = scanner.nextLine().toUpperCase();

        System.out.println("\nDate\t\t\tTime\t\t\tDescription\t\t\t\t\t\t\t\t Vendor\t\t\t\t\t  Amount");
        System.out.println("--------------------------------------------------------------------------------------------------------");

        // Count vendors found
        int entriesFound = 0;

        // For every entry in vendorMap, check if vendor matches & print
        for (Map.Entry<Transaction, String> map : vendorMap.entrySet())
        {
            if (map.getValue().contains(vendor))
            {
                Transaction t = map.getKey();
                printEntry(t);
                entriesFound++;
            }
        }

        // Display number of vendors found
        if(entriesFound > 1)
        {
            System.out.println("\nFOUND " + entriesFound + " ENTRIES FOR '" + vendor + "'");
        }
        else if(entriesFound == 1)
        {
            System.out.println("\nFOUND " + entriesFound + " ENTRY FOR '" + vendor + "'");
        }
        else
        {
            System.out.println("\nNO ENTRIES FOUND FOR '" + vendor + "'");
        }
    }

    // List all entries for custom search
    public void customSearch()
    {
        System.out.println("\n----------CUSTOM-SEARCH----------\n");

        System.out.println("Enter a value to filter by or enter 'X' to skip\n");

        int numMatches = 0;

        boolean startDateSkipped = false;
        boolean endDateSkipped = false;
        boolean descriptionSkipped = false;
        boolean vendorSkipped = false;
        boolean amountSkipped = false;

        LocalDate startDate = LocalDate.parse("0001-01-01");
        LocalDate endDate = LocalDate.parse("9999-12-31");;
        String description = "";
        String vendor = "";
        double amount = 0;

        // Get filters
        while(true)
        {
            // Handle invalid dates
            while(true)
            {
                try
                {
                    // Set start date or skip with 'X'
                    System.out.print("Enter start date: ");
                    String sDate = scanner.nextLine().strip();
                    if (sDate.equalsIgnoreCase("X"))
                    {
                        startDate = LocalDate.parse("0001-01-01");
                        startDateSkipped = true;
                    } else
                    {
                        startDate = LocalDate.parse(sDate);
                    }

                    // Set end date or skip with 'X'
                    System.out.print("Enter end date: ");
                    String eDate = scanner.nextLine().strip();
                    if (eDate.equalsIgnoreCase("X"))
                    {
                        endDate = LocalDate.parse("9999-12-31");
                        endDateSkipped = true;
                    } else
                    {
                        endDate = LocalDate.parse(eDate);
                    }
                }
                catch (Exception e)
                {
                    System.out.println("\nInvalid date.\n");
                    continue;
                }
                break;
            }

            // Set description or skip
            System.out.print("Enter description (partial/full): ");
            description = scanner.nextLine().toUpperCase().strip();
            if(description.equalsIgnoreCase("X"))
            {
                descriptionSkipped = true;
            }

            // Set vendor or skip
            System.out.print("Enter vendor (partial/full): ");
            vendor = scanner.nextLine().toUpperCase().strip();
            if(vendor.equalsIgnoreCase("X"))
            {
                vendorSkipped = true;
            }

            // Set amount or skip
            System.out.print("Enter amount (use '-' for payments): $");
            String sAmount = scanner.nextLine().toUpperCase().strip();
            if(sAmount.equalsIgnoreCase("X"))
            {
                amountSkipped = true;
            }
            else
            {
                amount = Double.parseDouble(sAmount);
            }
            break;
        }

        System.out.println("\n----------MATCHES----------\n");
        System.out.println("Date\t\t\tTime\t\t\tDescription\t\t\t\t\t\t\t\t Vendor\t\t\t\t\t  Amount");
        System.out.println("--------------------------------------------------------------------------------------------------------");

        // Go through all transactions & check if it matches each filter
        // Go through each filter one by one (of not skipped)
        for(Transaction t : transactions)
        {
            boolean matches = true;

            // Check start date
            if(!startDateSkipped && t.getDate().isBefore(startDate))
            {
                matches = false;
            }

            // Check end date
            if(!endDateSkipped && t.getDate().isAfter(endDate))
            {
                matches = false;
            }

            // Check description
            if(!descriptionSkipped && !t.getDescription().toUpperCase().contains(description))
            {
                matches = false;
            }

            // Check vendor
            if(!vendorSkipped && !t.getVendor().toUpperCase().contains(vendor))
            {
                matches = false;
            }

            // Check amount
            if(!amountSkipped && t.getAmount() != amount)
            {
                matches = false;
            }

            // If entry matches all criteria, then print & add 1 to number of matches
            if(matches)
            {
                printEntry(t);
                numMatches++;
            }
        }

        // Print how many matches found (if any)
        if(numMatches > 1)
        {
            System.out.println("\nFOUND " + numMatches + " MATCHES!");
        }
        else if(numMatches == 1)
        {
            System.out.println("\nFOUND " + numMatches + " MATCH!");
        }
        else
        {
            System.out.println("\nNO MATCHES FOUND.");
        }
    }

    // Run
    public void run()
    {
        // Read csv once to get existing data
        readCsv();
        displayHome();
    }
}
