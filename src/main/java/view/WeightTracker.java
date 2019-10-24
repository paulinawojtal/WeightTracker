package view;

import model.DateWeightPair;
import model.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WeightTracker {

    private Scanner scan = new Scanner(System.in);

    private String path = "src/main/java/users/";
    private int pathLength = path.length();

    public void showMenu(){

        boolean doYouWantToPlay = true;

        do {
            System.out.println("=======  MENU  =======");
            System.out.println("1 - Add user");
            System.out.println("2 - Show user");
            System.out.println("X - Close program");

            char userChoice = getUserInput().toLowerCase().charAt(0);
            switch(userChoice){
                case '1':
                    createUser();
                    break;
                case '2':
                    showUser();
                    break;
                case 'x':
                    System.out.println("Bye Bye..");
                    doYouWantToPlay = false;
                    break;
                default:
                    System.out.println("Choose what you want to do:");
            }
            System.out.println();
        }
        while(doYouWantToPlay);
    }

    private void createUser(){

        System.out.println("Give me a user name: ");

        String userName = getUserInput().toLowerCase();
        boolean checkUser = isUser(userName);

        if(!checkUser){
            User user = new User(userName);
            createNewFile(userName);
            updateFile(path + userName + ".txt");
        } else{
            System.out.println("User with the given name already exists. Do you want to update this user? Y/N");
            char userChoice = getUserInput().toLowerCase().charAt(0);

            switch(userChoice){
                case 'y':
                    // dodaje date i wage
                    String fullPath = path + userName + ".txt";
                    updateFile(fullPath);
                    break;
                case 'n':
                    break;
                default:
                    System.out.println(" ooooooo ");
                    break;
            }
        }
    }

    private void showUser(){
        System.out.println("Which user you want to look through? Choose the number.");

        int userChoice = 0;

        try (Stream<Path> walk = Files.walk(Paths.get(path))) {
            List<String> result = walk
                    .map(x -> x.toString())
                    .filter(f -> f.endsWith(".txt"))
                    .collect(Collectors.toList());

            result.forEach(s -> System.out.println((result.indexOf(s) + 1) + ": " + s.substring(pathLength, s.length() - 4)));

            do {
                try {
                    userChoice = Integer.parseInt(getUserInput());
                } catch (Exception e) {
                    System.out.println("No number given.");
                }
            } while (userChoice <= 0);

            String file = result.get(userChoice-1);
            System.out.printf("Data for user %s\n", file.substring(pathLength, file.length()-4));
            readDataFromFile(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateFile(String filePath){

        Pattern weightPattern = Pattern.compile("[0-9]+(\\.[0-9]+)?");
        Pattern datePattern = Pattern.compile("[0-9]{4}.[0-1]?[0-9].[0-3]?[0-9]");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

        String userWeight;
        String userDate;

        try (Stream<Path> walk = Files.walk(Paths.get(path))) {
            List<String> result = walk
                    .map(x -> x.toString())
                    .filter(f -> f.endsWith(".txt"))
                    .collect(Collectors.toList());

            do {
                System.out.println("Give weight in [70.3] and date of weighing in [YYYY.MM.DD]:");
                userWeight = getUserInput();
                userDate = getUserInput();

                boolean weightMatches = weightPattern.matcher(userWeight).matches();
                boolean dateMatches = datePattern.matcher(userDate).matches();
                //System.out.format("Weight match: %s, date match: %s\n", weightMatches, dateMatches);

                DateWeightPair newPair;

                if (weightMatches && dateMatches) {
                    try {
                        newPair = new DateWeightPair(sdf.parse(userDate), Float.parseFloat(userWeight));
                        appendWeightToFile(filePath, userDate, userWeight);

                        System.out.format("Added to user %s \nweight %s and date of weighing %s\n", filePath.substring(pathLength, filePath.length()-4), userWeight, userDate);
                    } catch (ParseException e) {
                        System.out.println("Something wrong with date. Example date: 2014.05.17");
                    } catch (NumberFormatException e1) {
                        System.out.println("Wrong weight format. Example weight: 74.5");
                    }
                } else {
                    System.out.println("===> !!! Wrong format of date or weight. Example weight: 74.5, example date: 2014.05.17 !!! <===");
                }
                System.out.println();
            } while (!weightPattern.matcher(userWeight).matches() || !datePattern.matcher(userDate).matches());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendWeightToFile(String fileName, String date, String weight){

        File file = new File(fileName);

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))){

            bw.write(date + ";" + weight);
            bw.newLine();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createNewFile(String name){

        String fileName = path + name + ".txt";
        File file = new File(fileName);

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            System.out.printf("New file %s.txt has been created.\n", name);
        } catch (IOException e) {
            System.out.println("Unable to read file " + file.toString());
        }
    }

    private void readDataFromFile(String path){

        String line;

        try(BufferedReader bfr = new BufferedReader(new FileReader(path))){
            while((line = bfr.readLine()) != null){
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Unable to read file " + path);
        }
    }

    private boolean isUser(String name){
        boolean isUserInList = false;

        try(Stream<Path> walk = Files.walk(Paths.get(path))){
            List<String> result = walk
                    .map(x -> x.toString())
                    .filter(f -> f.endsWith(".txt"))
                    .collect(Collectors.toList());

            for(String s : result){
                String user = s.substring(pathLength, s.length()-4);
                if(name.equals(user)){
                    isUserInList = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isUserInList;
    }

    private String getUserInput() {
        return scan.nextLine();
    }

}
