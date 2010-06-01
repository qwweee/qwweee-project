package Project.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Project.StaticManager;
import Project.gui.List.ListItem;
import Project.struct.BlackListStruct;

public class ListPanel extends JPanel{
    private BlackList frame;
    private InfoPanel infoPane;
    private JScrollPane scrollPane;
    private JList list;
    public ListPanel(BlackList frame, InfoPanel infoPane) {
        this.frame = frame;
        this.infoPane = infoPane;
        init();
    }
    private void init() {
        list = new JList();
        scrollPane = new JScrollPane(list);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.setBackground(Color.GREEN);
        this.setLayout(new BorderLayout());
        addComponents();
        addListeners();
        updateList(StaticManager.BLACKLIST);
    }
    private void addComponents() {
        this.add(scrollPane, BorderLayout.CENTER);
    }
    private void addListeners() {
        list.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList list = (JList)e.getSource();
                int index = list.getSelectedIndex();
                BlackListStruct data = null;
                if (index >= 0) {
                    data = (BlackListStruct) ((ListItem)list.getSelectedValue()).getData();
                    infoPane.setBlackData(data);
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
        });
    }
    public void updateList(int type) {
        list.setModel(StaticManager.readBlackList(type));
        StaticManager.CellRanderer.setList(list);
        list.setCellRenderer(StaticManager.CellRanderer);
        frame.listType = StaticManager.BLACKLISTTITLE[type];
        frame.updateTitle();
    }
}
