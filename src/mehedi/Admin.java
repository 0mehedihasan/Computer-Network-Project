package mehedi;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Admin {
    private static final String HOST = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            // Admin Login
            System.out.print("Enter email: ");
            String email = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            out.println("LOGIN;" + email + ";" + password);
            String loginResponse = in.readLine();
            if (!loginResponse.startsWith("SUCCESS")) {
                System.out.println("Login failed: " + loginResponse.split(";")[1]);
                return;
            }
            System.out.println("Welcome, Admin!");

            // Menu for admin
            while (true) {
                System.out.println("\n1. Add Student");
                System.out.println("2. View Student");
                System.out.println("3. Update Student");
                System.out.println("4. Delete Student");
                System.out.println("5. Add Course");
                System.out.println("6. View Courses");
                System.out.println("7. Delete Course");
                System.out.println("8. Exit");
                System.out.print("Choose an option: ");
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1: // Add Student
                        System.out.print("Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Intake: ");
                        String intake = scanner.nextLine();
                        System.out.print("Section: ");
                        String section = scanner.nextLine();
                        System.out.print("Department: ");
                        String dept = scanner.nextLine();
                        System.out.print("Address: ");
                        String address = scanner.nextLine();
                        System.out.print("Phone: ");
                        String phone = scanner.nextLine();

                        // Fetch and display existing courses
                        out.println("VIEW_COURSES");
                        String courseListResponse = in.readLine();
                        if (courseListResponse.startsWith("ERROR")) {
                            System.out.println("Error fetching courses: " + courseListResponse);
                        } else {
                            System.out.println("Available Courses:");
                            System.out.println(courseListResponse);

                            // Let the admin select courses
                            System.out.println("Enter Course Codes to assign to the student (comma-separated): ");
                            String selectedCourses = scanner.nextLine();

                            // Send all data to the server
                            out.println("ADD_STUDENT;" + name + ";" + intake + ";" + section + ";" + dept + ";" + address + ";" + phone + ";" + selectedCourses);
                            System.out.println(in.readLine());
                        }
                        break;

                    case 2: // View Student
                        System.out.print("Enter Student ID: ");
                        String viewId = scanner.nextLine();
                        out.println("VIEW_STUDENT;" + viewId);
                        System.out.println(in.readLine());
                        break;

                    case 3: // Update Student
                        System.out.print("Enter Student ID: ");
                        String updateId = scanner.nextLine();
                        System.out.print("New Name: ");
                        String newName = scanner.nextLine();
                        System.out.print("New Intake: ");
                        String newIntake = scanner.nextLine();
                        System.out.print("New Section: ");
                        String newSection = scanner.nextLine();
                        System.out.print("New Department: ");
                        String newDept = scanner.nextLine();
                        System.out.print("New Address: ");
                        String newAddress = scanner.nextLine();
                        System.out.print("New Phone: ");
                        String newPhone = scanner.nextLine();
                        out.println("UPDATE_STUDENT;" + updateId + ";" + newName + ";" + newIntake + ";" + newSection + ";" + newDept + ";" + newAddress + ";" + newPhone);
                        System.out.println(in.readLine());
                        break;

                    case 4: // Delete Student
                        System.out.print("Enter Student ID: ");
                        String deleteId = scanner.nextLine();
                        out.println("DELETE_STUDENT;" + deleteId);
                        System.out.println(in.readLine());
                        break;

                    case 5: // Add Course
                        System.out.print("Course Code: ");
                        String code = scanner.nextLine();
                        System.out.print("Course Name: ");
                        String courseName = scanner.nextLine();
                        out.println("ADD_COURSE;" + code + ";" + courseName);
                        System.out.println(in.readLine());
                        break;

                    case 6: // View Courses
                        out.println("VIEW_COURSES");
                        System.out.println(in.readLine());
                        break;

                    case 7: // Delete Course
                        System.out.print("Enter Course Code: ");
                        String deleteCode = scanner.nextLine();
                        out.println("DELETE_COURSE;" + deleteCode);
                        System.out.println(in.readLine());
                        break;

                    case 8: // Exit
                        System.out.println("Exiting...");
                        return;

                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
