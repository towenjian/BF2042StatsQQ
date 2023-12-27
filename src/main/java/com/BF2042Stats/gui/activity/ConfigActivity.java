package com.BF2042Stats.gui.activity;

import com.BF2042Stats.data.ConfigData;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.io.InputStream;

public class ConfigActivity {
    private JPanel rootPanel;
    private JPanel configPanel;
    private JTextField qqGroup_input;
    private JButton addButton;
    private JLabel qqAdmin;
    private JTextField qqAndim_input;
    private JButton btn_changeAdmin;
    private JCheckBox isOpenWelcome;
    private JTextArea welcomeString;
    private JTextArea menuString;
    private JButton btn_change_welcomeString;
    private JButton btn_change_menuString;
    private JCheckBox openRequestValidation;
    private JCheckBox openPre;
    private JCheckBox openPricx;
    private JCheckBox openCX;
    private JCheckBox openCL;
    private JCheckBox openVH;
    private JCheckBox openWP;
    private JCheckBox openKD;
    private JCheckBox openKill;
    private JList<String> list_qqGroup;
    //
    private Font font;

    public ConfigActivity() {
        bind();
        init();
    }
    private void init(){
        InputStream fontFile = getClass().getClassLoader().getResourceAsStream("AL.ttf");
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(40f);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        qqAdmin.setFont(font.deriveFont(20f));
        qqAdmin.setText(ConfigData.getUser());
        welcomeString.setFont(font.deriveFont(20f));
        welcomeString.setText(ConfigData.getWelcomeMessage());
        isOpenWelcome.setSelected(ConfigData.isOpenWelcome());
        menuString.setFont(font.deriveFont(20f));
        menuString.setText(ConfigData.getMenuMessage());
        openRequestValidation.setSelected(ConfigData.isOpenRequestValidation());
        openPre.setSelected(ConfigData.isPreGet());
        openPricx.setSelected(ConfigData.isPrivateChatSearch());
        openCX.setSelected(ConfigData.isCx());
        openCL.setSelected(ConfigData.isCl());
        openVH.setSelected(ConfigData.isVh());
        openWP.setSelected(ConfigData.isWp());
        openKD.setSelected(ConfigData.isKd());
        openKill.setSelected(ConfigData.isKill());
    }
    private void bind() {
        //qq群组
        DefaultListModel<String> model = new DefaultListModel<>();
        for (String num : ConfigData.getGroupList()) {
            model.addElement(num);
        }
        if (list_qqGroup != null) list_qqGroup.setModel(model);
        addButton.addActionListener(actionEvent -> {
            String num = qqGroup_input.getText();
            if (num == null) {
                JOptionPane.showMessageDialog(null, "群号请不要为空", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if(num.length()<6){
                JOptionPane.showMessageDialog(null, "这群号正确吗？", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            ConfigData.addGroupList(num);
            model.addElement(num);
        });
        list_qqGroup.addListSelectionListener(listSelectionEvent -> {
            if (!listSelectionEvent.getValueIsAdjusting()) {
                int result = JOptionPane.showConfirmDialog(null, "你确定要删除这个群号吗？", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    int index =list_qqGroup.getSelectedIndex();
                    if (index != -1) {
                        ConfigData.removeGroupList(model.remove(index));
                    }
                } else if (result == JOptionPane.NO_OPTION) {
                    System.out.println("已取消");
                }
            }
        });
        btn_changeAdmin.addActionListener(actionEvent -> {
            String num =  qqAndim_input.getText();
            if (num.length()<6){
                JOptionPane.showMessageDialog(null, "admin账号你确定？", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            ConfigData.changeAdmin(num);
            qqAdmin.setText(num);
        });
        btn_change_welcomeString.addActionListener(actionEvent -> {
            ConfigData.setWelcomeMessage(welcomeString.getText());
            System.out.println("入群欢迎已改变");
        });
        isOpenWelcome.addItemListener(itemEvent -> {
            boolean isOpen = itemEvent.getStateChange() == ItemEvent.SELECTED;
            ConfigData.setOpenWelcome(isOpen);
        });
        btn_change_menuString.addActionListener(actionEvent -> {
            ConfigData.setMenuMessage(menuString.getText());
        });
        openRequestValidation.addItemListener(itemEvent -> ConfigData.setOpenRequestValidation(itemEvent.getStateChange() == ItemEvent.SELECTED));
        openPricx.addItemListener(itemEvent -> ConfigData.setPrivateChatSearch(itemEvent.getStateChange() == ItemEvent.SELECTED));
        openPre.addItemListener(itemEvent -> ConfigData.setPreGet(itemEvent.getStateChange() == ItemEvent.SELECTED));
        openCX.addItemListener(itemEvent -> ConfigData.setCx(itemEvent.getStateChange() == ItemEvent.SELECTED));
        openCL.addItemListener(itemEvent -> ConfigData.setCl(itemEvent.getStateChange() == ItemEvent.SELECTED));
        openVH.addItemListener(itemEvent -> ConfigData.setVh(itemEvent.getStateChange() == ItemEvent.SELECTED));
        openWP.addItemListener(itemEvent -> ConfigData.setWp(itemEvent.getStateChange() == ItemEvent.SELECTED));
        openKD.addItemListener(itemEvent -> ConfigData.setKd(itemEvent.getStateChange() == ItemEvent.SELECTED));
        openKill.addItemListener(itemEvent -> ConfigData.setKill(itemEvent.getStateChange() == ItemEvent.SELECTED));
    }
    public void setUse(@NotNull String gn, boolean isOpen){
        switch (gn){
            case "cx":
                openCX.setSelected(isOpen);
                break;
            case "cl":
                openCL.setSelected(isOpen);
                break;
            case "vh":
                openVH.setSelected(isOpen);
                break;
            case "wp":
                openWP.setSelected(isOpen);
                break;
            case "kill":
                openKill.setSelected(isOpen);
                break;
            case "kd":
                openKD.setSelected(isOpen);
                break;
            case "pr":
                openPricx.setSelected(isOpen);
                break;
            case "pg":
                openPre.setSelected(isOpen);
                break;
            default:
                System.out.println("无效命令");
        }
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        configPanel = new JPanel();
        configPanel.setLayout(new GridLayoutManager(6, 2, new Insets(0, 10, 0, 10), -1, -1));
        rootPanel.add(configPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("配置文件");
        configPanel.add(label1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("QQGroup");
        configPanel.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        configPanel.add(panel1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("添加群组");
        panel2.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        qqGroup_input = new JTextField();
        panel2.add(qqGroup_input, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        addButton = new JButton();
        addButton.setText("Add");
        panel2.add(addButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        list_qqGroup = new JList();
        panel1.add(list_qqGroup, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Admin");
        configPanel.add(label4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        configPanel.add(panel3, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        qqAdmin = new JLabel();
        qqAdmin.setText("null");
        panel3.add(qqAdmin, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("qqAdmin");
        panel4.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        qqAndim_input = new JTextField();
        panel4.add(qqAndim_input, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btn_changeAdmin = new JButton();
        btn_changeAdmin.setText("改变");
        panel4.add(btn_changeAdmin, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("当前Admin");
        panel3.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("入群欢迎");
        configPanel.add(label7, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        configPanel.add(panel5, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        isOpenWelcome = new JCheckBox();
        isOpenWelcome.setText("是否打开入群欢迎");
        panel5.add(isOpenWelcome, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        welcomeString = new JTextArea();
        panel5.add(welcomeString, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        btn_change_welcomeString = new JButton();
        btn_change_welcomeString.setText("Change");
        panel5.add(btn_change_welcomeString, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("菜单文本");
        configPanel.add(label8, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        configPanel.add(panel6, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        menuString = new JTextArea();
        panel6.add(menuString, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        btn_change_menuString = new JButton();
        btn_change_menuString.setText("Change");
        panel6.add(btn_change_menuString, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("功能开关");
        configPanel.add(label9, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        configPanel.add(panel7, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        openRequestValidation = new JCheckBox();
        openRequestValidation.setText("是否打开入群验证");
        panel7.add(openRequestValidation, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        openPre = new JCheckBox();
        openPre.setText("是否打开前置查询");
        panel7.add(openPre, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        openPricx = new JCheckBox();
        openPricx.setText("是否允许私聊查询");
        panel7.add(openPricx, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        openCX = new JCheckBox();
        openCX.setText("cx功能");
        panel7.add(openCX, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        openCL = new JCheckBox();
        openCL.setText("cl功能");
        panel7.add(openCL, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        openVH = new JCheckBox();
        openVH.setText("vh功能");
        panel7.add(openVH, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        openWP = new JCheckBox();
        openWP.setText("wp功能");
        panel7.add(openWP, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        openKD = new JCheckBox();
        openKD.setText("kd功能");
        panel7.add(openKD, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        openKill = new JCheckBox();
        openKill.setText("kill功能");
        panel7.add(openKill, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }
}
