package address.management;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class WebService {
    FileAccessInterface fileDAO;

    public WebService(){
        fileDAO = DAOmysql.getInstance();
    }

    public Person getAddress(int id) {
        ArrayList<Person> person = fileDAO.read();

        for(Person currentPerson : person){
            if(currentPerson.id == id)
                return currentPerson;
        }
        return null;
    }

    public ArrayList<Person> getAddresses() {
        return fileDAO.read();
    }

    public void addAddress(Person person)throws InputValueException{
        Queue<String> input = new LinkedList<>();
        input.add(person.adress.street);
        input.add(person.adress.housenumber);
        input.add(person.adress.city);
        input.add(person.adress.country);
        input.add(Integer.toString(person.adress.postcode));
        input.add(person.firstname);
        input.add(person.lastname);


            Service.getInstance().validationV2(input);
            ArrayList<Person> ap = new ArrayList<>();
            ap.add(person);
            fileDAO.safe(ap);
    }

    public void calculateAges(){
        ArrayList<Person> addresses = fileDAO.read();
        try {
            int terminationCondition = addresses.size();

            for (int i = 0; i < terminationCondition; i++) {
                DateTime date = new LocalDateTime().toDateTime();
                DateTime birthday = new DateTime(addresses.get(i).birthday);
                addresses.get(i).age = date.getYear() - birthday.getYear();
            }

            fileDAO.safe(addresses);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
