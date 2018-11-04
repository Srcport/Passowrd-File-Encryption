package dashboard;

import home.Home;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import logs.Log;
import config.General;
import config.Settings;

public class Dashboard extends JPanel implements ActionListener, DropTargetListener
{

    private static final long serialVersionUID = 1L;
    
    private static JLabel title = new JLabel("Encrypt Text, Files and Folders", SwingConstants.CENTER);
    
    private static JLabel inputText = new JLabel("Input Text:");
    private static JLabel outputText = new JLabel("Output Text:");
    private static JLabel inputPass = new JLabel("Password:");
    private static JLabel inputDrop = new JLabel("Drop a File or Folder Here:", SwingConstants.RIGHT);
    private static JLabel selectFile = new JLabel("Select Item(s):");
    private static JLabel selectedFile = new JLabel("Location:");
   
    public static JLabel dropArea = new JLabel();
        
    private static JTextArea textField = new JTextArea();
    private static JScrollPane textScroll = new JScrollPane(textField, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private static JTextArea outTextField = new JTextArea();
    private static JScrollPane outTextScroll = new JScrollPane(outTextField, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);   
    private static JTextField locationField = new JTextField();
    private static JPasswordField passField = new JPasswordField();
    
    private static JButton textEncrypt = new JButton("Encrypt Text");
    private static JButton textDecrypt = new JButton("Decrypt Text");
    private static JButton fileLoad = new JButton("Load File / Folder");
    private static JButton fileEncrypt = new JButton("Encrypt File(s)");
    private static JButton fileDecrypt = new JButton("Decrypt File(s)");
    private static JButton textCopy = new JButton("Copy Output Text");
    private static JButton clear = new JButton("Clear");
    
    private static String itemLocation = "";
    public static int minPassLength = 8;

    public Dashboard() 
    {
	setLayout(new GridBagLayout());
	GridBagConstraints gbc = new GridBagConstraints();
	setBackground(Settings.AP_GREY);
	
	// Special
	DropTarget dropTarget = new DropTarget (this, this);
	dropArea.setDropTarget(dropTarget);
	textField.setLineWrap(true);
	textField.setWrapStyleWord(true);
	outTextField.setLineWrap(true);
	outTextField.setWrapStyleWord(true);
	outTextField.setEditable(false);
	locationField.setEditable(false);
	
	// Action Listeners
	textDecrypt.addActionListener(this);
	textEncrypt.addActionListener(this);
	fileLoad.addActionListener(this);
	fileEncrypt.addActionListener(this);
	fileDecrypt.addActionListener(this);
	textCopy.addActionListener(this);
	clear.addActionListener(this);
	
	// Sizes
	dropArea.setPreferredSize(new Dimension(200, 185));
	textScroll.setPreferredSize(new Dimension(200, 75));
	outTextScroll.setPreferredSize(new Dimension(200, 75));
	passField.setPreferredSize(new Dimension(200, 25));
	locationField.setPreferredSize(new Dimension(230, 25));

	// Design
	title.setFont(Settings.REG_FONT);
	title.setForeground(Settings.AP_ORANGE);
	dropArea.setBorder(BorderFactory.createMatteBorder(1, 5, 1, 1, Settings.AP_ORANGE));
	
	// Images
	java.net.URL imgURL = getClass().getResource("/resources/background.png");
	ImageIcon icon = new ImageIcon(imgURL);
	Image image = icon.getImage();
	Image imageIcon = image.getScaledInstance(300, 185,  java.awt.Image.SCALE_SMOOTH);			
	dropArea.setIcon(new ImageIcon(imageIcon));
	
	// Add items
	int i = 0;
	gbc.gridx = 0;
	gbc.gridy = i;
	gbc.anchor = GridBagConstraints.PAGE_START;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	gbc.gridwidth = 3;
	gbc.insets = new Insets(5, 0, 15, 0);
	add(title, gbc);
	
	i++;
	
	gbc.gridx = 0;
	gbc.gridy = i;
	gbc.insets = new Insets(0, 0, 5, 130);
	gbc.gridwidth = 3;
	add(inputDrop, gbc);
	
	i++;
	
	gbc.gridy = i;
	gbc.anchor = GridBagConstraints.LINE_START;
	gbc.gridwidth = 1;
	gbc.weightx = 0.0;
	gbc.insets = new Insets(0, 5, 5, 5);
	add(inputText, gbc);
	
	gbc.gridx = 1;
	gbc.gridwidth = 1;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	add(textScroll, gbc);
	
	gbc.anchor = GridBagConstraints.FIRST_LINE_END;
	gbc.gridx = 2;
	gbc.gridheight = 2;
	gbc.weightx = 1.0;
	add(dropArea, gbc);
	
	i++;
	
	gbc.gridx = 0;
	gbc.gridy = i;
	gbc.gridheight = 1;
	gbc.weightx = 0.0;
	gbc.anchor = GridBagConstraints.LINE_START;
	add(outputText, gbc);
	
	gbc.gridx = 1;
	add(outTextScroll, gbc);
	
	i++;
	
	gbc.gridx = 1;
	gbc.gridy = i;
	add(textCopy, gbc);
	
	gbc.fill = GridBagConstraints.NONE;
	gbc.gridx = 2;
	gbc.gridy = i;
	add(selectFile, gbc);
	
	gbc.insets = new Insets(0, 100, 5, 5);
	gbc.gridx = 2;
	add(fileLoad, gbc);
	
	i++;
	
	gbc.insets = new Insets(0, 5, 5, 5);
	gbc.gridx = 2;
	gbc.gridy = i;
	add(selectedFile, gbc);
	
	gbc.anchor = GridBagConstraints.LINE_END;
	gbc.gridx = 2;
	add(locationField, gbc);
	
	i++;
	
	gbc.anchor = GridBagConstraints.LINE_START;
	gbc.gridx = 0;
	gbc.gridy = i;
	add(inputPass, gbc);
	
	gbc.gridx = 1;
	gbc.gridwidth = 2;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	add(passField, gbc);
	
	i++;
	
	gbc.fill = GridBagConstraints.NONE;
	gbc.gridx = 0;
	gbc.gridy = i;
	gbc.gridwidth = 3;
	gbc.insets = new Insets(10, 0, 5, 5);
	add(textEncrypt, gbc);
	gbc.insets = new Insets(10, 120, 5, 5);
	add(textDecrypt, gbc);
	gbc.insets = new Insets(10, 241, 5, 5);
	add(fileEncrypt, gbc);
	gbc.insets = new Insets(10, 370, 5, 5);
	add(fileDecrypt, gbc);
	gbc.insets = new Insets(10, 520, 5, 0);
	add(clear, gbc);
    }

    @Override
    public void drop(DropTargetDropEvent dtde) 
    {	
	dropArea.setBorder(BorderFactory.createMatteBorder(1, 5, 1, 1, Settings.AP_ORANGE));
	Transferable transferable = dtde.getTransferable();
	if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) 
	{
            dtde.acceptDrop(dtde.getDropAction());
            try {

                @SuppressWarnings("unchecked")
		List<Object> transferData = (List<Object>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                if (transferData != null && transferData.size() > 0) 
                {
                    itemLocation = transferData.get(0).toString();
                    // TODO: Handle Multiple drags
                    new General().changeItemBackground(itemLocation);
                    dtde.dropComplete(true);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            dtde.rejectDrop();
        }
	locationField.setText(itemLocation);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) 
    { 
	if (e.getSource() == fileLoad) 
	{
	    itemLocation = new General().LoadFile();
	    locationField.setText(itemLocation);
	}
	else if (e.getSource() == textCopy)
	{
	    StringSelection ss = new StringSelection (outTextField.getText());
	    Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard ();
	    clpbrd.setContents (ss, null);
	}
	else if (e.getSource() == textEncrypt)
	{
	    String text = textField.getText();
	    String pass = new String(passField.getPassword());
	    
	    if (checkTextInput(false))
	    {
		outTextField.setText(CryptoEngine.encryptText(pass, text));
	    }
	}
	else if (e.getSource() == textDecrypt)
	{
	    String text = textField.getText();
	    String pass = new String(passField.getPassword());
	    
	    if (checkTextInput(false))
	    {
		outTextField.setText(CryptoEngine.decryptText(pass, text));
	    }
	}
	else if (e.getSource() == fileEncrypt)
	{
	    final String pass = new String(passField.getPassword());
	    
	    if (checkTextInput(true) && itemLocation.length() != 0)
	    {
		new Thread() {
		    public void run() 
		    {
			Home.HideAllButOne(Home.log);
			CryptoEngine.encryptFileFolder(pass, itemLocation);
		    }
		}.start();
	    }
	}
	else if (e.getSource() == fileDecrypt)
	{
	    final String pass = new String(passField.getPassword());
	    
	    if (checkTextInput(true) && itemLocation.length() != 0)
	    {
		new Thread() {
		    public void run() 
		    {
			Home.HideAllButOne(Home.log);
			CryptoEngine.decryptFileFolder(pass, itemLocation);
		    }
		}.start();
	    }
	}
	else if (e.getSource() == clear)
	{
	    itemLocation = "";
	    textField.setText("");
	    outTextField.setText("");
	    passField.setText("");
	    locationField.setText("");   
	    java.net.URL imgURL = getClass().getResource("/resources/background.png");
	    ImageIcon icon = new ImageIcon(imgURL);
	    Image image = icon.getImage();
	    Image imageIcon = image.getScaledInstance(300, 185,  java.awt.Image.SCALE_SMOOTH);			
	    dropArea.setIcon(new ImageIcon(imageIcon));
	}
    }
    
    public static boolean checkTextInput(boolean isFile)
    {
	String pass = new String(passField.getPassword());
	
	if (!isFile && textField.getText().length() < 1)
	{
	    setWarning("Please enter some text to encrypt or decrypt.");
	    return false;
	}
	else if (pass.length() < minPassLength)
	{
	    setWarning("Please enter a password of at least " + minPassLength + " characters.");
	    return false;
	}
	else 
	{
	    return true;
	}
    }
    
    public static void setWarning(final String message)
    {
	new Thread() {
	    public void run() {
		title.setText(message);
		Log.appendText("[-] Error: " + message + "\n\n");
		title.setForeground(Color.RED);
		try {
		    Thread.sleep(4000);
		    
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		title.setText("Encrypt Text, Files and Folders");
		title.setForeground(Settings.AP_ORANGE);
	    }
	}.start();
    }
    
    public static void setOutText(String text) 
    {
	outTextField.setText(text);
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) { }

    @Override
    public void dragExit(DropTargetEvent dte) 
    { 
	dropArea.setBorder(BorderFactory.createMatteBorder(1, 5, 1, 1, Settings.AP_ORANGE));
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) 
    { 
	dropArea.setBorder(BorderFactory.createMatteBorder(1, 5, 1, 1, Settings.AP_GREEN));
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) { }
}
