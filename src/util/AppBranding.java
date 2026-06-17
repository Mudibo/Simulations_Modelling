package util;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Shared branding constants and icon generation for dashboard windows.
 */
public final class AppBranding {
    public static final String APP_TITLE = "Bank Queue Simulation Dashboard";
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font HEADING_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 13);

    private AppBranding() {
    }

    public static Image createAppIcon() {
        int size = 64;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(44, 109, 186));
        g2.fillRoundRect(4, 4, 56, 56, 14, 14);

        g2.setColor(new Color(232, 241, 255));
        g2.fillRoundRect(12, 12, 40, 40, 10, 10);

        g2.setColor(new Color(38, 83, 146));
        g2.fillRoundRect(18, 33, 6, 13, 3, 3);
        g2.fillRoundRect(29, 26, 6, 20, 3, 3);
        g2.fillRoundRect(40, 20, 6, 26, 3, 3);

        g2.dispose();
        return image;
    }

    public static ImageIcon createAppIcon16() {
        return new ImageIcon(createAppIcon().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
    }
}
