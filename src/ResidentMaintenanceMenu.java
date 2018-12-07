/*
 * Evan Johnson
 * Group Project
 *
 * prebuilt class
 *
 * should just be able to call ResidentMaintenanceMenu()
 * in the main code after importing this class file, without modification
 *
 *
 */

import java.sql.*;
import java.io.*;

public class ResidentMaintenanceMenu
{

    public static void ResidentMM(Connection conn, String residentUserID) throws IOException
    {
        boolean running = true;

        try
        {

            while (running)
            {
                displayMenu(); // INCLUDE - displays menu

                System.out.print("Select option: ");
                System.out.flush();
                String ch = readLine();
                System.out.println();
                switch (ch.charAt(0))
                {
                    case ('1'): // OPTION 1

                        System.out.println("Submit new maintenance request: [NOT IMPLEMENTED]");
                        BellevueHousing.waitForEnter();
                        break;

                    case ('2'): // OPTION 2 doesn't work

                        String query2 = "SELECT Building_num, appt_num, date_submitted,	date_scheduled,	user_description, address "
                                + "FROM " + "Maintenance_request " + "WHERE " + "resident_user_id = '" + residentUserID
                                + "' " + "AND is_completed = FALSE " + "ORDER BY date_scheduled DESC;";

//			String query2 = "SELECT Building_num, appt_num, date_submitted,	date_scheduled,	user_description, address "
//					+ "FROM " + "Maintenance_request " + "WHERE is_completed = FALSE " + "ORDER BY date_scheduled DESC;";

                        PreparedStatement p2 = conn.prepareStatement(query2);
                        p2.clearParameters();
                        ResultSet r2 = p2.executeQuery();

                        while (r2.next())
                        {
                            System.out.println();
                            System.out.println("Building Num: " + r2.getInt(1));
                            System.out.println("Room Num: " + r2.getInt(2));
                            System.out.println("Date Submitted: " + r2.getDate(3));
                            System.out.println("Date Scheduled: " + r2.getDate(4));
                            System.out.println("Description: " + r2.getString(5));
                            System.out.println("Address: " + r2.getString(6));
                        } // OPTION 2
                        BellevueHousing.waitForEnter();
                        break;

                    case ('3'):// OPTION 3

                        String query3 = "SELECT \n" + "    Building_num,\n" + "    appt_num,\n" + "    date_completed,\n"
                                + "    date_submitted,\n" + "    user_description,\n" + "    address\n" + "FROM\n"
                                + "    Maintenance_request\n" + "WHERE\n" + "    resident_user_id = '" + residentUserID
                                + "' \n" + "        AND is_completed = TRUE\n" + "ORDER BY date_completed;";
                        PreparedStatement p3 = conn.prepareStatement(query3);
                        p3.clearParameters();
                        ResultSet r3 = p3.executeQuery();
                        while (r3.next())
                        {
                            System.out.println();
                            System.out.println("Building Num: " + r3.getInt(1));
                            System.out.println("Room Num: " + r3.getInt(2));
                            System.out.println("Date Submitted: " + r3.getDate(3));
                            System.out.println("Date Scheduled: " + r3.getDate(4));
                            System.out.println("Description: " + r3.getString(5));
                            System.out.println("Address: " + r3.getString(6));
                        } // OPTION 3
                        BellevueHousing.waitForEnter();
                        break;

                    case ('4'): // if option 4 selected
                        running = false;
                        break;

                }

            } // end of while
        }

        catch (SQLException ex) // handle exceptions
        {
            System.out.println(ex);
        }

    }

    private static void displayMenu() // TODO reformat to match other UI format
    {
        System.out.flush();
        System.out.println();
        System.out.println("***********************************************************");
        System.out.println("               Resident Maintenance Request");
        System.out.println("***********************************************************");
        System.out.println();
        System.out.println("         1. Submit new maintenance request.");
        System.out.println("         2. View status of current maintenance requests.");
        System.out.println("         3. View maintenance request history.");
        System.out.println("         4. Exit to Main Menu");
    }

    private static String readLine() // fancy readline statement provided by Sara's sample code
    {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr, 1);
        String line = "";

        try
        {
            line = br.readLine();
        } catch (IOException e)
        {
            System.out.println("Error in SimpleIO.readLine: " + "IOException was thrown");
            System.exit(1);
        }
        return line;

    }

}
