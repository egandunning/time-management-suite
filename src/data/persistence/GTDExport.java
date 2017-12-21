package data.persistence;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import models.GTDListItem;
import ui.element.GTDText;

/**
 * Class for handling export of GTD list items.
 * @author Egan Dunning
 *
 */
public class GTDExport {
		
	/**
	 * Write the list of listname, VBox pairs to csv file.
	 * @param filename the file to write to.
	 * @param lists the list of lists to write from.
	 * @param overwrite overwrites existing file if true.
	 * @return true if successful.
	 */
	public static boolean write(String filename, ArrayList<Pair<String,VBox>> lists, boolean overwrite) {
		
		File csvFile = null;
		
		try {
			csvFile = new File(filename);
			if(csvFile.exists() && !overwrite) {
				return false;
			}
			csvFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		try(PrintWriter fout = new PrintWriter(csvFile)) {
			
			//write list of elements to row in csv file
			for(Pair<String,VBox> list : lists) {
				//list name
				System.out.println("writing " + list.getKey() + " elements...");
				fout.write(list.getKey() + ", ");
				
				//list elements
				for(Node n : list.getValue().getChildren()) {
					if(n instanceof GTDText) {
						//write item name if not null
						GTDListItem item = ((GTDText)n).getItem();
						fout.write((item != null)?item.getText():"");
						
						//append date if not null
						if(item.getDeadline() != null) {
							fout.write(":" + item.getDeadline().toString());
						}
						fout.write(",");
					}
				}
				//new line
				fout.write(System.lineSeparator());
			}
			return true;
			
		} catch (IOException e) {
			e.printStackTrace();
			//delete previously created file
			try {
				csvFile.delete();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return false;
	}

}
