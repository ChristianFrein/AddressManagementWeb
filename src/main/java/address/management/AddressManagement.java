package address.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.xml.bind.annotation.XmlRootElement;

@SpringBootApplication
@XmlRootElement
public class AddressManagement {

    public static void main(String[] args){
        SpringApplication.run(AddressManagement.class,args);
        Service service = Service.getInstance();
        service.loop();
    }
}