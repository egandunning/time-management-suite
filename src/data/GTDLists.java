package data;

import java.util.ArrayList;

/**
 * A class to store the different types of lists in GTD
 * 
 * @author Egan Dunning
 *
 */
public class GTDLists {

    private static GTDLists instance;
    private ArrayList<String> lists;

    public static GTDLists getInstance() {
	if (instance == null) {
	    instance = new GTDLists();
	}
	return instance;
    }

    private GTDLists() {
	lists = new ArrayList<String>();
	lists.add("in");
	lists.add("next actions");
	lists.add("waiting for");
	lists.add("projects");
	lists.add("some day/maybe");
    }

    public ArrayList<String> getLists() {
	return lists;
    }
}