package com.safetynet.safetynetalert.unit.configuration;

import com.safetynet.safetynetalert.configuration.DatabaseConfig;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

class DatabaseConfigTest {
    private DatabaseConfig databaseConfig;

    @BeforeEach
    void initTests(){
        databaseConfig = new DatabaseConfig();
    }

    @Tag("DatabaseConfigTest")
    @Test
    void openConnection() {
        JSONObject data = databaseConfig.openConnection();
        assertThat(data).isNotEmpty();

        JSONArray persons = (JSONArray) data.get("persons");
        JSONArray stations = (JSONArray) data.get("firestations");
        JSONArray medicalRecords = (JSONArray) data.get("medicalrecords");

        assertThat(persons.size()).isPositive();
        assertThat(stations.size()).isPositive();
        assertThat(medicalRecords.size()).isPositive();

        data = null;
        persons = null;
        stations = null;
        medicalRecords = null;

        data = databaseConfig.openConnection("src/main/resources/static/data.json");
        assertThat(data).isNotEmpty();

        persons = (JSONArray) data.get("persons");
        stations = (JSONArray) data.get("firestations");
        medicalRecords = (JSONArray) data.get("medicalrecords");

        assertThat(persons.size()).isPositive();
        assertThat(stations.size()).isPositive();
        assertThat(medicalRecords.size()).isPositive();

        assertThat(databaseConfig.openConnection("")).isNullOrEmpty();
    }

    @Tag("DatabaseConfigTest")
    @Test
    void getData() {
        assertThat(databaseConfig.getData()).isNullOrEmpty();
        JSONObject data = databaseConfig.openConnection();
        assertThat(databaseConfig.getData()).isNotEmpty();
        assertThat(databaseConfig.getData()).isEqualTo(data);
    }

    @Tag("DatabaseConfigTest")
    @Test
    void closeConnection() {
        assertThat(databaseConfig.getData()).isNullOrEmpty();
        assertThat(databaseConfig.openConnection()).isNotEmpty();
        databaseConfig.closeConnection();
        assertThat(databaseConfig.getData()).isNullOrEmpty();
    }

    @AfterEach
    void undefTests() {
        databaseConfig = null;
    }
}