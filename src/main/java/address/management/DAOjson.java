package address.management;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;

public class DAOjson implements FileAccessInterface
{
    private static File _personFile = new File("PersonManagement.json");
    private static DAOjson _singleton = new DAOjson();

    private DAOjson(){
        try{
            if(!_personFile.exists())
                _personFile.createNewFile();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    static DAOjson getInstance(){
        return _singleton;
    }

    public ArrayList<Person> read(){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

            AddressList al =  objectMapper.readValue(_personFile, AddressList.class);

            return al.addresses;
        }catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public void safe(ArrayList<Person> addresses){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(_personFile, addresses);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList<Person> search(Person person){
        return null;
    }
}
