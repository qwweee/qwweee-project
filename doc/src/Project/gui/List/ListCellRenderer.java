package Project.gui.List;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import Project.StaticManager;
import Project.struct.BlackListStruct;
import Project.struct.SWRunTableStruct;

public class ListCellRenderer extends DefaultListCellRenderer{
    private JList _data;
    public ListCellRenderer() {
    }
    public void setList(JList data) {
        this._data = data;
    }
    public Component getListCellRendererComponent(
            JList list,
            Object value,            // value to display
            int index,               // cell index
            boolean isSelected,      // is the cell selected
            boolean cellHasFocus)    // the list and the cell have the focus
        {
            final Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            final ListItem item = (ListItem) value;
            if (item.getData() instanceof BlackListStruct) {
                setText(StaticManager.BlackListString(item));
            } else if (item.getData() instanceof SWRunTableStruct) {
                setText(StaticManager.ProcessListString(item));
            }
            setFont(StaticManager.default_font);
            /*
            setFont(new Font(Frame1.default_font_name , 
            Frame1.default_font_style , 
            Frame1.default_font_size));
            */
            //setText(item.getDescription());
            return c;
        }

}
