package address.management;

import java.util.ArrayList;

public interface FileAccessInterface
{
    ArrayList<Person> read();
    void safe(ArrayList<Person> addresses);
    ArrayList<Person> search(Person person);
}
