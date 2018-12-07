import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class BellevueHousing {

    public static void runHousingProgram(){
        Connection conn = null;
        try {

            Class.forName("com.mysql.cj.jdbc.Driver"); //want to use the driver from jdbc driver this and statement below are equivalent
            // DriverManager.registerDriver(new Driver());
            //default SSL will be true, Company is the schema name that we are working with
            String url = "jdbc:mysql://localhost:3306/PartB?serverTimezone=UTC&useSSL=TRUE"; //this is the connection we have with the database, need timezone in java
            String user, pass;
            user = readEntry("UserId: "); //get the user ID from user
            pass = readEntry("Password: "); //get the PassWord from user
            conn = DriverManager.getConnection(url, user, pass);//where, username, and password these are the parts that the driver manager needs to know about

            boolean done = false;
            logIn(conn);
            do {
                done = true;
            }
            while (!done);

        } catch (ClassNotFoundException e) {
            System.out.println("Could not load the driver");
        } catch (SQLException ex) {
            System.out.println(ex);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) { /* ignored */}
            }
        }
    }


    static String readEntry(String prompt) {
        try {
            StringBuffer buffer = new StringBuffer();
            System.out.print(prompt);
            System.out.flush();
            int c = System.in.read();
            while (c != '\n' && c != -1) {
                buffer.append((char) c);
                c = System.in.read();
            }
            return buffer.toString().trim();
        } catch (IOException e) {
            return "";
        }
    }

    //Alex did this
    static void logIn(Connection conn) throws SQLException, IOException {
        boolean adminCheck = false;
        boolean isApplicant = false;
        System.out.println("*****************************************************************************");
        System.out.println("                  Welcome to Bellevue College log in System            ");
        System.out.println("                              *****************                    ");
        System.out.println("*****************************************************************************");
        String UserID = readEntry("Please enter User ID: ");
        String userName = readEntry("User Name: ");
        String password = readEntry("Password: ");
        String userTypeQuery = "SELECT EXISTS(SELECT * FROM Staff_ WHERE user__ID = '" + UserID  +"')";
        String applicantTypeQuery = "SELECT EXISTS(SELECT * FROM USER_ WHERE user__ID = '" + UserID  +"')";
        PreparedStatement isAdmin = conn.prepareStatement(userTypeQuery);
        PreparedStatement isApp = conn.prepareStatement(applicantTypeQuery);
        ResultSet r = isAdmin.executeQuery();
        ResultSet rs = isApp.executeQuery();
        while (r.next()) { //as long as there are more, continue reading
            adminCheck = r.getBoolean(1);
        }
        while(rs.next()){
            isApplicant = rs.getBoolean(1);

        }
        if(adminCheck){
            printAdminMenu(conn);
        } else if(isApplicant){
            System.out.println("true, why no working.");
            printLoadMenu(conn);
        } else {
            String query = "insert into USER_ (user__ID, username, passwords) values (?,?,?)";
            PreparedStatement updateStmt = conn.prepareStatement(query);
            updateStmt.setString(1, UserID);
            updateStmt.setString(2, userName);
            updateStmt.setString(3, password);
            updateStmt.executeUpdate();
            printLoadMenu(conn);
        }
    }

    //Alex Did this
    static void printAdminMenu(Connection conn) throws SQLException, IOException{

        boolean done = false;
        do {
            System.out.println("*****************************************************************************");
            System.out.println("                  Welcome to Bellevue College Housing System            ");
            System.out.println("                                Administrators Staff                    ");
            System.out.println("*****************************************************************************");
            System.out.println("                            1. Manage Residents");
            System.out.println("                            2. Manage Applicants");
            System.out.println("                            3. Demographic Studies");
            System.out.println("                            4. Manage Maintenance orders");
            System.out.println("                            5. Administrative Reports");
            System.out.println("                            6. Quit to Log-in Menu");
            System.out.println("                            7. Exit program");
            System.out.print("Type in your option: ");
            System.out.flush();
            String ch = readLine();
            System.out.println();
            switch (ch.charAt(0)) {
                case '1':
                    System.out.println("This is where you'll manage residents");
                    waitForEnter();
                    break;
                case '2':
                    System.out.println("This is where you'll manage applicants");
                    waitForEnter();
                    break;
                case '3':
                    System.out.println("This is where you'll manage demographic");
                    waitForEnter();
                    break;
                case '4':
                    System.out.println("This is where you'll manage Maintenance");
                    waitForEnter();
                    break;
                case '5':
                    System.out.println("This is where you'll manage reports");
                    waitForEnter();
                    break;
                case '6':
                    logIn(conn);
                    break;
                case'7':
                    done = true;
                    break;
                default:
                    System.out.println(" Not a valid option ");
            }
        }
        while (!done);

    }
    // Alex did this
    static void printApply(Connection conn) throws SQLException, IOException{
        boolean isMarried = false;
        boolean isAlumn = false;
        System.out.println("Please fill out the following:");
        String ssn = readEntry("SSN:");
        String userID = readEntry("User ID:");
        String name = readEntry("Name:");
        String gender = readEntry("Gender(M/F):").toUpperCase();
        String married = readEntry("Are you married?(Y/N):").toUpperCase();
        if(married.charAt(0) == 'Y'){
            isMarried = true;
        }
        String alumn = readEntry("Are you and Alumni?(Y/N)").toUpperCase();
        if(alumn.charAt(0) == 'Y'){
            isAlumn = true;
        }
        int n = 1;
        String address = readEntry("Address:");
        String phone = readEntry("Phone Number:");
        String housing = readEntry("Housing Preference: (Suite/Apartment)");
        String college = readEntry("Current College:");
        // create the java mysql update preparedstatement
        String query = "insert into Applicant_ (applicant__ssn, user__ID, Name_, gender, is_married, is__alumni, address, phone__num, application__num, housing__preference, college) values (?,?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement updateStmt = conn.prepareStatement(query);
        updateStmt.setString   (1, ssn);
        updateStmt.setString   (2, userID);
        updateStmt.setString   (3, name);
        updateStmt.setString   (4, gender);
        updateStmt.setBoolean   (5, isMarried);
        updateStmt.setBoolean   (6, isAlumn);
        updateStmt.setString   (7, address);
        updateStmt.setString   (8, phone);
        updateStmt.setInt       (9, ++n);
        updateStmt.setString   (10, housing);
        updateStmt.setString   (11, college);
        updateStmt.executeUpdate();


    }
    // Alex did this
    static void printLoadMenu(Connection conn) throws SQLException, IOException{
        try {
            boolean done = false;
            do {
                System.out.println("*****************************************************************************");
                System.out.println("                       Welcome to the Housing System");
                System.out.println("                              ************");
                System.out.println("*****************************************************************************");
                System.out.println("                       1. Resident Login");
                System.out.println("                       2. Applicant Registration/Apply");
                System.out.println("                       3. Maintenance Request");
                System.out.println("                       4. Quit");
                System.out.print("Type in your option: ");
                System.out.flush();
                String ch = readLine();
                System.out.println();
                switch (ch.charAt(0)) {
                    case '1':
                        printResidentLogin(conn);
                        break;
                    case '2':
                        printApply(conn);
                        break;
                    case '3':
                        String userID = readEntry("Enter User ID to log into Maintenance Menu: ");
                        ResidentMaintenanceMenu.ResidentMM(conn,userID);
                        break;
                    case '4':
                        done = true;
                        break;
                    default:
                        System.out.println(" Not a valid option ");
                }
            }
            while (!done);
        }  catch (SQLException ex) {
            System.out.println(ex);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) { /* ignored */}
            }
        }
    }
    // Alex did this
    static void printResidentLogin(Connection conn) throws SQLException, IOException{
        String UserID = readEntry("Please Enter your User ID to check if you're a resident: ");
        boolean residentCheck = false;
        String userTypeQuery = "SELECT EXISTS(SELECT * FROM Applicant_ WHERE user__ID = '" + UserID  +"')";
        PreparedStatement isAdmin = conn.prepareStatement(userTypeQuery);
        ResultSet r = isAdmin.executeQuery();
        while (r.next()) { //as long as there are more, continue reading
            residentCheck = r.getBoolean(1);
        }
        if(residentCheck) {
            System.out.println("Congrats! You're a resident.");
        }    else {
            System.out.println("Sorry! You're not a resident.");
        }
        waitForEnter();
    }
    // Alex did this
    static void waitForEnter()throws IOException{
        System.out.println("Press Enter to continue...");
        System.in.read();
        System.out.println();
    }
    private static String readLine() {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr, 1);
        String line = "";
        try {
            line = br.readLine();
        } catch (IOException e) {
            System.out.println("Error in SimpleIO.readLine: " + "IOException was thrown");
            System.exit(1);
        }
        return line;
    }




}

