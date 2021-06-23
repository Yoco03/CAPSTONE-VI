// Level 3 - Capstone Project I: Task 8 - Compulsory Task 2

// Importing necessary classes for use in the main program.
import java.sql.*;
import java.text.ParseException;

/**
 * Project Management System for Poised.
 * This is the MainMenu class and runs the main program which call all methods.
 * It has a superclass "InputChecks" which inherits all input checks when user input a value.
 *
 * <p>
 * Task was given to create a project management system for a small engineering company called 'PoisePMS'.
 * We make use of a JDBC driver to access external information.
 * The program will display a menu where the user can add new projects, edit existing projects and so on.
 * results are obtained and then displayed to the user which are then updated in the external database.
 *
 *
 *
 * @author Yorick Cockrell
 */
public class MainMenu extends InputChecks {  // Class declaration.

    /**
     * This is the main method which runs the Poised Management System Program.
     * <p>
     * @param args java command line arguments
     * @throws ParseException occurs if a date string is in the wrong format to be parsed
     */
    public static void main(String[] args) throws ParseException {  // Main method declaration.

        // creating 'AllProjects' object to call methods from the AllProjects class.
        AllProjects projectObject = new AllProjects();

        // Will display a project management message
        System.out.println("Project Management System for Poised\n");

        // while loop will be use to return the user to the main menu after each choice made,
        // until they select number 8, to exit the loop and log out of the program.
        while(true) {

            // Poised menu display with user options.
            System.out.println("\nPlease choose a number option from the menu below: "
                    + "\n1. View Existing projects"
                    + "\n2. Add a New Project"
                    + "\n3. Update Existing Project Info"
                    + "\n4. Finalize a Project"
                    + "\n5. View Incomplete projects"
                    + "\n6. View Overdue projects"
                    + "\n7. Find a Project"
                    + "\n8. Exit program");

            // The user's choice is checked and saved in an integer variable.
            // The 'intCheck()' method is defined and explained below the main program method.
            int selectNum = intInput("menu choice");

            // A try-catch block is used to connect to the MySQL server and access the Poise database.
            try {
                Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/PoisePMS?useSSL=false",
                        "root",
                        "01234"
                );

                // Statement object created.
                Statement statement = connection.createStatement();

                /* When user enter 1 they will be able to view all projects.
                 *
                 * The printAllFromTable()
                 */

                if (selectNum == 1) {

                    projectObject.printAllFromTable(statement);

                    // When user enter 2 they will be able to create new project

                } else if (selectNum == 2) {

                    projectObject.addProject(statement);

                    // When user enter 3 they will be able to edit project

                } else if (selectNum == 3) {

                    projectObject.editProject(statement);

                    // When user enter 4 they will be able to finalise a project.

                } else if (selectNum == 4) {

                    projectObject.finaliseProject(statement);

                    // When user enter 5 they will be able to view incomplete projects

                } else if (selectNum == 5) {

                    projectObject.viewIncomplete(statement);

                    // When user enter 6 they will be able to view overdue projects

                } else if (selectNum == 6) {

                    projectObject.viewOverdue(statement);

                    // When user enter 7 they will be able to find a project
                } else if (selectNum == 7) {

                    projectObject.findProject(statement);

                    // When user enter 8 they will be able to exit the program.
                } else if (selectNum == 8) {

                    // When user selected 8 user will be see a message that you have logged out..
                    System.out.println("You have logged out.");
                    break;

                }
                // Catch created for SQL exception.
            } catch (SQLException e) {
                e.printStackTrace();

            }
        }
    }
}
