package Project.gui;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class TimeMapIcon implements Icon{
    private int width;
    private int height;
    private byte[] data;
    private int piconw;
    private int piconh;
    public TimeMapIcon (int w, int h, byte[] data) {
        this.width = w;
        this.height = h;
        this.data = data;
        this.piconw = 0;
        this.piconh = 0;
    }
    @Override
    public int getIconHeight() {
        return this.width;
    }

    @Override
    public int getIconWidth() {
        return this.height;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        int rx = 0;
        int ry = 0;
        processIconSize();
        
    }
    private void processIconSize() {
        int count = data.length;
        this.piconh = this.height;
        this.piconw = this.width / count;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
}
