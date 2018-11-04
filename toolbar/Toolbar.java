package toolbar;

import home.Home;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import config.Settings;

public class Toolbar extends JPanel implements ActionListener
{

    private static final long serialVersionUID = 1L;
    
    private static JButton dashboard = new JButton("Dashboard");
    private static JButton log = new JButton("Log");
    private static JButton settings = new JButton("Settings");

    public Toolbar() 
    {
	// Panel settings
	setLayout(new BoxLayout(this, BoxLayout.X_AXIS));	
	setPreferredSize(new Dimension(Settings.WIDTH, 40));
	
	// Design 
	setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, Settings.AP_ORANGE));
	
	// ActionListeners
	
	dashboard.addActionListener(this);
	log.addActionListener(this);
	settings.addActionListener(this);
		
	// Add items
	add(dashboard);
	add(log);
	add(Box.createHorizontalGlue());
	add(settings);
	
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
	if (e.getSource() == dashboard) 
	{
	    Home.HideAllButOne(Home.dashboard);
	} 
	else if (e.getSource() == settings) 
	{
	    Home.HideAllButOne(Home.userSettings);
	} 
	else if (e.getSource() == log)
	{
	    Home.HideAllButOne(Home.log);
	}
    }

}
