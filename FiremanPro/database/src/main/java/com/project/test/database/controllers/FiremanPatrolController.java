package com.project.test.database.controllers;

import android.content.Context;

import com.project.test.database.Entities.Photos;
import com.project.test.database.Entities.fireman_patrol.Fireman;
import com.project.test.database.Entities.fireman_patrol.Fireman_patrol;
import com.project.test.database.Entities.fireman_patrol.Truck;
import com.project.test.database.Entities.fireman_patrol.Type_of_unit;
import com.project.test.database.controllers.report.Types_all_Controller;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.Date;
import java.util.List;

/**
 * kontrolira sve aktivnosti vezane za dodavanje novih vatrogasnih postrojbi i njima pripadnih vatrogasaca i vozila, te svih ostalih podataka.
 * <p>
 * <p>
 * Created by Zoran on 24.10.2017..
 * </p>
 *
 * @author Zoran Hrnčić
 */

public class FiremanPatrolController {

    Types_all_Controller types_all_controller = new Types_all_Controller();


    public FiremanPatrolController() {
    }


    /**
     * Dodavanje nove vatrogasne postrojbe tipa DVD.
     *
     * @param name naziv vatrogasne postrojbe
     * @return novookreirani zapis/objekt vatrogasne postrojbe
     */
    public Fireman_patrol addNew_DVD_FiremanPatrol(String name) {

        java.util.Date CurrentDate = new java.util.Date(System.currentTimeMillis());
        Fireman_patrol fireman_patrol = new Fireman_patrol(name, CurrentDate, CurrentDate, types_all_controller.get_DVD_type_of_unit());
        fireman_patrol.save();
        return fireman_patrol;
    }


    /**
     * Dodavanje nove vatrogasne postrojbe tipa JVP.
     *
     * @param name naziv vatrogasne postrojbe
     * @return novookreirani zapis/objekt vatrogasne postrojbe
     */
    public Fireman_patrol addNew_JVP_FiremanPatrol(String name) {

        java.util.Date CurrentDate = new java.util.Date(System.currentTimeMillis());
        Fireman_patrol fireman_patrol = new Fireman_patrol(name, CurrentDate, CurrentDate, types_all_controller.get_JVP_type_of_unit());
        fireman_patrol.save();
        return fireman_patrol;
    }

    /**
     * Dodavanje nove vatrogasne postrojbe tipa PVP.
     *
     * @param name naziv vatrogasne postrojbe
     * @return novookreirani zapis/objekt vatrogasne postrojbe
     */
    public Fireman_patrol addNew_PVP_FiremanPatrol(String name) {

        java.util.Date CurrentDate = new java.util.Date(System.currentTimeMillis());
        Fireman_patrol fireman_patrol = new Fireman_patrol(name, CurrentDate, CurrentDate, types_all_controller.get_PVP_type_of_unit());
        fireman_patrol.save();
        return fireman_patrol;
    }

    /**
     * Dodavanje nove vatrogasne postrojbe tipa DIP.
     *
     * @param name naziv vatrogasne postrojbe
     * @return novookreirani zapis/objekt vatrogasne postrojbe
     */
    public Fireman_patrol addNew_DIP_FiremanPatrol(String name) {

        java.util.Date CurrentDate = new java.util.Date(System.currentTimeMillis());
        Fireman_patrol fireman_patrol = new Fireman_patrol(name, CurrentDate, CurrentDate, types_all_controller.get_DIP_type_of_unit());
        fireman_patrol.save();
        return fireman_patrol;
    }

    /**
     * Dodavanje nove vatrogasne postrojbe tipa HV.
     *
     * @param name naziv vatrogasne postrojbe
     * @return novookreirani zapis/objekt vatrogasne postrojbe
     */
    public Fireman_patrol addNew_HV_FiremanPatrol(String name) {

        java.util.Date CurrentDate = new java.util.Date(System.currentTimeMillis());
        Fireman_patrol fireman_patrol = new Fireman_patrol(name, CurrentDate, CurrentDate, types_all_controller.get_HV_type_of_unit());
        fireman_patrol.save();
        return fireman_patrol;
    }





    /**
     * Popis svih vatrogasnih postrojbi.
     *
     * @return lista svih zapisa iz tablice Fireman_patrol
     * @see Fireman_patrol
     */
    public List<Fireman_patrol> GetAllRecordsFromTable() {


        return SQLite.select().from(Fireman_patrol.class).queryList();


    }

    /**
     * Brisanje svih zapisa u tablici Fireman Patrol, te sa njima povezanih vatrgasni vozila i vatrogasaca.
     *
     * @see Fireman_patrol
     * @see Truck
     * @see Fireman
     */
    public void DeleteAllRecordsInTable() {

        final List<Fireman_patrol> gndPlan = GetAllRecordsFromTable();
        for (int i = 0; i < gndPlan.size(); i++) {

            gndPlan.get(i).delete();
            //delete all item in table House
        }


        final List<Fireman> gndPlan1 = SQLite.select().from(Fireman.class).queryList();
        for (int i = 0; i < gndPlan1.size(); i++) {

            gndPlan1.get(i).delete();
            //delete all item in table House
        }


        final List<Truck> trucks = SQLite.select().from(Truck.class).queryList();
        for (int i = 0; i < trucks.size(); i++) {

            trucks.get(i).delete();
            //delete all item in table House
        }


    }


}
