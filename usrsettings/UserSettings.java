package usrsettings;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import logs.Log;

import config.Settings;
import dashboard.Dashboard;
import dashboard.CryptoEngine;

public class UserSettings extends JPanel implements ActionListener 
{

    private static final long serialVersionUID = 1L;

    private static JLabel filler = new JLabel();
    private static JLabel title = new JLabel("User Settings");
    
    private static JLabel algorithmLabel = new JLabel("Select encryption algorithm to use:");
    private static JLabel aesLabel = new JLabel("AES:");
    private static JLabel desLabel = new JLabel("DES:");
    private static JLabel tripDesLabel = new JLabel("Tripple DES:");
    private static JLabel blowfishLabel = new JLabel("Blowfish:");
    
    private static JLabel passwordLabel = new JLabel("Select minimum password length:");
    private static JLabel passLenLabel6 = new JLabel("6 characters:");
    private static JLabel passLenLabel8 = new JLabel("8 characters:");
    private static JLabel passLenLabel12 = new JLabel("12 characters:");
    private static JLabel passLenLabel16 = new JLabel("16 characters:");
    
    private static JLabel logOptions = new JLabel("Delete or export the system log:");
    private static JButton delLog = new JButton("Delete Log");
    private static JButton exportLog = new JButton("Export Log");

    private static JRadioButton aesChoice = new JRadioButton();
    private static JRadioButton desChoice = new JRadioButton();
    private static JRadioButton tripDesChoice = new JRadioButton();
    private static JRadioButton blowfishChoice = new JRadioButton();
    
    private static JRadioButton passLen6 = new JRadioButton();
    private static JRadioButton passLen8 = new JRadioButton();
    private static JRadioButton passLen12 = new JRadioButton();
    private static JRadioButton passLen16 = new JRadioButton();
    
