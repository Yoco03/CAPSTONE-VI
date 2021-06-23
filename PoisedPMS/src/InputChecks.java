
import java.util.Scanner;

/**
 * Project Management System for Poised.
 * InputChecks is an superclass for the Poised project management program.
 * <p>
 *  InputCheck class has three check methods for different user inputs.
 *  intInput(), stringInput(), floatInput()
 *  These methods are inherited by the three subclasses.
 *  We use this methode to check each input value of the Scanner class.
 *
 *  @author Yorick Cockrell
 */


public class InputChecks {  // Main class declaration.
    /**
     * InputChecks class of 'floatCheck()' methode check user input when asked to enter an double, number, decimal number or integer.
     * returnType describes the input required for the user to enter
     * @return returns the verified output float
     */
    public static float floatInput(String returnType) {

        while(true) {  // While loop will iterate through until correct data returnType is entered.
            Scanner userInput = new Scanner(System.in);
            String input = userInput.nextLine();

            try {
                float output = Float.parseFloat(input); // checking for correct input that is parse for float
                return output;

            } catch (NumberFormatException ex) {
                System.out.println("Please re-enter the " + returnType + ": \n");  // Error message displayed if parsing is not possible.

            }
        }
    }
    /**
     * InputChecks class of 'stringInput()' methode check user input when asked to enter an word or letters.
     * returnType describes the input required for the user to enter
     * @return returns the verified user input
     */
    public static String stringInput(String returnType) {

        while(true) {  // While loop will iterate through until correct data returnType is entered.
            Scanner userInput = new Scanner(System.in);
            String input = userInput.nextLine();

            if ((input == null) || (input.length() > 150)) { // checking for correct input that is default "null" or length bigger then 120 letters / char values
                System.out.println("Please re-enter the " + returnType + ": \n");

            } else {
                return input;  // Returning the user's correctly inputted string.

            }
        }
    }

    /**
     * This method verifies user input when asked to enter an integer as intInput()
     * @param returnType returnType describes the input required for the user to enter
     * @return returns the verified output integer
     */
    public static int intInput(String returnType) {

        while(true) {
            // While loop will iterate through until correct data returnType is entered.
            Scanner userInput = new Scanner(System.in);
            String input = userInput.nextLine();

            try {
                int output = Integer.parseInt(input);   // checking for correct input that is parse for Int
                return output;

            } catch (NumberFormatException ex) {
                System.out.println("Please re-enter the " + returnType + ": \n");  // Error message displayed.

            }
        }
    }

}



