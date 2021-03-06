package com.safetynet.safetynetalert.interfaces;

import com.safetynet.safetynetalert.models.MedicalRecord;

public interface MedicalRecordsServiceInterface {

    /**
     * Add MedicalRecord from HTTP POST
     * @param newMedicalRecord
     */
    String httpPost(MedicalRecord newMedicalRecord);

    /**
     * Update MedicalRecord from HTTP PUT
     * @param medicalRecord
     */
    String httpPut(MedicalRecord medicalRecord);

    /**
     * Delete MedicalRecord from HTTP DELETE
     * @param id
     */
    String httpDelete(Integer id);
}