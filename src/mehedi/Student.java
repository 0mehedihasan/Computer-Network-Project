package mehedi;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Student {
    private static final String HOST = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            // Student Login
            System.out.print("Enter Student ID: ");
            String studentId = scanner.nextLine();
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            // Send login request
            out.println("LOGIN;" + studentId + ";" + password);

            String loginResponse = in.readLine();
            if (!loginResponse.startsWith("SUCCESS")) {
                System.out.println("Login failed: " + loginResponse.split(";")[1]);
                return;
            }
            System.out.println("Login successful! Welcome, Student ID: " + studentId);

            // Menu for student
            while (true) {
                System.out.println("\n1. View My Information");
                System.out.println("2. View My Courses");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1: // View Student Information
                        out.println("VIEW_STUDENT;" + studentId);
                        String studentInfo = in.readLine();
                        if (studentInfo.startsWith("ERROR")) {
                            System.out.println("Error: " + studentInfo.split(";")[1]);
                        } else {
                            System.out.println("Your Information: ");
                            System.out.println(studentInfo);
                        }
                        break;

                    case 2: // View Courses
                        out.println("VIEW_COURSES;" + studentId);
                        String coursesInfo = in.readLine();
                        if (coursesInfo.startsWith("ERROR")) {
                            System.out.println("Error: " + coursesInfo.split(";")[1]);
                        } else {
                            System.out.println("Your Enrolled Courses: ");
                            System.out.println(coursesInfo);
                        }
                        break;

                    case 3: // Exit
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
