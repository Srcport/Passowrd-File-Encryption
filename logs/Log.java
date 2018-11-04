package logs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class Log extends JPanel {

    private static final long serialVersionUID = 1L;
    
    private static JTextArea textArea = new JTextArea();
    private static JScrollPane textScroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    public static JProgressBar mainBar = new JProgressBar();
    
    public Log() 
    {
	setBackground(Color.WHITE);
	setLayout(new BorderLayout());
	
	textArea.setLineWrap(true);
	textArea.setWrapStyleWord(true);
	textArea.setEditable(false);
	
	mainBar.setValue(0);
	mainBar.setStringPainted(true);
	
	DefaultCaret caret = (DefaultCaret)textArea.getCaret();
	caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	
	textScroll.setBorder(BorderFactory.createEmptyBorder());
	
	add(textScroll, BorderLayout.CENTER);
	add(mainBar, BorderLayout.SOUTH);
    }
    
    public static void appendText(String text)
    {
	textArea.append(" " + getCurTime() + "     " + text);
    }
    
    public static void clearLog()
    {
	textArea.setText("");
    }
    
    public static String getLogData()
    {
	return textArea.getText();
    }
    
    public static String getCurTime()
    {
	String timestamp = new SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
	
	return timestamp;
    }
    
    public static void updateScrollPosition()
    {
	DefaultCaret caret = (DefaultCaret)textArea.getCaret();
	caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }
}
