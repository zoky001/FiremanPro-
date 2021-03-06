package com.project.test.database.controllers.report;

import com.project.test.database.Entities.fire_intervention.Spatial_spread;
import com.project.test.database.Entities.fire_intervention.Size_of_fire;
import com.project.test.database.Entities.fire_intervention.Spreading_smoke;
import com.project.test.database.Entities.fire_intervention.Time_spread;
import com.project.test.database.Entities.fireman_patrol.Fireman;
import com.project.test.database.Entities.fireman_patrol.Truck;
import com.project.test.database.Entities.fireman_patrol.Type_of_truck;
import com.project.test.database.Entities.fireman_patrol.Type_of_unit;
import com.project.test.database.Entities.report.Intervention_Type;
import com.project.test.database.Entities.report.Intervention_Type_Table;
import com.project.test.database.Entities.report.Intervention_track_Table;
import com.project.test.database.Entities.report.Outdoor_type;
import com.project.test.database.Entities.report.Sort_of_intervention;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.project.test.database.Entities.fireman_patrol.Type_of_unit_Table;

import com.project.test.database.Entities.fireman_patrol.Type_of_truck_Table;

import com.project.test.database.Entities.report.Sort_of_intervention_Table;


import java.util.List;

import com.project.test.database.Entities.Address_Table;

/**
 * Klasa kontrolira sve aktivnosti sa svim tipovima zapisa pohranjenih u bazu i korištenih tokom korištenja aplikacije.
 * <p>
 * Omogućuje upisivanj i dohvaćanje svih šifranika.
 * <p>
 * Vrste vozila, vrste požara, vrste intervencija, vrste vatrogasnih postrojbi itd.
 * <p>
 * <p>
 * Created by Zoran on 24.10.2017..
 * </p>
 *
 * @author Zoran Hrnčić
 * @see Spatial_spread
 * @see Size_of_fire
 * @see Spreading_smoke
 * @see Time_spread
 * @see Type_of_truck
 * @see Type_of_unit
 * @see Intervention_Type
 * @see Outdoor_type
 * @see Sort_of_intervention
 */

public class Types_all_Controller {

    java.util.Date CurrentDate = new java.util.Date(System.currentTimeMillis());


    public Types_all_Controller() {
    }

// Type_of_unit BEGIN

    /**
     * Dodavanje vrsta vatrogasnih postrojbi u šifranik (DVD, JVP...)
     *
     * @param id_of_type id vrste
     * @param name naziv vrste
     *
     * @return novokreirani objekt vrste/tipa
     */
    private Type_of_unit addNewFiremanPatrolUnit_Type(int id_of_type, String name) {
        Type_of_unit type_of_unit = new Type_of_unit(id_of_type, name, CurrentDate, CurrentDate);
        type_of_unit.save();

        return type_of_unit;
    }

    /**
     * Dohvaćanje objekta vrste DVD.
     *
     * @return objekt vrste/tipa DVD
     */
    public Type_of_unit get_DVD_type_of_unit() {
        List<Type_of_unit> type = SQLite.select().from(Type_of_unit.class).where(Type_of_unit_Table.name.is("DVD")).queryList();


        if (type.size() < 1) { // provjera dali postoje upisani tipovi
            Type_of_unit type_of_unit = new Type_of_unit(100, "DVD", CurrentDate, CurrentDate);
            type_of_unit.save();

            return type_of_unit;
        } else {
            return type.get(0);
        }

    }

    /**
     * Dohvaćanje objekta vrste JVP
     *
     * @return objekt vrste JVP
     */
    public Type_of_unit get_JVP_type_of_unit() {
        List<Type_of_unit> type = SQLite.select().from(Type_of_unit.class).where(Type_of_unit_Table.name.is("JVP")).queryList();


        if (type.size() < 1) { // provjera dali postoje upisani tipovi
            Type_of_unit type_of_unit = new Type_of_unit(101, "JVP", CurrentDate, CurrentDate);
            type_of_unit.save();

            return type_of_unit;
        } else {
            return type.get(0);
        }

    }

    /**
     * Dohvaćanje objekta vrste PVP
     *
     * @return objekt vrste PVP
     */
    public Type_of_unit get_PVP_type_of_unit() {
        List<Type_of_unit> type = SQLite.select().from(Type_of_unit.class).where(Type_of_unit_Table.name.is("PVP")).queryList();


        if (type.size() < 1) { // provjera dali postoje upisani tipovi
            Type_of_unit type_of_unit = new Type_of_unit(102, "PVP", CurrentDate, CurrentDate);
            type_of_unit.save();

            return type_of_unit;
        } else {
            return type.get(0);
        }

    }

