package pl.szczepanski.marek.demo.databases;

import org.springframework.stereotype.Service;

import java.sql.*;

@SuppressWarnings("ALL")
@Service
public class JDBCService {

    // Connection, Statment, executeQuery, Exceptions
    public String example1() throws SQLException {

        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;

        try {
            System.out.println("\n\n==========");
            // 1. Get a connection to database
            myConn = DriverManager
                    .getConnection("jdbc:h2:mem:testdb", "sa", "");
            System.out.println("Database connection successful!\n");

            // 2. Create a statement
            myStmt = myConn.createStatement();

            // 3. Execute SQL query
            myRs = myStmt.executeQuery("select * from employees");

            // 4. Process the result set
            while (myRs.next()) {
                String emploee = myRs.getString("last_name")
                        + ", " + myRs.getString("first_name");
                System.out.println(emploee);
            }

            return "OK";
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            if (myRs != null) {
                myRs.close();
            }
            if (myStmt != null) {
                myStmt.close();
            }
            if (myConn != null) {
                myConn.close();
            }
        }
        return "ERROR";
    }

    // try-with-resources
    public String example2() throws SQLException {

        String dbUrl = "jdbc:h2:mem:testdb";
        String user = "sa";
        String pass = "";

            // 1. Get a connection to database
        try (
                Connection myConn = DriverManager.getConnection(dbUrl, user, pass)
        ) {

            // 2. Create a statement
            Statement myStmt = myConn.createStatement();

            // 3. Execute SQL query
            ResultSet myRs = myStmt.executeQuery("select * from employees");

            // 4. Process the result set
            while (myRs.next()) {
                System.out.println(myRs.getString("last_name")
                        + ", " + myRs.getString("first_name"));
            }
            ResultSet myRs1 = myStmt
                    .executeQuery("select * from employees " +
                            "where LOWER(department) = 'legal'");
            return "OK";
        }
    }

    // insert
    public String example3() throws SQLException {

        String dbUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
        String user = "sa";
        String pass = "";

            // 1. Get a connection to database
        try (Connection myConn = DriverManager.getConnection(dbUrl, user, pass)) {

            // 2. Create a statement
            Statement myStmt = myConn.createStatement();

            // 3. Insert a new employee
            System.out.println("Inserting a new employee to database\n");

            int rowsAffected = myStmt.executeUpdate(
            "insert into employees " +
                "(last_name, first_name, email, department, salary) " +
                "values " +
                "('Wright', 'Eric', 'eric.wright@foo.com', 'HR', 33000.00)");

            System.out.println(rowsAffected + "rows affected.\n");

            return "OK";
        }
    }

    // update
    public String example4() throws SQLException {
        String dbUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
        String user = "sa";
        String pass = "";

        // Get a connection to database
        try (Connection myConn = DriverManager.getConnection(dbUrl, user, pass)) {

            // Create a statement
            Statement myStmt = myConn.createStatement();

            // UPDATE the employee
            System.out.println("\nEXECUTING THE UPDATE FOR: John Doe\n");

            int rowsAffected = myStmt.executeUpdate(
                    "update employees " +
                            "set email='john.doe@luv2code.com' " +
                            "where department = 'HR'");

            System.out.println(rowsAffected + "rows affected.\n");

            return "OK";
        }
    }

    // delete
    public String example5() throws SQLException {
        String dbUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
        String user = "sa";
        String pass = "";

        // Get a connection to database
        try (Connection myConn = DriverManager.getConnection(dbUrl, user, pass)) {

            // Create a statement
            Statement myStmt = myConn.createStatement();

            // DELETE the employee
            System.out.println("\nDELETING THE EMPLOYEE: John Doe\n");

            int rowsAffected = myStmt.executeUpdate(
                    "delete from employees " +
                            "where last_name='Doe' and first_name='John'");

            System.out.println(rowsAffected + "rows affected.\n");

            return "OK";
        }
    }

    // SQL Injection, prepareStatement
    public String example6() throws SQLException {
        String dbUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
        String user = "sa";
        String pass = "";

        // Get a connection to database
        try (Connection myConn = DriverManager.getConnection(dbUrl, user, pass)) {

            // Create a statement
            Statement myStmt1 = myConn.createStatement();

            // QUERY FOR SALARY > 80000
            String departament = "HR";
            ResultSet myRs1 = myStmt1
                    .executeQuery(
                    "select * from employees where department = '" + departament + "'");
            // SQL injection PROBLEM!!!

            // Prepare statement
            PreparedStatement myStmt = myConn.prepareStatement(
                "select * from employees where salary > ? and department = ?");

            // Set the parameters
            myStmt.setDouble(1, 80000);
            myStmt.setString(2, "Legal");
            // Execute SQL query
            ResultSet myRs = myStmt.executeQuery();

            // Display the result set
            display(myRs);

            System.out.println("\nReuse the prepared statement:  salary > 25000,  department = HR");

            // Set the parameters
            myStmt.setDouble(1, 25000);
            myStmt.setString(2, "HR");
            //  Execute SQL query
            myRs = myStmt.executeQuery();

            // Display the result set
            display(myRs);

            return "OK";
        }
    }

    private static void display(ResultSet myRs) throws SQLException {
        while (myRs.next()) {
            String lastName = myRs.getString("last_name");
            String firstName = myRs.getString("first_name");
            double salary = myRs.getDouble("salary");
            String department = myRs.getString("department");

            System.out.printf("%s, %s, %.2f, %s\n", lastName, firstName, salary, department);
        }
    }

    // transactions
    public String example7() throws SQLException {
        String dbUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
        String user = "sa";
        String pass = "";

        // Get a connection to database
        try (Connection myConn = DriverManager.getConnection(dbUrl, user, pass)) {

            // Create a statement
            Statement myStmt = myConn.createStatement();

            // Turn off auto commit
            myConn.setAutoCommit(false);

            // Transaction Step 1: Delete all HR employees
            myStmt = myConn.createStatement();
            myStmt.executeUpdate("delete from employees where department='HR'");

            // Transaction Step 2: Set salaries to 300000 for all Engineering
            // employees
            myStmt.executeUpdate(
            "update employees set salary=300000 where department='Engineering'");

            System.out.println("\n>> Transaction steps are ready.\n");

            // store in database
            myConn.commit();
            System.out.println("Transaction commited\n");

            return "OK";
        }
    }
}
