package mehedi;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// Class for Student
class Student {
    String id, name, intake, section, dept, address, phone;
    List<String> courses;

    Student(String id, String name, String intake, String section, String dept, String address, String phone) {
        this.id = id;
        this.name = name;
        this.intake = intake;
        this.section = section;
        this.dept = dept;
        this.address = address;
        this.phone = phone;
        this.courses = new ArrayList<>();
    }
}

// Class for Course
class Course {
    String courseCode, courseName;

    Course(String courseCode, String courseName) {
        this.courseCode = courseCode;
        this.courseName = courseName;
    }
}

public class Server {
    private static final int PORT = 12345;
    private static final Map<String, Student> studentDB = new ConcurrentHashMap<>();
    private static final Map<String, Course> courseDB = new ConcurrentHashMap<>();
    private static int studentIDCounter = 1000;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT + "...");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress());
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private final Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                String request;
                while ((request = in.readLine()) != null) {
                    String response = handleRequest(request);
                    out.println(response);
                }
            } catch (IOException e) {
                System.out.println("Connection error: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private String handleRequest(String request) {
            String[] parts = request.split(";");
            String command = parts[0];

            switch (command) {
                case "LOGIN":
                    return login(parts);
                case "ADD_STUDENT":
                    return addStudent(parts);
                case "VIEW_STUDENT":
                    return viewStudent(parts);
                case "UPDATE_STUDENT":
                    return updateStudent(parts);
                case "DELETE_STUDENT":
                    return deleteStudent(parts);
                case "ADD_COURSE":
                    return addCourse(parts);
                case "VIEW_COURSES":
                    return viewCourses();
                case "DELETE_COURSE":
                    return deleteCourse(parts);
                default:
                    return "ERROR;Unknown command";
            }
        }

        private String login(String[] parts) {
            if (parts[1].equals("admin@mehedi.com") && parts[2].equals("mehedi")) {
                return "SUCCESS;Admin login";
            } else if (studentDB.containsKey(parts[1]) && parts[2].equals("Mehedi")) {
                return "SUCCESS;Student login";
            } else {
                return "ERROR;Invalid login credentials";
            }
        }

        private String addStudent(String[] parts) {
            String name = parts[1];
            String intake = parts[2];
            String section = parts[3];
            String dept = parts[4];
            String address = parts[5];
            String phone = parts[6];

            String id = "S" + (studentIDCounter++);
            Student student = new Student(id, name, intake, section, dept, address, phone);
            studentDB.put(id, student);
            return "SUCCESS;Student added;ID=" + id;
        }

        private String viewStudent(String[] parts) {
            String id = parts[1];
            Student student = studentDB.get(id);
            if (student == null) {
                return "ERROR;Student not found";
            }
            return "SUCCESS;" + student.name + ";" + student.intake + ";" + student.section + ";" + student.dept +
                    ";" + student.address + ";" + student.phone + ";" + String.join(",", student.courses);
        }

        private String updateStudent(String[] parts) {
            String id = parts[1];
            Student student = studentDB.get(id);
            if (student == null) {
                return "ERROR;Student not found";
            }
            student.name = parts[2];
            student.intake = parts[3];
            student.section = parts[4];
            student.dept = parts[5];
            student.address = parts[6];
            student.phone = parts[7];
            return "SUCCESS;Student updated";
        }

        private String deleteStudent(String[] parts) {
            String id = parts[1];
            if (studentDB.remove(id) == null) {
                return "ERROR;Student not found";
            }
            return "SUCCESS;Student deleted";
        }

        private String addCourse(String[] parts) {
            String code = parts[1];
            String name = parts[2];
            if (courseDB.containsKey(code)) {
                return "ERROR;Course already exists";
            }
            courseDB.put(code, new Course(code, name));
            return "SUCCESS;Course added";
        }

        private String viewCourses() {
            StringBuilder response = new StringBuilder("SUCCESS");
            for (Course course : courseDB.values()) {
                response.append(";").append(course.courseCode).append(",").append(course.courseName);
            }
            return response.toString();
        }

        private String deleteCourse(String[] parts) {
            String code = parts[1];
            if (courseDB.remove(code) == null) {
                return "ERROR;Course not found";
            }
            return "SUCCESS;Course deleted";
        }
    }
}
