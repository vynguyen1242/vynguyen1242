package GUI.MainContent;

import BUS.NhaCungCapBUS;
import BUS.TaiKhoanBUS;
import DTO.NhaCungCapDTO;
import DTO.TaiKhoanDTO;
import GUI.MainContentDiaLog.AddAndEditSupplierGUI;
import Utils.UIButton;
import Utils.UIConstants;
import Utils.UIScrollPane;
import Utils.UITable;
import Utils.UITextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class SupplierMainContentGUI extends JPanel {
    private UIButton btnAdd, btnDelete, btnEdit;
    private UITextField txtSearch;
    private UITable tblContent;
    private JPanel pnlHeader, pnlContent;
    private DefaultTableModel tableModel;
    private NhaCungCapBUS nhaCungCapBUS;
    private TaiKhoanBUS taiKhoanBUS;

    public SupplierMainContentGUI(TaiKhoanDTO taiKhoan) {
        this.taiKhoanBUS = new TaiKhoanBUS();
        this.nhaCungCapBUS = new NhaCungCapBUS();
        this.setBackground(UIConstants.SUB_BACKGROUND);
        this.setPreferredSize(new Dimension(UIConstants.WIDTH_CONTENT, UIConstants.HEIGHT_CONTENT));
        this.setLayout(new BorderLayout(5, 5));

        //==============================( PANEL HEADER )================================//
        pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(UIConstants.MAIN_BACKGROUND);
        pnlHeader.setPreferredSize(new Dimension(this.getWidth(), 50));

        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
        pnlButton.setBackground(UIConstants.MAIN_BACKGROUND);
        btnAdd = new UIButton("menuButton", "THÊM", 90, 40, "/Icon/them_icon.png");
        btnAdd.addActionListener(e -> addSupplier());
        btnDelete = new UIButton("menuButton", "XÓA", 90, 40, "/Icon/xoa_icon.png");
        btnDelete.addActionListener(e -> deleteSupplier());
        btnEdit = new UIButton("menuButton", "SỬA", 90, 40, "/Icon/sua_icon.png");
        btnEdit.addActionListener(e -> editSupplier());
        pnlButton.add(btnAdd);
        pnlButton.add(btnDelete);
        pnlButton.add(btnEdit);
        applyPermissions(taiKhoan.getTenDangNhap(), 6);
        
        JPanel pnlSearchFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
        pnlSearchFilter.setBackground(UIConstants.MAIN_BACKGROUND);
        txtSearch = new UITextField(190 ,30);
        pnlSearchFilter.add(txtSearch);

        pnlHeader.add(pnlButton, BorderLayout.WEST);
        pnlHeader.add(pnlSearchFilter, BorderLayout.CENTER);
        //==============================( End Panel Header )============================//

        
        
        //================================( PANEL CONTENT )=============================//
        pnlContent = new JPanel();
        pnlContent.setLayout(new BorderLayout());
        pnlContent.setBackground(UIConstants.MAIN_BACKGROUND);
        pnlContent.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        String[] columnNames = {"MÃ NHÀ CUNG CẤP", "TÊN NHÀ CUNG CẤP", "SỐ ĐIỆN THOẠI", "ĐỊA CHỈ"};
        tableModel = new DefaultTableModel(columnNames, 0); 
        tblContent = new UITable(tableModel);
        UIScrollPane scrollPane = new UIScrollPane(tblContent);
        pnlContent.add(scrollPane, BorderLayout.CENTER);
        //===============================( End Panel Content )===========================//
        
        this.add(pnlHeader, BorderLayout.NORTH);
        this.add(pnlContent, BorderLayout.CENTER);
        loadTableData();
        addSearchFunctionality();
    }
    
    private void applyPermissions(String username, int maCN) {
        btnAdd.setVisible(taiKhoanBUS.hasPermission(username, maCN, "add"));
        btnEdit.setVisible(taiKhoanBUS.hasPermission(username, maCN, "edit"));
        btnDelete.setVisible(taiKhoanBUS.hasPermission(username, maCN, "delete"));
    }
    
    private void loadTableData(){
        tableModel.setRowCount(0);
        for(NhaCungCapDTO ncc : nhaCungCapBUS.getAllNhaCungCap()){
            tableModel.addRow(new Object[]{
                ncc.getMaNCC(),
                ncc.getTenNCC(),
                ncc.getSdt(),
                ncc.getDiaChi()
            });
        }
    }
    
    private void addSupplier(){
        Window window = SwingUtilities.getWindowAncestor(this);
        new AddAndEditSupplierGUI((JFrame) window, nhaCungCapBUS, "Thêm Nhà Cung Cấp", "add");
        loadTableData();
    }
    
    private void editSupplier(){
        int selectedRow = tblContent.getSelectedRow();
        if(selectedRow == -1){
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhà cung cấp để chỉnh sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int maNcc = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        NhaCungCapDTO ncc = nhaCungCapBUS.getById(maNcc);
        Window window = SwingUtilities.getWindowAncestor(this);
        new AddAndEditSupplierGUI((JFrame) window, nhaCungCapBUS, "Chỉnh sửa nhà cung cấp", "save", ncc);
        loadTableData();
    }
    
    private void deleteSupplier(){
        int selectedRow = tblContent.getSelectedRow();
        if(selectedRow == -1){
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhà cung cấp để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int maNcc = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            if(nhaCungCapBUS.deleteNhaCungCap(maNcc)){
                JOptionPane.showMessageDialog(this, "Xóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void addSearchFunctionality() {
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { searchSupplier(); }
            public void removeUpdate(DocumentEvent e) { searchSupplier(); }
            public void changedUpdate(DocumentEvent e) { searchSupplier(); }
        });
    }
    
    private void searchSupplier() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        tableModel.setRowCount(0); 
        ArrayList<NhaCungCapDTO> list = nhaCungCapBUS.searchNhaCungCap(keyword);
        for (NhaCungCapDTO obj : list) {
            tableModel.addRow(new Object[]{
                obj.getMaNCC(),
                obj.getTenNCC(),
                obj.getSdt(),
                obj.getDiaChi()
            });
        }
    }
}
