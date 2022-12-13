/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.jdbctemplateexample;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;


@SpringBootApplication
public class App implements CommandLineRunner {
    @Autowired
    private JdbcTemplate jdbc;
    private static Scanner sc;


    public static void main(String args[]) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        sc = new Scanner(System.in);

        do {
            System.out.println("To-Do List");
            System.out.println("1. Display List");
            System.out.println("2. Add Item");
            System.out.println("3. Update Item");
            System.out.println("4. Remove Item");
            System.out.println("5. Exit");

            System.out.println("Enter an option:");
            String option = sc.nextLine();
            try {
                switch (option) {
                    case "1":
                        displayList();
                        break;
                    case "2":
                        addItem();
                        break;
                    case "3":
                        updateItem();
                        break;
                    case "4":
                        removeItem();
                        break;
                    case "5":
                        System.out.println("Exiting");
                        System.exit(0);
                    default:
                        System.out.println("I don't understand");
                }
            } catch (Exception ex) {
                System.out.println("Error communicating with database");
                System.out.println(ex.getMessage());
                System.exit(0);
            }

        } while (true);
    }

    private void displayList() throws SQLException {
        List<ToDo> todos = jdbc.query("SELECT * FROM todo", new ToDoMapper());
            for(ToDo td : todos) {
                System.out.printf("%s: %s -- %s - %s\n",
                        td.getId(),
                        td.getTodo(),
                        td.getNote(),
                        td.isFinished());
            }
        System.out.println("");
    }

    private void addItem() throws SQLException {
        System.out.println("Add Todo");
        System.out.println("What is the task?");
        String task = sc.nextLine();
        System.out.println("Any additional notes?");
        String note = sc.nextLine();
        jdbc.update("INSERT INTO todo(todo, note) VALUES(?, ?)", task, note);
        System.out.println("Add complete");
    }

    private void updateItem() throws SQLException {
        System.out.println("Update Todo");
        System.out.println("Which item would you like to update?");
        String itemId = sc.nextLine();
        ToDo item = jdbc.queryForObject("SELECT * FROM todo WHERE id = ?", new ToDoMapper(), itemId);
        System.out.println("1. Todo - " + item.getTodo());
        System.out.println("1. Note - " + item.getNote());
        System.out.println("1. Finished - " + item.isFinished());
        System.out.println("What would you like to change?");
        String choice = sc.nextLine();
        switch(choice) {
            case "1":
                System.out.println("Enter new todo: ");
                String todo = sc.nextLine();
                item.setTodo(todo);
                break;
            case "2":
                System.out.println("Enter new note: ");
                String note = sc.nextLine();
                item.setNote(note);
                break;
            case "3":
                System.out.println("Toggling finished status to : " + !item.isFinished());
                item.setFinished(!item.isFinished());
                break;
            default:
                System.out.println("No change made");
                return;
        }
        jdbc.update("UPDATE todo SET todo = ?, note = ?, finished = ? WHERE id = ?",
                item.getTodo(),
                item.getTodo(),
                item.isFinished(),
                item.getId());
        System.out.println("Todo updated successfully");
    }



    private void removeItem() throws SQLException {
        System.out.println("Remove Todo");
        System.out.println("Which Todo would you like to remove?");
        String itemId = sc.nextLine();
        jdbc.update("DELETE FROM todo WHERE id = ?", itemId);
        System.out.println("Todo removed successfully");
    }

}