    /**
     *
     * @return objekt tipa DIP
     */
    public Type_of_unit get_DIP_type_of_unit() {
        List<Type_of_unit> type = SQLite.select().from(Type_of_unit.class).where(Type_of_unit_Table.name.is("DIP")).queryList();


        if (type.size() < 1) { // provjera dali postoje upisani tipovi
            Type_of_unit type_of_unit = new Type_of_unit(103, "DIP", CurrentDate, CurrentDate);
            type_of_unit.save();

            return type_of_unit;
        } else {
            return type.get(0);
        }

    }


    /**
     *
     * @return objekt tipa HV
     *
     */
    public Type_of_unit get_HV_type_of_unit() {
        List<Type_of_unit> type = SQLite.select().from(Type_of_unit.class).where(Type_of_unit_Table.name.is("HV")).queryList();


        if (type.size() < 1) { // provjera dali postoje upisani tipovi
            Type_of_unit type_of_unit = new Type_of_unit(104, "HV", CurrentDate, CurrentDate);
            type_of_unit.save();

            return type_of_unit;
        } else {
            return type.get(0);
        }

    }


    /**
     * Metoda vraća sve zapise iz tablice Type_of_unit (vrste vatrogasnih postrojbi)
     *
     * @return popis tipova vatrogasnih postrojbi
     *
     */
    public List<Type_of_unit> GetAllRecordsFromTable_Type_of_unit() {

        return SQLite.select().from(Type_of_unit.class).queryList();


    }

    /**
     * briše sve zapise u tablici Type_of_unit (vrste vatrogasnih postrojbi)
     *
     * @see Type_of_unit
     */
    public void DeleteAllRecordsInTable_Type_of_unit() {

        final List<Type_of_unit> gndPlan = GetAllRecordsFromTable_Type_of_unit();
        for (int i = 0; i < gndPlan.size(); i++) {

            gndPlan.get(i).delete();
            //delete all item in table House
        }

    }
// Type_of_unit END

    // Type_of_truck BEGIN
    /**
     *
     * @return objekt tipa "navalno vozilo"
     */
    public Type_of_truck get_naval_vehicle_type_of_truck() {
        List<Type_of_truck> type = SQLite.select().from(Type_of_truck.class).where(Type_of_truck_Table.type_name.is("NAVAL_VEHICLE")).queryList();

        if (type.size() < 1) { // provjera dali postoje upisani tipovi
            Type_of_truck type_of_truck = new Type_of_truck(100, "NAVAL VEHICLE", CurrentDate, CurrentDate);
            type_of_truck.save();
            return type_of_truck;
        } else {
            return type.get(0);
        }
    }

    /**
     *
     * @return objekt tipa "transportno vozilo"
     */
    public Type_of_truck get_transportation_vehicle_type_of_truck() {
        List<Type_of_truck> type = SQLite.select().from(Type_of_truck.class).where(Type_of_truck_Table.type_name.is("TRANSPORTATION_VEHICLE")).queryList();


        if (type.size() < 1) { // provjera dali postoje upisani tipovi
            Type_of_truck type_of_truck = new Type_of_truck(101, "TRANSPORTATION VEHICLE", CurrentDate, CurrentDate);
            type_of_truck.save();
            return type_of_truck;
        } else {
            return type.get(0);
        }

    }

    /**
     *
     * @return objekt tipa "specijalno vozilo"
     */
    public Type_of_truck get_SPECIAL_vehicle_type_of_truck() {
        List<Type_of_truck> type = SQLite.select().from(Type_of_truck.class).where(Type_of_truck_Table.type_name.is("SPECIAL_VEHICLE")).queryList();


        if (type.size() < 1) { // provjera dali postoje upisani tipovi
            Type_of_truck type_of_truck = new Type_of_truck(102, "SPECIAL VEHICLE", CurrentDate, CurrentDate);
            type_of_truck.save();
            return type_of_truck;
        } else {
            return type.get(0);
        }

    }


    /**
     * Vraća sve zapise iz tablice "Type_of_truck"
     *
     * @return lista zapisa tipova vozila
     *
     *
     */
    public List<Type_of_truck> GetAllRecordsFromTable_Type_of_truck() {

        return SQLite.select().from(Type_of_truck.class).queryList();


    }

    /**
     * brisanje svih zapisa u tablici Type_of_truck
     *
     * @see Type_of_truck
     */
    public void DeleteAllRecordsInTable_Type_of_truck() {

        final List<Type_of_truck> gndPlan = GetAllRecordsFromTable_Type_of_truck();
        for (int i = 0; i < gndPlan.size(); i++) {

            gndPlan.get(i).delete();
            //delete all item in table House
        }

    }
// Type_of_truck END

