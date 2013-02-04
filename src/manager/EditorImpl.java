package manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;			// holds the clipboard items
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;			// holds the new elements info to generate them
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;

import java.util.Set;
import interfaces.Contact;
import interfaces.Editor;
import interfaces.Meeting;
import interfaces.PastMeeting;

public class EditorImpl implements Editor {
	
	private static final 
	String FILENAME = "userdata.xml";	// user data file name
	private File file;
	private Scanner scanner;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private ArrayList<HashMap<String, String>> clipboard;			// holder of new objects elements (contact or meetings).
	
	/* REVERSED HASHMAP : the content are the keys of the Map and the 
	 * xml tags are the value. Though it might sound counter-intuitive
	 * it is the only way to have several contact ids since the hashmap 
	 * keys have to be unique. A side effect is that empty elements are
	 * not stored. s
	 * Each object (contact or meeting) has a group of elements/attributes.
	 * The key is the corresponding xml tag and the values (the content)
	 * For example: <id>999</id> -> key: "id" value: "999" 
	 * !! BOTH STRINGS ONLY! 
	 * !! SPECIAL CASE: <contac> or <meeting> this is useful to determine the type
	 * of object that needs to be created. Hence, even though it is only used to
	 * wrap the object type, respectively, it is extracted to inform us of object type
	 * that needs to be generated. The id value of the object is added as its value
	 * for commodity (key: "contact" value: "999").
	 */
	private HashMap<String, String> objectAttributes;	
	
	// Own List
	//
	Set<Contact> initialContactList = new TreeSet<Contact>();
	List<Meeting> initialMeetingList = new LinkedList<Meeting>(); 
	List<PastMeeting> initialPastMeetingList = new LinkedList<PastMeeting>();
	
	// Reference to lists
	//
	Set<Contact> contactList;
	List<Meeting> meetingList; 
	List<PastMeeting> pastMeetingList;
	
	
	/*
	 *  TASK LIST
	 *  
	 *  1 ......		The Editor must receive the contactList, meetingList
	 *  			and pastMeetingList. Why? At start the client will
	 *  			have this lists empty, if there is a user data file
	 *  			the editor can populate the lists with the user data.
	 *  			So, to ensure that the user data is not overwritten,
	 *  			the editor must receive the lists first.
	 *  
	 *  2 ......		Check if the user file exists: 
	 *  
	 *  		2.1 ...	If FALSE, create one.
	 *  			 	The editor must create a skeleton xml file.
	 *  		2.2 ...	If TRUE, load the file, read it and populate the client
	 *  				list accordingly and keep a copy.
	 *  
	 *  3 ......		if the client wants to save the current information
	 *  			on is lists the editor must apply all modifications: 
	 *  
	 *  		3.1 ... Editor must go through every list and spot changes
	 *  				(the only changes allowed are and additions).
	 *  		3.2 ... After the addition list is set the Editor
	 *  				must read the xml file and add new items to the list.
	 *  				
	 *  	
	 */
	
	public EditorImpl(Set<Contact> contacts, List<Meeting> meetings, List<PastMeeting> pastMeetings)
	{
		// is there a xml user date source file? = need method to check
		//System.out.print(setFile());
		
		readFile();
		objectGenerator();
		addInitialLists(contacts, meetings, pastMeetings);

	}


	private void addInitialLists(Set<Contact> contacts, List<Meeting> meetings, List<PastMeeting> pastMeetings) 
	// TODO implement pastMeeting
	{	
		meetings.addAll(initialMeetingList);
		contacts.addAll(initialContactList);	
	}
	
	/**
	 * 
	 * 
	 * @return	true if the file already exists or 
	 * 		   	the result of making the file.
	 */
	private boolean setFile()
	{
		if(fileExists() == true)
		{
			readFile();
			return true;
		}else{
			return makeFile();
		}
	}
	
	/**
	 * 
	 * @return true if user data file exists;
	 */
	private boolean fileExists()
	{
		try {
			scanner = new Scanner(new FileReader(FILENAME));
		}catch (IOException ex)
		{
			return false;
		}
		return true;

	}
	
