package GUI.MainContentDiaLog;

import BUS.NhaXuatBanBUS;
import DTO.NhaXuatBanDTO;
import Utils.UIButton;
import Utils.UIConstants;
import Utils.UILabel;
import Utils.UITextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AddAndEditPublisherGUI extends JDialog {
    private UITextField txtMaNXB, txtTenNXB;
    private UIButton btnAdd, btnSave, btnCancel;
    private NhaXuatBanBUS nxbBus;
    private NhaXuatBanDTO nxb;

    public AddAndEditPublisherGUI(JFrame parent, NhaXuatBanBUS nxbBus, String title, String type, NhaXuatBanDTO nxb) {
        super(parent, title, true);
        this.nxbBus = nxbBus;
        this.nxb = nxb;
        initComponent(type);
        if (nxb != null) {
            txtMaNXB.setText(String.valueOf(nxb.getMaNXB()));
            txtTenNXB.setText(nxb.getTenNXB());
            txtMaNXB.setEditable(false);
        }
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    public AddAndEditPublisherGUI(JFrame parent, NhaXuatBanBUS nxbBus, String title, String type) {
        super(parent, title, true);
        this.nxbBus = nxbBus;
        initComponent(type);
        txtMaNXB.setText(nxbBus.getNextMaNxb());
        txtMaNXB.setEditable(false);
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    private void initComponent(String type) {
        this.setSize(350, 180);
        this.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBackground(UIConstants.MAIN_BACKGROUND);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(new UILabel("Mã nhà xuất bản:"));
        inputPanel.add(txtMaNXB = new UITextField(0,0));
        inputPanel.add(new UILabel("Tên nhà xuất bản:"));
        inputPanel.add(txtTenNXB = new UITextField(0,0));

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
        btnAdd.addActionListener(e -> addPublisher());
        btnSave.addActionListener(e -> savePublisher());
    }

    private void savePublisher() {
        if (!CheckFormInput()) return;
        try {
            int maNXB = Integer.parseInt(txtMaNXB.getText().trim());
            String tenNXB = txtTenNXB.getText().trim();
            NhaXuatBanDTO publisher = new NhaXuatBanDTO(maNXB, tenNXB);

            if (nxbBus.updateNhaXuatBan(publisher)) {
                JOptionPane.showMessageDialog(this, "Cập nhật nhà xuất bản thành công!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi nhập dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addPublisher() {
        if (!CheckFormInput()) return;
        try {
            int maNXB = Integer.parseInt(txtMaNXB.getText().trim());
            String tenNXB = txtTenNXB.getText().trim();
            NhaXuatBanDTO publisher = new NhaXuatBanDTO(maNXB, tenNXB);

            if (nxbBus.addNhaXuatBan(publisher)) {
                JOptionPane.showMessageDialog(this, "Thêm nhà xuất bản thành công!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi nhập dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean CheckFormInput() {
        try {
            String tenNXB = txtTenNXB.getText().trim();
            if (tenNXB.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên NXB không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu nhập vào không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

}

