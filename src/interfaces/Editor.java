package interfaces;
import java.util.List;
import java.util.Set;
/**
*  A class to manage the ContactManager stored file.
*  At start this class reads the user stored file and initializes
*  the lists of contacts and meetings. It fills the client lists 
* with the information from the user stored file.
*  A the end or at save it must receive the list back to
* delete, add or change records of file.
*/
public interface Editor {

/**
* Save the user data and changes.
*
* @param the set of contacts
* @param the list of meetings
*
* @return the if successful
*/
boolean save(Set<Contact> contacts, List<Meeting> meetings);

}