	/**
	 * 
	 * @return true if file creation and writing succeeds
	 */
	private boolean makeFile()
	{
		file = new File(FILENAME);
		try {
			out = new PrintWriter(file);
			XmlFormat xml = new XmlFormat();
			out.write(xml.getSkeleton());
		} catch (FileNotFoundException ex) {
			// This happens if file does not exist and cannot be created,
			// or if it exists, but is not writable, return false
			System.out.println("could not read file");
			return false;
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			out.close();
		}
		return true;
	}
	
	/*
	 * IMPORTANT: The readeFile and related methods needs 
	 * every xml tag to be on their own line
	 */
	private boolean readFile()
	{
		try{
//System.out.println("Reading file");  //+++++++++++++++++++++++++++++++++
			
			in = new BufferedReader(new FileReader("userdata99.xml"));
			String line;						// store the line of file being read
						
			/* This element will be used by the readerLineProcessor. It needs to 
			 * be instantiated here because we only need from here on.
			 */
			clipboard = new ArrayList<HashMap>();	// store attribute objects(each a list of values)
			
			while ((line = in.readLine()) != null) 
			{
//System.out.println("going to: readerLineProcessor(line);"); //+++++++++++++++++++++++++++++++++
				readerLineProcessor(line);
//System.out.println("back from: readerLineProcessor(line);"); //+++++++++++++++++++++++++++++++++
			}
			in.close();
		}catch(IOException ex)
		{
			System.out.println("could not read file");
			return false;
		}
		
		return true;
	}
	
	/*
	 * This variables are needed for the readerLineProcessor
	 * They are instantiated just before the ^ method
	 */
	private String  value = "", attribute = "";
	private Boolean readLines = false;	// sentinel for which lines to read or ignore
	
	private void readerLineProcessor(String line)
	/*
	 *  Receives each line read and processes it. 
	 */
	{
		if(line.startsWith("<contact>") | line.startsWith("<meeting>"))
		{
//System.out.println("Open - line.startsWith"); //+++++++++++++++++++++++++++++++++
			objectAttributes = new HashMap();	// Create new object of attributes
			attribute = trimToKey(line);			// trim line to the command
			objectAttributes.put(attribute, attribute);
			readLines = true;
			
		}else{
			// Process Lines only after <contact> or <meeting> until they close
			//
			if(readLines == true)
				{
//System.out.println("readlines : " + readLines); //+++++++++++++++++++++++++++++++++
				// Xml closing tag
				//
				if(line.startsWith("</contact>") | line.startsWith("</meeting>"))
				{
//System.out.println("Close - line.startsWith"); //+++++++++++++++++++++++++++++++++
					clipboard.add(objectAttributes);
					readLines = false;
				}else{
					if(!line.startsWith("</") && !line.startsWith("<cidList>")){
						value = trimToValue(line);
//System.out.println("value = trimToValue(line): " + value);
						attribute = trimToKey(line);
						objectAttributes.put(value, attribute);
						value = "";
						attribute = "";
					}
				}
			}
		}

	}
	
	
	private String trimToValue(String str)
	/*
	* Removes the xml tags
	* example: "<id>999</id>" -> "999"
	*/
	{
		return (str.replaceAll("<(/)*\\w*>", ""));
	}
	
	private String trimToKey(String str)
	/*
	* Trims xml line to open tag only
	* example: "<id>999</id>" -> "<id>"
	*/
	{
		return str.substring(1, str.indexOf('>'));
	}
	
	/*
	 * After the file has been read it is time to
	 * fill the client list with their respective objects
	 * IMPORTANT!! First the contacts and after the Meetings
	 * WHY? Because the Meetings have sets of contacts and 
	 * the contactList will supply them. 
	 */
	private void objectGenerator()
	{
		// Iterate over each and call the appropriate creator method
		//System.out.println("objectGenerator()"); //+++++++++++++++++++++++++++++++++++++++++++++++
		for(int i = 0; i < clipboard.size(); i++)
		{
			// using a temp hashmap already defined in class
			// 
			objectAttributes = clipboard.get(i);
			
			if(objectAttributes.containsValue("contact"))
			{
				//System.out.println("Its a Contact"); //+++++++++++++++++++++++++++++++++++++++++++++++
				addContact(objectAttributes);
			}else if(objectAttributes.containsValue("meeting"))
			{
				//System.out.println("Its a meeting"); //+++++++++++++++++++++++++++++++++++++++++++++++
				addMeeting(objectAttributes);
			}
			
		}
	}
	
