package com.kizo.report;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.project.test.database.Entities.fire_intervention.Size_of_fire;
import com.project.test.database.Entities.fire_intervention.Spatial_spread;
import com.project.test.database.Entities.fire_intervention.Spreading_smoke;
import com.project.test.database.Entities.fire_intervention.Time_spread;
import com.project.test.database.Entities.fireman_patrol.Fireman;
import com.project.test.database.Entities.fireman_patrol.Fireman_patrol;
import com.project.test.database.Entities.fireman_patrol.Truck;
import com.project.test.database.Entities.fireman_patrol.Type_of_unit;
import com.project.test.database.Entities.report.Intervention_Type;
import com.project.test.database.Entities.report.Intervention_track;
import com.project.test.database.Entities.report.Outdoor_type;
import com.project.test.database.Entities.report.Sort_of_intervention;
import com.project.test.database.controllers.FiremanPatrolController;
import com.project.test.database.controllers.report.InterventionController;
import com.project.test.database.controllers.report.Types_all_Controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ernestoyaquello.com.verticalstepperform.VerticalStepperFormLayout;
import ernestoyaquello.com.verticalstepperform.fragments.BackConfirmationFragment;
import ernestoyaquello.com.verticalstepperform.interfaces.VerticalStepperForm;

public class NewReportFormActivity extends AppCompatActivity implements VerticalStepperForm {

    private static final String NO_SELECTED = "Odaberite..";
    Types_all_Controller types_all_controller = new Types_all_Controller();
    public static final String NEW_ALARM_ADDED = "new_alarm_added";

    // Information about the steps/fields of the form
    private static final int MAIN_INFORMATION_NUM = 0;
    private static final int USED_RESOURCES_STEP_NUM = 1;
    private static final int FIRE_STEP_NUM = 2;
    private static final int OWNER_AND_MATERIAL_STEP_NUM = 3;
    private static final int DESCRIPTION_HELPER_STEP_NUM = 4;
    //  private static final int MEHANIZATION_STEP_NUM = 5;
    private static final int INTERVENTION_STEP_NUM = 5;
    private static final int FIREMEN_NUM = 6;
    private static final int END_NUM = 7;


    // Title step
    private EditText chooseTypeAndSort;
    private static final int MIN_CHARACTERS_TITLE = 3;
    public static final String STATE_TITLE = "Osnovne informacije";

    // Description step
    private EditText descriptionEditText;
    public static final String STATE_DESCRIPTION = "description";


    private List<Integer> firemans_id_selected = new ArrayList<Integer>();
    private boolean confirmBack = true;
    private ProgressDialog progressDialog;
    private VerticalStepperFormLayout verticalStepperForm;

    Spinner spinnerType, spinnerSort;
    int userSelectedIndex;

    Spinner spinnerVehicle;
    EditText surfaceNumber;
    EditText superficiesNumber;
    EditText kmNumber;
    EditText clockNumber;
    EditText waterNumber;
    EditText foamNumber;
    EditText powderNumber;
    EditText co2Number;
    EditText numberOfFiremanParticipated;


    EditText navalVehicleNumber;
    EditText commandVehicleNumber;
    EditText tehnicalVehicleNumber;
    EditText automaticLadderNumber;
    EditText roadTankersNumber;
    EditText specialVehicleNumber;
    EditText transportationVehicleNumber;
    EditText insuranceVehicleNumber;
    EditText powerPumpVehicleNumber;
    EditText apsorbentVehicleNumber;

    Spinner firemanSpinner;
    EditText numberOfFiremanNumber;

    String sviOdabranivatrogasci = "";
    int brojac = 0;

    LinearLayout mehanizationContent;

    Spinner spinnerFiremanPatrol;

    Spinner usedTruck;

    //fire
    Spinner sizeOfFire;
    EditText destroyedSpace;
    Spinner repeatedSpinner;
    Spinner spatialSpread;
    Spinner timeSpread;
    Spinner smokeSpread;
    Spinner outdoorSpread;
    private Intervention_track intervencije;
    private EditText interventionDescription;

    /* dio koji se popunjava u USEDRESOURCES a koristi se i kod interventionCosta
     */

