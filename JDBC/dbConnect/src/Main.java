import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        System.out.println("\n JDBC CRUD OPERATION \n");
        while(true){
            System.out.println("1. Display DATA");
            System.out.println("2. CREATE DATA");
            System.out.println("3. UPDATE DATA");
            System.out.println("4. DELETE DATA\n");
            System.out.print("ENTER ANY NUMBER FOR OPERATION: ");
            int operationNumber = scan.nextInt();

            switch (operationNumber) {
                case 1 : {
                    getData();
                    break;
                }
                case 2 : {
                    createData();
                    break;
                }
                case 3 : {
                    updateData();
                    break;
                }
                case 4 : {
                    deleteData();
                    break;
                }
                default: {
                    System.out.println("\n PLEASE ENTER A VALID NUMBER \n");
                }
            }
        }
    }

    // make connection between mysql and java program using jdbc driver
    public static Connection getConnection(){
        try {
            String driver = "com.mysql.cj.jdbc.Driver";
            String databaseUrl = "jdbc:mysql://localhost:3306/jdbc";
            String username = "root";
            String password = "2004";

            Class.forName(driver);
            Connection connection = DriverManager.getConnection(databaseUrl, username, password);

            System.out.println("Database connected, now you can do more operations");

            return connection;
        } catch (Exception e){
            System.out.println("Some Error: " + e);
        }
        return null;
    }

    public static void getData(){
        try {
            System.out.println("DATA IS DISPLAYED \n");
            Statement statement = getConnection().createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM user");

            while (result.next()){
                System.out.println("***********************************");
                System.out.println("ID: " + result.getString("id"));
                System.out.println("Name: " + result.getString("name"));
                System.out.println("Email: " + result.getString("email"));
                System.out.println("***********************************");
            }
        } catch (Exception e) {
            System.out.println("Some Error: " + e);
        }
    }

    public static void deleteData() {
        System.out.print("Enter the User's ID you want to delete: ");
        Scanner sc = new Scanner(System.in);
        int id = sc.nextInt();

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM user WHERE id = ?");
                PreparedStatement psDel = conn.prepareStatement("DELETE FROM user WHERE id = ?");
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                psDel.setInt(1, id);
                psDel.executeUpdate();
                System.out.println("Record deleted successfully.");
            } else {
                System.out.println("Record not found.");
            }
        } catch (Exception e) {
            System.out.println("Some Error: " + e);
        }
    }

    // Placeholder methods for createData and updateData
    public static void createData() {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO user (name, email) VALUES (?, ?)")) {

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Email: ");
            String email = scanner.nextLine();

            ps.setString(1, name);
            ps.setString(2, email);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Data inserted successfully.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void updateData() {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE user SET email = ? WHERE id = ?")) {

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter User ID: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter New Email: ");
            String email = scanner.nextLine();

            ps.setString(1, email);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Record updated successfully.");
            } else {
                System.out.println("User ID not found.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
