import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.undo.*;

public class TextEditor implements ActionListener{

	JFrame frame;
	JTextArea ta;
	JScrollPane scroll;
	JMenuBar mb;
	JMenu file, edit;
	JMenuItem itemNew, open, save, saveas, exit, undo, redo;
	
	String fileName, fileAddress;
	
	UndoManager u = new UndoManager();
	//RedoManager r = new RedoManager();
	
	public static void main(String[] args) {

		new TextEditor();
	}
	
	public TextEditor() {
		
		createFrame();
		createTextArea();
		createMenuBar();
		fileMenu();
		editMenu();
		
		frame.setVisible(true);
	}

	//Creating GUI Frame
	public void createFrame() {
		
		frame = new JFrame("Text Editor");
		frame.setSize(500, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	//Creating TextArea
	public void createTextArea() {
		
		ta = new JTextArea();
		
		ta.getDocument().addUndoableEditListener(
				new UndoableEditListener() {
					public void undoableEditHappened(UndoableEditEvent e) {
						
						u.addEdit(e.getEdit());
					}
				});
		
		scroll = new JScrollPane(ta, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setBorder(BorderFactory.createEmptyBorder());
		
		frame.add(scroll);
	}
	
	//Creating Menu Bar
	public void createMenuBar() {
		
		mb = new JMenuBar();
		file = new JMenu("File");
		edit = new JMenu("Edit");
		
		frame.setJMenuBar(mb);
		mb.add(file);
		mb.add(edit);
		
	}
	
	//File Menu
	public void fileMenu() {
		
		//Add menuitems
		itemNew = new JMenuItem("New");
		open = new JMenuItem("Open");
		save = new JMenuItem("Save");
		saveas = new JMenuItem("Save As");
		exit = new JMenuItem("Exit");
		
		//Add actionListener
		itemNew.addActionListener(this);
		open.addActionListener(this);
		save.addActionListener(this);
		saveas.addActionListener(this);
		exit.addActionListener(this);
				
		//set actionCommands
		itemNew.setActionCommand("New");
		open.setActionCommand("Open");
		save.setActionCommand("Save");
		saveas.setActionCommand("Save As");
		exit.setActionCommand("Exit");
		
		//add item to menu
		file.add(itemNew);
		file.add(open);
		file.add(save);
		file.add(saveas);
		file.add(exit);
	}
	
	//Edit Menu
	public void editMenu() {
		
		undo = new JMenuItem("Undo");
		redo = new JMenuItem("Redo");
		
		undo.addActionListener(this);
		redo.addActionListener(this);
		
		undo.setActionCommand("Undo");
		redo.setActionCommand("Redo");
		
		edit.add(undo);
		edit.add(redo);
	}

	//Create new file
	public void newFile() {
		
		ta.setText("");
		frame.setTitle("Text Editor");
		fileName = null;
		fileAddress = null;
	}
	
	//Open file dialog
	public void open() {
		
		FileDialog fd = new FileDialog(frame, "Open", FileDialog.LOAD);
		fd.setVisible(true);
		
		if(fd.getFile() != null) {
			fileName = fd.getFile();
			fileAddress = fd.getDirectory();
			frame.setTitle(fileName);
		}
		
		System.out.println("File address and file name " + fileAddress + fileName);
		
		try {
			BufferedReader b = new BufferedReader(new FileReader(fileAddress + fileName));
			
			ta.setText("");
			
			String line = null;
			
			while((line = b.readLine()) != null) {
				
				ta.append(line + "\n");
			}
			
			b.close();
			
		} catch(Exception e) {
			
			System.out.println("File Not Opened!");
		}
	}
	
	//Save file
	public void save() {
		
		if(fileName == null) {
			saveas();
		}else {
			try {
				FileWriter fw = new FileWriter(fileAddress + fileName);
				fw.write(ta.getText());
				frame.setTitle(fileName);
				fw.close();
				
			}catch(Exception e) {
				
			}
		}
	}
	
	public void saveas() {
		
		FileDialog fd = new FileDialog(frame, "Save", FileDialog.SAVE);
		fd.setVisible(true);
		
		if(fd.getFile() != null) {
			
			fileName = fd.getFile();
			fileAddress = fd.getDirectory();
			frame.setTitle(fileName);
		}
		
		try {
			
			FileWriter fw = new FileWriter(fileAddress + fileName);
			fw.write(ta.getText());
			fw.close();
			
		}catch(Exception e) {
			
			System.out.println("Something Wrong!");
		}
	}
	
	public void exit() {
		
		System.exit(0);
	}
	
	public void undo() {
		
		u.undo();
	}
	
	public void redo() {
		
		u.redo();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		String command = e.getActionCommand();
		
		switch(command) {
		case "New": newFile();
		break;
		
		case "Open": open();
		break;
		
		case "Save": save();
		break;
		
		case "Save As": saveas();
		break;
		
		case "Exit": exit();
		break;
		
		case "Undo": undo();
		break;
		
		case "Redo": redo();
		break;
		}
	}

}
