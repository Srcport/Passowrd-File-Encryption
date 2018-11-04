package home;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import logs.Log;
import toolbar.Toolbar;
import usrsettings.UserSettings;
import config.Settings;
import dashboard.Dashboard;

public class Home 
{    
    private static JFrame home = new JFrame();
    public static Toolbar toolbar = new Toolbar();
    public static UserSettings userSettings = new UserSettings();
    public static Dashboard dashboard = new Dashboard();
    public static Log log = new Log();

    public Home()
    {
	// Frame settings
	home.setMinimumSize(new Dimension(Settings.WIDTH, Settings.HEIGHT));
	home.setTitle(Settings.TITLE);
	home.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	home.setResizable(false);
	
	home.setLayout(new GridBagLayout());
	GridBagConstraints gbc = new GridBagConstraints();
	
	// Hide JPanels
	userSettings.setVisible(false);
	log.setVisible(false);
	
	// Frame style
	home.setBackground(new Color(155, 155, 155, 255));
	
	// Add Items & Layout
	int i = 0;
	gbc.gridx = 0;
	gbc.gridy = i;
	gbc.weightx = 1.0;
	gbc.anchor = GridBagConstraints.PAGE_START;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	home.add(toolbar, gbc);
	
	i++;
	
	gbc.gridx = 0;
	gbc.gridy = i;
	gbc.weighty = 1.0;
	gbc.fill = GridBagConstraints.BOTH;
	home.add(userSettings, gbc);
	home.add(dashboard, gbc);
	home.add(log, gbc);
	
	// Last frame constant
	home.setVisible(true);
    }
    
    // Iterates through all JPanel components hiding all but the specified panel.
    public static void HideAllButOne(JPanel showMe) 
    {	
	for (int i = 0; i < home.getContentPane().getComponentCount(); i++) 
	{
            Component comp = home.getContentPane().getComponent(i);

            if (comp instanceof JPanel && comp != toolbar) 
            {
                comp.setVisible(false);
            }
	}
	
	showMe.setVisible(true);
    }
}