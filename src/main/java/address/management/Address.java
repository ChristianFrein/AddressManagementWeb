package address.management;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@XmlRootElement
public class Address implements Serializable {
    String street;
    String housenumber;
    String country;
    String city;
    int postcode;
    @Id
    int id;

    @JsonCreator
    public Address(@JsonProperty("street") String street, @JsonProperty("housenumber") String housenumber, @JsonProperty("city") String city,
                   @JsonProperty("country") String country, @JsonProperty("postcode") int postcode){
        this.street = street;
        this.housenumber = housenumber;
        this.city = city;
        this.country = country;
        this.postcode = postcode;
    }

    public Address(){

    }

    @XmlElement
    public String getStreet(){
        return street;
    }

    public void setStreet(String street){
        this.street = street;
    }

    @XmlElement
    public String getHousenumber(){
        return housenumber;
    }

    public void setHousenumber(String housenumber){
        this.housenumber = housenumber;
    }

    @XmlElement
    public String getCountry(){
        return country;
    }

    public void setCountry(String country){
        this.country = country;
    }

    @XmlElement
    public String getCity(){
        return city;
    }

    public void setCity(String city){
        this.city = city;
    }

    @XmlElement
    public int getPostcode(){
        return postcode;
    }

    public void setPostcode(int postcode){
        this.postcode = postcode;
    }
}
