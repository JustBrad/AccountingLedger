# AccountingLedger

**AccountingLedger** is a simple app for tracking deposits and payments in a CSV file.
It allows you to add new entries, view them in a ledger, and generate various reports.

## Features
- Add new deposits and payments to CSV file with date, time, description, vendor, and amount
- View entries in the ledger (All, Deposits Only, Payments Only)
- Generate reports for month to date, previous month, year to date, previous year
- Search for entries by vendor

## Home Screen
From the **Home** screen, you can choose one of the following:
- **Add Deposit** - Add a deposit entry to the CSV file
- **Make Payment (Debit)** - Add a payment entry to the CSV file
- **Ledger** - Display the Ledger screen
- **Exit** - Exit the application

## Adding Deposits/Making Payments
After selecting **Add Deposit** or **Make Payment (Debit)**, you will be prompted for the following:
- Date (YYYY-MM-DD)
- Time (HH:MM:SS)
- Description
- Vendor
- Amount

If all inputs are valid, the entry will be added to the CSV file.

## Ledger Screen
On the **Ledger** screen, you can choose one of the following:
- **All** - Display all entries in the CSV
- **Deposits** - Display all deposit entries in CSV
- **Payments** - Display all payment entries in CSV
- **Reports** - Display the Reports screen
- **Home** - Go back to the Home screen

## Displaying Entries
