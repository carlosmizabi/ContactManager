package manager;

/**
 * This class holds the xml templates.
 * For each element requested it returns
 * a a string array with the open element
 * in [0] and the closing element in [1].
 * 
 * @author carlosmarques
 *
 */

public class XmlFormat {
	
	private static final String xmlSkeleton = 	
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" 
			+
			"<contactManager>\n"
			+		
			"<contactList>\n"
			+			
			"</contactList>\n"
			+
			"<meetingList>\n"
			+		
			"</meetingList>\n"
			+
			"</contactManager>\n";

	private static final String CONTACT = "contact";
	private static final String MEETING = "meeting";
	private static final String NAME = "name";
	private static final String NOTE = "note";
	private static final String ID = "id";
	private static final String DATE = "date";
	private static final String TEXT = "text";
	private static final String CIDLIST = "cidlist";
	private static final String CID = "cid";
			
	
	/**
	 * @return the contact
	 */
	public String[] getContact() {
		return toArray(CONTACT);
	}

	/**
	 * @return the meeting
	 */
	public String[] getMeeting() {
		return toArray(MEETING);
	}

	/**
	 * @return the name
	 */
	public String[] getName() {
		return toArray(NAME);
	}

	/**
	 * @return the note
	 */
	public String[] getNote() {
		return toArray(NOTE);
	}

	/**
	 * @return the id
	 */
	public String[] getId() {
		return toArray(ID);
	}

	/**
	 * @return the date
	 */
	public String[] getDate() {
		return toArray(DATE);
	}

	/**
	 * @return the text
	 */
	public String[] getText() {
		return toArray(TEXT);
	}

	/**
	 * @return the cidlist
	 */
	public String[] getCidlist() {
		return toArray(CIDLIST);
	}

	/**
	 * @return the cid
	 */
	public String[] cid() {
		return toArray(CID);
	}

	public String getSkeleton()
	{
		return xmlSkeleton;
	}
	  
	private String[] toArray(String element)
	{
		String open = "<" + element + ">";
		String close = "</" + element.substring(1) + "\n";
		String[] array = {open,close};
		return array;
	}

}






