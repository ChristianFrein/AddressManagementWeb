package address.management;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

@XmlRootElement
public class AddressList implements Serializable
{
    ArrayList<Person> addresses;

    public AddressList() {
    }

    @JsonCreator
    public AddressList(@JsonProperty ArrayList<Person> addresses){
        this.addresses = addresses;
    }

    public ArrayList<Person> getAddresses(){
        return addresses;
    }

    public void setAddresses(ArrayList<Person> addresses){
        this.addresses = addresses;
    }
}
