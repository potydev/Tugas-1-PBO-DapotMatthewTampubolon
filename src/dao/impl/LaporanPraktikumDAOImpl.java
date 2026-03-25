package dao.impl;

import config.DatabaseConfig;
import dao.LaporanPraktikumDAO;
import model.LaporanPraktikum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LaporanPraktikumDAOImpl implements LaporanPraktikumDAO {
    private final Connection connection = DatabaseConfig.getConnection();

    @Override
    public void insert(LaporanPraktikum entity) {
        String sql = "INSERT INTO laporan_praktikum (jadwal_id, mahasiswa_id, judul, hasil_praktikum, tanggal_kumpul, status, nilai, catatan_dosen) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, entity.getJadwalId());
            statement.setInt(2, entity.getMahasiswaId());
            statement.setString(3, entity.getJudul());
            statement.setString(4, entity.getHasilPraktikum());
            statement.setDate(5, entity.getTanggalKumpul());
            statement.setString(6, entity.getStatus());

            if (entity.getNilai() == null) {
                statement.setNull(7, java.sql.Types.INTEGER);
            } else {
                statement.setInt(7, entity.getNilai());
            }

            statement.setString(8, entity.getCatatanDosen());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                }
            }
            System.out.println("Laporan praktikum berhasil dikumpulkan.");
        } catch (SQLException e) {
            System.err.println("Gagal insert laporan praktikum: " + e.getMessage());
        }
    }

    @Override
    public LaporanPraktikum findById(int id) {
        String sql = "SELECT l.*, m.nama AS nama_mahasiswa, j.materi, j.batas_akhir " +
                "FROM laporan_praktikum l " +
                "JOIN mahasiswa m ON l.mahasiswa_id = m.id " +
                "JOIN jadwal_pengumpulan j ON l.jadwal_id = j.id " +
                "WHERE l.id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapLaporan(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal findById laporan praktikum: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<LaporanPraktikum> findAll() {
        List<LaporanPraktikum> list = new ArrayList<>();
        String sql = "SELECT l.*, m.nama AS nama_mahasiswa, j.materi, j.batas_akhir " +
                "FROM laporan_praktikum l " +
                "JOIN mahasiswa m ON l.mahasiswa_id = m.id " +
                "JOIN jadwal_pengumpulan j ON l.jadwal_id = j.id " +
                "ORDER BY l.id DESC";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                list.add(mapLaporan(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Gagal findAll laporan praktikum: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void update(LaporanPraktikum entity) {
        String sql = "UPDATE laporan_praktikum SET judul = ?, hasil_praktikum = ?, status = ?, nilai = ?, catatan_dosen = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getJudul());
            statement.setString(2, entity.getHasilPraktikum());
            statement.setString(3, entity.getStatus());

            if (entity.getNilai() == null) {
                statement.setNull(4, java.sql.Types.INTEGER);
            } else {
                statement.setInt(4, entity.getNilai());
            }

            statement.setString(5, entity.getCatatanDosen());
            statement.setInt(6, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal update laporan praktikum: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM laporan_praktikum WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal delete laporan praktikum: " + e.getMessage());
        }
    }

    @Override
    public List<LaporanPraktikum> findByMahasiswaId(int mahasiswaId) {
        List<LaporanPraktikum> list = new ArrayList<>();
        String sql = "SELECT l.*, m.nama AS nama_mahasiswa, j.materi, j.batas_akhir " +
                "FROM laporan_praktikum l " +
                "JOIN mahasiswa m ON l.mahasiswa_id = m.id " +
                "JOIN jadwal_pengumpulan j ON l.jadwal_id = j.id " +
                "WHERE l.mahasiswa_id = ? ORDER BY l.id DESC";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, mahasiswaId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    list.add(mapLaporan(resultSet));
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal findByMahasiswaId laporan praktikum: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<LaporanPraktikum> findByDosenId(int dosenId) {
        List<LaporanPraktikum> list = new ArrayList<>();
        String sql = "SELECT l.*, m.nama AS nama_mahasiswa, j.materi, j.batas_akhir " +
                "FROM laporan_praktikum l " +
                "JOIN mahasiswa m ON l.mahasiswa_id = m.id " +
                "JOIN jadwal_pengumpulan j ON l.jadwal_id = j.id " +
                "WHERE j.dosen_id = ? ORDER BY l.id DESC";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, dosenId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    list.add(mapLaporan(resultSet));
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal findByDosenId laporan praktikum: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void updateNilaiDanCatatan(int laporanId, int nilai, String catatan) {
        String sql = "UPDATE laporan_praktikum SET nilai = ?, catatan_dosen = ?, status = 'DINILAI' WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, nilai);
            statement.setString(2, catatan);
            statement.setInt(3, laporanId);
            int updated = statement.executeUpdate();
            if (updated > 0) {
                System.out.println("Nilai laporan berhasil disimpan.");
            } else {
                System.out.println("ID laporan tidak ditemukan.");
            }
        } catch (SQLException e) {
            System.err.println("Gagal update nilai laporan praktikum: " + e.getMessage());
        }
    }

    private LaporanPraktikum mapLaporan(ResultSet resultSet) throws SQLException {
        LaporanPraktikum laporan = new LaporanPraktikum(
                resultSet.getInt("id"),
                resultSet.getInt("jadwal_id"),
                resultSet.getInt("mahasiswa_id"),
                resultSet.getString("judul"),
                resultSet.getString("hasil_praktikum"),
                resultSet.getDate("tanggal_kumpul"),
                resultSet.getString("status"),
                (Integer) resultSet.getObject("nilai"),
                resultSet.getString("catatan_dosen")
        );
        laporan.setNamaMahasiswa(resultSet.getString("nama_mahasiswa"));
        laporan.setMateri(resultSet.getString("materi"));
        laporan.setBatasAkhir(resultSet.getDate("batas_akhir"));
        return laporan;
    }
}