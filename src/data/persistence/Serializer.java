package data.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.GTDListItem;
import ui.element.GTDText;
import util.ListItemElement;

/**
 * A class for writing objects to file and reading objects from file.
 * 
 * @author Egan Dunning
 */
public class Serializer {

    private static Serializer instance = null;
    private static final String ROOT_DIR = "data";

    private Serializer() {
    }

    /**
     * Get the instance of the Serializer object.
     * 
     * @return a Serializer.
     */
    public static Serializer getInstance() {

	if (instance == null) {
	    instance = new Serializer();
	}
	return instance;
    }

    public boolean writeNodeList(ObservableList<Node> textList, String filename) {

	ArrayList<String> list = new ArrayList<>(textList.size());
	for (Node t : textList) {
	    list.add(((Text) t).getText());
	}

	return write(list, filename);
    }

    @SuppressWarnings("unchecked")
    public ObservableList<Node> readNodeList(String filename) {

	ArrayList<String> list = (ArrayList<String>) read(filename);

	if (list == null) {
	    return null;
	}

	ObservableList<Node> textList = new VBox().getChildren();

	for (String t : list) {
	    textList.add(new Text(t));
	}
	return textList;
    }

    /**
     * Writes an object to specified file. If overwrite parameter is true, any
     * existing file with the same filename is overwritten. Files are saved in a
     * directory called data.
     * 
     * @param obj
     *            The object to write to file.
     * @param filename
     *            the filename to write the object to.
     * @param overwrite
     *            a flag to determine if an existing file should be overwritten.
     *            True causes existing files to be overwritten.
     * @return true if the object was written to file.
     */
    public boolean write(Serializable obj, String filename, boolean overwrite) {

	System.out.println("Object to write: " + obj.toString());

	File f = new File(ROOT_DIR + "\\" + filename + ".txt");

	// if overwrite flag is false and the file exists, return false
	if (!overwrite && f.exists()) {
	    return false;
	}

	// write object to file
	try (FileOutputStream fout = new FileOutputStream(f); ObjectOutputStream oout = new ObjectOutputStream(fout)) {

	    f.createNewFile();
	    oout.writeObject(obj);
	    return true;

	} catch (IOException e) {
	    e.printStackTrace();
	}
	return false;
    }

    /**
     * Writes an object to specified file. Any existing file with the same filename
     * is overwritten. Files are saved in a directory called data.
     * 
     * @param obj
     *            The object to write to file.
     * @param filename
     *            the filename to write the object to.
     * @return true if the object was written to file
     */
    public boolean write(Serializable obj, String filename) {
	return write(obj, filename, true);
    }

    /**
     * Read an object from a file.
     * 
     * @param filename
     *            the file to read.
     * @return the deserialized object stored in the file.
     */
    public Serializable read(String filename) {

	File f = new File(ROOT_DIR + "\\" + filename + ".txt");

	// read object from file
	try (FileInputStream fin = new FileInputStream(f); ObjectInputStream oin = new ObjectInputStream(fin)) {

	    Serializable temp = (Serializable) oin.readObject();
	    System.out.println("Object read: " + temp);
	    return temp;

	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * Write a list of GTDText objects to disk.
     * 
     * @param list
     *            the list of GTDText objects.
     * @param filename
     *            the location to store the list.
     * @return true if operation was successful.
     */
    public boolean writeGTDList(ArrayList<GTDText> list, String filename) {

	if (list == null) {
	    return false;
	}

	ArrayList<GTDListItem> beans = new ArrayList<>(list.size());

	for (GTDText node : list) {
	    beans.add(node.getItem());
	}

	return write(beans, filename);
    }

    /**
     * Read list of GTDListItems that were written to file using writeGTDList.
     * 
     * @param filename
     *            The file to read from.
     * @return
     */
    @SuppressWarnings("unchecked")
    public ArrayList<GTDText> readGTDList(String filename) {

	Serializable obj = read(filename);
	if (obj == null) {
	    return null;
	}
	ArrayList<GTDListItem> beans = (ArrayList<GTDListItem>) obj;

	ArrayList<GTDText> nodes = new ArrayList<>(beans.size());

	for (GTDListItem bean : beans) {
	    nodes.add(ListItemElement.generate(bean));
	}

	return nodes;
    }
}
