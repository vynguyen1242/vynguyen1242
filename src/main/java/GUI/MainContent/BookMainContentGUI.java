package GUI.MainContent;

import BUS.NhaXuatBanBUS;
import BUS.SachBUS;
import BUS.TacGiaBUS;
import BUS.TaiKhoanBUS;
import BUS.TheLoaiBUS;
import DTO.SachDTO;
import DTO.TaiKhoanDTO;
import GUI.MainContentDiaLog.*;
import Utils.UIConstants;
import Utils.UIButton;
import Utils.UILabel;
import Utils.UIScrollPane;
import Utils.UITable;
import Utils.UITextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class BookMainContentGUI extends JPanel implements ReloadablePanel{
    private UIButton btnAdd, btnDelete, btnEdit, btnView;
    private UITextField txtSearch;
    private UITable tblContent;
    private JPanel pnlHeader, pnlContent;
    private DefaultTableModel tableModel;
    private SachBUS sachBUS;
    private NhaXuatBanBUS nhaXuatBanBUS;
    private TaiKhoanBUS taiKhoanBUS;

    public BookMainContentGUI(TaiKhoanDTO taiKhoan) {
        taiKhoanBUS = new TaiKhoanBUS();
        sachBUS = new SachBUS();
        nhaXuatBanBUS = new NhaXuatBanBUS();
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
        btnAdd.addActionListener(e -> addBook());
        btnDelete = new UIButton("menuButton", "XÓA", 90, 40, "/Icon/xoa_icon.png");
        btnDelete.addActionListener(e -> deleteBook());
        btnEdit = new UIButton("menuButton", "SỬA", 90, 40, "/Icon/sua_icon.png");
        btnEdit.addActionListener(e -> editBook());
        btnView = new UIButton("menuButton", "XEM", 90, 40, "/Icon/chitiet_icon.png");
        btnView.addActionListener(e -> viewBookDetails());
        pnlButton.add(btnAdd);
        pnlButton.add(btnDelete);
        pnlButton.add(btnEdit);
        pnlButton.add(btnView);
        applyPermissions(taiKhoan.getTenDangNhap(), 1);

        JPanel pnlSearchFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
        pnlSearchFilter.setBackground(UIConstants.MAIN_BACKGROUND);
        txtSearch = new UITextField(190,30);
        pnlSearchFilter.add(txtSearch);

        pnlHeader.add(pnlButton, BorderLayout.WEST);
        pnlHeader.add(pnlSearchFilter, BorderLayout.CENTER);
        //==============================( End Panel Header )============================//

        
        
        //================================( PANEL CONTENT )=============================//
        pnlContent = new JPanel(new BorderLayout());
        pnlContent.setBackground(UIConstants.MAIN_BACKGROUND);
        pnlContent.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
        String[] columnNames = {"MÃ SÁCH", "TÊN SÁCH", "GIÁ", "NHÀ XUẤT BẢN", "TỒN KHO"};
        tableModel = new DefaultTableModel(columnNames, 0); 
        tblContent = new UITable(tableModel);
       
        UIScrollPane scrollPane = new UIScrollPane(tblContent);  
        pnlContent.add(scrollPane, BorderLayout.CENTER);
        //===============================( End Panel Content )===========================//

        
        this.add(pnlHeader, BorderLayout.NORTH);
        this.add(pnlContent, BorderLayout.CENTER);
        addSearchFunctionality();
        loadTableData();
    }

    private void applyPermissions(String username, int maCN) {
        btnAdd.setVisible(taiKhoanBUS.hasPermission(username, maCN, "add"));
        btnEdit.setVisible(taiKhoanBUS.hasPermission(username, maCN, "edit"));
        btnDelete.setVisible(taiKhoanBUS.hasPermission(username, maCN, "delete"));
    }
    
    public void loadTableData() {
        tableModel.setRowCount(0); 
        for (SachDTO sach : sachBUS.getAllSach()) {
            tableModel.addRow(new Object[]{
                sach.getMaSach(),
                sach.getTenSach(),
                String.format("%,.0f VNĐ", (double)sach.getGiaSach()),
                nhaXuatBanBUS.getById(sachBUS.getById(sach.getMaSach()).getMaNXB()).getTenNXB(),
                sach.getSoLuongTon()
            });
        }
    }

    private void addBook() {
        Window window = SwingUtilities.getWindowAncestor(this);
        new AddAndEditBookGUI((JFrame) window, sachBUS, "Thêm sách", "add");
        loadTableData(); 
    }

    private void editBook() {
        int selectedRow = tblContent.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một cuốn sách để chỉnh sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int maSach = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        SachDTO sach = sachBUS.getById(maSach);
        Window window = SwingUtilities.getWindowAncestor(this);
        new AddAndEditBookGUI((JFrame) window, sachBUS, "Chỉnh sửa sách", "save", sach);
        loadTableData();
    }
    
    private void deleteBook() {
        int selectedRow = tblContent.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một cuốn sách để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa cuốn sách này không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int maSach = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            if (sachBUS.deleteSach(maSach)) { 
                JOptionPane.showMessageDialog(this, "Xóa sách thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa sách thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addSearchFunctionality() {
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { searchBook(); }
            public void removeUpdate(DocumentEvent e) { searchBook(); }
            public void changedUpdate(DocumentEvent e) { searchBook(); }
        });
    }
    
    private void searchBook() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        tableModel.setRowCount(0); 
        ArrayList<SachDTO> listSach = sachBUS.searchSach(keyword);
        for (SachDTO sach : listSach) {
            tableModel.addRow(new Object[]{
                sach.getMaSach(),
                sach.getTenSach(),
                sach.getGiaSach(),
                nhaXuatBanBUS.getById(sachBUS.getById(sach.getMaSach()).getMaNXB()).getTenNXB(),
                sach.getSoLuongTon()
            });
        }
    }
    
    private void viewBookDetails() {
        int selectedRow = tblContent.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một cuốn sách để xem thông tin!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int maSach = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        SachDTO sach = sachBUS.getById(maSach);
        NhaXuatBanBUS nhaXuatBanBUS = new NhaXuatBanBUS();
        String tenNXB = nhaXuatBanBUS.getById(sach.getMaNXB()).getTenNXB();
        TacGiaBUS tacGiaBUS = new TacGiaBUS();
        ArrayList<String> dsTacGia = tacGiaBUS.getTacGiaByMaSach(maSach);
        TheLoaiBUS theLoaiBUS = new TheLoaiBUS();
        ArrayList<String> dsTheLoai = theLoaiBUS.getTheLoaiByMaSach(maSach);
        // Hiển thị hộp thoại thông tin sách
        showBookDetailsDialog(sach, tenNXB, dsTacGia, dsTheLoai);
    }
    
    private void showBookDetailsDialog(SachDTO sach, String tenNXB, ArrayList<String> dsTacGia, ArrayList<String> dsTheLoai) {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Thông tin chi tiết sách", true);
        dialog.setSize(500, 300);
        dialog.setLayout(new BorderLayout());
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panel.add(new UILabel("MÃ SÁCH: " + sach.getMaSach(), 450, 30));
        panel.add(new UILabel("TÊN SÁCH: " + sach.getTenSach(), 450, 30));
        panel.add(new UILabel("GIÁ SÁCH: " + sach.getGiaSach() + " VND", 450, 30));
        panel.add(new UILabel("NHÀ XUẤT BẢN: " + tenNXB, 450, 30));
        panel.add(new UILabel("TÁC GIẢ: " + String.join(", ", dsTacGia), 450, 30));
        panel.add(new UILabel("THỂ LOẠI: " + String.join(", ", dsTheLoai), 450, 30));

        dialog.add(panel, BorderLayout.CENTER);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
