package address.management;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class FileDAO
{
    private static File _jsonPersonFile = new File("PersonManagement.json");
    private static File _xmlPersonFile = new File("PersonManagement.xml");
    private static FileDAO singletonFileDAO = new FileDAO();

    private FileDAO(){
        try{
            if(!_jsonPersonFile.exists())
                _jsonPersonFile.createNewFile();

            if(!_xmlPersonFile.exists())
                _xmlPersonFile.createNewFile();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static FileDAO getInstance(){
        return singletonFileDAO;
    }

    public ArrayList<Person> readJSON(){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

            AddressList al =  objectMapper.readValue(_jsonPersonFile, AddressList.class);

            return al.addresses;
        }catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public void safeJSON(ArrayList<Person> addresses){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(_jsonPersonFile, addresses);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList<Person> readXML(){
        try {
            JAXBContext contextObject = JAXBContext.newInstance(AddressList.class);
            Unmarshaller unmarshaller = contextObject.createUnmarshaller();

            AddressList al = (AddressList) unmarshaller.unmarshal(_xmlPersonFile);
            return al.addresses;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void safeXML(ArrayList<Person> addresses){
        try {
            JAXBContext contextObject = JAXBContext.newInstance(AddressList.class);

            Marshaller marshaller = contextObject.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            AddressList al = new AddressList();
            al.addresses = addresses;
            marshaller.marshal(al, new FileOutputStream(_xmlPersonFile));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
