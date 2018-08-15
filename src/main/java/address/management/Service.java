package address.management;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Queue;
import java.util.Scanner;

public class Service {
    public ArrayList<Person> addresses;
    private final FileAccessInterface fileDAO;
    private static Service _service = new Service();

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";

    private Service(){
        addresses = new ArrayList<>();
        fileDAO = DAOmysql.getInstance();
    }

    public void loop(){

        Scanner scan = new Scanner(System.in);
        boolean isRunning = true;

        while(isRunning){
            String input;
            printInstructions();
            input = scan.next();

            switch(input){
                case "close":
                    System.out.print("Program is shutting down");
                    System.exit(1);
                    break;
                case "print":
                    printList();
                    break;
                case "delete":
                    delete();
                    break;
                case "add":
                    add();
                    break;
                case "ages":
                    calculateAges();
                    break;
                case "safe":
                    fileDAO.safe(addresses);
                    break;
                case "read":
                    addresses = fileDAO.read();
                    break;
                case "bday":
                    changeBirthday();
                    break;
            }
        }
    }

    public static Service getInstance(){
        return _service;
    }

    public void changeBirthday(){
        try {
            System.out.print("id: ");
            Scanner scan = new Scanner(System.in);
            int id = scan.nextInt();
            System.out.println("Input Birthday yyyy-mm-dd: ");
            scan = new Scanner(System.in);
            String bdayString = scan.nextLine();

            for(int i=0;i<addresses.size();i++){
                if(addresses.get(i).id == id){
                    addresses.get(i).birthday = getDate(bdayString);
                }
            }
        }catch(Exception e){

        }
    }

    private Date getDate(String dateString){
        DateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        Date date = null;
        try {
            date = format.parse(dateString);
        }catch(Exception e){

        }

        return date;
    }

    public void printInstructions(){
        String instructions[] = {"add - add new Address",
                "ages - calculates all ages",
                "print - prints list",
                "read - load Information",
                "safe - safe Information",
                "delete - delete specified Address",
                "close - close Program",
                "bday - change Birthday"};

        for(int i=0;i<instructions.length;i++){
            System.out.println(ANSI_GREEN + instructions[i] + ANSI_RESET);
        }

        System.out.println();
        System.out.print("Input: ");
    }

    public void printList(){
        try{
            int terminationCondition = addresses.size();
            int i = 0;

            do{
                System.out.println(" ");
                System.out.println("Address: " + addresses.get(i).adress.street + " " + addresses.get(i).adress.housenumber);
                System.out.println("City: " + addresses.get(i).adress.city);
                System.out.println("Country: " + addresses.get(i).adress.country);
                System.out.println("Postcode: " + addresses.get(i).adress.postcode);
                System.out.println("Firstname: " + addresses.get(i).firstname);
                System.out.println("Lastname: " + addresses.get(i).lastname);
                System.out.println("Birthday: " + addresses.get(i).birthday);
                if(addresses.get(i).age != 0)
                    System.out.println("Age: " + addresses.get(i).age);
                System.out.println("ID: " + addresses.get(i).id);
                System.out.println(" ");

                i++;
                terminationCondition--;
            }while(terminationCondition > 0);
        }catch(Exception e){
        }
    }

    public void delete(){
        System.out.print("Input Person ID: ");
        try{
            Scanner scan = new Scanner(System.in);
            String input = scan.next();

            if(input.matches("[0-9]+")) {
                int id = Integer.parseInt(input);

                DAOmysql dao = DAOmysql.getInstance();
                dao.delete(id);

                int index = addresses.size()/2;
                boolean addressFound = false;

                while(!addressFound){
                    if(addresses.get(index).id == id){
                        addresses.remove(index);
                        addressFound = true;
                    }else if(addresses.get(index).id < id){
                        index += (addresses.size()-index)/2;
                    }else if(addresses.get(index).id > id){
                        index -= index/2;
                    }
                }

                System.out.println("Deletion Successful");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void add(){
        try{
            String attribute[] = {"Street","Housenumber","City","Country","Postcode","Firstname","Lastname"};

            for(int i=0;i<attribute.length;i++)
                System.out.print(attribute[i]+" ");

            System.out.println();

            String values[] = new Scanner(System.in).nextLine().split(" ");

            if(values.length%attribute.length != 0)
                throw new InputValueException("The input doesn't match the requirements!");

            validation(values);

            try{
                addresses.add(new Person(values[0],values[1],values[2],values[3],Integer.parseInt(values[4]),values[5],values[6]));
                addresses.get(addresses.size()-1).id = addresses.get(addresses.size()-2).id + 1;
            }catch(Exception e){

            }
        }catch(Exception e){
            System.out.println(ANSI_RED + e.getMessage() + ANSI_RESET);
        }
    }

    public void validation(String input[]) throws Exception {

        if(!input[0].matches("[a-zA-Z-]+"))
            throw new InputValueException("Error in Token 'Street'!");

        if(!input[1].matches("[0-9]+"))
            throw new InputValueException("Error in Token 'Housenumber'!");

        if(!input[2].matches("[a-zA-Z-]+"))
            throw new InputValueException("Error in Token 'City'!");

        if(!input[3].matches("[a-zA-Z-]+"))
            throw new InputValueException("Error in Token 'Country'!");

        if(!input[4].matches("[0-9][0-9][0-9][0-9][0-9]"))
            throw new InputValueException("Error in Token 'Postcode'!");

        if(!input[5].matches("[a-zA-Z-]+"))
            throw new InputValueException("Error in Token 'Firstname'!");

        if(!input[6].matches("[a-zA-Z-]+"))
            throw new InputValueException("Error in Token 'Lastname'!");
    }

    public void validationV2(Queue<String> input) throws InputValueException{

        if(!input.poll().matches("[a-zA-Z-]+"))
            throw new InputValueException("Error in Token 'Street'!");

        if(!input.poll().matches("[0-9]+"))
            throw new InputValueException("Error in Token 'Housenumber'!");

        if(!input.poll().matches("[a-zA-Z-]+"))
            throw new InputValueException("Error in Token 'City'!");

        if(!input.poll().matches("[a-zA-Z-]+"))
            throw new InputValueException("Error in Token 'Country'!");

        if(!input.poll().matches("[0-9][0-9][0-9][0-9][0-9]"))
            throw new InputValueException("Error in Token 'Postcode'!");

        if(!input.poll().matches("[a-zA-Z-]+"))
            throw new InputValueException("Error in Token 'Firstname'!");

        if(!input.poll().matches("[a-zA-Z-]+"))
            throw new InputValueException("Error in Token 'Lastname'!");
    }

    public void calculateAges(){
        try {
            int terminationCondition = addresses.size();

            for (int i = 0; i < terminationCondition; i++) {
                DateTime date = new LocalDateTime().toDateTime();
                DateTime birthday = new DateTime(addresses.get(i).birthday);
                addresses.get(i).age = date.getYear() - birthday.getYear();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