    String kmText;
    String waterText;
    String powderText;
    String foamText;
    String co2Text;
    String numberOfFiremansText;
    String clockText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_stepper_report_form);


        try {
            int a = Integer.parseInt(getIntent().getStringExtra("IDintervencije"));
            intervencije = InterventionController.getInterventionByID(a);

        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }


        System.out.println("SESSION FRAGMENT_idkuce: " + intervencije.getHouse().getName_owner());
        initializeActivity();
    }

    private void initializeActivity() {
        // Vertical Stepper form vars
        int colorPrimary = ContextCompat.getColor(getApplicationContext(), R.color.glavna);
        int colorPrimaryDark = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDarkREPORT);

        String[] stepsTitles = getResources().getStringArray(R.array.steps_titles);
        // String[] stepsSubtitles = getResources().getStringArray(R.array.steps_subtitles);

        // Here we find and initialize the form
        verticalStepperForm = (VerticalStepperFormLayout) findViewById(R.id.vertical_stepper_form);
        VerticalStepperFormLayout.Builder.newInstance(verticalStepperForm, stepsTitles, this, this)
                //.stepsSubtitles(stepsSubtitles)
                .materialDesignInDisabledSteps(false)// false by default
                .showVerticalLineWhenStepsAreCollapsed(true) // false by default
                .primaryColor(colorPrimary)
                // .primaryDarkColor(colorPrimaryDark)
                .displayBottomNavigation(true)
                .init();
    }

    // METHODS THAT HAVE TO BE IMPLEMENTED TO MAKE THE LIBRARY WORK
    // (Implementation of the interface "VerticalStepperForm")

    @Override
    public View createStepContentView(int stepNumber) {
        // Here we generate the content view of the correspondent step and we return it so it gets
        // automatically added to the step layout (AKA stepContent)

        View view = null;
        switch (stepNumber) {
            case MAIN_INFORMATION_NUM:
                // view = createAlarmTitleStep();
                view = createTypeAndSortStep();
                break;
            case USED_RESOURCES_STEP_NUM:
                view = createUsedResourcesStep();
                break;
            case FIRE_STEP_NUM:
                view = createFireStep();
                break;
            case OWNER_AND_MATERIAL_STEP_NUM:
                // view = createUsedResources();
                view = createOwnerAndMaterialCostStep();
                break;
            case DESCRIPTION_HELPER_STEP_NUM:
                view = createDescriptionStep();
                break;
            /*
            case MEHANIZATION_STEP_NUM:
              view = createMehanizationStep();
               break;
            */
            case INTERVENTION_STEP_NUM:
                view = createInterventionCostStep();
                break;
            case FIREMEN_NUM:
                view = createFiremenStep();
        }
        return view;
    }

    @Override
    public void onStepOpening(int stepNumber) {
        switch (stepNumber) {
            case MAIN_INFORMATION_NUM:
                break;
            case USED_RESOURCES_STEP_NUM:
                save_MAIN_INFORMATION();
                break;

            case FIRE_STEP_NUM:
                save_USED_RESOURCES();
                break;
            case OWNER_AND_MATERIAL_STEP_NUM:
                save_FIRE_STEP();
                validate_OWNER_AND_MATERIAL_COST();
                break;
            case DESCRIPTION_HELPER_STEP_NUM:
                save_OWNER_AND_MATERIAL_COST();
                validate_DESCRIPTION_STEP_HELPER();
                System.out.println("surface: " + intervencije.getReports().getSurface_m2());
                //verticalStepperForm.setStepAsCompleted(stepNumber);
                break;
        /*   case MEHANIZATION_STEP_NUM:
                save__DESCRIPTION_STEP_HELPER();
                verticalStepperForm.setStepAsCompleted(stepNumber);
                break;
                */
            case INTERVENTION_STEP_NUM:
                save__DESCRIPTION_STEP_HELPER();
                validate_INTERVENTION_COST();
                // verticalStepperForm.setStepAsCompleted(stepNumber);
                break;
            case FIREMEN_NUM:
                save_INTERVENTION_COST();
                verticalStepperForm.setStepAsCompleted(stepNumber);
                break;
            case END_NUM:
                sendMail();
                verticalStepperForm.setStepAsCompleted(stepNumber);
                break;
        }
    }

    private void sendMail() {
        /* određujemo naslov maila tj subject */
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String currentDateandTime = sdf.format(new Date());
        String subjectText = "Intervencija na dan: " + currentDateandTime;
        String bodyText = "Intervencija na dan: " + currentDateandTime + " vezan za adresu: "
                + intervencije.getHouse().getAddress().getStreetNameIfExist().toString()
                + " " + intervencije.getHouse().getAddress().getStreetNumber().toString() + ", " + intervencije.getHouse().getAddress().getPlaceNameIfExist().toString()
                + " \n  \n" + "U privitku se nalazi izvještaj. ";

        /* slanje maila */
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

       // emailIntent.setType("text/plain");
       // emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{// intervencije.getEmailTo().toString()

        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {// intervencije.getEmailTo().toString()

                "matea.bodulusic@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subjectText);
        emailIntent.putExtra(Intent.EXTRA_TEXT, bodyText);


        /*
        File root = Environment.getExternalStorageDirectory();

        String pathToMyAttachedFile = "temp/attachement.xml";
        File file = new File(root, pathToMyAttachedFile);
        if (!file.exists() || !file.canRead()) {
            return;
        }
        Uri uri = Uri.fromFile(file);

        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        */
        startActivity(Intent.createChooser(emailIntent, "Odaberite email providera: "));
    }

    @Override
    public void sendData() {
        System.out.println("SEND DATA");
//ovo je samo za probu, treba obrisati START
        for (Integer id :
                firemans_id_selected) {

            intervencije.getReports().addFiremanToIntervention(Fireman.getFiremanbyID(id));

        }
        intervencije.getReports().addFiremanSignedToIntervention(Fireman.getRandomType());
        intervencije.completeInterventionTrack();
//sve do ovdje START

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.show();
        progressDialog.setMessage(getString(R.string.vertical_form_stepper_form_sending_data_message));
        executeDataSending();
    }

    // OTHER METHODS USED TO MAKE THIS EXAMPLE WORK

    private void executeDataSending() {

        // TODO Use here the data of the form as you wish

        // Fake data sending effect
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    intent.putExtra(NEW_ALARM_ADDED, true);
                    intent.putExtra(STATE_TITLE, chooseTypeAndSort.getText().toString());
                    intent.putExtra(STATE_DESCRIPTION, descriptionEditText.getText().toString());

                    // You must set confirmBack to false before calling finish() to avoid the confirmation dialog
                    confirmBack = false;
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start(); // You should delete this code and add yours

    }

    private View createFireStep() {
        chooseTypeAndSort = new EditText(this);
        // titleEditText.setHint(R.string.form_hint_title);
        // titleEditText.setSingleLine(true);

        LayoutInflater inflate = LayoutInflater.from(getBaseContext());
        final LinearLayout fireContent = (LinearLayout) inflate.inflate(R.layout.step_fire, null, false);

        sizeOfFire = addSpinnerValue(sizeOfFire, fireContent, R.id.size_of_fire, getSizeOfFireAdapter());

        repeatedSpinner = addSpinnerValue_listener_FIRE_STEP(repeatedSpinner, fireContent, R.id.repeated, getYesNo());
        spatialSpread = addSpinnerValue_listener_FIRE_STEP(spatialSpread, fireContent, R.id.spatial_spread, getSpatialSpreadAdapter());
        timeSpread = addSpinnerValue_listener_FIRE_STEP(timeSpread, fireContent, R.id.time_spread, getTimeSpreadAdapter());
        smokeSpread = addSpinnerValue_listener_FIRE_STEP(smokeSpread, fireContent, R.id.smoke_spread, getSmokeSpreadAdapter());
        outdoorSpread = addSpinnerValue_listener_FIRE_STEP(outdoorSpread, fireContent, R.id.outdoor_spread, getOutdoorSpreadAdapter());

        destroyedSpace = (EditText) fireContent.findViewById(R.id.destroyed_space);
        numberKeybord(destroyedSpace);
        destroyedSpace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validate_FIRE_STEP();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return fireContent;
    }

    // -- need to add validation for spinners !!!
    private boolean validate_FIRE_STEP() {
        boolean isCorrect = false;
        System.out.println("validateFIRE");
        String destroyed = destroyedSpace.getText().toString();

        if (destroyed.length() > 0 & validSpinner(sizeOfFire) & validSpinner(repeatedSpinner) & validSpinner(spatialSpread) & validSpinner(timeSpread) & validSpinner(smokeSpread) & validSpinner(outdoorSpread)) {
            isCorrect = true;
            verticalStepperForm.setActiveStepAsCompleted();

        } else {
            String titleErrorString = "Niste popunili sve podatke!";
            verticalStepperForm.setActiveStepAsUncompleted(titleErrorString);
        }

        return isCorrect;
    }

    private boolean validSpinner(Spinner spinner) {
        if (spinner == null || spinner.getSelectedItem().toString().equals(NO_SELECTED)) {
            return false;
        } else {
            return true;
        }

    }


    private void save_FIRE_STEP() {
        java.util.Date localzationTime = new java.util.Date(System.currentTimeMillis());
        java.util.Date fire_extinguished_time = new java.util.Date(System.currentTimeMillis());
        if (validate_FIRE_STEP()) {
            System.out.println("SAVE FIRE STEP");


            intervencije.getReports().addFireInterventionDetails(
                    localzationTime,
                    fire_extinguished_time,
                    Integer.parseInt(destroyedSpace.getText().toString()),
                    repeatedSpinner.getSelectedItem().toString().equals("DA") ? true : false,
                    Spreading_smoke.getByName(smokeSpread.getSelectedItem().toString()),
                    Spatial_spread.getByName(spatialSpread.getSelectedItem().toString()),
                    Time_spread.getByName(timeSpread.getSelectedItem().toString()),
                    Outdoor_type.getByName(outdoorSpread.getSelectedItem().toString()),
                    Size_of_fire.getByName(sizeOfFire.getSelectedItem().toString())
            );

            System.out.println("SAVE FIRE STEP" + intervencije.getReports().getFireInterventionDetails().getSpreading_smoke().getName());
            // insert in database
        }
    }

    private ArrayAdapter<String> getOutdoorSpreadAdapter() {
        List<String> typeAll = new ArrayList<String>();
        Types_all_Controller type_all_controller = new Types_all_Controller();
        List<Outdoor_type> all_size_of_fire = type_all_controller.GetAllRecordsFromTable_Outdoor_type();
        typeAll.add(NO_SELECTED);

        for (Outdoor_type t : all_size_of_fire) {
            typeAll.add(t.getName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeAll);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        return dataAdapter;
    }

    private ArrayAdapter<String> getSmokeSpreadAdapter() {
        List<String> typeAll = new ArrayList<String>();
        Types_all_Controller type_all_controller = new Types_all_Controller();
        List<Spreading_smoke> all_size_of_fire = type_all_controller.GetAllRecordsFromTable_Spreading_smoke();
        typeAll.add(NO_SELECTED);

        for (Spreading_smoke t : all_size_of_fire) {
            typeAll.add(t.getName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeAll);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        return dataAdapter;
    }

    private ArrayAdapter<String> getTimeSpreadAdapter() {
        List<String> typeAll = new ArrayList<String>();
        Types_all_Controller type_all_controller = new Types_all_Controller();
        List<Time_spread> all_size_of_fire = type_all_controller.GetAllRecordsFromTable_Time_spread();
        typeAll.add(NO_SELECTED);

        for (Time_spread t : all_size_of_fire) {
            typeAll.add(t.getName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeAll);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        return dataAdapter;
    }

    private ArrayAdapter<String> getSpatialSpreadAdapter() {
        List<String> typeAll = new ArrayList<String>();
        Types_all_Controller type_all_controller = new Types_all_Controller();
        List<Spatial_spread> all_size_of_fire = type_all_controller.GetAllRecordsFromTable_Spatial_spread();
        typeAll.add(NO_SELECTED);
        for (Spatial_spread s : all_size_of_fire) {
            typeAll.add(s.getName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeAll);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        return dataAdapter;
    }


    private ArrayAdapter<String> getSizeOfFireAdapter() {
        List<String> typeAll = new ArrayList<String>();
        Types_all_Controller type_all_controller = new Types_all_Controller();
        List<Size_of_fire> all_size_of_fire = type_all_controller.GetAllRecordsFromTable_Size_of_fire();
        typeAll.add(NO_SELECTED);
        for (Size_of_fire s : all_size_of_fire) {
            typeAll.add(s.getName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeAll);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        return dataAdapter;
    }

    private ArrayAdapter<String> getYesNo() {
        List<String> typeAll = new ArrayList<String>();

        typeAll.add(NO_SELECTED);
        typeAll.add("DA");
        typeAll.add("NE");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeAll);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        return dataAdapter;
    }

    private Spinner addSpinnerValue(Spinner spinner, LinearLayout content, int id, ArrayAdapter<String> methodArray) {
        spinner = (Spinner) content.findViewById(id);
        spinner.setAdapter(methodArray);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                Object item = parentView.getItemAtPosition(position);
                System.out.println("SPINNER_size of fire " + item.toString());
                validate_FIRE_STEP();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                spinnerType.setVisibility(View.INVISIBLE);
                // your code here
            }
        });
        return spinner;
    }

    private Spinner addSpinnerValue_listener_FIRE_STEP(Spinner spinner, LinearLayout content, int id, ArrayAdapter<String> methodArray) {
        spinner = (Spinner) content.findViewById(id);
        spinner.setAdapter(methodArray);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                Object item = parentView.getItemAtPosition(position);
                System.out.println("SPINNER_size of fire " + item.toString());
                validate_FIRE_STEP();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                spinnerType.setVisibility(View.INVISIBLE);
                // your code here
            }
        });
        return spinner;
    }


    private View createTypeAndSortStep() {
        chooseTypeAndSort = new EditText(this);
        // titleEditText.setHint(R.string.form_hint_title);
        // titleEditText.setSingleLine(true);
        // verticalStepperForm.setActiveStepAsUncompleted("Potrebno je upisani opis intervencije");
        LayoutInflater inflate = LayoutInflater.from(getBaseContext());
        final LinearLayout typeAndSortContent = (LinearLayout) inflate.inflate(R.layout.type_and_sort_of_intervention, null, false);

        spinnerSort = (Spinner) typeAndSortContent.findViewById(R.id.sort_of_intervention);
        spinnerSort.setAdapter(getSortOfInterventionAdapter());


        interventionDescription = (EditText) typeAndSortContent.findViewById(R.id.description);


        interventionDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("AFTER");
                validate_MAIN_INFORMATIONA();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
/*
        spinnerType = (Spinner) typeAndSortContent.findViewById(R.id.type_of_intervention);
        spinnerType.setAdapter(getTypeOfInterventionAdapter());
*/
        spinnerType = (Spinner) typeAndSortContent.findViewById(R.id.type_of_intervention);
        spinnerType.setVisibility(View.INVISIBLE);

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                Object item = parentView.getItemAtPosition(position);
                System.out.println("SPINNER" + item.toString());


                spinnerType.setAdapter(getTypeOfInterventionAdapter(item.toString()));
                spinnerType.setVisibility(View.VISIBLE);

                validate_MAIN_INFORMATIONA();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                spinnerType.setVisibility(View.INVISIBLE);
                // your code here
            }
        });

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                validate_MAIN_INFORMATIONA();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return typeAndSortContent;
    }


    private boolean validate_MAIN_INFORMATIONA() {
        boolean titleIsCorrect = false;
        String title = interventionDescription.getText().toString();

        if (title.length() > 3 && !spinnerSort.getSelectedItem().toString().equals(NO_SELECTED) && !spinnerType.getSelectedItem().toString().equals(NO_SELECTED)) {
            titleIsCorrect = true;

            verticalStepperForm.setActiveStepAsCompleted();
            // Equivalent to: verticalStepperForm.setStepAsCompleted(TITLE_STEP_NUM);

        } else {
            String titleErrorString = "Potrebno je upisati opis intervencije  i odabrati vrstu i tip intervencije";
            verticalStepperForm.setActiveStepAsUncompleted(titleErrorString);
            // Equivalent to: verticalStepperForm.setStepAsUncompleted(TITLE_STEP_NUM, titleError);

        }

        return titleIsCorrect;
    }

    private void save_MAIN_INFORMATION() {
        if (validate_MAIN_INFORMATIONA()) {

            String title = interventionDescription.getText().toString();
            // insert in database
            intervencije.addDescriptionOfIntervention(title);

            if (spinnerSort.getSelectedItem().toString().equals(types_all_controller.get_FIRE_Sort_of_intervention().getName())) {

                intervencije.setThisInterventionAsFire();
                intervencije.getReports().addFireIntervention(Types_all_Controller.get_Intervention_typeByName(spinnerType.getSelectedItem().toString()));
                System.out.println("SAve first step");
            }
            if (spinnerSort.getSelectedItem().toString().equals(types_all_controller.get_TRHNICAL_Sort_of_intervention().getName())) {

                intervencije.setThisInterventionAsTehnical();
                intervencije.getReports().addTehnicalInterventionDetails(Types_all_Controller.get_Intervention_typeByName(spinnerType.getSelectedItem().toString()));
                System.out.println("SAve first step");
            }
            if (spinnerSort.getSelectedItem().toString().equals(types_all_controller.get_OTHER_Sort_of_intervention().getName())) {

                intervencije.setThisInterventionAsOther();
                intervencije.getReports().addOtherInterventionDetails(Types_all_Controller.get_Intervention_typeByName(spinnerType.getSelectedItem().toString()));
                System.out.println("SAve first step");
            }
        }
    }

    private ArrayAdapter<String> getTypeOfInterventionAdapter(String sortName) {
        List<String> typeAll = new ArrayList<String>();
        Types_all_Controller type_all_controller = new Types_all_Controller();
        List<Intervention_Type> type_of_intervention = type_all_controller.GetAllRecordsFromTable_Intervention_type();

        typeAll.add(NO_SELECTED); // first item, "unselected"
        for (Intervention_Type i : type_of_intervention) {

            if (i.getSort_of_intervention().getName().equals(sortName)) {
                typeAll.add(i.getName());
            }
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeAll);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        return dataAdapter;
    }

    private ArrayAdapter<String> getSortOfInterventionAdapter() {
        List<String> sortAll = new ArrayList<String>();
        Types_all_Controller type_all_controller = new Types_all_Controller();
        List<Sort_of_intervention> sort_of_intervention = type_all_controller.GetAllRecordsFromTable_Sort_of_intervention();
        sortAll.add(NO_SELECTED); // first item, "unselected"
        for (Sort_of_intervention s : sort_of_intervention) {
            sortAll.add(s.getName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sortAll);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        return dataAdapter;
    }

    private View createUsedResourcesStep() {
        chooseTypeAndSort = new EditText(this);
        // titleEditText.setHint(R.string.form_hint_title);
        // titleEditText.setSingleLine(true);
        LayoutInflater inflate = LayoutInflater.from(getBaseContext());
        final LinearLayout v = (LinearLayout) inflate.inflate(R.layout.step_used_resources, null, false);
        final LinearLayout ll = v;

        /* Button koji omogućuje doavanje još resursa jer se tako traži u službenom izvještaju */

        final Button prvi = new Button(this);
        prvi.setText("Dodaj resurs");

        //addUsedResources(prvi, vehicleContent, vehicleContent);


// pocetak



        // pocetak - prvi prikaz za odabir resursa

        //spinnerVehicle.setVisibility(View.INVISIBLE);
        spinnerFiremanPatrol = (Spinner) v.findViewById(R.id.sort_of_unit);
        spinnerFiremanPatrol.setAdapter(getFiremanPatrols());


        spinnerFiremanPatrol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!parent.getSelectedItem().toString().equals(NO_SELECTED)) {
                    spinnerVehicle = addSpinnerValue_listener_USED_RESOURCES_STEP(spinnerVehicle, v, R.id.vehicle, getVehicleAdapter(Fireman_patrol.getPatrolByName(parent.getSelectedItem().toString())));
                } else {
                    spinnerVehicle = addSpinnerValue_listener_USED_RESOURCES_STEP(spinnerVehicle, v, R.id.vehicle, getVehicleAdapter(Fireman_patrol.getPatrolByName(parent.getSelectedItem().toString())));

                }
                validate_USED_RESOURCES();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        //numberKeybor omogućava upisa samo brojeva i to je realizirano kroz metodu

        kmNumber = addTextChangeListenerWithValidation(v,R.id.km);//v.findViewById(R.id.km);// addTextChangeListenerWithValidation (kmNumber, v, R.id.km);

        numberKeybord(kmNumber);


        clockNumber = addTextChangeListenerWithValidation(v, R.id.clock);
        numberKeybord(clockNumber);


        numberOfFiremanParticipated = addTextChangeListenerWithValidation(v, R.id.number_of_firemen_in_truck);
        numberKeybord(numberOfFiremanParticipated);


        waterNumber = addTextChangeListenerWithValidation(v, R.id.water);
        numberKeybord(waterNumber);


        foamNumber = addTextChangeListenerWithValidation(v, R.id.foam);
        numberKeybord(foamNumber);


        powderNumber = addTextChangeListenerWithValidation(v, R.id.powder);
        numberKeybord(powderNumber);


        co2Number = addTextChangeListenerWithValidation(v, R.id.CO_2);
        numberKeybord(co2Number);


        ll.addView(prvi);


        LayoutInflater factory = LayoutInflater.from(this);
        final View myView = factory.inflate(R.layout.step_used_resources, null);

        Button b = new Button(this);
        b.setText("Dodaj resurs");


        addNewUsedResources(prvi, b, ll, myView);
//kraj
        return v;
    }


    private void addUsedResources(Button prvi, final View v, final LinearLayout ll) {

        LayoutInflater factory = LayoutInflater.from(this);
        final View myView = factory.inflate(R.layout.step_used_resources, null);


        //spinnerVehicle.setVisibility(View.INVISIBLE);
        spinnerFiremanPatrol = (Spinner) v.findViewById(R.id.sort_of_unit);
        spinnerFiremanPatrol.setAdapter(getFiremanPatrols());

        spinnerFiremanPatrol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


             if (!parent.getSelectedItem().toString().equals(NO_SELECTED)) {
                 spinnerVehicle = addSpinnerValue_listener_USED_RESOURCES_STEP(spinnerVehicle, v, R.id.vehicle, getVehicleAdapter(Fireman_patrol.getPatrolByName(parent.getSelectedItem().toString())));
             }
             else {
                 spinnerVehicle = addSpinnerValue_listener_USED_RESOURCES_STEP(spinnerVehicle, v, R.id.vehicle, getVehicleAdapter(Fireman_patrol.getPatrolByName(parent.getSelectedItem().toString())));


                }
                validate_USED_RESOURCES();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        kmNumber = addTextChangeListenerWithValidation(v, R.id.km);//v.findViewById(R.id.km);// addTextChangeListenerWithValidation (kmNumber, v, R.id.km);
        numberKeybord(kmNumber);


        clockNumber = addTextChangeListenerWithValidation(v, R.id.clock);
        numberKeybord(clockNumber);


        numberOfFiremanParticipated = addTextChangeListenerWithValidation(v, R.id.number_of_firemen_in_truck);
        numberKeybord(numberOfFiremanParticipated);


        waterNumber = addTextChangeListenerWithValidation(v, R.id.water);
        numberKeybord(waterNumber);


        foamNumber = addTextChangeListenerWithValidation(v, R.id.foam);
        numberKeybord(foamNumber);


        powderNumber = addTextChangeListenerWithValidation(v, R.id.powder);
        numberKeybord(powderNumber);


        co2Number = addTextChangeListenerWithValidation(v, R.id.CO_2);
        numberKeybord(co2Number);


        ll.addView(prvi);

        LayoutInflater inflate = LayoutInflater.from(getBaseContext());
        final LinearLayout addedVehicleContent = (LinearLayout) inflate.inflate(R.layout.step_used_resources, null, false);


        Button b = new Button(this);
        b.setText("Dodaj resurs");
        addNewUsedResources(prvi, b, ll, myView);
    }

    private boolean validate_USED_RESOURCES() {
        boolean isCorrect = false;
        System.out.println("validateUSED_RESOURCES");


        if (validSpinner(spinnerVehicle) & validSpinner(spinnerFiremanPatrol) & isValidEditbox(co2Number) & isValidEditbox(powderNumber) & isValidEditbox(foamNumber) & isValidEditbox(waterNumber) & isValidEditbox(numberOfFiremanParticipated) & isValidEditbox(clockNumber) & isValidEditbox(kmNumber)) {
            isCorrect = true;
            System.out.println("validateUSED_RESOURCES_CORRECT");
            verticalStepperForm.setActiveStepAsCompleted();
        } else {
            try {
                String titleErrorString = "Niste popunili sve podatke!";
                verticalStepperForm.setActiveStepAsUncompleted(titleErrorString);
                verticalStepperForm.setStepAsUncompleted(USED_RESOURCES_STEP_NUM, titleErrorString);
            } catch (Exception e) {
                System.out.println("GREŠKA: " + e);

            }
        }

        return isCorrect;
    }

    private boolean isValidEditbox(EditText editText) {

        if (editText == null || editText.getText().toString().isEmpty())
            return false;
        else
            return true;
    }


    private Spinner addSpinnerValue_listener_USED_RESOURCES_STEP(Spinner spinner, View content, int id, ArrayAdapter<String> methodArray) {
        spinner = (Spinner) content.findViewById(id);
        spinner.setAdapter(methodArray);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                Object item = parentView.getItemAtPosition(position);
                System.out.println("SPINNER_size of fire " + item.toString());
                validate_USED_RESOURCES();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                spinnerType.setVisibility(View.INVISIBLE);
                // your code here
            }
        });
        return spinner;
    }

    private EditText addTextChangeListenerWithValidation(View view, int id) {

        EditText editText = (EditText) view.findViewById(id);
        // editText.setText("0");
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validate_USED_RESOURCES();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return editText;
    }

