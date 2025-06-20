package GUI.ThongKeComponent;

import BUS.PhieuNhapBUS;
import BUS.PhieuXuatBUS;
import Utils.UIButton;
import Utils.UIConstants;
import Utils.UILabel;
import Utils.UIScrollPane;
import Utils.UITable;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.swing.border.EmptyBorder;


public class ThongKeDoanhThu extends JPanel {
    private UITable table, summaryTable;
    private DefaultTableModel model, summaryModel;
    private JDateChooser dateFrom;
    private JDateChooser dateTo;
    private PhieuXuatBUS phieuXuatBUS;
    private PhieuNhapBUS phieuNhapBUS;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public ThongKeDoanhThu() {
        phieuXuatBUS = new PhieuXuatBUS();
        phieuNhapBUS = new PhieuNhapBUS();
        this.setLayout(new BorderLayout(5, 5));
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setBackground(UIConstants.MAIN_BACKGROUND);

        //================================================
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        filterPanel.setBackground(UIConstants.MAIN_BACKGROUND);

        UILabel lblFromDate = new UILabel("Từ:",30,30);
        dateFrom = new JDateChooser();
        dateFrom.setPreferredSize(new Dimension(150, 30));
        dateFrom.setLocale(new Locale("vi", "VN"));
        UILabel lblToDate = new UILabel("Đến:",35,30);
        dateTo = new JDateChooser();
        dateTo.setPreferredSize(new Dimension(150, 30));
        dateTo.setLocale(new Locale("vi", "VN"));
        UIButton btnLocThang = new UIButton("add", "THỐNG KÊ", 100, 30);
        btnLocThang.addActionListener(e -> loadDataTheoKhoangThoiGian());

        filterPanel.add(lblFromDate);
        filterPanel.add(dateFrom);
        filterPanel.add(lblToDate);
        filterPanel.add(dateTo);
        filterPanel.add(btnLocThang);

        // =======================================================
        String[] columnNames = {"NGÀY", "VỐN", "DOANH THU", "LỢI NHUẬN"};
        model = new DefaultTableModel(columnNames, 0); 
        table = new UITable(model);
        UIScrollPane scrollPane = new UIScrollPane(table);          
        
        
        // =======================================================
        String[] summaryColumnNames = {"", "", "", ""};
        summaryModel = new DefaultTableModel(summaryColumnNames, 1);
        summaryTable = new UITable(summaryModel);
        summaryTable.setRowSelectionAllowed(false);
        summaryTable.setColumnSelectionAllowed(false);
        summaryTable.setCellSelectionEnabled(false);
        summaryTable.setFont(new Font("Roboto", Font.BOLD, 17));
        summaryTable.setRowHeight(35);
        summaryTable.setBackground(UIConstants.WHITE_FONT);
        
        this.add(filterPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(summaryTable, BorderLayout.SOUTH);
    }

    private void loadDataTheoKhoangThoiGian() {
        model.setRowCount(0);

        Date fromDate = dateFrom.getDate();
        Date toDate = dateTo.getDate();
        
        if (fromDate == null || toDate == null || fromDate.after(toDate)) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn khoảng thời gian hợp lệ!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fromDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(toDate);
        double tongVon = 0;
        double tongDoanhThu = 0;
        double tongLoiNhuan = 0;

        // Duyệt qua từng ngày trong khoảng thời gian
        while (!calendar.after(endCalendar)) {
            Date currentDate = calendar.getTime();
            String currentDateStr = dateFormat.format(currentDate);

            double von = phieuNhapBUS.getTongTienTheoNgay(currentDate);
            double doanhThu = phieuXuatBUS.getTongTienTheoNgay(currentDate);
            double loiNhuan = doanhThu - von;

            tongVon += von;
            tongDoanhThu += doanhThu;
            tongLoiNhuan += loiNhuan;
            
            model.addRow(new Object[]{
                currentDateStr,
                String.format("%,.0f VNĐ", von),
                String.format("%,.0f VNĐ", doanhThu),
                String.format("%,.0f VNĐ", loiNhuan)
            });
            calendar.add(Calendar.DATE, 1); 
        }
        summaryModel.setValueAt("Tổng: ", 0, 0);
        summaryModel.setValueAt(String.format("%,.0f VNĐ", tongVon), 0, 1);
        summaryModel.setValueAt(String.format("%,.0f VNĐ", tongDoanhThu), 0, 2);
        summaryModel.setValueAt(String.format("%,.0f VNĐ", tongLoiNhuan), 0, 3);
    }
}
