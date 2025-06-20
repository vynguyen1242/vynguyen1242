package DAO;
import DTO.TacGiaDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TacGiaDAO {

    public int add(TacGiaDTO obj) {
        String sql = "INSERT INTO tacgia (maTG, tenTG) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, obj.getMaTG());
            ps.setString(2, obj.getTenTG());
            return ps.executeUpdate(); 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int update(TacGiaDTO obj) {
        String sql = "UPDATE tacgia SET tenTG=? WHERE maTG=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, obj.getTenTG());
            ps.setInt(2, obj.getMaTG());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(int maTG) {
        String sql = "UPDATE tacgia SET trangThaiXoa=1 WHERE maTG=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maTG);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ArrayList<TacGiaDTO> getAll() {
        ArrayList<TacGiaDTO> dsSach = new ArrayList<>();
        String sql = "SELECT * FROM tacgia WHERE trangThaiXoa=0";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {
            while (rs.next()) {
                dsSach.add(new TacGiaDTO(
                    rs.getInt("maTG"),
                    rs.getString("tenTG")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsSach;
    }

    public TacGiaDTO getById(int id) {
        String sql = "SELECT * FROM tacgia WHERE maTG=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TacGiaDTO(
                        rs.getInt("maTG"),
                        rs.getString("tenTG")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public String getNextMaTg() {
        String sql = "SELECT MAX(maTG) AS nextID FROM tacgia";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int nextId = rs.getInt("nextID");
                return String.valueOf(nextId + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "1";
    }
    
    public ArrayList<Integer> getMaTacGiaBySach(int maSach) {
        ArrayList<Integer> danhSachMaTG = new ArrayList<>();
        String sql = "SELECT maTG FROM nhomtacgia WHERE maSach = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, maSach);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                danhSachMaTG.add(rs.getInt("maTG"));
            }
        } catch (SQLException e) {
        }
        return danhSachMaTG;
    }

    public int getMaTgByTenTg(String tenTg){
        String sql = "SELECT maTG FROM tacgia WHERE tenTG = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, tenTg);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("maTG");
            }
        } catch (SQLException e) {
        }
        return 0;
    }
}
