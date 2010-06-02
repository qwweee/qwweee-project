package Project.gui;

import java.awt.Color;
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
        return this.height;
    }

    @Override
    public int getIconWidth() {
        return this.width;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        int ty = (c.getHeight()-height)/2;
        int tx = 0;
        int range = c.getWidth()/data.length;
        tx = (c.getWidth()-range*data.length)/2;
        for (int i = 0 ; i < data.length ; i ++) {
            if (data[i] == 1) {
                g.setColor(Color.red);
            } else {
                g.setColor(Color.black);
            }
            g.fillRect(tx, ty, range, height);
            tx += range;
        }
    }
    public void setData(byte[] data) {
        this.data = data;
        for (int i = 0 ; i < data.length ; i ++) {
            System.out.print(data[i]);
        }
        System.out.println();
    }
}
