import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

/**
 * The Allprojects class contains all the methods for the Project Management System for 'PoisePMS'.
 * It inherits input check methods from the superclass 'InputChecks' to validate user input.
 * <p>
 * @author Yorick Cockrell
 */
public class AllProjects extends InputChecks {
    /**
     * This method allows users to view all projects that are incomplete
     * in the main_project_info table in the external 'Poise' database.
     * <p>
     * @param statement statement object linked to connection to perform SQL commands
     * @throws SQLException occurs if there is an error accessing the database information
     */
    public void viewIncomplete(Statement statement) throws SQLException {

        System.out.println("\nView all incomplete projects below: \n");

        ResultSet outCome3 = statement.executeQuery("SELECT * FROM main_project_info WHERE Finalised = 'No' AND CompletionDate = 'None'");

        // All incomplete projects are displayed.
        while (outCome3.next()) {
            System.out.println(
                    "Project Number: \t" + outCome3.getInt("ProjectNumber")
                            + "\nProject Name: \t" + outCome3.getString("ProjectName")
                            + "\nBuilding Type: \t" + outCome3.getString("BuildingType")
                            + "\nPhysical Address: " + outCome3.getString("Address")
                            + "\nERF Number: \t" + outCome3.getString("ERFNumber")
                            + "\nTotal Fee: \tR" + outCome3.getFloat("TotalFee")
                            + "\nAmount Paid: \t" + outCome3.getFloat("AmountPaid")
                            + "\nDeadline: \t" + outCome3.getString("Deadline")
                            + "\nFinalised: \t" + outCome3.getString("Finalised")
                            + "\nCompletion Date: " + outCome3.getString("CompletionDate")
                            + "\n"
            );
        }
    }
    /**
     * This method allows users to edit project information, relating to the project due date and total amount paid.
     * <p>
     * It displays a sub-menu to the user with two edit choices and executes the action depending on their choice.
     * The edited information is then written under the corresponding column of the 'main_project_info' table in the
     * external PoisePMS database.
     * <p>
     * @param statement statement object linked to connection to perform SQL commands
     * @throws SQLException occurs if there is an error accessing the database information
     */
    public void editProject(Statement statement) throws SQLException {

        // The user is prompted to enter a project number to edit.
        System.out.println("Please enter project number that you want to update: \n");
        int projectNumber = intInput("project number");

        // Edit options displayed.
        System.out.println("Do you want to:" +
                "\n1. Edit due date or" +
                "\n2. Edit total amount paid of the fee to date?" +
                "\nChoose either 1 or 2");

        int selectEdit = intInput("edit choice");

        /* When user enter '1' to edit date they will be granted to input a new date.
         * main_project_info table with the executeUpdate() SQL statement.
         */
        if (selectEdit == 1) {
            System.out.println("Please enter a new project due date in this format dd/mm/yy (e.g. dd/mm/yy: 3 November 2020): ");
            String newDeadline = stringInput("due date");

            statement.executeUpdate(
                    "UPDATE main_project_info SET Deadline = '" + newDeadline + "'" + " WHERE Project_Num = " + projectNumber
            );

            //  view the list of updated projects.
            System.out.println("Your project info has been updated. View projects below: ");
            printAllFromTable(statement);

            /* When user enter '2' to edit amount they will be granted to input new amount paid.
             * main_project_info table with the executeUpdate() SQL statement.
             */
        } else if (selectEdit == 2) {
            System.out.println("Please enter a new total amount paid: ");
            float new_amount = floatInput("new amount paid");

            statement.executeUpdate(
                    "UPDATE main_project_info SET Amount_Paid = " + new_amount + " WHERE Project_Num = " + projectNumber
            );

            // A successful message is displayed to the user and then they are able to view the list of updated projects.
            System.out.println("Your project info has been updated. View projects below: ");
            printAllFromTable(statement);

        }
    }
    /**
     * Project Management System for Poised.
     * FinaliseProject class for finaliseProject() method located in the main_project_info table in the external 'PoisePMS' database.
     * <p>
     * Will generating an invoice, if requirements are met and marking the project as finalised
     * by adding a completion date to the project info.
     * After that it will mark project as finalised and adds a completion date.
     * Finalised project will be stored in the external database.
     * <p>
     * @param statement statement object linked to connection to perform SQL commands
     * @throws SQLException occurs if there is an error accessing the database information
     */
    public void finaliseProject(Statement statement) throws SQLException {

        // project number to finalise.
        System.out.println("Please enter the number of the project you want to finalise: ");
        int projectNumber = intInput("project number");

        // Selecting the TotalFee and AmountPaid columns from the table.
        ResultSet outCome = statement.executeQuery("SELECT Total_Fee, Amount_Paid FROM main_project_info WHERE ProjectNumber = " + projectNumber);
        float totalFee = 0;
        float amountPaid = 0;

        // Iterating through the columns and storing the two float numbers.
        while (outCome.next()) {
            totalFee = outCome.getFloat("Total_Fee");
            amountPaid = outCome.getFloat("Amount_Paid");

        }
        // When project has been paid in full, the amount paid will equal the total fee.
        // outcome = no invoice needs to be generated.
        if (totalFee == amountPaid) {
            System.out.println("Project has already been paid in full. No need to generate an invoice.");

            // The user is then prompted to enter a completion date,

            System.out.println("Enter completion date for the project: ");
            String completionDate = stringInput("completion date");

            // Completion date added to user's chosen project by project number.
            statement.executeUpdate(
                    "UPDATE main_project_info SET CompletionDate = " + "'" + completionDate + "'" + " WHERE ProjectNumber = " + projectNumber
            );

            // The project is then marked as finalised by entering 'Yes'
            statement.executeUpdate(
                    "UPDATE main_project_info SET Finalised = 'Yes' WHERE ProjectNumber  = " + projectNumber
            );

            // A message is displayed. The user is able to view the updated project.
            System.out.println("Your project has been finalised. View projects below: ");
            printAllFromTable(statement);

            /* Invoice wll generated when theres an outstanding amount on the selected project.
             * A 'CustomerPersons' object is then created to access the 'displayPerson() method from the CustomerPersons class.
             * The customer details for the selected project are displayed.
             */
        } else if (totalFee != amountPaid) {
            System.out.println("You have a outstanding amount to be paid for this project. View your invoice below: \n");

            CustomerPersons client = new CustomerPersons();
            client.displayCustomer(statement, projectNumber);

            // Outstanding amount
            System.out.println("\nAmount Outstanding: R" + (totalFee - amountPaid));

            // Enter completion date
            System.out.println("\nEnter completion date for the project: ");
            String completionDate = stringInput("completion date");

            // The date entered by the user is written to the main_project_info table under the 'Completion_Date' column.
            statement.executeUpdate(
                    "UPDATE main_project_info SET CompletionDate = " + "'" + completionDate + "'" + " WHERE ProjectNumber = " + projectNumber
            );

            // The project is then marked as finalised by entering 'Yes'.
            statement.executeUpdate(
                    "UPDATE main_project_info SET Finalised = 'Yes' WHERE ProjectNumber = " + projectNumber
            );

            // A message is displayed and the user is able to view the updated project.
            System.out.println("Your project has been finalised. View projects below: ");
            printAllFromTable(statement);

        }
    }

