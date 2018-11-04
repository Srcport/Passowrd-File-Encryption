/* Application settings are stored here and called throughout the program.
 * Includes: Sizes, colours, dimensions, fonts, styles */

package config;

import java.awt.Color;
import java.awt.Font;

public class Settings 
{   
    /* Tier 1 Application Settings */
    public static final int WIDTH = 600;
    public static final int HEIGHT = 460;
    
    public static final String TITLE = "AES, BlowFish, DES, DESede Encryption";
    
    /* General Colours & Styles */ 
    public static final Color AP_ORANGE = new Color(253, 139, 37);
    public static final Color AP_GREY = new Color(229, 229, 229);
    public static final Color AP_GREEN = new Color(0, 165, 77);
    
    /* Fonts & Text */
    public static final Font REG_FONT = new Font("sansserif", Font.PLAIN, 16);
    
    public Settings()
    {
	System.setProperty("file.encoding", "UTF-8");
    }
}