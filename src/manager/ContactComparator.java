package manager;

import java.util.Comparator;
import interfaces.Contact;


/**
 * This Comparator<T> implementation takes contacts and 
 * compares them based on their id
 * 
 * @author carlosmarques
 */
public class ContactComparator implements Comparator<Contact> {

	@Override
	public int compare(Contact arg0, Contact arg1) 
	{
		if(arg0.getId() > arg1.getId())
		{
			return 1;
		}else if(arg0.getId() == arg1.getId()){
			return 0;
		}else{
			return -1;
		}
	}

}
