package dao.impl;

import config.DatabaseConfig;
import dao.JadwalPengumpulanDAO;
import model.JadwalPengumpulan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JadwalPengumpulanDAOImpl implements JadwalPengumpulanDAO {
    private final Connection connection = DatabaseConfig.getConnection();

    @Override
    public void insert(JadwalPengumpulan entity) {
        String sql = "INSERT INTO jadwal_pengumpulan (dosen_id, materi, tanggal_praktikum, batas_akhir) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, entity.getDosenId());
            statement.setString(2, entity.getMateri());
            statement.setDate(3, entity.getTanggalPraktikum());
            statement.setDate(4, entity.getBatasAkhir());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                }
            }
            System.out.println("Jadwal pengumpulan berhasil dibuat.");
        } catch (SQLException e) {
            System.err.println("Gagal insert jadwal pengumpulan: " + e.getMessage());
        }
    }

    @Override
    public JadwalPengumpulan findById(int id) {
        String sql = "SELECT j.*, d.nama AS nama_dosen FROM jadwal_pengumpulan j JOIN dosen d ON j.dosen_id = d.id WHERE j.id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    JadwalPengumpulan jadwal = new JadwalPengumpulan(
                            resultSet.getInt("id"),
                            resultSet.getInt("dosen_id"),
                            resultSet.getString("materi"),
                            resultSet.getDate("tanggal_praktikum"),
                            resultSet.getDate("batas_akhir")
                    );
                    jadwal.setNamaDosen(resultSet.getString("nama_dosen"));
                    return jadwal;
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal findById jadwal pengumpulan: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<JadwalPengumpulan> findAll() {
        List<JadwalPengumpulan> list = new ArrayList<>();
        String sql = "SELECT j.*, d.nama AS nama_dosen FROM jadwal_pengumpulan j JOIN dosen d ON j.dosen_id = d.id ORDER BY j.tanggal_praktikum DESC";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                JadwalPengumpulan jadwal = new JadwalPengumpulan(
                        resultSet.getInt("id"),
                        resultSet.getInt("dosen_id"),
                        resultSet.getString("materi"),
                        resultSet.getDate("tanggal_praktikum"),
                        resultSet.getDate("batas_akhir")
                );
                jadwal.setNamaDosen(resultSet.getString("nama_dosen"));
                list.add(jadwal);
            }
        } catch (SQLException e) {
            System.err.println("Gagal findAll jadwal pengumpulan: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void update(JadwalPengumpulan entity) {
        String sql = "UPDATE jadwal_pengumpulan SET materi = ?, tanggal_praktikum = ?, batas_akhir = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getMateri());
            statement.setDate(2, entity.getTanggalPraktikum());
            statement.setDate(3, entity.getBatasAkhir());
            statement.setInt(4, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal update jadwal pengumpulan: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM jadwal_pengumpulan WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal delete jadwal pengumpulan: " + e.getMessage());
        }
    }

    @Override
    public List<JadwalPengumpulan> findByDosenId(int dosenId) {
        List<JadwalPengumpulan> list = new ArrayList<>();
        String sql = "SELECT j.*, d.nama AS nama_dosen FROM jadwal_pengumpulan j JOIN dosen d ON j.dosen_id = d.id WHERE j.dosen_id = ? ORDER BY j.tanggal_praktikum DESC";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, dosenId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    JadwalPengumpulan jadwal = new JadwalPengumpulan(
                            resultSet.getInt("id"),
                            resultSet.getInt("dosen_id"),
                            resultSet.getString("materi"),
                            resultSet.getDate("tanggal_praktikum"),
                            resultSet.getDate("batas_akhir")
                    );
                    jadwal.setNamaDosen(resultSet.getString("nama_dosen"));
                    list.add(jadwal);
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal findByDosenId jadwal pengumpulan: " + e.getMessage());
        }
        return list;
    }
}