package com.safetynet.safetynetalert.services;

import com.safetynet.safetynetalert.dao.MedicalRecordDAO;
import com.safetynet.safetynetalert.dao.PersonDAO;
import com.safetynet.safetynetalert.dao.StationDAO;
import com.safetynet.safetynetalert.interfaces.InformationsServicesInterface;
import com.safetynet.safetynetalert.models.MedicalRecord;
import com.safetynet.safetynetalert.models.Person;
import com.safetynet.safetynetalert.models.Station;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONValue;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Singleton
public class InformationService implements InformationsServicesInterface {
    /**
     * Logger
     */
    private static final Logger logger = LogManager.getLogger("InformationService");

    /**
     * Persons Profiles
     */
    private PersonDAO personDAO;

    /**
     * Fire station information
     */
    private StationDAO stationDAO;

    /**
     * Persons medical records
     */
    private MedicalRecordDAO medicalRecordDAO;

    /**
     * Constructor
     * @param personDAO
     * @param stationDAO
     * @param medicalRecordDAO
     */
    public InformationService(PersonDAO personDAO, StationDAO stationDAO, MedicalRecordDAO medicalRecordDAO) {
        this.personDAO = personDAO;
        this.stationDAO = stationDAO;
        this.medicalRecordDAO = medicalRecordDAO;
    }

    /**
     * @see com.safetynet.safetynetalert.interfaces.InformationsServicesInterface {@link #getAllPersons()}
     */
    @Override
    public String getAllPersons() {
        StringBuilder data = new StringBuilder();
        data.append("{\"persons\" : [");

        for (Person iPerson : this.personDAO.getAllPersons()){
            data.append("{");
            data.append("\"id\" : ").append(iPerson.getId()).append(",");
            data.append("\"firstName\" : \"").append(JSONValue.escape(iPerson.getFirstName())).append("\",");
            data.append("\"lastName\" : \"").append(JSONValue.escape(iPerson.getLastName())).append("\",");
            data.append("\"address\" : \"").append(JSONValue.escape(iPerson.getAddress())).append("\",");
            data.append("\"city\" : \"").append(JSONValue.escape(iPerson.getCity())).append("\",");
            data.append("\"zip\" : \"").append(JSONValue.escape(iPerson.getZip())).append("\",");
            data.append("\"email\" : \"").append(JSONValue.escape(iPerson.getEmail())).append("\",");
            data.append("\"phone\" : \"").append(JSONValue.escape(iPerson.getPhone())).append("\"");
            data.append("},");
        }

        deleteLastComma(data);
        data.append("]}");
        logger.info(new StringBuffer("GET : All Persons"));
        return data.toString();
    }

    /**
     * @see com.safetynet.safetynetalert.interfaces.InformationsServicesInterface {@link #getAllFirestations()}
     */
    @Override
    public String getAllFirestations() {
        StringBuilder data = new StringBuilder();
        data.append("{\"stations\" : [");

        for (Station iStation : this.stationDAO.getAllStations()){
            data.append("{");
            data.append("\"number\" : ").append(iStation.getNumber()).append(",");
            data.append("\"address\" : \"").append(JSONValue.escape(iStation.getAddress())).append("\"");
            data.append("},");
        }

        deleteLastComma(data);
        data.append("]}");
        logger.info(new StringBuffer("GET : All Stations"));
        return data.toString();
    }

    /**
     * @see com.safetynet.safetynetalert.interfaces.InformationsServicesInterface {@link #getAllMedicalRecords()}
     */
    @Override
    public String getAllMedicalRecords() {
        StringBuilder data = new StringBuilder();
        data.append("{\"medicalRecords\" : [");

        for (MedicalRecord iMedicalRecord : this.medicalRecordDAO.getAllMedicalRecords()){
            data.append("{");
            data.append("\"id\" : ").append(iMedicalRecord.getId()).append(",");
            data.append("\"birthdate\" : \"").append(JSONValue.escape(iMedicalRecord.getBirthdate())).append("\",");
            data.append("\"age\" : ").append(iMedicalRecord.getAge()).append(",");
            data.append("\"medicalRecords\" : {");
            data.append("\"medications\" : [");
            for (String medication : iMedicalRecord.getMedications()) {
                data.append("\"").append(JSONValue.escape(medication)).append("\",");
            }
            deleteLastComma(data);
            data.append("], \"allergies\" : [");
            for (String allergy : iMedicalRecord.getAllergies()) {
                data.append("\"").append(JSONValue.escape(allergy)).append("\",");
            }
            deleteLastComma(data);
            deleteLastComma(data);
            data.append("]}},");
        }

        deleteLastComma(data);
        data.append("]}");
        logger.info(new StringBuffer("GET : All Medical Records"));
        return data.toString();
    }

