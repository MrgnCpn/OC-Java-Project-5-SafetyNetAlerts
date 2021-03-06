package com.safetynet.safetynetalert.interfaces;

import com.safetynet.safetynetalert.models.MedicalRecord;

import java.util.List;

public interface MedicalRecordDAOInterface {

    /**
     * Get one medicalRecord from allMedicalRecords choose by person id
     * @param id
     * @return One MedicalRecord
     */
    MedicalRecord getMedicalRecord(Integer id);

    /**
     *  Get all medicalRecords
     * @return List of all medicalRecords
     */
    List<MedicalRecord> getAllMedicalRecords();

    /**
     * Add one new medical record
     * @param medicalRecord
     */
    boolean addNewMedicalRecord(MedicalRecord medicalRecord);

    /**
     * Update one medical record in allMedicalRecords
     * @param medicalRecord
     */
    boolean updateMedicalRecord(MedicalRecord medicalRecord);

    /**
     * Delete the medical record in allMedicalRecords
     * @param id
     */
    boolean deleteMedicalRecord(Integer id);
}
