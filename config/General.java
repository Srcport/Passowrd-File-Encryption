package config;

import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import dashboard.Dashboard;


public class General 
{

    public String LoadFile()
    {
	JFileChooser fc = new JFileChooser();
	fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	//fc.setCurrentDirectory(new File(fc.getSelectedFile().toString()));
	int value = fc.showOpenDialog(home.Home.toolbar);
	if (value == JFileChooser.APPROVE_OPTION) 
	{
	    String loc = fc.getSelectedFile().toString();
	    changeItemBackground(loc);
	    return loc;
	}
	else 
	{
	    return null;
	}
    }
    
    // Changes the background of the dropbox based on the file type
    public void changeItemBackground(String location)
    {
	File target = new File(location);
	String loadImage = "";
	String extension = "";
	
	int i = location.lastIndexOf('.');
	if (i > 0) 
	{
	    extension = location.substring(i+1);
	}
	
	if (target.isDirectory())
	{
	    loadImage = "/resources/background-folder.png";
	}
	else if (extension.equals("secure"))
	{
	    loadImage = "/resources/background-secure.png";
	}
	else if (target.isFile())
	{
	    loadImage = "/resources/background-file.png";
	}
	
	java.net.URL imgURL = getClass().getResource(loadImage);
	ImageIcon icon = new ImageIcon(imgURL);
	Image image = icon.getImage();
	Image imageIcon = image.getScaledInstance(300, 185,  java.awt.Image.SCALE_SMOOTH);			
	Dashboard.dropArea.setIcon(new ImageIcon(imageIcon));
    }
}
