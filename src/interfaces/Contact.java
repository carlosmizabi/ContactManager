package interfaces;

/**
* A contact is a person we are making business with or may do in the future.
*
* Contacts have an ID (unique), a name (probably unique, but maybe
* not), and notes that the user may want to save about them.
*/
public interface Contact {
/**
* Returns the ID of the contact.
*
* @return the ID of the contact.
*/
int getId();
/**
* Returns the name of the contact.
*
* @return the name of the contact.
*/
String getName();
/**
* Returns our notes about the contact, if any.
*
* If we have not written anything about the contact, the empty
* string is returned.
*
* @return a string with notes about the contact, maybe empty.
*/
String getNotes();
/**
* Add notes about the contact.
*
* @param note the notes to be added
*/
void addNotes(String note);
}
