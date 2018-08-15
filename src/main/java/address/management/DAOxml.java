package address.management;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class DAOxml implements FileAccessInterface{
    private static File _personFile = new File("PersonManagement.xml");
    private static DAOxml _singleton = new DAOxml();

    private DAOxml(){
        try{
            if(!_personFile.exists())
                _personFile.createNewFile();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    static DAOxml getInstance(){
        return _singleton;
    }

    public ArrayList<Person> read(){
        try {
            JAXBContext contextObject = JAXBContext.newInstance(AddressList.class);
            Unmarshaller unmarshaller = contextObject.createUnmarshaller();

            AddressList al = (AddressList) unmarshaller.unmarshal(_personFile);
            return al.addresses;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void safe(ArrayList<Person> addresses){
        try {
            JAXBContext contextObject = JAXBContext.newInstance(AddressList.class);

            Marshaller marshaller = contextObject.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            AddressList al = new AddressList();
            al.addresses = addresses;
            marshaller.marshal(al, new FileOutputStream(_personFile));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Person> search(Person person){
        return null;
    }
}