/*
    private boolean validate_USED_RESOURCES() {
        boolean correctInformation = true; //all together

        boolean correct = true;
        if(spinnerVehicle.getSelectedItem().toString().equals(NO_SELECTED)) {
            String titleErrorVehicle = "Potrebno je odabrati vozilo!";
            verticalStepperForm.setActiveStepAsUncompleted(titleErrorVehicle);
            correct = false;
        }
        if (spinnerFiremanPatrol.getSelectedItem().toString().equals(NO_SELECTED)) {
            String titleErrorUnit = "Potrebno je odabrati vrstu i naziv postrojbe!";
            verticalStepperForm.setActiveStepAsUncompleted(titleErrorUnit);
            correct = false;
        }
        if(kmNumber.getText().toString().length() == 0){
            String titleErrorKm = "Potrebno je unjeti broj kilometara!";
            verticalStepperForm.setActiveStepAsUncompleted(titleErrorKm);
            correct = false;
        }
        if(clockNumber.getText().toString().length() == 0){
            String titleErrorClock = "Potrebno je unjeti broj sati!";
            verticalStepperForm.setActiveStepAsUncompleted(titleErrorClock);
            correct = false;
        }
        if(numberOfFiremanParticipated.getText().toString().length() == 0){
            String titleErrorFiremans = "Potrebno je unjeti broj vatrogasaca!";
            verticalStepperForm.setActiveStepAsUncompleted(titleErrorFiremans);
            correct = false;
        }
        if(waterNumber.getText().toString().length() == 0){
            String titleErrorWater = "Potrebno je unjeti količinu vode!";
            verticalStepperForm.setActiveStepAsUncompleted(titleErrorWater);
            correct = false;
        }
        if(foamNumber.getText().toString().length() == 0){
            String titleErrorFoam = "Potrebno je unjeti količinu pjenila!";
            verticalStepperForm.setActiveStepAsUncompleted(titleErrorFoam);
            correct = false;
        }
        if(powderNumber.getText().toString().length() == 0){
            String titleErrorPowder = "Potrebno je unjeti količinu praha!";
            verticalStepperForm.setActiveStepAsUncompleted(titleErrorPowder);
            correct = false;
        }
        if(co2Number.getText().toString().length() == 0){
            String titleErrorco2 = "Potrebno je unjeti količinu co2!";
            verticalStepperForm.setActiveStepAsUncompleted(titleErrorco2);
            correct = false;
        }

        if(correct == true ){
            verticalStepperForm.setActiveStepAsCompleted();
        } else{
            verticalStepperForm.setActiveStepAsUncompleted("Niste unjeli sve podatke!");
        }

        return correctInformation;
    }*/


    public void save_USED_RESOURCES() {
        if(validate_USED_RESOURCES()) {
            kmText =kmNumber.getText().toString();
            waterText = waterNumber.getText().toString();
            powderText = powderNumber.getText().toString();
            foamText = foamNumber.getText().toString();
            co2Text = co2Number.getText().toString();
            numberOfFiremansText = numberOfFiremanParticipated.getText().toString();
            clockText = clockNumber.getText().toString();
            Fireman_patrol patrol = Fireman_patrol.getPatrolByName(spinnerFiremanPatrol.getSelectedItem().toString());
            Truck truck = patrol.getTruckByName(spinnerVehicle.getSelectedItem().toString());


            // insert in database
            intervencije.getReports().addFiremanPatrolandTruck(Integer.parseInt(numberOfFiremansText),
                    Double.parseDouble(waterText),
                    Double.parseDouble(foamText), Double.parseDouble(powderText),
                    Double.parseDouble(co2Text), Double.parseDouble(kmText),
                    Double.parseDouble(clockText),
                    truck,
                    patrol
            );
        }

        System.out.println("SAVE USED RESOURCE " + intervencije.getReports().getTrucksAndPatrols().get(0).getFireman_patrol().getName());
    }

    private void addNewUsedResources(final Button prvi, final Button noviB, final LinearLayout ll, final View myView) {
        prvi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                save_USED_RESOURCES();
                ll.removeView(prvi);
                ll.addView(myView);
                addUsedResources(noviB, myView, ll);
            }
        });
    }

    private ArrayAdapter<String> getFiremanPatrols() {
        List<String> typeAll = new ArrayList<String>();
        Types_all_Controller type_all_controller = new Types_all_Controller();
        FiremanPatrolController firemanPatrol = new FiremanPatrolController();
        List<Fireman_patrol> type_of_intervention = firemanPatrol.GetAllRecordsFromTable();
        typeAll.add(NO_SELECTED);
        for (Fireman_patrol i : type_of_intervention) {
            typeAll.add(i.getName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeAll);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        return dataAdapter;
    }

    private ArrayAdapter<String> getVehicleAdapter(Fireman_patrol patrol) {
        List<String> vehicleAll = new ArrayList<String>();
        Types_all_Controller type_all_controller = new Types_all_Controller();
        vehicleAll.add(NO_SELECTED);
        if (patrol != null) {
            List<Truck> type_of_truck = patrol.getAllTrucks();

            for (Truck t : type_of_truck) {
                vehicleAll.add(t.getName());
            }
        }
        final ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, vehicleAll);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);

        return dataAdapter2;
    }

    private void numberKeybord(EditText et) {
        et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        et.setTransformationMethod(new NumericKeyBoardTransformationMethod());
    }

    private class NumericKeyBoardTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return source;
        }
    }

    private View createOwnerAndMaterialCostStep() {
        chooseTypeAndSort = new EditText(this);
        // titleEditText.setHint(R.string.form_hint_title);
        // titleEditText.setSingleLine(true);

        LayoutInflater inflate = LayoutInflater.from(getBaseContext());
        LinearLayout ownerAndCostContent = (LinearLayout) inflate.inflate(R.layout.step_owner_and_cost, null, false);

        surfaceNumber = (EditText) ownerAndCostContent.findViewById(R.id.surface);
        numberKeybord(surfaceNumber);

        surfaceNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validate_OWNER_AND_MATERIAL_COST();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        superficiesNumber = (EditText) ownerAndCostContent.findViewById(R.id.superficies);
        numberKeybord(superficiesNumber);
        superficiesNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validate_OWNER_AND_MATERIAL_COST();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return ownerAndCostContent;
    }


    private boolean validate_OWNER_AND_MATERIAL_COST() {
        boolean isCorrect = false;
        String surface = surfaceNumber.getText().toString();
        String superficies = superficiesNumber.getText().toString();

        if (surface.length() > 0 && superficies.length() > 0) {
            isCorrect = true;
            verticalStepperForm.setActiveStepAsCompleted();

        } else {
            String titleErrorString = "Potrebno je upisati površinu objekata i vanjskog prostora!";
            verticalStepperForm.setActiveStepAsUncompleted(titleErrorString);
        }

        return isCorrect;
    }

    private void save_OWNER_AND_MATERIAL_COST() {
        if (validate_OWNER_AND_MATERIAL_COST()) {

            String surface = surfaceNumber.getText().toString();
            String superficies = superficiesNumber.getText().toString();
            // insert in database
            intervencije.addObjectSuperficies_ha(Double.parseDouble(superficies));
            intervencije.addObjectSurface_m2(Double.parseDouble(surface));
        }
    }

    private View createInterventionCostStep() {
        chooseTypeAndSort = new EditText(this);
        // titleEditText.setHint(R.string.form_hint_title);
        // titleEditText.setSingleLine(true);

        LayoutInflater inflate = LayoutInflater.from(getBaseContext());
        LinearLayout interventionCostContent = (LinearLayout) inflate.inflate(R.layout.step_intervention_cost, null, false);



        navalVehicleNumber = (EditText) interventionCostContent.findViewById(R.id.navalVehicle);
        numberKeybord(navalVehicleNumber);
        navalVehicleNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validate_INTERVENTION_COST();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        commandVehicleNumber = (EditText) interventionCostContent.findViewById(R.id.commandVehicle);
        numberKeybord(commandVehicleNumber);
        commandVehicleNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validate_INTERVENTION_COST();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tehnicalVehicleNumber = (EditText) interventionCostContent.findViewById(R.id.tehnicalVehicle);
        numberKeybord(tehnicalVehicleNumber);
        tehnicalVehicleNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validate_INTERVENTION_COST();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        automaticLadderNumber = (EditText) interventionCostContent.findViewById(R.id.automaticLadder);
        numberKeybord(automaticLadderNumber);
        automaticLadderNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validate_INTERVENTION_COST();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        roadTankersNumber = (EditText) interventionCostContent.findViewById(R.id.roadTankers);
        numberKeybord(roadTankersNumber);
        roadTankersNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validate_INTERVENTION_COST();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        specialVehicleNumber = (EditText) interventionCostContent.findViewById(R.id.specialVehicle);
        numberKeybord(specialVehicleNumber);
        specialVehicleNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validate_INTERVENTION_COST();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        transportationVehicleNumber = (EditText) interventionCostContent.findViewById(R.id.transportationVehicle);
        numberKeybord(transportationVehicleNumber);
        transportationVehicleNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validate_INTERVENTION_COST();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        insuranceVehicleNumber = (EditText) interventionCostContent.findViewById(R.id.insurance);
        numberKeybord(insuranceVehicleNumber);
        insuranceVehicleNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validate_INTERVENTION_COST();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        powerPumpVehicleNumber = (EditText) interventionCostContent.findViewById(R.id.powerPumpClock);
        numberKeybord(powerPumpVehicleNumber);
        powerPumpVehicleNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validate_INTERVENTION_COST();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        apsorbentVehicleNumber = (EditText) interventionCostContent.findViewById(R.id.apsorbentNum );
        numberKeybord(apsorbentVehicleNumber);
        apsorbentVehicleNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validate_INTERVENTION_COST();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return interventionCostContent;
    }

    private boolean validate_INTERVENTION_COST() {
        boolean isCorrect = false;

        String navalVehicle = navalVehicleNumber.getText().toString();
        String commandVehicle = commandVehicleNumber.getText().toString();
        String technicalVehicle = tehnicalVehicleNumber.getText().toString();
        String automaticLadder = automaticLadderNumber.getText().toString();
        String roadTanker = roadTankersNumber.getText().toString();
        String specialVehicle = specialVehicleNumber.getText().toString();
        String transportVehicle = transportationVehicleNumber.getText().toString();
        String insurance = insuranceVehicleNumber.getText().toString();
        String powerPuump = powerPumpVehicleNumber.getText().toString();
        String apsorbent = apsorbentVehicleNumber.getText().toString();

        if (navalVehicle.length() > 0 && commandVehicle.length() > 0 && technicalVehicle.length() > 0
                && automaticLadder.length() > 0 && roadTanker.length() > 0 && specialVehicle.length() > 0 && transportVehicle.length() > 0
                && insurance.length() > 0 && powerPuump.length() > 0 && apsorbent.length() > 0) {
            isCorrect = true;
            verticalStepperForm.setActiveStepAsCompleted();

        } else {
            String titleErrorString = "Niste popunili sve podatke!";
            verticalStepperForm.setActiveStepAsUncompleted(titleErrorString);
        }

        return isCorrect;
    }

    private void save_INTERVENTION_COST() {
        if (validate_INTERVENTION_COST()) {

            double navalVehicle = Double.valueOf(navalVehicleNumber.getText().toString());
            double commandVehicle = Double.valueOf(commandVehicleNumber.getText().toString());
            double technicalVehicle = Double.valueOf(tehnicalVehicleNumber.getText().toString());
            double automaticLadder = Double.valueOf(automaticLadderNumber.getText().toString());
            double roadTanker = Double.valueOf(roadTankersNumber.getText().toString());
            double specialVehicle = Double.valueOf(specialVehicleNumber.getText().toString());
            double transportVehicle = Double.valueOf(transportationVehicleNumber.getText().toString());

            double powerPump = Double.valueOf(powerPumpVehicleNumber.getText().toString());
            double insurance = Double.valueOf(insuranceVehicleNumber.getText().toString());
            double apsorbent = Double.valueOf(apsorbentVehicleNumber.getText().toString());

            intervencije.getReports().addConsumption(apsorbent,automaticLadder,Double.parseDouble(co2Text),commandVehicle,
                    0, //id2
                     0,//fire_extinguisher
                     0, //fire_fighter
                    Double.parseDouble(foamText),
                    insurance, navalVehicle, powerPump, roadTanker,specialVehicle,technicalVehicle,transportVehicle);
            intervencije.getReports().getConsumption().save();
            intervencije.getReports().save();
            intervencije.save();


            System.out.println("SAVE_COST + " + intervencije.getReports().getConsumption().getNavalVehicle());

            // insert in database
        }
    }

    private View createDescriptionStep() {
        descriptionEditText = new EditText(this);
        descriptionEditText.setHint(R.string.form_hint_description);
        descriptionEditText.setSingleLine(true);
        descriptionEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                verticalStepperForm.goToNextStep();
                return false;
            }
        });
        descriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validate_DESCRIPTION_STEP_HELPER();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return descriptionEditText;
    }

    private boolean validate_DESCRIPTION_STEP_HELPER() {
        boolean isCorrect = false;
        String surface = surfaceNumber.getText().toString();
        String superficies = superficiesNumber.getText().toString();

        if (descriptionEditText.length() > 0) {
            isCorrect = true;
            verticalStepperForm.setActiveStepAsCompleted();

        } else {
            String titleErrorString = "Potrebno je popuniti polje! ";
            verticalStepperForm.setActiveStepAsUncompleted(titleErrorString);
        }

        return isCorrect;

    }

    private void save__DESCRIPTION_STEP_HELPER() {


        intervencije.addHelpers(descriptionEditText.getText().toString());

        System.out.println("HELPERS: " + intervencije.getReports().getHelp());

    }

    /*  private View createMehanizationStep() {
          chooseTypeAndSort = new EditText(this);

          final LayoutInflater inflate = LayoutInflater.from(getBaseContext());
          mehanizationContent = (LinearLayout) inflate.inflate(R.layout.step_mehanization, null, false);

          makeMehanizationSpinnerFull(mehanizationContent, mehanizationContent);

          /*
          LayoutInflater factory = LayoutInflater.from(this);
          final View myView = factory.inflate(R.layout.step_mehanization, null);

          addVehicleButton.setOnClickListener(new View.OnClickListener() {
              public void onClick(View v) {
                  makeMehanizationSpinnerFull(v);
                  mehanizationContent.addView(myView);

              }
                 /* Spinner vehicleSpinner = new Spinner(this);
                  RadioGroup.LayoutParams rprms = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                  rgp.addView(vehicleSpinner, rprms);

              }

          });

<<<<<<< HEAD
  /*
          RadioGroup rgp = (RadioGroup) findViewById(R.id.mehanizationRadio);
          for (int i = 0; i < mehanizationAll.toArray().length; i++)
          {
              Spinner vehicleSpinner = onNewIntent();
              RadioButton radioButton = new RadioButton(this);
              radioButton.setText(String.valueOf(mehanizationAll.get(i)));
              radioButton.setId(i);
              RadioGroup.LayoutParams rprms = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
              rgp.addView(radioButton, rprms);
          }
=======
/*
        RadioGroup rgp = (RadioGroup) findViewById(R.id.mehanizationRadio);
        for (int i = 0; i < mehanizationAll.toArray().length; i++)
        {
            Spinner vehicleSpinner = onNewIntent();
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(String.valueOf(mehanizationAll.get(i)));
            radioButton.setId(i);
            RadioGroup.LayoutParams rprms = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            rgp.addView(radioButton, rprms);
        }

>>>>>>> d3a5c040031224fb237c587238b5a25ee14fa5b4



          return mehanizationContent;
      }
  */
    private void addMoreMehanization(final Button b, final View myView, final LinearLayout ll) {
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ll.removeView(b);
                ll.addView(myView);
                makeMehanizationSpinnerFull(ll, myView);
            }
        });
    }


    private void makeMehanizationSpinnerFull(LinearLayout ll, View v) {
        usedTruck = (Spinner) v.findViewById(R.id.vehicleUsed);
        usedTruck.setAdapter(getTruckAdapter());

        EditText kmOdabrano = (EditText) v.findViewById(R.id.km);
        numberKeybord(kmOdabrano);

        final Button b = new Button(this);
        ll.addView(b);
        b.setText("noviii");

        LayoutInflater factory = LayoutInflater.from(this);
        final View myView = factory.inflate(R.layout.step_mehanization, null);

        addMoreMehanization(b, myView, ll);
    }


    private SpinnerAdapter getTruckAdapter() {
        List<String> truckAll = new ArrayList<String>();
        Types_all_Controller type_all_controller = new Types_all_Controller();
        List<Truck> all_trucks = type_all_controller.GetAllRecordsFromTable_Truck();
        for (Truck t : all_trucks) {
            truckAll.add(t.getName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, truckAll);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        return dataAdapter;
    }


    private View createFiremenStep() {
        chooseTypeAndSort = new EditText(this);

        LayoutInflater inflate = LayoutInflater.from(getBaseContext());
        final LinearLayout firemenContent = (LinearLayout) inflate.inflate(R.layout.step_firemen, null, false);

        final List<Integer> id_fireman = new ArrayList<Integer>();
        final List<String> firemanList = new ArrayList<String>();

        Types_all_Controller type_all_controller = new Types_all_Controller();
        List<Fireman> type_of_truck = type_all_controller.GetAllRecordsFromTable_Fireman();
        firemanList.add(NO_SELECTED);
        id_fireman.add(-1);
        for (Fireman t : type_of_truck) {
            firemanList.add(t.getName() + " " + t.getSurname());
            id_fireman.add(t.getId());
        }


        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, firemanList);

        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);

        firemanSpinner = (Spinner) firemenContent.findViewById(R.id.participatedFireman);
        firemanSpinner.setAdapter(dataAdapter2);

        final TextView ispis = (TextView) firemenContent.findViewById(R.id.all);

        firemanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                } else {

                    String selectedItemText = (String) parent.getItemAtPosition(position);

                    sviOdabranivatrogasci += selectedItemText + "\n";
                    Integer i = id_fireman.get(position);
                    firemans_id_selected.add(i);
                    ispis.setTextSize(18);
                    ispis.setText(sviOdabranivatrogasci);
                    System.out.println("SELECTED: " + selectedItemText + " ID: " + id_fireman.get(position));

                    // firemanList.remove(parent.getItemAtPosition(position));
                    //  id_fireman.remove(position);
                    for (Integer id_fir :
                            firemans_id_selected) {
                        System.out.println("SELECTED: " + Fireman.getFiremanbyID(id_fir).getName());
                    }

                }
                brojac++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return firemenContent;
    }


    private boolean checkTitleStep(String title) {
        boolean titleIsCorrect = false;

        if (title.length() >= MIN_CHARACTERS_TITLE) {
            titleIsCorrect = true;

            verticalStepperForm.setActiveStepAsCompleted();
            // Equivalent to: verticalStepperForm.setStepAsCompleted(TITLE_STEP_NUM);

        } else {
            String titleErrorString = getResources().getString(R.string.error_title_min_characters);
            String titleError = String.format(titleErrorString, MIN_CHARACTERS_TITLE);

            verticalStepperForm.setActiveStepAsUncompleted(titleError);
            // Equivalent to: verticalStepperForm.setStepAsUncompleted(TITLE_STEP_NUM, titleError);

        }

        return titleIsCorrect;
    }


    private View createSortOfUnitStep() {
        LayoutInflater inflate = LayoutInflater.from(getBaseContext());
        LinearLayout typeAndSortContent = (LinearLayout) inflate.inflate(R.layout.sort_of_unit_report, null, false);

        List<String> sortOfUnitAll = new ArrayList<String>();

        Types_all_Controller type_all_controller = new Types_all_Controller();
        List<Type_of_unit> sort_of_units = type_all_controller.GetAllRecordsFromTable_Sort_of_unit();
        for (Type_of_unit t : sort_of_units) {
            sortOfUnitAll.add(t.getName());
        }

        RadioGroup rgp = (RadioGroup) findViewById(R.id.sortOfUnit);
        for (int i = 0; i < sortOfUnitAll.toArray().length; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(String.valueOf(sortOfUnitAll.get(i)));
            radioButton.setId(i);
            RadioGroup.LayoutParams rprms = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            rgp.addView(radioButton, rprms);
        }

        return typeAndSortContent;
    }

    // CONFIRMATION DIALOG WHEN USER TRIES TO LEAVE WITHOUT SUBMITTING

    private void confirmBack() {
        if (confirmBack && verticalStepperForm.isAnyStepCompleted()) {
            BackConfirmationFragment backConfirmation = new BackConfirmationFragment();
            backConfirmation.setOnConfirmBack(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    confirmBack = true;
                }
            });
            backConfirmation.setOnNotConfirmBack(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    confirmBack = false;
                    finish();
                }
            });
            backConfirmation.show(getSupportFragmentManager(), null);
        } else {
            confirmBack = false;
            finish();
        }
    }

    private void dismissDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && confirmBack) {
            confirmBack();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        confirmBack();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissDialog();
    }

    @Override
    protected void onStop() {
        super.onStop();
        dismissDialog();
    }

    // SAVING AND RESTORING THE STATE

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        /*
        // Saving title field
        if(titleEditText != null) {
            savedInstanceState.putString(STATE_TITLE, titleEditText.getText().toString());
        }
*/
        // Saving description field
        if (descriptionEditText != null) {
            savedInstanceState.putString(STATE_DESCRIPTION, descriptionEditText.getText().toString());
        }

        /*
        // Saving time field
        if(time != null) {
            savedInstanceState.putInt(STATE_TIME_HOUR, time.first);
            savedInstanceState.putInt(STATE_TIME_MINUTES, time.second);
        }

        // Saving week days field
        if(weekDays != null) {
            savedInstanceState.putBooleanArray(STATE_WEEK_DAYS, weekDays);
        }
        */
        // The call to super method must be at the end here
        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        // Restoration of title field
        if (savedInstanceState.containsKey(STATE_TITLE)) {
            String title = savedInstanceState.getString(STATE_TITLE);
            // titleEditText.setText(title);
        }

        // Restoration of description field
        if (savedInstanceState.containsKey(STATE_DESCRIPTION)) {
            String description = savedInstanceState.getString(STATE_DESCRIPTION);
            descriptionEditText.setText(description);
        }

        /*
        // Restoration of time field
        if(savedInstanceState.containsKey(STATE_TIME_HOUR)
                && savedInstanceState.containsKey(STATE_TIME_MINUTES)) {
            int hour = savedInstanceState.getInt(STATE_TIME_HOUR);
            int minutes = savedInstanceState.getInt(STATE_TIME_MINUTES);
            time = new Pair<>(hour, minutes);
            if(timePicker == null) {
            } else {
                timePicker.updateTime(hour, minutes);
            }
        }

        // Restoration of week days field
        if(savedInstanceState.containsKey(STATE_WEEK_DAYS)) {
            weekDays = savedInstanceState.getBooleanArray(STATE_WEEK_DAYS);
            if (weekDays != null) {
                for (int i = 0; i < weekDays.length; i++) {
                    if (weekDays[i]) {
                    } else {
                    }
                }
            }
        }
        */

        // The call to super method must be at the end here
        super.onRestoreInstanceState(savedInstanceState);
    }


}