    // Spreading_smoke BEGIN

    /**
     * dodavanje nove vrste "širenja dima" u šifranik
     *
     * @param id_of_type id vrste
     * @param name naziv vrste
     * @param description opis vrste
     *
     * @return novokreirani objekt nove vrste
     */
    public Spreading_smoke addNewSpreading_smoke_Type(int id_of_type, String name, String description) {
        Spreading_smoke spreading_smoke = new Spreading_smoke(id_of_type, name, description, CurrentDate, CurrentDate);
        spreading_smoke.save();

        return spreading_smoke;
    }


    /**
     *
     * @return svi zapisi iz tablice Spreading_smoke
     *
     * @see Spreading_smoke
     *
     */
    public List<Spreading_smoke> GetAllRecordsFromTable_Spreading_smoke() {

        return SQLite.select().from(Spreading_smoke.class).queryList();


    }

    /**
     * brisanje svih zapisa u tablici  Spreading_smoke
     *
     * @see Spreading_smoke
     */
    public void DeleteAllRecordsInTable_Spreading_smoke() {

        final List<Spreading_smoke> gndPlan = GetAllRecordsFromTable_Spreading_smoke();
        for (int i = 0; i < gndPlan.size(); i++) {

            gndPlan.get(i).delete();
            //delete all item in table House
        }

    }
// Spreading_smoke END

    // Spatial_spread BEGIN

    /**
     * Dodavanje nove vrste prostornog širenja požara u šifranik
     *
     * @see Spatial_spread
     * @param id_of_type id vrste
     * @param name naziv vrste
     * @param description opis vrste
     *
     * @return novokrirani objekt/zapis u bazi
     *
     */
    public Spatial_spread addNewSpatial_spread_Type(int id_of_type, String name, String description) {
        Spatial_spread spatial_spread = new Spatial_spread(id_of_type, name, description, CurrentDate, CurrentDate);
        spatial_spread.save();

        return spatial_spread;
    }

    /**
     *
     * @return svi zapisi iz tablice  Spatial_spread
     *
     */
    public List<Spatial_spread> GetAllRecordsFromTable_Spatial_spread() {

        return SQLite.select().from(Spatial_spread.class).queryList();


    }


    /**
     * brisanje svih zapisa u tablici Spatial_spread
     */
    public void DeleteAllRecordsInTable_Sepatial_spread() {

        final List<Spatial_spread> gndPlan = GetAllRecordsFromTable_Spatial_spread();
        for (int i = 0; i < gndPlan.size(); i++) {

            gndPlan.get(i).delete();
            //delete all item in table House
        }

    }
// Spatial_spread  END

    // Time_spread BEGIN

    /**
     * Dodavanje nove vrste vremenskog širenja požara u šifranik
     * @param id_of_type id vrste
     * @param name naziv vrste
     * @param description opis vrste
     * @return novokrirani objekt/zapis u bazi
     */
    public Time_spread addNewTime_spread_Type(int id_of_type, String name, String description) {
        Time_spread time_spread = new Time_spread(id_of_type, name, description, CurrentDate, CurrentDate);
        time_spread.save();

        return time_spread;
    }


    /**
     *
     * @return svi zapisi iz tablice  Time_spread
     */
    public List<Time_spread> GetAllRecordsFromTable_Time_spread() {

        return SQLite.select().from(Time_spread.class).queryList();


    }

    /**
     * brisanje svih zapisa u tablici Time_spread
     *
     * @see Time_spread
     */
    public void DeleteAllRecordsInTable_Time_spread() {

        final List<Time_spread> gndPlan = GetAllRecordsFromTable_Time_spread();
        for (int i = 0; i < gndPlan.size(); i++) {

            gndPlan.get(i).delete();
            //delete all item in table House
        }

    }
// Time_spread  END

    // Outdoor_type BEGIN
    public Outdoor_type addNewOutdoor_type_Type(int id_of_type, String name, String description) {
        Outdoor_type outdoor_type = new Outdoor_type(id_of_type, name, description, CurrentDate, CurrentDate);
        outdoor_type.save();

        return outdoor_type;
    }

    public List<Outdoor_type> GetAllRecordsFromTable_Outdoor_type() {

        return SQLite.select().from(Outdoor_type.class).queryList();


    }

    public void DeleteAllRecordsInTable_Outdoor_type() {

        final List<Time_spread> gndPlan = GetAllRecordsFromTable_Time_spread();
        for (int i = 0; i < gndPlan.size(); i++) {

            gndPlan.get(i).delete();
            //delete all item in table House
        }

    }
// Outdoor_type  END

