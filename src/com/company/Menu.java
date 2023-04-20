package com.company;

import com.company.observable.CSI;
import com.company.observer.KUPA;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {

    ArrayList<KUPA> registeredUsers = new ArrayList<>();
    private KUPA kupa;
    private final CSI csi = new CSI();
    Scanner scanner = new Scanner(System.in);

    public Menu() {
        csi.startNotificationLoop();
    }

    public void start() {
        startApp();
    }

    public void startApp() {
        System.out.println("Enter: ");
        System.out.println(" - 1 - to sing up ");
        System.out.println(" - 2 - to log in ");
        System.out.println(" - 0 - to quit ");

        int choice = -1;
        try {
            choice = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.err.println("You cannot enter letters here");
        }
        scanner.nextLine();
        String login;

        switch (choice) {
            case 1 -> {
                System.out.println("To sign up please enter your login");
                login = scanner.nextLine();
                kupa = new KUPA(login);
                registeredUsers.add(kupa);
                System.out.println(login + ", welcome to our app. You're signed up");
                startApp();
            }
            case 2 -> {
                System.out.println("To log in please enter your login");
                login = scanner.nextLine();
                KUPA user = new KUPA(login);
                if (registeredUsers.contains(user)) {
                    for (KUPA k : registeredUsers) {
                        if (k.equals(user)) {
                            kupa = k;
                        }
                    }
                    System.out.println(kupa.getLogin() + ", you're logged in");
                    printMenu();
                } else {
                    System.err.println("Please first sign up");
                    startApp();
                }
            }
            case 0 -> {
                try {
                    Thread.sleep(1000);
                    csi.stopNotifications();
                    csi.waitFinish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("That's the end of my app");
            }
            default -> {
                System.err.println("Wrong input, please enter a correct one");
                startApp();
            }
        }
    }

    public void printMenu() {
        System.out.println("Enter: ");
        System.out.println(" - 1 - to subscribe a location");
        System.out.println(" - 2 - to print the collected data");
        System.out.println(" - 3 - to print the subscribed locations");
        System.out.println(" - 4 - to save collected data to Json");
        System.out.println(" - 5 - to print min, max and average value");
        System.out.println(" - 6 - to unsubscribe a location");
        System.out.println(" - 0 - to log out");
        run();
    }

    public void subscribe() {
        System.out.println("Enter:");
        csi.getLocations().printLocations();
        try {
            int nr = scanner.nextInt();
            scanner.nextLine();
            if (nr <= csi.getLocations().getLocationToParameters().keySet().size() && nr > 0) {
                String location = csi.getLocations().getLocationsList().get(nr - 1);
                if (kupa.getSubscribedLocations().contains(location)) {
                    System.out.println("You've already subscribed this location");
                } else {
                    csi.register(kupa, location);
                    kupa.subscribe(location);
                }
            } else {
                System.err.println("This location does not exist");
            }
        } catch (InputMismatchException e) {
            System.err.println("You cannot enter letters here");
        }
    }

    public void unsubscribe() {
        System.out.println("Enter:");
        csi.getLocations().printLocations();
        try {
            int nr = scanner.nextInt();
            scanner.nextLine();
            if (nr <= csi.getLocations().getLocationToParameters().size() && nr > 0) {
                String location = csi.getLocations().getLocationsList().get(nr - 1);
                if (csi.getLocationToSubscribers().get(location).size() != 0) {
                    csi.remove(kupa, location);
                    kupa.unsubscribe(location);
                } else {
                    System.err.println("You don't subscribe this location, so you cannot unsubscribe it");
                }
            } else {
                System.err.println("This location does not exist");
            }
        } catch (InputMismatchException e) {
            System.err.println("You cannot enter letters here");
        }
    }

    public void run() {
        int choice = -1;
        try {
            choice = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.err.println("You cannot enter letters here");
        }
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                subscribe();
                printMenu();
            }
            case 2 -> {
                kupa.printData();
                printMenu();
            }
            case 3 -> {
                kupa.printSubscribedLocations();
                printMenu();
            }
            case 4 -> {
                System.out.println(kupa.saveToJson());
                printMenu();
            }
            case 5 -> {
                kupa.analyse();
                printMenu();
            }
            case 6 -> {
                unsubscribe();
                printMenu();
            }
            case 0 -> {
                System.out.println("Thank you for using my app");
                startApp();
            }
            default -> {
                System.err.println("Wrong number, please enter a correct one");
                printMenu();
            }
        }
    }
}
