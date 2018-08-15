package address.management;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/addressManagement")
public class RestController {
    private WebService webService;

    private RestController() {
        webService = new WebService();
    }

    @GetMapping(value = "/addresses/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Person> getAddress(@RequestHeader("Content-Type") String contentType, @PathVariable int id) {
        HttpHeaders responseHeader = new HttpHeaders();

        if(contentType.equals("application/json")){
            responseHeader.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(webService.getAddress(id), responseHeader, HttpStatus.OK);

        }else if(contentType.equals("application/xml")){
            responseHeader.setContentType(MediaType.APPLICATION_XML);
            return new ResponseEntity<>(webService.getAddress(id), responseHeader, HttpStatus.OK);

        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/addresses", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAddresses(@RequestHeader("Content-Type") String contentType) {
        HttpHeaders responseHeader = new HttpHeaders();

        if(contentType.equals("application/json")) {
            responseHeader.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(webService.getAddresses(), responseHeader, HttpStatus.OK);

        }else if(contentType.equals("application/xml")) {
            responseHeader.setContentType(MediaType.APPLICATION_XML);
            return new ResponseEntity<>(new EntityList<Person>(webService.getAddresses()), responseHeader, HttpStatus.OK);

        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/addresses/")
    public ResponseEntity addAddress(@RequestBody Person person){
        try {
            webService.addAddress(person);
        }catch(InputValueException e){
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/addresses/ages/")
    public void calculateAges(){
        webService.calculateAges();
    }

    @PutMapping(value = "/addresses/search", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> search(@RequestHeader("Content-Type") String contentType, @RequestBody Person person){
        HttpHeaders responseHeader = new HttpHeaders();
        ArrayList<Person> personList = webService.fileDAO.search(person);

        if(personList.size() > 0){
            if(contentType.equals("application/json")){
                responseHeader.setContentType(MediaType.APPLICATION_JSON);
                return new ResponseEntity<>(personList, responseHeader, HttpStatus.OK);
            }else if(contentType.equals("application/xml")){
                responseHeader.setContentType(MediaType.APPLICATION_XML);
                return new ResponseEntity<>(new EntityList<Person>(personList), responseHeader, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<ArrayList<Person>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
