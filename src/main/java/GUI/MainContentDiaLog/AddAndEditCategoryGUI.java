package GUI.MainContentDiaLog;

import BUS.TheLoaiBUS;
import DTO.TheLoaiDTO;
import Utils.UIButton;
import Utils.UIConstants;
import Utils.UILabel;
import Utils.UITextField;
import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AddAndEditCategoryGUI extends JDialog {
    private UITextField txtMaTL, txtTenTL;
    private UIButton btnAdd, btnSave, btnCancel;
    private TheLoaiBUS tlBus;
    private TheLoaiDTO tl;

    public AddAndEditCategoryGUI(JFrame parent, TheLoaiBUS tlBus, String title, String type, TheLoaiDTO tl) {
        super(parent, title, true);
        this.tlBus = tlBus;
        this.tl = tl;
        initComponent(type);
        if (tl != null) {
            txtMaTL.setText(String.valueOf(tl.getMaTL()));
            txtTenTL.setText(tl.getTenTL());
            txtMaTL.setEditable(false);
        }
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    public AddAndEditCategoryGUI(JFrame parent, TheLoaiBUS tlBus, String title, String type) {
        super(parent, title, true);
        this.tlBus = tlBus;
        initComponent(type);
        txtMaTL.setText(tlBus.getNextMaTl());
        txtMaTL.setEditable(false);
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    private void initComponent(String type) {
        this.setSize(350, 180);
        this.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBackground(UIConstants.MAIN_BACKGROUND);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(new UILabel("Mã thể loại:"));
        inputPanel.add(txtMaTL = new UITextField(0,0));
        inputPanel.add(new UILabel("Tên thể loại:"));
        inputPanel.add(txtTenTL = new UITextField(0,0));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setBackground(UIConstants.MAIN_BACKGROUND);
        btnAdd = new UIButton("add", "THÊM", 90, 35);
        btnSave = new UIButton("confirm", "LƯU", 90, 35);
        btnCancel = new UIButton("cancel", "HỦY", 90, 35);

        switch (type) {
            case "add" -> btnPanel.add(btnAdd);
            case "save" -> btnPanel.add(btnSave);
        }
        btnPanel.add(btnCancel);

        this.add(inputPanel, BorderLayout.CENTER);
        this.add(btnPanel, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dispose());
        btnAdd.addActionListener(e -> addCategory());
        btnSave.addActionListener(e -> saveCategory());
    }

    private void saveCategory() {
        if (!validateInput()) return;
        try {
            int maTL = Integer.parseInt(txtMaTL.getText().trim());
            String tenTL = txtTenTL.getText().trim();
            TheLoaiDTO category = new TheLoaiDTO(maTL, tenTL);

            if (tlBus.updateTheLoai(category)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thể loại thành công!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi nhập dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addCategory() {
        if (!validateInput()) return;
        try {
            int maTL = Integer.parseInt(txtMaTL.getText().trim());
            String tenTL = txtTenTL.getText().trim();
            TheLoaiDTO category = new TheLoaiDTO(maTL, tenTL);

            if (tlBus.addTheLoai(category)) {
                JOptionPane.showMessageDialog(this, "Thêm thể loại thành công!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi nhập dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateInput() {
        try {
            String tenTL = txtTenTL.getText().trim();
            if (tenTL.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên thể loại không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu nhập vào không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
