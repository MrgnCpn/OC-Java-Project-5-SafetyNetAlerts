package com.safetynet.safetynetalert.services;

import com.safetynet.safetynetalert.dao.MedicalRecordDAO;
import com.safetynet.safetynetalert.interfaces.MedicalRecordsServiceInterface;
import com.safetynet.safetynetalert.models.MedicalRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Singleton;

@Singleton
public class MedicalRecordsService implements MedicalRecordsServiceInterface {
    /**
     * Logger
     */
    private static final Logger logger = LogManager.getLogger("MedicalRecordsService");

    /**
     * Persons medical records
     */
    private MedicalRecordDAO medicalRecordDAO;

    /**
     * Constructor
     * @param medicalRecordDAO
     */
    public MedicalRecordsService(MedicalRecordDAO medicalRecordDAO) {
        this.medicalRecordDAO = medicalRecordDAO;
    }

    /**
     * @see com.safetynet.safetynetalert.interfaces.MedicalRecordsServiceInterface {@link #httpPost(MedicalRecord)}
     */
    @Override
    public String httpPost(MedicalRecord newMedicalRecord) {
        if (newMedicalRecord != null) {
            if (
                (newMedicalRecord.getId() != null)
                && (newMedicalRecord.getBirthdate() != null)
                && (newMedicalRecord.getMedications() != null)
                && (newMedicalRecord.getAllergies() != null)
            ) {
                if (medicalRecordDAO.addNewMedicalRecord(newMedicalRecord)) {
                    logger.info(new StringBuffer("New medical record for person n°").append(newMedicalRecord.getId()).append(" added"));
                    return "Medical record added";
                } else {
                    logger.error("Medical record can't be added");
                    return "Error : This Medical record can't be added";
                }
            } else throw new NullPointerException("Data are not complete");
        } else throw new NullPointerException();
    }

    /**
     * @see com.safetynet.safetynetalert.interfaces.MedicalRecordsServiceInterface {@link #httpPut(MedicalRecord)}
     */
    @Override
    public String httpPut(MedicalRecord medicalRecord) {
        if (medicalRecord != null) {
            if (
                (medicalRecord.getId() != null)
                && (medicalRecord.getAge() != null)
                && (medicalRecord.getMedications() != null)
                && (medicalRecord.getAllergies() != null)
            ) {
                if (medicalRecordDAO.updateMedicalRecord(medicalRecord)) {
                    logger.info(new StringBuffer("Medical record for person n°").append(medicalRecord.getId()).append(" updated"));
                    return "Medical record updated";
                } else {
                    logger.error("Medical record can't be updated");
                    return "Error : This Medical record can't be updated";
                }
            } else throw new NullPointerException("Data are not complete");
        } else throw new NullPointerException();
    }

    /**
     * @see com.safetynet.safetynetalert.interfaces.MedicalRecordsServiceInterface {@link #httpDelete(Integer)}
     */
    @Override
    public String httpDelete(Integer id) {
        if (medicalRecordDAO.deleteMedicalRecord(id)) {
            logger.info(new StringBuffer("Medical record for person n°").append(id).append(" deleted"));
            return "Medical record deleted";
        } else {
            logger.error("Medical record can't be deleted");
            return "Error : This Medical record can't be deleted";
        }
    }

}
