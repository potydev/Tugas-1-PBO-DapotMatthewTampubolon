package dao;

import model.JadwalPengumpulan;

import java.util.List;

public interface JadwalPengumpulanDAO extends BaseDAO<JadwalPengumpulan> {
    List<JadwalPengumpulan> findByDosenId(int dosenId);
}