	private void addContact(HashMap<String, String> contactHashMap)
	{
		//System.out.println("Its a Contact"); //+++++++++++++++++++++++++++++++++++++++++++++++
		String name = "";
		String note = "";
		int id = 0;
		
		for(Map.Entry<String, String> entry : contactHashMap.entrySet())
		{
			if(entry.getValue().equals("name"))
			{
				name = entry.getKey();
			}else if(entry.getValue().equals("note"))
			{
				note += "\n" + entry.getKey();
			}else if(entry.getValue().equals("id"))
			{
				System.out.println(value);
				id = Integer.parseInt(entry.getKey());
			}
		}
		ContactImpl newContact = new ContactImpl(name,note,id);
		//
		// add to list
		initialContactList.add((Contact) newContact);
		
		
//		System.out.print("Contact:\nId: " +  newContact.getId() + 
//							"\nname: " +newContact.getName() +
//								"\nnotes: "  + newContact.getNotes());
		
	}
	
	private void addMeeting(HashMap<String, String> meetingHashMap)
	{
		// System.out.println("Its a Meeting"); 
		Set<Contact> contacts;
		Calendar date = null;
		String text = "";
		int[] cid = new int[40];
		int cidCounter = 0;
		int id = 0;
		
		for(Map.Entry<String, String> entry : meetingHashMap.entrySet())
		{
			if(entry.getValue().equals("id"))
			{
				id = Integer.parseInt(entry.getKey());
				
			}else if(entry.getValue().equals("date"))
			{
				
				int[] dateArray =  processDate(entry.getKey());
				
				// dateArray[0] = year, dateArray[1] = month, dateArray[2] = day
				//
				date = new GregorianCalendar(dateArray[0], dateArray[1], dateArray[2]);
				
			}else if(entry.getValue().equals("text"))
			{
				text += "\n" + entry.getKey();
				
			}else if(entry.getValue().equals("cid"))
			{
				cid[cidCounter] = Integer.parseInt(entry.getKey());
				cidCounter++;
			}
		}
		
		//In order for meeting to be set up, there has to be a minimum 
		// of one contact in the contact list
		//
		if(cidCounter > 0)
		{
			Set<Contact> set = new TreeSet<Contact>();
			// search the contact list for the contact id.
				Iterator<Contact> itit = initialContactList.iterator();
				while(itit.hasNext()){
					for(int i = 0; i < cidCounter; i++)
					{
						if(itit.next().getId() == i)
						{
							set.add(itit.next()); 
						}
					}
				}
				
			MeetingImpl newMeeting = new MeetingImpl(set,date,id);
			//
			// add to list
			initialMeetingList.add(newMeeting);
			
//			System.out.println("initialContactList Size: " +  initialContactList.size());
//			System.out.println("initialMeetingList Size: " +  initialMeetingList.size());
//			System.out.println("Meeting:\nId: " +  newMeeting.getId());
		}
	}
	
	private int[] processDate(String dateString)
	{
		String str = "20" + dateString.substring(6);
		int year = Integer.parseInt(str);
		
		str = dateString.substring(3,5);
		int month = Integer.parseInt(str);
		
		str = dateString.substring(0,1);
		int day = Integer.parseInt(str);
		
		int[] date = {year, month, day};
		return date;
	}
	@Override
	public boolean save(Set<Contact> contacts, List<Meeting> meetings) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/*
	 * Prints size of objects on the clipboard, and the objects and its values
	 * uncomment only if needed for development or debugging.
	 */
//	public void printClipBoard()
//	{
//		System.out.println("Clipboard " + " size " + clipboard.size());
//		for(int i = 0; i < clipboard.size(); i++)
//		{
//			// using a temp hashmap already defined in class
//			// 
//			objectAttributes = clipboard.get(i);
//			System.out.println("\n" + "Object: " + i);
//			System.out.println("\n" + "Object: " + objectAttributes);
//			for(Map.Entry<String, String> entry :objectAttributes.entrySet())
//			{
//				// Remember reversed HashMap
//				//
//				String value = entry.getKey();
//				String key = entry.getValue();
//				
//				System.out.println( key + " " + "=" + " " + value);
//			}
//		}
//		
//	}

}