    /**
     * @see com.safetynet.safetynetalert.interfaces.InformationsServicesInterface {@link #getAllPersonsServedByTheStationWithCount(Integer)}
     */
    @Override
    public String getAllPersonsServedByTheStationWithCount(Integer stationNumber) {
        int childCount = 0;
        int adultCount = 0;
        StringBuilder data = new StringBuilder();
        data.append("{\"station\" : " + stationNumber + ",");
        data.append("\"persons\" : [");

        for (Station iStation : this.stationDAO.getAllStations()){
            if (iStation.getNumber().equals(stationNumber)) {
                for (Person iPerson : this.personDAO.getAllPersons()){
                    if (iPerson.getAddress().equals(iStation.getAddress())) {
                        data.append("{");
                        data.append("\"firstName\" : \"").append(JSONValue.escape(iPerson.getFirstName())).append("\",");
                        data.append("\"lastName\" : \"").append(JSONValue.escape(iPerson.getLastName())).append("\",");
                        data.append("\"address\" : \"").append(JSONValue.escape(iPerson.getAddress())).append("\",");
                        data.append("\"phone\" : \"").append(JSONValue.escape(iPerson.getPhone())).append("\"");

                        if (this.medicalRecordDAO.getMedicalRecord(iPerson.getId()).getAge() <= 18) {
                            childCount ++;
                        } else adultCount ++;

                        data.append("},");
                    }
                }
            }
        }

        deleteLastComma(data);
        data.append("],");

        data.append("\"adultCount\" : ").append(adultCount).append(",");
        data.append("\"childCount\" : ").append(childCount);
        data.append("}");
        logger.info(new StringBuffer("GET : All Persons Served By The Station n°").append(stationNumber).append(" With Count"));
        return data.toString();
    }

    /**
     * @see com.safetynet.safetynetalert.interfaces.InformationsServicesInterface {@link #getAllChildByAddress(String)}
     */
    @Override
    public String getAllChildByAddress(String address) {
        int childCount = 0;
        StringBuilder data = new StringBuilder();

        data.append("{\"address\" : \"").append(JSONValue.escape(address)).append("\",");
        data.append("\"childs\" : [");

        for (Person iPerson : this.personDAO.getAllPersons()) {
            if (
                    (medicalRecordDAO.getMedicalRecord(iPerson.getId()).getAge() <= 18)
                            && (iPerson.getAddress().equals(address))
            ) {
                data.append("{");
                data.append("\"firstName\" : \"").append(JSONValue.escape(iPerson.getFirstName())).append("\",");
                data.append("\"lastName\" : \"").append(JSONValue.escape(iPerson.getLastName())).append("\",");
                data.append("\"age\" : ").append(medicalRecordDAO.getMedicalRecord(iPerson.getId()).getAge()).append("},");
                childCount ++;
            }
        }

        deleteLastComma(data);
        data.append("], \"adults\" : [");

        for (Person iPerson : this.personDAO.getAllPersons()) {
            if (
                    (medicalRecordDAO.getMedicalRecord(iPerson.getId()).getAge() > 18)
                            && (iPerson.getAddress().equals(address))
            ) {
                data.append("{");
                data.append("\"firstName\" : \"").append(JSONValue.escape(iPerson.getFirstName())).append("\",");
                data.append("\"lastName\" : \"").append(JSONValue.escape(iPerson.getLastName())).append("\"");
                data.append("},");
            }
        }

        deleteLastComma(data);
        data.append("]}");

        logger.info(new StringBuffer("GET : All child for the address : ").append(address).append(", count : ").append(childCount));
        if (childCount == 0) {
            return null;
        }
        return data.toString();
    }

    /**
     * @see com.safetynet.safetynetalert.interfaces.InformationsServicesInterface {@link #getAllPersonsPhoneByStationNumber(Integer)}
     */
    @Override
    public String getAllPersonsPhoneByStationNumber(Integer stationNumber) {
        StringBuilder data = new StringBuilder();
        data.append("{\"station\": ").append(stationNumber).append(",");
        data.append("\"phones\" : [");

        for (Station iStation : this.stationDAO.getAllStations()) {
            if (iStation.getNumber().equals(stationNumber)) {
                for (Person iPerson : this.personDAO.getAllPersons()) {
                    if (iPerson.getAddress().equals(iStation.getAddress())) {
                        data.append("\"").append(JSONValue.escape(iPerson.getPhone())).append("\",");
                    }
                }
            }
        }

        deleteLastComma(data);
        data.append("]}");

        logger.info(new StringBuffer("GET : All Persons Phone By Station Number : ").append(stationNumber));
        return data.toString();
    }

