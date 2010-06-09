package Project.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import Project.StaticManager;
import Project.db.DBFunction;
import Project.struct.BlackListStruct;
import Project.struct.SWRunTableStruct;

public class ProcessPanel extends JPanel{
    private BlackList frame;
    private InfoPanel infoPane;
    private JScrollPane scrollPane;
    private JList list;
    private JPopupMenu popMenu;
    private JMenuItem addBlackItem;
    private JMenuItem addWhiteItem;
    public ProcessPanel(BlackList frame, InfoPanel infoPane) {
        this.frame = frame;
        this.infoPane = infoPane;
        init();
    }
    private void init() {
        list = new JList();
        scrollPane = new JScrollPane(list);
        popMenu = new JPopupMenu();
        addBlackItem = new JMenuItem("加入黑名單");
        addWhiteItem = new JMenuItem("加入白名單");
        addBlackItem.setFont(StaticManager.menu_font);
        addWhiteItem.setFont(StaticManager.menu_font);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        popMenu.add(addBlackItem);
        popMenu.add(addWhiteItem);
        this.setBackground(Color.LIGHT_GRAY);
        this.setLayout(new BorderLayout());
        addComponents();
        addListeners();
    }
    private void addComponents() {
        this.add(scrollPane, BorderLayout.CENTER);
    }
    private void addListeners() {
        addBlackItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!list.isSelectionEmpty()) {
                    addBlackList(list.getSelectedIndices(), StaticManager.BLACKLIST);
                    frame.updateListPane(StaticManager.BLACKLIST);
                }
            }
        });
        addWhiteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!list.isSelectionEmpty()) {
                    addBlackList(list.getSelectedIndices(), StaticManager.WHITELIST);
                    frame.updateListPane(StaticManager.WHITELIST);
                }
            }
        });
        list.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList list = (JList)e.getSource();
                int index = list.getSelectedIndex();
                SWRunTableStruct data = null;
                if (index >= 0) {
                    data = StaticManager.ProcessList.get(index);
                    infoPane.setProcessData(data);
                    // TODO z done gui infoPanel show 資料
                }
                //data.print();
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
        list.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
            }
            @Override
            public void keyReleased(KeyEvent e) {
                JList list = (JList)e.getSource();
                int index = list.getSelectedIndex();
                SWRunTableStruct data = null;
                if (index >= 0) {
                    data = StaticManager.ProcessList.get(index);
                    infoPane.setProcessData(data);
                }
            }
            @Override
            public void keyTyped(KeyEvent e) {
            }
        });
    }
    private void addBlackList(int[] index, int type) {
        // arraylist 檢查
        for (int i : index) {
            SWRunTableStruct data = StaticManager.ProcessList.get(i);
            boolean inList = StaticManager.BlackList.equals(data);
            if (!inList) {
                // db 加入黑名單 檢查 黑名單有無重複
                // db 加入白名單 檢查 白名單有無重複
                if (!DBFunction.getInstance().insertBlackList(data, type)) {
                    StaticManager.showWarningMsg(frame, "新增"+StaticManager.BLACKLISTTITLE[type]+"錯誤！", 
                            "新增"+StaticManager.BLACKLISTTITLE[type]);
                } else {
                    BlackListStruct add = new BlackListStruct();
                    add.Name = data.Name;
                    add.Path = data.Path;
                    add.Paraments = data.Parametes;
                    add.Type = data.Type;
                    add.Status = type;
                    StaticManager.BlackList.add(add);
                }
            } else {
                StaticManager.showWarningMsg(frame, data.Name+"\n程序有相同處理程序在名單中！", 
                        "新增"+StaticManager.BLACKLISTTITLE[type]);
            }
        }
    }
    public void updateList(String ip, int type) {
        list.setModel(StaticManager.readProcessList(ip, type));
        StaticManager.CellRanderer.setList(list);
        list.setCellRenderer(StaticManager.CellRanderer);
    }
}
