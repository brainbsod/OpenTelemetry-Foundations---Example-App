package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");

        try (PrintWriter out = response.getWriter()) {
            // JDBC connection parameters
            String jdbcUrl = "jdbc:mysql://localhost:3306/mydatabase";
            String jdbcUser = "myuser";
            String jdbcPassword = "mypassword";

            // Establishing the connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
                    Statement statement = connection.createStatement()) {

                String query = "SELECT * FROM mytable";
                ResultSet resultSet = statement.executeQuery(query);

                out.println("<html><body>");
                out.println("<h1>Database Results</h1>");
                out.println("<table border='1'>");
                out.println("<tr><th>ID</th><th>Name</th></tr>");

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    out.println("<tr><td>" + id + "</td><td>" + name + "</td></tr>");
                }

                out.println("</table>");
                out.println("</body></html>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "An error occurred while processing the request.");
        }
    }
}