    public UserSettings() 
    {
	
	setLayout(new GridBagLayout());
	GridBagConstraints gbc = new GridBagConstraints();
	setBackground(Settings.AP_GREY);
	
	title.setFont(Settings.REG_FONT);
	title.setForeground(Settings.AP_ORANGE);
	
	java.net.URL imgURL = getClass().getResource("/resources/orangeWave.png");
	ImageIcon icon = new ImageIcon(imgURL);
	Image image = icon.getImage();
	Image imageIcon = image.getScaledInstance(600, 185,  java.awt.Image.SCALE_SMOOTH);			
	filler.setIcon(new ImageIcon(imageIcon));
	
	ButtonGroup algorithmGroup = new ButtonGroup();
	algorithmGroup.add(aesChoice);
	algorithmGroup.add(desChoice);
	algorithmGroup.add(tripDesChoice);
	algorithmGroup.add(blowfishChoice);
	
	ButtonGroup passLenGroup = new ButtonGroup();
	passLenGroup.add(passLen6);
	passLenGroup.add(passLen8);
	passLenGroup.add(passLen12);
	passLenGroup.add(passLen16);
	
	aesChoice.doClick();
	passLen8.doClick();
	
	aesChoice.addActionListener(this);
	desChoice.addActionListener(this);
	tripDesChoice.addActionListener(this);
	blowfishChoice.addActionListener(this);
	
	passLen6.addActionListener(this);
	passLen8.addActionListener(this);
	passLen12.addActionListener(this);
	passLen16.addActionListener(this);
	
	delLog.addActionListener(this);
	exportLog.addActionListener(this);
	
	int i = 0;
	
	gbc.insets = new Insets(10, 10, 0, 0);
	gbc.gridx = 0;
	gbc.gridy = i;
	gbc.gridwidth = 10;
	add(title, gbc);
	
	i++;
	gbc.gridx = 0;
	gbc.gridy = i;
	gbc.gridwidth = 10;
	gbc.weightx = 1.0;
	gbc.anchor = GridBagConstraints.LINE_START;
	add(algorithmLabel, gbc);
	
	i++;
	gbc.gridx = 0;
	gbc.gridy = i;
	gbc.gridwidth = 1;
	gbc.weightx = 0.1;
	gbc.anchor = GridBagConstraints.LINE_END;
	add(aesLabel, gbc);
	
	gbc.gridx = 1;
	gbc.anchor = GridBagConstraints.LINE_START;
	add(aesChoice, gbc);
	
	gbc.gridx = 2;
	gbc.anchor = GridBagConstraints.LINE_END;
	add(desLabel, gbc);
	
	gbc.gridx = 3;
	gbc.anchor = GridBagConstraints.LINE_START;
	add(desChoice, gbc);
	
	gbc.gridx = 4;
	gbc.anchor = GridBagConstraints.LINE_END;
	add(tripDesLabel, gbc);
	
	gbc.gridx = 5;
	gbc.anchor = GridBagConstraints.LINE_START;
	add(tripDesChoice, gbc);
	
	gbc.gridx = 6;
	gbc.anchor = GridBagConstraints.LINE_END;
	add(blowfishLabel, gbc);
	
	gbc.gridx = 7;
	gbc.anchor = GridBagConstraints.LINE_START;
	add(blowfishChoice, gbc);
	
	i++;
	gbc.gridx = 0;
	gbc.gridy = i;
	gbc.gridwidth = 10;
	gbc.weightx = 1.0;
	gbc.anchor = GridBagConstraints.LINE_START;
	add(passwordLabel, gbc);
	
	i++;
	gbc.gridx = 0;
	gbc.gridy = i;
	gbc.gridwidth = 1;
	gbc.weightx = 0.1;
	gbc.anchor = GridBagConstraints.LINE_END;
	add(passLenLabel6, gbc);
	
	gbc.gridx = 1;
	gbc.anchor = GridBagConstraints.LINE_START;
	add(passLen6, gbc);
	
	gbc.gridx = 2;
	gbc.anchor = GridBagConstraints.LINE_END;
	add(passLenLabel8, gbc);
	
	gbc.gridx = 3;
	gbc.anchor = GridBagConstraints.LINE_START;
	add(passLen8, gbc);
	
	gbc.gridx = 4;
	gbc.anchor = GridBagConstraints.LINE_END;
	add(passLenLabel12, gbc);
	
	gbc.gridx = 5;
	gbc.anchor = GridBagConstraints.LINE_START;
	add(passLen12, gbc);
	
	gbc.gridx = 6;
	gbc.anchor = GridBagConstraints.LINE_END;
	add(passLenLabel16, gbc);
	
	gbc.gridx = 7;
	gbc.anchor = GridBagConstraints.LINE_START;
	add(passLen16, gbc);
	
	i++;
	gbc.gridx = 0;
	gbc.gridy = i;
	gbc.gridwidth = 10;
	gbc.weightx = 1.0;
	gbc.anchor = GridBagConstraints.LINE_START;
	add(logOptions, gbc);
	
	i++;
	gbc.gridx = 0;
	gbc.gridy = i;
	gbc.gridwidth = 1;
	gbc.weightx = 0.1;
	gbc.anchor = GridBagConstraints.LINE_START;
	add(delLog, gbc);
	
	gbc.gridx = 1;
	gbc.gridwidth = 2;
	add(exportLog, gbc);
	
	i++;
	gbc.insets = new Insets(0, 0, 0, 0);
	gbc.gridwidth = 10;
	gbc.fill = GridBagConstraints.BOTH;
	gbc.anchor = GridBagConstraints.PAGE_END;
	gbc.gridx = 0;
	gbc.gridy = i;
	gbc.weighty = 1.0;
	add(filler, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
	if (e.getSource() == aesChoice)
	{
	    CryptoEngine.algo = "AES";
	}
	else if (e.getSource() == desChoice)
	{
	    CryptoEngine.algo = "DES";
	}
	else if (e.getSource() == tripDesChoice)
	{
	    CryptoEngine.algo = "DESede";
	}
	else if (e.getSource() == blowfishChoice)
	{
	    CryptoEngine.algo = "Blowfish";
	}
	else if (e.getSource() == passLen6)
	{
	    Dashboard.minPassLength = 6;
	}
	else if (e.getSource() == passLen8)
	{
	    Dashboard.minPassLength = 8;
	}
	else if (e.getSource() == passLen12)
	{
	    Dashboard.minPassLength = 12;
	}
	else if (e.getSource() == passLen16)
	{
	    Dashboard.minPassLength = 16;
	}
	else if (e.getSource() == delLog)
	{
	    Log.clearLog();
	}
	else if (e.getSource() == exportLog)
	{
	    createLogFile();
	}
    }

    private static void createLogFile()
    {
	PrintWriter writer;
	String desktop = System.getProperty("user.home") + "/Desktop";
	String date = new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
	try {
	    writer = new PrintWriter(desktop + "/encryption-log.txt", "UTF-8");
	    writer.print("|------ Encryption Log [" + date + "] ------|\n\n");
	    writer.print(Log.getLogData());
	    writer.close();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
    }
}