    /**
     * @see com.safetynet.safetynetalert.interfaces.InformationsServicesInterface {@link #getAllPersonsLivingAtTheAddress(String)}
     */
    @Override
    public String getAllPersonsLivingAtTheAddress(String address) {
        StringBuilder data = new StringBuilder();
        MedicalRecord personMedicalRecords;

        data.append("{\"address\" : \"").append(JSONValue.escape(address)).append("\",");
        data.append("\"station\": ").append(stationDAO.getStationByAddress(address).getNumber()).append(",");
        data.append("\"persons\" : [");

        for (Person iPerson : this.personDAO.getAllPersons()) {
            if (iPerson.getAddress().equals(address)) {
                personMedicalRecords = medicalRecordDAO.getMedicalRecord(iPerson.getId());
                data.append("{");
                data.append("\"firstName\" : \"").append(JSONValue.escape(iPerson.getFirstName())).append("\",");
                data.append("\"lastName\" : \"").append(JSONValue.escape(iPerson.getLastName())).append("\",");
                data.append("\"phone\" : \"").append(JSONValue.escape(iPerson.getPhone())).append("\",");
                data.append("\"birthdate\" : \"").append((personMedicalRecords != null) ? JSONValue.escape(personMedicalRecords.getBirthdate()) : "").append("\",");
                data.append("\"age\" : ").append((personMedicalRecords != null) ? personMedicalRecords.getAge() : "").append(",");
                data.append("\"medicalRecords\" : {");
                data.append("\"medications\" : [");
                if (personMedicalRecords != null) {
                    for (String medication : personMedicalRecords.getMedications()) {
                        data.append("\"").append(JSONValue.escape(medication)).append("\",");
                    }
                    deleteLastComma(data);
                }
                data.append("], \"allergies\" : [");
                if (personMedicalRecords != null) {
                    for (String allergy : personMedicalRecords.getAllergies()) {
                        data.append("\"").append(JSONValue.escape(allergy)).append("\",");
                    }
                    deleteLastComma(data);
                }
                deleteLastComma(data);
                data.append("]}},");
            }
        }

        deleteLastComma(data);
        data.append("]}");

        logger.info(new StringBuffer("GET : All Persons Living At The Address : ").append(address));
        return data.toString();
    }

    /**
     * @see com.safetynet.safetynetalert.interfaces.InformationsServicesInterface {@link #getAllPersonsServedByTheStations(String)}
     */
    @Override
    public String getAllPersonsServedByTheStations(String stations) {
        StringBuilder data = new StringBuilder();

        List<Integer> stationNumbers = new ArrayList<>();
        for(String subStr : Arrays.asList(stations.split("/"))) stationNumbers.add(Integer.valueOf(subStr));

        data.append("{\"stations\" : [");
        for (Integer iNumStation : stationNumbers) {
            data.append("{\"number\" : ").append(iNumStation).append(",");
            data.append("\"homes\" : [");

            for (Station iStation : stationDAO.getStationByNumber(iNumStation)) {
                data.append("{ \"address\" : \"").append(JSONValue.escape(iStation.getAddress())).append("\",");

                MedicalRecord personMedicalRecords;

                data.append("\"persons\" : [");
                for (Person iPerson : this.personDAO.getAllPersons()) {
                    if (iPerson.getAddress().equals(iStation.getAddress())) {
                        personMedicalRecords = this.medicalRecordDAO.getMedicalRecord(iPerson.getId());
                        data.append("{");
                        data.append("\"firstName\" : \"").append(JSONValue.escape(iPerson.getFirstName())).append("\",");
                        data.append("\"lastName\" : \"").append(JSONValue.escape(iPerson.getLastName())).append("\",");
                        data.append("\"phone\" : \"").append(JSONValue.escape(iPerson.getPhone())).append("\",");
                        data.append("\"birthdate\" : \"").append((personMedicalRecords != null) ? JSONValue.escape(personMedicalRecords.getBirthdate()) : "").append("\",");
                        data.append("\"age\" : ").append((personMedicalRecords != null) ? personMedicalRecords.getAge() : "").append(",");
                        data.append("\"medicalRecords\" : {");
                        data.append("\"medications\" : [");
                        if (personMedicalRecords != null) {
                            for (String medication : personMedicalRecords.getMedications()) {
                                data.append("\"").append(JSONValue.escape(medication)).append("\",");
                            }
                            deleteLastComma(data);
                        }
                        data.append("], \"allergies\" : [");
                        if (personMedicalRecords != null) {
                            for (String allergy : personMedicalRecords.getAllergies()) {
                                data.append("\"").append(JSONValue.escape(allergy)).append("\",");
                            }
                            deleteLastComma(data);
                        }
                        data.append("]}},");
                    }
                }
                deleteLastComma(data);
                data.append("]},");
            }
            deleteLastComma(data);
            data.append("]},");
        }
        deleteLastComma(data);
        data.append("]}");

        logger.info(new StringBuffer("GET : All Persons Served By The Stations : ").append(stations));
        return data.toString();
    }