    /**
     * This method will create a new project, which is added to the 'main_project_info' table in the PoisePMS database.
     * <p>
     * User can enter info based on what is displayed to the user which connects it to the database and updates project.
     * The 'MainMenu' class calls on this method when user wants to add a new project.
     * <p>
     * @param statement statement object linked to connection to perform SQL commands
     * @throws SQLException occurs if there is an error accessing the database information
     */
    public void addProject(Statement statement) throws SQLException {

        // User prompted for input regarding project information.
        System.out.println("\nPlease enter project number (Only numbers): ");
        int projectNumber = intInput("project number");

        System.out.println("\nPlease enter project name: ");
        String projectName = stringInput("project name: ");

        System.out.println("\nPlease enter building type (E.g. House, apartment block or store, etc. : ");
        String buildingType = stringInput("building type");

        System.out.println("\nPlease enter physical address for the project: ");
        String address = stringInput("project address");

        System.out.println("\nPlease enter ERF number: ");
        String erfNumber = stringInput("ERF number");

        System.out.println("\nPlease enter total fee charged for the project (Only numbers): ");
        float totalFee = floatInput("total fee");

        System.out.println("\nPlease enter total amount paid to date (Only numbers): ");
        float amountPaid = floatInput("total amount");

        System.out.println("Please enter project deadline in this format dd/mm/yy (e.g. dd/mm/yy: 3 November 2020) : ");
        String deadline = stringInput("project deadline");

        // Completion and Status variables set to negative as this is a newly added project.
        String finalise = "Not finalised";
        String comp_date = "None";

        /* The main_project_info table in the 'Poise' database is then updated.
         * The information inputted by the user is inserted.
         * thus creating and storing a new project.
         */
        statement.executeUpdate(
                "INSERT INTO main_project_info VALUES (" + projectNumber + ", " + "'" + projectName + "'" + ", " + "'"
                        + buildingType + "'" + ", " + "'" + address + "'" + ", " + "'" + erfNumber + "'" + ", " + totalFee + ", " + amountPaid + ", " +
                        "'" + deadline + "'" + ", " + "'" + finalise + "'" + ", " + "'" + comp_date + "'" + ");"
        );

        // A successful message is displayed and the user can then view the updated project list.
        System.out.println("\nYour project was successfully added: \n");
        printAllFromTable(statement);

    }
    /**
     * This method allows users to view all projects that are overdue in the main_project_info table in the
     * external 'PoisePMS' database.
     * <p>
     * @param statement statement object linked to connection to perform SQL commands
     * @throws SQLException occurs if there is an error accessing the database information
     * @throws ParseException occurs if a date string is in the wrong format to be parsed
     */
    public void viewOverdue(Statement statement) throws SQLException, ParseException {


        /* projectCheck is set as false
         * Then we use a integer array to store numbers 1 to 12, which scan the number of months in the year.
         * Then we use our second String array is created to store months of the year to get date information on a project.
         * An integer 'monthNum' is set to 0.
         */
        boolean projectCheck = false;
        String[] info;
        int[] months = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        String[] monthsofYear = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int monthNum = 0;

        // Overdue projects will be incomplete.
        ResultSet outCome4 = statement.executeQuery("SELECT Deadline FROM main_project_info WHERE Finalised = 'No' AND CompletionDate = 'None'");

        // Iterating through the deadline dates.
        while (outCome4.next()) {

            // The deadline date in the project is stored in the string variable 'dateInfo'.
            // This variable is then split into an array called 'info'
            // The first indexed value of 'info' is then parsed and stored into an integer variable called 'day'.
            String dateInfo = outCome4.getString("Deadline");
            info = dateInfo.split("-");
            int day = Integer.parseInt(info[0]);

            /* The 2de indexed value is stored in a variable called 'monthInfo'.
             * monthInfo is then split to store only 3 letters of the month name into string variable 'month' (e.g. 'Dec').
             * A year variable is also created and the 3de index in 'info' array.
             */
            String monthInfo = info[1];
            String month = (monthInfo.substring(0,2));
            int year = Integer.parseInt(info[2]);

            /*
             * month and monthsofYear is been compared using a for loop.
             * Once matched then it
             * is stored in the 'monthNum' variable, for use as date info.
             */
            for (int index = 0; index < monthsofYear.length ; index++) {
                if (month.equalsIgnoreCase(monthsofYear[index])) {
                    monthNum = months[index];

                }
            }
            // nowDate date and storing it as a string.
            String nowDate = "" + java.time.LocalDate.now();

            // Creating new simple date format .
            SimpleDateFormat dateObject = new SimpleDateFormat("yyyy-MM-dd");


            // Dates date1 and date2 are then created by parsing string info from 'nowDate' date
            // and date info gathered.
            Date date1 = dateObject.parse(nowDate);

            Date date2 = dateObject.parse(day + "-" + monthNum + "-" + year);

            // When nowDate date has passed the deadline for the project, it is overdue.
            // The projectCheck is set to 'true'.
            if (date1.compareTo(date2) < 0) {
                projectCheck = true;

                System.out.println("\nView all overdue projects below: \n");
                ResultSet results5 = statement.executeQuery("SELECT * FROM main_project_info WHERE Deadline = '" + dateInfo + "'");

                // Iterating and display
                while (results5.next()) {
                    System.out.println(
                            "Project Number: \t" + results5.getInt("ProjectNumber")
                                    + "\nProject Name: \t" + results5.getString("ProjectName")
                                    + "\nBuilding Type: \t" + results5.getString("BuildingType")
                                    + "\nPhysical Address: " + results5.getString("Address")
                                    + "\nERF Number: \t" + results5.getString("ERFNumber")
                                    + "\nTotal Fee: \tR" + results5.getFloat("TotalFee")
                                    + "\nAmount Paid: \t" + results5.getFloat("AmountPaid")
                                    + "\nDeadline: \t" + results5.getString("Deadline")
                                    + "\nFinalised: \t" + results5.getString("Finalised")
                                    + "\nCompletion Date: " + results5.getString("CompletionDate")
                                    + "\n"
                    );
                }
                // if there are no overdue projects, projectCheck is set to 'false'.
            } else {
                projectCheck = false;
            }
            // If projectCheck is set at false after the projects are all checked, an message is displayed to the user.
        } if (projectCheck == false) {
            System.out.println("There are no overdue projects.");
        }
    }
    /**
     * This method allows users to find project from the main_project_info table in the external 'PoisePMS'
     * database by either entering the project name or number.
     * <p>
     * Using either name or number, from external database and displayed.
     * <p>
     * @param statement statement object linked to connection to perform SQL commands
     * @throws SQLException occurs if there is an error accessing the database information
     */
    public void findProject(Statement statement) throws SQLException {

        System.out.println("Please enter the following number: Search for your project by entering: \n1.) for project number or entering 2.) for project name?: \n.");
        int searchCheck = intInput("Number search option");

        /* option 1, enter the project number.
         * The program selects all info related to that project to display to the user.
         */
        if (searchCheck == 1) {
            System.out.println("\nEnter the number of the project you wish to locate: ");
            int projectNumber = intInput("project number");

            System.out.println("\nView your project details below: \n");

            ResultSet outCome6 = statement.executeQuery("SELECT * FROM main_project_info WHERE ProjectNumber = " + projectNumber);

            // Iterating through project info by column of the project selected by the user.
            while (outCome6.next()) {
                System.out.println(
                        "Project Number: \t" + outCome6.getInt("ProjectNumber")
                                + "\nProject Name: \t" + outCome6.getString("ProjectName")
                                + "\nBuilding Type: \t" + outCome6.getString("BuildingType")
                                + "\nPhysical Address: " + outCome6.getString("Address")
                                + "\nERF Number: \t" + outCome6.getString("ERFNumber")
                                + "\nTotal Fee: \tR" + outCome6.getFloat("TotalFee")
                                + "\nAmount Paid: \t" + outCome6.getFloat("AmountPaid")
                                + "\nDeadline: \t" + outCome6.getString("Deadline")
                                + "\nFinalised: \t" + outCome6.getString("Finalised")
                                + "\nCompletion Date: " + outCome6.getString("CompletionDate")
                                + "\n"
                );
            }
            /* option 2,  enter the project name.
             * program select all info related to that project to display to the user.
             */
        } else if (searchCheck == 2) {
            System.out.println("\nPlease enter the name of the project you wish to locate: ");
            String projectName = stringInput("project name");

            System.out.println("\nPlease view your project details below: \n");

            ResultSet outCome7 = statement.executeQuery("SELECT * from main_project_info WHERE Project_Name = '" + projectName + "'");

            // Iterating through project info by column of the project selected by the user.
            while (outCome7.next()) {
                System.out.println(
                        "Project Number: \t" + outCome7.getInt("ProjectNumber")
                                + "\nProject Name: \t" + outCome7.getString("ProjectName")
                                + "\nBuilding Type: \t" + outCome7.getString("BuildingType")
                                + "\nPhysical Address: " + outCome7.getString("Address")
                                + "\nERF Number: \t" + outCome7.getString("ERFNumber")
                                + "\nTotal Fee: \tR" + outCome7.getFloat("TotalFee")
                                + "\nAmount Paid: \t" + outCome7.getFloat("AmountPaid")
                                + "\nDeadline: \t" + outCome7.getString("Deadline")
                                + "\nFinalised: \t" + outCome7.getString("Finalised")
                                + "\nCompletion Date: " + outCome7.getString("CompletionDate")
                                + "\n"
                );
            }
        }
    }
    /**
     * This method displays all information from the main_project_info table in the 'PoisePMS' database
     * <p>
     * @param statement statement object linked to connection to perform SQL commands
     * @throws SQLException occurs if there is an error accessing the database information
     */
    public void printAllFromTable(Statement statement) throws SQLException{

        // Selecting all information
        ResultSet outCome8 = statement.executeQuery("SELECT * FROM main_project_info");

        // Iterating through info
        while (outCome8.next()) {
            System.out.println(
                    "Project Number: \t" + outCome8.getInt("ProjectNumber")
                            + "\nProject Name: \t" + outCome8.getString("ProjectName")
                            + "\nBuilding Type: \t" + outCome8.getString("BuildingType")
                            + "\nPhysical Address: " + outCome8.getString("Address")
                            + "\nERF Number: \t" + outCome8.getString("ERFNumber")
                            + "\nTotal Fee: \tR" + outCome8.getFloat("TotalFee")
                            + "\nAmount Paid: \t" + outCome8.getFloat("AmountPaid")
                            + "\nDeadline: \t" + outCome8.getString("Deadline")
                            + "\nFinalised: \t" + outCome8.getString("Finalised")
                            + "\nCompletion Date: " + outCome8.getString("CompletionDate")
                            + "\n"
            );
        }
    }
}
