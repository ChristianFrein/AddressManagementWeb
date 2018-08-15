package address.management;

import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Null;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DAOmysql implements FileAccessInterface
{
    private static String _username = "ffchristianfrein";
    private static String _password = "H:KNO__P/]()JLmt}@Ab#WpaB";
    private static String _url = "jdbc:mysql://localhost:3306/person?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
    private static DAOmysql _singleton = new DAOmysql();
    Connection connection;

    private DAOmysql(){
    }

    static DAOmysql getInstance(){
        return _singleton;
    }

    public void safe(ArrayList<Person> addresses){
        try {
            connection = DriverManager.getConnection(_url, _username, _password);
            Statement initalStatement = connection.createStatement();
            ResultSet rs = initalStatement.executeQuery("SELECT COUNT(*) id FROM address");

            int addressCounter = 0;
            if(rs.next())
                addressCounter = rs.getInt("id") + 1;


            rs = initalStatement.executeQuery("SELECT COUNT(*) address FROM person");

            int personCounter = 0;
            if(rs.next())
                personCounter = rs.getInt("address") + 1;


            int terminationCondition = addresses.size();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            for(int i=0;i<terminationCondition;i++){
                Person person = addresses.get(i);

                if(!person.fromSQL) {
                    PreparedStatement pst = connection.prepareStatement("INSERT INTO address (street,housenumber,city,country,postcode) VALUES (?,?,?,?,?)");
                    pst.setString(1, person.adress.street);
                    pst.setString(2, person.adress.housenumber);
                    pst.setString(3, person.adress.city);
                    pst.setString(4, person.adress.country);
                    pst.setString(5, Integer.toString(person.adress.postcode));
                    pst.execute();
                    pst.close();

                    connection = DriverManager.getConnection(_url, _username, _password);
                    int addressid = 0;

                    pst = connection.prepareStatement("SELECT id FROM address ORDER BY ID DESC LIMIT 1");
                    ResultSet rst = pst.executeQuery();

                    if (rst.next())
                        addressid = rst.getInt("id");


                    pst = connection.prepareStatement("INSERT INTO person (firstname,lastname,address,birthday,age) VALUES (?,?,?,?,?)");
                    pst.setString(1, person.firstname);
                    pst.setString(2, person.lastname);
                    pst.setString(3, Integer.toString(addressid));
                    if (person.birthday != null) {
                        pst.setString(4, formatter.format(person.birthday));
                    } else {
                        pst.setString(4, formatter.format(new Date()));
                    }
                    pst.setString(5, Integer.toString(person.age));
                    pst.execute();
                    pst.close();

                    addressCounter++;
                    personCounter++;

                    person.fromSQL = true;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public ArrayList<Person> read(){
        ArrayList<Person> addresses = new ArrayList<Person>();

        try {
            connection = DriverManager.getConnection(_url, _username, _password);
            Statement statement = connection.createStatement();

            ResultSet amount = statement.executeQuery("SELECT COUNT(*) address FROM person");
            int terminationCondition = 0;

            try {
                if (amount.next())
                    terminationCondition = amount.getInt("address");
            }catch(Exception e) {
                e.printStackTrace();
            }

            ArrayList<Address> ad = new ArrayList<Address>();

            for (int i = 0; i < terminationCondition+1; i++) {
                String query = "SELECT * FROM address LIMIT ";
                query += terminationCondition;
                query += " ";
                query += "OFFSET ";
                query += i;

                ResultSet rsAddress = statement.executeQuery(query);

                if(rsAddress.next()) {
                    Address address = null;

                    address = new Address();
                    address.street = rsAddress.getString("street");
                    address.housenumber = rsAddress.getString("housenumber");
                    address.city = rsAddress.getString("city");
                    address.id = rsAddress.getInt("id");
                    address.country = rsAddress.getString("country");
                    address.postcode = rsAddress.getInt("postcode");

                    ad.add(address);
                }
                rsAddress.close();
            }

            for(int i=0;i<terminationCondition+1;i++) {
                String query = "SELECT * FROM person LIMIT ";
                query += terminationCondition;
                query += " ";
                query += "OFFSET ";
                query += i;
                ResultSet rsPerson = statement.executeQuery(query);

                if(rsPerson.next()) {
                    Person person = new Person();

                    person.firstname = rsPerson.getString("firstname");
                    person.lastname = rsPerson.getString("lastname");
                    person.age = rsPerson.getInt("age");
                    person.id = rsPerson.getInt("id");
                    person.birthday = getDate(rsPerson.getString("birthday"));
                    person.adress = ad.get(i);

                    person.fromSQL = true;
                    addresses.add(person);
                }
                rsPerson.close();
            }

            return addresses;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /*public ArrayList<Person> search(Person person){
        try{
            ArrayList<Person> persons = new ArrayList<>();

            connection = DriverManager.getConnection(_url, _username, _password);
            Statement statement = connection.createStatement();

            String queryEnd = getQuery(person);
            String query = "SELECT COUNT(*) address ";
            query += queryEnd;

            ResultSet rs = statement.executeQuery(query);

            int terminationCondition = 0;

            if(rs.next()){
                terminationCondition = rs.getInt("address");
            }

            queryEnd += " LIMIT ";
            queryEnd += terminationCondition;
            queryEnd += " ";
            queryEnd += "OFFSET ";

            for(int i=0;i<terminationCondition;i++) {
                if(i != 0){
                    queryEnd = queryEnd.substring(0, queryEnd.length() - 1);
                }

                queryEnd += i;
                rs = statement.executeQuery("SELECT * " + queryEnd);

                if(rs.next()) {
                    Person persontmp = new Person();

                    persontmp.firstname = rs.getString("firstname");
                    persontmp.lastname = rs.getString("lastname");
                    persontmp.age = rs.getInt("age");
                    persontmp.id = rs.getInt("id");
                    persontmp.birthday = getDate(rs.getString("birthday"));

                    Address address = new Address();

                    address.street = rs.getString("street");
                    address.housenumber = rs.getString("housenumber");
                    address.city = rs.getString("city");
                    address.country = rs.getString("country");
                    address.postcode = rs.getInt("postcode");
                    address.id = rs.getInt("address");

                    persontmp.adress = address;

                    persons.add(persontmp);
                }
            }

            return persons;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }*/

    public ArrayList<Person> search(Person person){
        try{
            ArrayList<Person> persons = new ArrayList<>();

            connection = DriverManager.getConnection(_url, _username, _password);
            Statement statement = connection.createStatement();

            String queryEnd = getQuery(person);


            ResultSet rs = statement.executeQuery("SELECT * " + queryEnd);

            while(rs.next()) {
                    Person persontmp = new Person();

                    persontmp.firstname = rs.getString("firstname");
                    persontmp.lastname = rs.getString("lastname");
                    persontmp.age = rs.getInt("age");
                    persontmp.id = rs.getInt("id");
                    persontmp.birthday = getDate(rs.getString("birthday"));

                    Address address = new Address();

                    address.street = rs.getString("street");
                    address.housenumber = rs.getString("housenumber");
                    address.city = rs.getString("city");
                    address.country = rs.getString("country");
                    address.postcode = rs.getInt("postcode");
                    address.id = rs.getInt("address");

                    persontmp.adress = address;

                    persons.add(persontmp);
            }

            return persons;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private String getQuery(Person person){
        String queryEnd = "FROM person FULL JOIN address ON address = address.id WHERE ";
        boolean isFirst = true;

        if(StringUtils.isNotBlank(person.firstname)) {
            if (isFirst) {
                queryEnd += "firstname = '" + person.firstname + "' ";
                isFirst = false;
            }
        }

        if(StringUtils.isNotBlank(person.lastname)) {
            if (!isFirst) {
                queryEnd += "AND " + "lastname = '" + person.lastname + "' ";
            } else {
                queryEnd += "lastname = '" + person.lastname + "' ";
                isFirst = false;
            }
        }

        if(person.birthday != null && person.birthday.equals("")) {
            if (!isFirst) {
                queryEnd += "AND " + "birthday = '" + person.birthday + "' ";
            } else {
                queryEnd += "birthday = '" + person.birthday + "' ";
                isFirst = false;
            }
        }

        if(person.age != 0) {
            if (!isFirst) {
                queryEnd += "AND " + "age = '" + person.age + "' ";
            } else {
                queryEnd += "age = '" + person.age + "' ";
                isFirst = false;
            }
        }

        if(StringUtils.isNotBlank(person.adress.street)) {
            if (!isFirst) {
                queryEnd += "AND " + "street = '" + person.adress.street + "' ";
            } else {
                queryEnd += "street = '" + person.adress.street + "' ";
                isFirst = false;
            }
        }

        if(StringUtils.isNotBlank(person.adress.housenumber)) {
            if (!isFirst) {
                queryEnd += "AND " + "housenumber = '" + person.adress.housenumber + "' ";
            } else {
                queryEnd += "housenumber = '" + person.adress.housenumber + "' ";
                isFirst = false;
            }
        }

        if(StringUtils.isNotBlank(person.adress.city)) {
            if (!isFirst) {
                queryEnd += "AND " + "city = '" + person.adress.city + "' ";
            } else {
                queryEnd += "city = '" + person.adress.city + "' ";
                isFirst = false;
            }
        }

        if(StringUtils.isNotBlank(person.adress.country)) {
            if (!isFirst) {
                queryEnd += "AND " + "country = '" + person.adress.country + "' ";
            } else {
                queryEnd += "country = '" + person.adress.country + "' ";
                isFirst = false;
            }
        }

        if(person.adress.postcode != 0) {
            if (!isFirst) {
                queryEnd += "AND " + "postcode = '" + person.adress.postcode + "' ";
            } else {
                queryEnd += "postcode = '" + person.adress.postcode + "' ";
                isFirst = false;
            }
        }

        queryEnd = queryEnd.substring(0, queryEnd.length() - 1);

        return queryEnd;
    }

    public void delete(int id){
        try{
            connection = DriverManager.getConnection(_url, _username, _password);
            PreparedStatement pst = connection.prepareStatement("DELETE FROM person WHERE id = ?");
            pst.setString(1,Integer.toString(id));
            pst.execute();
            pst.close();

            pst = connection.prepareStatement("DELETE FROM address WHERE id = ?");
            pst.setString(1,Integer.toString(id));
            pst.execute();
            pst.close();

        }catch(Exception e){
            e.printStackTrace();
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
}
