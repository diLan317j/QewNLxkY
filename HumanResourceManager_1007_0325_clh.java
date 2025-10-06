// 代码生成时间: 2025-10-07 03:25:30
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder.*;
import io.javalin.http.Context;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HumanResourceManager {

    private static final Map<String, Employee> employees = new HashMap<>();

    // Employee class to hold employee data
    public static class Employee {
        private String id;
        private String name;
        private String role;

        public Employee(String id, String name, String role) {
            this.id = id;
            this.name = name;
            this.role = role;
        }

        // Getters and setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);

        // Route to add an employee
        app.post("/employees", ctx -> {
            Employee employee = ctx.bodyAsClass(Employee.class);
            if (employee == null) {
                ctx.status(400).result("Invalid employee data");
                return;
            }
            employees.put(employee.getId(), employee);
            ctx.status(201).result("Employee added successfully");
        });

        // Route to get all employees
        app.get("/employees", ctx -> {
            List<Employee> allEmployees = employees.values().stream().collect(Collectors.toList());
            ctx.json(allEmployees);
        });

        // Route to get an employee by ID
        app.get("/employees/:id", ctx -> {
            String id = ctx.pathParam("id");
            Employee employee = employees.get(id);
            if (employee == null) {
                ctx.status(404).result("Employee not found");
                return;
            }
            ctx.json(employee);
        });

        // Route to update an employee
        app.put("/employees/:id", ctx -> {
            String id = ctx.pathParam("id");
            Employee employee = ctx.bodyAsClass(Employee.class);
            if (employee == null || !id.equals(employee.getId())) {
                ctx.status(400).result("Invalid employee data or ID mismatch");
                return;
            }
            employees.put(id, employee);
            ctx.status(200).result("Employee updated successfully");
        });

        // Route to delete an employee
        app.delete("/employees/:id", ctx -> {
            String id = ctx.pathParam("id");
            if (!employees.containsKey(id)) {
                ctx.status(404).result("Employee not found");
                return;
            }
            employees.remove(id);
            ctx.status(200).result("Employee deleted successfully");
        });
    }
}
