package dao;

import model.LaporanPraktikum;

import java.util.List;

public interface LaporanPraktikumDAO extends BaseDAO<LaporanPraktikum> {
    List<LaporanPraktikum> findByMahasiswaId(int mahasiswaId);

    List<LaporanPraktikum> findByDosenId(int dosenId);

    void updateNilaiDanCatatan(int laporanId, int nilai, String catatan);
}