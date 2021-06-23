import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The CustomerPersons class contains a method to view a customer's details.
 * <p>
 * The method in this class is used in the Allprojects class to create a customer object.
 * This  will allow us to display and generate an invoice
 * @author Yorick Cockrell
 */
public class CustomerPersons {

    /**
     * CustomerPersons will allow us to to display a Customer
     * <p>
     * 'Project_Person_Details' will select a customer in the external database.
     * <p>
     * @param statement statement object linked to connection to perform SQL commands
     * @param projectNumber projectNumber an integer entered by the user to locate a specific project object
     * @throws SQLException occurs if there is an error accessing the database information
     */
    public void displayCustomer(Statement statement, int projectNumber) throws SQLException {

        ResultSet results1 = statement.executeQuery("SELECT Name, ContactNumber, PhysicalAddress, EmailAddress FROM Project_Person_Details WHERE ProjectNumber = " + projectNumber
                + " AND PersonType = 'Customer'");

        while (results1.next()) {  // Customer details displayed using iterator in table.
            System.out.println(
                    "\nName: " + results1.getString("Name")
                            + "\nNumber: " + results1.getInt("ContactNumber")
                            + "\nPhysical Address: " + results1.getString("PhysicalAddress")
                            + "\nEmail Address: " + results1.getString("EmailAddress")
            );

        }
    }
}