    /**
     * @see com.safetynet.safetynetalert.interfaces.InformationsServicesInterface {@link #getAllCompleteProfileOfPersonsByName(String, String)}
     */
    @Override
    public String getAllCompleteProfileOfPersonsByName(String firstName, String lastName) {
        StringBuilder data = new StringBuilder();
        MedicalRecord personMedicalRecords;

        data.append("{\"persons\" : [");

        for (Person iPerson : this.personDAO.getAllPersons()){
            if (iPerson.getFirstName().equals(firstName) && iPerson.getLastName().equals(lastName)) {
                personMedicalRecords = medicalRecordDAO.getMedicalRecord(iPerson.getId());
                data.append("{");
                data.append("\"id\" : ").append(iPerson.getId()).append(",");
                data.append("\"firstName\" : \"").append(JSONValue.escape(iPerson.getFirstName())).append("\",");
                data.append("\"lastName\" : \"").append(JSONValue.escape(iPerson.getLastName())).append("\",");
                data.append("\"address\" : \"").append(JSONValue.escape(iPerson.getAddress())).append("\",");
                data.append("\"city\" : \"").append(JSONValue.escape(iPerson.getCity())).append("\",");
                data.append("\"zip\" : \"").append(JSONValue.escape(iPerson.getZip())).append("\",");
                data.append("\"email\" : \"").append(JSONValue.escape(iPerson.getEmail())).append("\",");
                data.append("\"phone\" : \"").append(JSONValue.escape(iPerson.getPhone())).append("\",");
                data.append("\"birthdate\" : \"").append((personMedicalRecords != null) ? JSONValue.escape(personMedicalRecords.getBirthdate()) : "").append("\",");
                data.append("\"age\" : ").append((personMedicalRecords != null) ? personMedicalRecords.getAge() : "").append(",");
                data.append("\"medicalRecords\" : {");
                data.append("\"medications\" : [");
                if (personMedicalRecords != null) {
                    for (String medication : personMedicalRecords.getMedications()) {
                        data.append("\"").append(JSONValue.escape(medication)).append("\",");
                    }
                    deleteLastComma(data);
                }
                data.append("], \"allergies\" : [");
                if (personMedicalRecords != null) {
                    for (String allergy : personMedicalRecords.getAllergies()) {
                        data.append("\"").append(JSONValue.escape(allergy)).append("\",");
                    }
                    deleteLastComma(data);
                }
                data.append("]}},");
            }
        }
        deleteLastComma(data);
        data.append("]}");

        logger.info(new StringBuffer("GET : All Complete Profile Of Persons By Name : ").append(firstName).append(" ").append(lastName));
        return data.toString();
    }

    /**
     * @see com.safetynet.safetynetalert.interfaces.InformationsServicesInterface {@link #getAllPersonsEmailByCity(String)}
     */
    @Override
    public String getAllPersonsEmailByCity(String city) {
        StringBuilder data = new StringBuilder();
        data.append("{\"emails\" : [");

        for (Person iPerson : this.personDAO.getAllPersons()){
            if (iPerson.getCity().equals(city)) {
                data.append("\"").append(JSONValue.escape(iPerson.getEmail())).append("\",");
            }
        }

        deleteLastComma(data);
        data.append("]}");

        logger.info(new StringBuffer("GET : All Persons Email By City : ").append(city));
        return data.toString();
    }

    /**
     * Delete last comma ',' from data
     */
    private void deleteLastComma(StringBuilder data){
        if (data.charAt(data.length() - 1) == ',') data.delete(data.length() - 1, data.length());
    }

}