    // Size_of_fire BEGIN
    public Size_of_fire addNewSize_of_fire_Type(int id_of_type, String name, String description) {
        Size_of_fire size_of_fire = new Size_of_fire(id_of_type, name, description, CurrentDate, CurrentDate);
        size_of_fire.save();

        return size_of_fire;
    }

    public List<Size_of_fire> GetAllRecordsFromTable_Size_of_fire() {

        return SQLite.select().from(Size_of_fire.class).queryList();


    }

    public void DeleteAllRecordsInTable_Size_of_fire() {

        final List<Size_of_fire> gndPlan = GetAllRecordsFromTable_Size_of_fire();
        for (int i = 0; i < gndPlan.size(); i++) {

            gndPlan.get(i).delete();
            //delete all item in table House
        }

    }
// Size_of_fire  END

    // Sort_of_intervention BEGIN
    public Sort_of_intervention addNewSort_of_intervention_Type(int id_of_type, String name, String description) {
        Sort_of_intervention sort_of_intervention = new Sort_of_intervention(id_of_type, name, description, CurrentDate, CurrentDate);
        sort_of_intervention.save();

        return sort_of_intervention;
    }



    public Sort_of_intervention get_FIRE_Sort_of_intervention(){
        List<Sort_of_intervention> type = SQLite.select().from(Sort_of_intervention.class).where(Sort_of_intervention_Table.name.is("Požar")).queryList();


        if (type.size() < 1){ // provjera dali postoje upisani tipovi
        Sort_of_intervention sort_of_intervention = new Sort_of_intervention(100,"Požar","",CurrentDate,CurrentDate);
            sort_of_intervention.save();
            return sort_of_intervention;
        } else {
            return type.get(0);
        }

    }



    public Sort_of_intervention get_TRHNICAL_Sort_of_intervention(){
        List<Sort_of_intervention> type = SQLite.select().from(Sort_of_intervention.class).where(Sort_of_intervention_Table.name.is("Tehnička intervencija")).queryList();
        if (type.size() < 1){ // provjera dali postoje upisani tipovi
            Sort_of_intervention sort_of_intervention = new Sort_of_intervention(101,"Tehnička intervencija","",CurrentDate,CurrentDate);
            sort_of_intervention.save();
            return sort_of_intervention;
        } else {
            return type.get(0);
        }

    }

    public Sort_of_intervention get_OTHER_Sort_of_intervention(){
        List<Sort_of_intervention> type = SQLite.select().from(Sort_of_intervention.class).where(Sort_of_intervention_Table.name.is("Ostalo")).queryList();


        if (type.size() < 1){ // provjera dali postoje upisani tipovi
            Sort_of_intervention sort_of_intervention = new Sort_of_intervention(102,"Ostalo","",CurrentDate,CurrentDate);
            sort_of_intervention.save();
            return sort_of_intervention;
        } else {
            return type.get(0);
        }

    }


    public List<Sort_of_intervention> GetAllRecordsFromTable_Sort_of_intervention() {

        return SQLite.select().from(Sort_of_intervention.class).queryList();


    }

    public List<Type_of_unit> GetAllRecordsFromTable_Sort_of_unit() {

        return SQLite.select().from(Type_of_unit.class).queryList();


    }

    public void DeleteAllRecordsInTable_Sort_of_intervention() {

        final List<Sort_of_intervention> gndPlan = GetAllRecordsFromTable_Sort_of_intervention();
        for (int i = 0; i < gndPlan.size(); i++) {

            gndPlan.get(i).delete();
            //delete all item in table House
        }

    }

    //all firemen
    public List<Fireman> GetAllRecordsFromTable_Fireman() {

        return SQLite.select().from(Fireman.class).queryList();


    }

    //all types of intervention
    public List<Intervention_Type> GetAllRecordsFromTable_Intervention_type() {
        return SQLite.select().from(Intervention_Type.class).queryList();
    }

    public void DeleteAllRecordsTable_Intervention_type() {

        final List<Intervention_Type> gndPlan = GetAllRecordsFromTable_Intervention_type();
        for (int i = 0; i < gndPlan.size(); i++) {

            gndPlan.get(i).delete();
            //delete all item in table House
        }

    }

    public static Intervention_Type get_Intervention_typeByName(String name) {
        return SQLite.select().from(Intervention_Type.class).where(Intervention_Type_Table.name.is(name)).querySingle();
    }


// Sort_of_intervention  END

    //all trucks
    public List<Truck> GetAllRecordsFromTable_Truck() {
        return SQLite.select().from(Truck.class).queryList();
    }


}
