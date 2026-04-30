package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/health_tracker?useSSL=false&serverTimezone=UTC&characterEncoding=utf8";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver không tìm thấy!");
            System.err.println("Hãy thêm file này vào Classpath: lib/mysql-connector-j-9.6.0.jar");
            javax.swing.JOptionPane.showMessageDialog(null, 
                "Thiếu thư viện kết nối MySQL!\n\nHãy thêm file JAR trong thư mục 'lib' vào dự án của bạn.", 
                "Lỗi Thư Viện", javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối database: " + e.getMessage());
            javax.swing.JOptionPane.showMessageDialog(null, 
                "Không thể kết nối đến MySQL XAMPP!\n\n1. Hãy mở XAMPP và Start MySQL.\n2. Kiểm tra đã import database chưa.", 
                "Lỗi Kết Nối", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Lỗi đóng kết nối: " + e.getMessage());
        }
    }
}
