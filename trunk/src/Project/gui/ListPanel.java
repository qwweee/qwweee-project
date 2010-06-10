package Project.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import Project.StaticManager;
import Project.db.DBFunction;
import Project.gui.List.ListItem;
import Project.struct.BlackListStruct;

public class ListPanel extends JPanel{
    private BlackList frame;
    private InfoPanel infoPane;
    private JScrollPane scrollPane;
    private JList list;
    private JPopupMenu popMenu;
    private JMenuItem addBlackItem;
    private JMenuItem addWhiteItem;
    private JMenuItem removeItem;
    public ListPanel(BlackList frame, InfoPanel infoPane) {
        this.frame = frame;
        this.infoPane = infoPane;
        init();
    }
    private void init() {
        list = new JList();
        popMenu = new JPopupMenu();
        addBlackItem = new JMenuItem("移至黑名單");
        addWhiteItem = new JMenuItem("移至白名單");
        removeItem = new JMenuItem("移除");
        addBlackItem.setFont(StaticManager.menu_font);
        addWhiteItem.setFont(StaticManager.menu_font);
        removeItem.setFont(StaticManager.menu_font);
        scrollPane = new JScrollPane(list);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        popMenu.add(addBlackItem);
        popMenu.add(addWhiteItem);
        popMenu.add(removeItem);
        this.setBackground(Color.GREEN);
        this.setLayout(new BorderLayout());
        addComponents();
        addListeners();
        this.updateList(StaticManager.BLACKLIST);
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
                if (e.getButton() == 3) { // 滑鼠右鍵 popMenu
                    if (index != -1){
                        popMenu.show(list, e.getX(), e.getY());
                    }
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
        addBlackItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!list.isSelectionEmpty()) {
                    int type = moveBlackList(list.getSelectedValues(), StaticManager.BLACKLIST);
                    frame.updateListPane(type);
                }
            }
        });
        addWhiteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!list.isSelectionEmpty()) {
                    int type = moveBlackList(list.getSelectedValues(), StaticManager.WHITELIST);
                    frame.updateListPane(type);
                }
            }
        });
        removeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!list.isSelectionEmpty()) {
                    int type = moveBlackList(list.getSelectedValues(), StaticManager.REMOVEITEM);
                    frame.updateListPane(type);
                }
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
    private int moveBlackList(Object[] index, int type) {
        for (Object i : index) {
            ListItem item = (ListItem) i;
            BlackListStruct data = (BlackListStruct) item.getData();
            //data.print();
            if (type == data.Status) {
                break;
            }
            switch (type) {
            case StaticManager.BLACKLIST:
            case StaticManager.WHITELIST:
                // TODO z done db 更新blacklist資料
                data.Status = type;
                if (!DBFunction.getInstance().updateBlackList(type, data.No)) {
                    System.err.println("更新BlackList錯誤!");
                }
                break;
            case StaticManager.REMOVEITEM:
                // TODO z done db 移除blacklist資料
                type = data.Status;
                if (!DBFunction.getInstance().removeBlackList(data.No)) {
                    System.err.println("移除BlackList錯誤!");
                }
                break;
            default:
                System.err.println("移動/移除BlackList錯誤!");
            }
        }
        return type;
    }
}
