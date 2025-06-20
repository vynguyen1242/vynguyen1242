package GUI.MainContentDiaLog;

import BUS.NhanVienBUS;
import BUS.VaiTroBUS;
import DTO.NhanVienDTO;
import DTO.VaiTroDTO;
import Utils.UIButton;
import Utils.UIConstants;
import Utils.UILabel;
import Utils.UITextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AddAndEditStaffGUI extends JDialog{
    private UITextField txtMaNV, txtTenNV, txtEmail, txtSDT, txtVaiTro;
    private JComboBox cbMaVT;
    private UIButton btnAdd, btnSave, btnCancel;
    private NhanVienBUS nvBus;
    private NhanVienDTO nv;
    private VaiTroBUS vtBus;
    
    public AddAndEditStaffGUI(JFrame parent, NhanVienBUS nvBus, String title, String type, NhanVienDTO nv) {
        super(parent, title, true);
        this.nvBus = nvBus;
        this.nv = nv;
        initComponent(type);
        if (nv != null) {
            txtMaNV.setText(String.valueOf(nv.getMaNV()));
            txtTenNV.setText(nv.getTenNV());
            txtEmail.setText(nv.getEmail());
            txtSDT.setText(nv.getSdt());
            txtMaNV.setEditable(false);
            cbMaVT.setSelectedItem(nv.getMaVT());
            txtVaiTro.setText(vtBus.getById(nv.getMaVT()).getTenVT());
        }
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    public AddAndEditStaffGUI(JFrame parent, NhanVienBUS nvBus, String title, String type) {
        super(parent, title, true);
        this.nvBus = nvBus;
        initComponent(type);
        txtMaNV.setText(nvBus.getNextMaNv());
        txtMaNV.setEditable(false);
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }
    
    private void initComponent(String type){
        this.setSize(400, 300);
        this.setLayout(new BorderLayout());
        vtBus = new VaiTroBUS();

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBackground(UIConstants.MAIN_BACKGROUND);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));    
        inputPanel.add(new UILabel("Mã nhân viên:"));
        inputPanel.add(txtMaNV = new UITextField(0,0));  
        inputPanel.add(new UILabel("Tên nhân viên:"));
        inputPanel.add(txtTenNV = new UITextField(0,0)); 
        inputPanel.add(new UILabel("Email:"));
        inputPanel.add(txtEmail = new UITextField(0,0));
        inputPanel.add(new UILabel("Số điện thọại:"));
        inputPanel.add(txtSDT = new UITextField(0,0));
        inputPanel.add(new UILabel("Vai Trò:"));
        JPanel groupVT = new JPanel(new BorderLayout(5,0));
        cbMaVT = new JComboBox();
        cbMaVT.setBackground(Color.white);
        cbMaVT.setPreferredSize(new Dimension(40,0));
        cbMaVT.removeAllItems(); 
        for (VaiTroDTO vt : vtBus.getAllVaiTro()) {
            cbMaVT.addItem(vt.getMaVT()); 
        }
        cbMaVT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer selectedMaVT = (Integer) cbMaVT.getSelectedItem();
                if (selectedMaVT != null) {
                    String tenVT = vtBus.getById(selectedMaVT).getTenVT();
                    txtVaiTro.setText(tenVT);
                }
            }
        });
        txtVaiTro = new UITextField(0, 0);
        txtVaiTro.setEditable(false);
        groupVT.add(txtVaiTro , BorderLayout.CENTER);
        groupVT.add(cbMaVT, BorderLayout.EAST);
        inputPanel.add(groupVT);
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        btnPanel.setBackground(UIConstants.MAIN_BACKGROUND);
        btnAdd = new UIButton("add", "THÊM", 90, 35);
        btnSave = new UIButton("confirm", "LƯU", 90, 35);
        btnCancel = new UIButton("cancel", "HỦY", 90, 35);
        if ("add".equals(type)) btnPanel.add(btnAdd);
        if ("save".equals(type)) btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        this.add(inputPanel, BorderLayout.CENTER);
        this.add(btnPanel, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dispose());
        btnAdd.addActionListener(e -> addStaff());
        btnSave.addActionListener(e -> saveStaff());
    }
    
    private void addStaff(){
        if(!CheckFormInput()) return;
        try {
            int maNV = Integer.parseInt(txtMaNV.getText().trim());
            String tenNV = txtTenNV.getText().trim();
            String email = txtEmail.getText().trim();
            String sdt = txtSDT.getText().trim();
            int maVT = Integer.parseInt(cbMaVT.getSelectedItem().toString());
            NhanVienDTO nv = new NhanVienDTO(maNV, tenNV, email, sdt, maVT);
            if(nvBus.addNhanVien(nv)){
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
                dispose();
            }else {
                JOptionPane.showMessageDialog(this, "Mã nhân viên đã tồn tại hoặc dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lỗi nhập dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void saveStaff(){
        if(!CheckFormInput()) return;
        try {
            int maNV = Integer.parseInt(txtMaNV.getText().trim());
            String tenNV = txtTenNV.getText().trim();
            String email = txtEmail.getText().trim();
            String sdt = txtSDT.getText().trim();
            int maVT = Integer.parseInt(cbMaVT.getSelectedItem().toString());
            NhanVienDTO nv = new NhanVienDTO(maNV, tenNV, email, sdt, maVT);
            if(nvBus.updateNhanVien(nv)){
                JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công!");
                dispose();
            }else {
                JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lỗi nhập dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean CheckFormInput(){
        try {
            String tenNV = txtTenNV.getText().trim();
            if (tenNV.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên nhân viên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            String email = txtEmail.getText().trim();
            if (!email.isEmpty() && !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                JOptionPane.showMessageDialog(this, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            String sdt = txtSDT.getText().trim();
            if (sdt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Số điện thoại không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (!sdt.matches("0\\d{9}")) {
                JOptionPane.showMessageDialog(this, "Số điện thoại phải có 10 chữ số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            String vaiTro = txtVaiTro.getText().trim();
            if(vaiTro.isEmpty()){
                JOptionPane.showMessageDialog(this, "Vui lòng chọn vai trò!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu nhập vào không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
