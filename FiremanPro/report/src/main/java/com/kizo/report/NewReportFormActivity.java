package com.kizo.report;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.BoolRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
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
import android.widget.Toast;

import com.project.test.database.Entities.Settings;
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

    // suma vatrogasaca i co2 koja se dobije kod dodavanja v a izračunava se koliko košta prema zbroju uz cost
    int sumFireman = 0;
    Double co2Sum = 0.0;
    Double foamSum = 0.0;
    Double powderSum = 0.0;

    // selected sort
    String selectedSort;
    TextView notFire;

    // ukoliko je izbrisan resurs ne radi validacijuu
    boolean spremljenResrs = false;

    // ako je korisnik već bio na ovom koraku i sad promijeni nešto spremiti promijene
    // Information about the steps/fields of the form
    boolean prviUlaz_MAIN = true;
    boolean prviUlaz_USED_RESOURCES_STEP_NUM = true; // NA NE SPREMLJENI ULAZ SPREMAJ INAĆE PRESKOČI
    boolean prviUlaz_FIRE_STEP_NUM = true;
    boolean prviUlaz_OWNER_AND_MATERIAL_STEP_NUM = true;
    boolean prviUlaz_DESCRIPTION_HELPER_STEP_NUM = true;
    boolean prviUlaz_INTERVENTION_STEP_NUM = true;
    boolean prviUlaz_FIREMEN_NUM = true;


    boolean promijenaINTERVENTION_STEP_NUM = true;

    Button prvi ;

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

    /**
     * Methoda kkoja postavlja vertical stepper form
     */

    private void initializeActivity() {
        // Vertical Stepper form vars
        int colorPrimary = ContextCompat.getColor(getApplicationContext(), R.color.glavna);
        int colorPrimaryDark = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDarkREPORT);

        String[] stepsTitles = getResources().getStringArray(R.array.steps_titles);

        verticalStepperForm = (VerticalStepperFormLayout) findViewById(R.id.vertical_stepper_form);
        VerticalStepperFormLayout.Builder.newInstance(verticalStepperForm, stepsTitles, this, this)
                .materialDesignInDisabledSteps(false)// false by default
                .showVerticalLineWhenStepsAreCollapsed(true) // false by default
                .primaryColor(colorPrimary)
                .displayBottomNavigation(true)
                .init();
    }

    /**
     * Methoda s kojom se omogućava(/ dopupta korištenje steppera
     * automatski se zbog switcha popunjava sve i ako se vratimo na prethodni korak
     */
    @Override
    public View createStepContentView(int stepNumber) {
        View view = null;
        switch (stepNumber) {
            case MAIN_INFORMATION_NUM:
                view = createTypeAndSortStep();
                break;
            case USED_RESOURCES_STEP_NUM:
                view = createUsedResourcesStep();
                break;
            case FIRE_STEP_NUM:
                view = createFireStep();
                break;
            case OWNER_AND_MATERIAL_STEP_NUM:
                view = createOwnerAndMaterialCostStep();
                break;
            case DESCRIPTION_HELPER_STEP_NUM:
                view = createDescriptionStep();
                break;
            case INTERVENTION_STEP_NUM:
                view = createInterventionCostStep();
                break;
            case FIREMEN_NUM:
                view = createFiremenStep();
        }
        return view;
    }


    /**
     * Methoda u kojoj se zadaju radnje kod otvaranja stepa
     * to su provjere i spremanja/updatanja podatakoa koji se budu spremili
     */
    @Override
    public void onStepOpening(int stepNumber) {
        switch (stepNumber) {
            case MAIN_INFORMATION_NUM:
                prviUlaz_MAIN = true;
                    break;
            case USED_RESOURCES_STEP_NUM:
                if(prviUlaz_MAIN) {
                    System.out.println("PRVI ULAZ MAIn -- SAVE: " + selectedSort);
                    save_MAIN_INFORMATION();
                    break;
                }
                else {
                    System.out.println("NIJEEEE PRVI ULAZ MAIn -- SAVE: " + selectedSort);
                    break;
                }
            case FIRE_STEP_NUM:
                if(prviUlaz_USED_RESOURCES_STEP_NUM) {
                    System.out.println("SElected sortu ušao u step fire PRVI ULAZZ-- SAVE: " + selectedSort);
                    if(prviUlaz_MAIN) save_MAIN_INFORMATION();
                    save_USED_RESOURCES();
                    break;
                }
                else {
                    System.out.println("nije prvi ulaz fire step  -- SAVE: " + selectedSort);
                    if(prviUlaz_MAIN) save_MAIN_INFORMATION();
                    prviUlaz_FIRE_STEP_NUM = true;
                    // save_USED_RESOURCES();
                    break;
                }
            case OWNER_AND_MATERIAL_STEP_NUM:
                if(prviUlaz_FIRE_STEP_NUM) {
                    System.out.println("Ušao u OWNER AND MATERIJAL STEP -- SAVE: " + selectedSort);
                    System.out.println(" SAVEE selected sort u owner je : " + selectedSort);
                    save_FIRE_STEP();
                    validate_OWNER_AND_MATERIAL_COST();
                    break;
                }
                else {
                    if(prviUlaz_MAIN) save_MAIN_INFORMATION();
                    if(prviUlaz_FIRE_STEP_NUM) save_FIRE_STEP();
                    prviUlaz_OWNER_AND_MATERIAL_STEP_NUM = true;
                    verticalStepperForm.setStepAsCompleted(OWNER_AND_MATERIAL_STEP_NUM);
                    break;
                }
            case DESCRIPTION_HELPER_STEP_NUM:
                if(prviUlaz_OWNER_AND_MATERIAL_STEP_NUM) {
                    System.out.println("Usao u descriptionnn ");
                    save_OWNER_AND_MATERIAL_COST();
                    validate_DESCRIPTION_STEP_HELPER();
                    System.out.println("surface: " + intervencije.getReports().getSurface_m2());
                    break;
                }
                else {
                    if(prviUlaz_MAIN) save_MAIN_INFORMATION();
                    if(prviUlaz_FIRE_STEP_NUM) save_FIRE_STEP();
                    if(prviUlaz_OWNER_AND_MATERIAL_STEP_NUM) save_OWNER_AND_MATERIAL_COST();
                    prviUlaz_DESCRIPTION_HELPER_STEP_NUM = true;
                    verticalStepperForm.setStepAsCompleted(DESCRIPTION_HELPER_STEP_NUM);
                    break;
                }
            case INTERVENTION_STEP_NUM:
                if(prviUlaz_DESCRIPTION_HELPER_STEP_NUM) {
                    save__DESCRIPTION_STEP_HELPER();
                    validate_INTERVENTION_COST();
                    break;
                }
                else {
                    if(prviUlaz_MAIN) save_MAIN_INFORMATION();
                    if(prviUlaz_FIRE_STEP_NUM) save_FIRE_STEP();
                    if(prviUlaz_OWNER_AND_MATERIAL_STEP_NUM) save_OWNER_AND_MATERIAL_COST();
                    if(prviUlaz_DESCRIPTION_HELPER_STEP_NUM) save__DESCRIPTION_STEP_HELPER();
                    prviUlaz_INTERVENTION_STEP_NUM = true;
                    save__DESCRIPTION_STEP_HELPER();
                    break;
                }
            case FIREMEN_NUM:
                if(prviUlaz_INTERVENTION_STEP_NUM) {
                    save_INTERVENTION_COST();
                    verticalStepperForm.setStepAsCompleted(stepNumber);
                    break;
                }
                else {
                    if(prviUlaz_MAIN) save_MAIN_INFORMATION();
                    if(prviUlaz_FIRE_STEP_NUM) save_FIRE_STEP();
                    if(prviUlaz_OWNER_AND_MATERIAL_STEP_NUM) save_OWNER_AND_MATERIAL_COST();
                    if(prviUlaz_DESCRIPTION_HELPER_STEP_NUM) save__DESCRIPTION_STEP_HELPER();
                    if(promijenaINTERVENTION_STEP_NUM) save_INTERVENTION_COST();
                    prviUlaz_FIREMEN_NUM = true;
                    break;
                }
            case END_NUM:
                if(prviUlaz_MAIN) save_MAIN_INFORMATION();
                if(prviUlaz_FIRE_STEP_NUM) save_FIRE_STEP();
                if(prviUlaz_OWNER_AND_MATERIAL_STEP_NUM) save_OWNER_AND_MATERIAL_COST();
                if(prviUlaz_DESCRIPTION_HELPER_STEP_NUM) save__DESCRIPTION_STEP_HELPER();
                if(promijenaINTERVENTION_STEP_NUM) save_INTERVENTION_COST();
                if(prviUlaz_FIREMEN_NUM) save_FIRE_STEP();
                verticalStepperForm.setStepAsCompleted(stepNumber);
                sendMail();
                break;
        }
    }

    /**
     * Methoda oja omogućava slanje na mail
     */

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
        System.out.println("Send email SAVEE");

        String[] TO = {Settings.getSettings().getEmailToSendReport()};
        // String[] CC = {"xyz@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("message/rfc822");

/*
        File root = Environment.getExternalStorageDirectory();

        String pathToMyAttachedFile = "SD card/Download/zapisnik-intervencije-MojiKomentari.com";
        File file = new File(root, pathToMyAttachedFile);
        if (!file.exists() || !file.canRead()) {
            return;
        }
        Uri uri = Uri.fromFile(file);

        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);

*/
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        // emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subjectText);
        emailIntent.putExtra(Intent.EXTRA_TEXT, bodyText);

       try {
            startActivity(Intent.createChooser(emailIntent, "Odaberite email providera: "));

            // finish();
            System.out.println("Finished sending email. SAVEE");
        } catch (android.content.ActivityNotFoundException ex) {
            System.out.println("There is no email client installed. SAVEE");
        }
    }
    /*
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        // emailIntent.setType("text/plain");
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {// intervencije.getEmailTo().toString()
                "matea.bodulusic@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subjectText);
        emailIntent.putExtra(Intent.EXTRA_TEXT, bodyText);



        File root = Environment.getExternalStorageDirectory();

        String pathToMyAttachedFile = "temp/attachement.xml";
        File file = new File(root, pathToMyAttachedFile);
        if (!file.exists() || !file.canRead()) {
            return;
        }
        Uri uri = Uri.fromFile(file);

        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(emailIntent, "Odaberite email providera: "));

    }
    */

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
                    confirmBack = false;
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Methoda koja zadaje i popunjava fire step
     * koliko srt nije fire onda se ovaj step preskače i polja nisu enabla
     */

    private View createFireStep() {
        chooseTypeAndSort = new EditText(this);

        LayoutInflater inflate = LayoutInflater.from(getBaseContext());
        final LinearLayout fireContent = (LinearLayout) inflate.inflate(R.layout.step_fire, null, false);

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

        sizeOfFire = addSpinnerValue(sizeOfFire, fireContent, R.id.size_of_fire, getSizeOfFireAdapter());

        repeatedSpinner = addSpinnerValue_listener_FIRE_STEP(repeatedSpinner, fireContent, R.id.repeated, getYesNo());
        spatialSpread = addSpinnerValue_listener_FIRE_STEP(spatialSpread, fireContent, R.id.spatial_spread, getSpatialSpreadAdapter());
        timeSpread = addSpinnerValue_listener_FIRE_STEP(timeSpread, fireContent, R.id.time_spread, getTimeSpreadAdapter());
        smokeSpread = addSpinnerValue_listener_FIRE_STEP(smokeSpread, fireContent, R.id.smoke_spread, getSmokeSpreadAdapter());
        outdoorSpread = addSpinnerValue_listener_FIRE_STEP(outdoorSpread, fireContent, R.id.outdoor_spread, getOutdoorSpreadAdapter());

        System.out.println("Selected sort u dijelu createFireSTep: " + selectedSort + " SAVEEE ");


        if(!spinnerSort.getSelectedItem().toString().equals(types_all_controller.get_FIRE_Sort_of_intervention().getName().toString()) && spinnerSort.getSelectedItem().toString() != null  ) {
            notFire = (TextView) fireContent.findViewById(R.id.nijeFire);
        }
        return fireContent;
    }

    /**
     * Methoda  koja provjerava mo želi se nastaviti dalje odnosno spremiti upisani podaci vezani za požar
     */    private boolean validate_FIRE_STEP() {
        boolean isCorrect = false;
        System.out.println("validateFIRE");
        String destroyed = destroyedSpace.getText().toString();

        if(!spinnerSort.getSelectedItem().toString().equals(types_all_controller.get_FIRE_Sort_of_intervention().getName().toString()) && selectedSort != null){
            verticalStepperForm.setStepAsCompleted(FIRE_STEP_NUM);
        }
        else {
            if (destroyed.length() > 0 & validSpinner(sizeOfFire) & validSpinner(repeatedSpinner) & validSpinner(spatialSpread) & validSpinner(timeSpread) & validSpinner(smokeSpread) & validSpinner(outdoorSpread)) {
                isCorrect = true;
                verticalStepperForm.setActiveStepAsCompleted();
            } else {
                String titleErrorString = "Niste popunili sve podatke!";
                verticalStepperForm.setActiveStepAsUncompleted(titleErrorString);
            }
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

    /**
     * Methoda kkoja priprema fire d za spremanje, a on se sprema kod dodavanja intervencije na zadnjem koraku
     */
    private void save_FIRE_STEP() {
        java.util.Date localzationTime = new java.util.Date(System.currentTimeMillis());
        java.util.Date fire_extinguished_time = new java.util.Date(System.currentTimeMillis());
        System.out.println("SAVE - ušao u step save FIRE STEP");

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
            // insert in database
        }
        prviUlaz_FIRE_STEP_NUM = false;
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
                System.out.println("SPINNER: " + item.toString() + ", a selectedSort je " + selectedSort);

                validate_FIRE_STEP();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                spinnerType.setVisibility(View.INVISIBLE);
            }
        });
        return spinner;
    }

    /**
     * Methoda koja sprema view na kojemu su popunjeni spinneri s potrebnim podaima i oni edittextovi kojima ograničenja atributa baze ograničavaju brojeve ommoguće samo takav odabir
     */
    private View createTypeAndSortStep() {
        chooseTypeAndSort = new EditText(this);
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

        spinnerType = (Spinner) typeAndSortContent.findViewById(R.id.type_of_intervention);
        spinnerType.setVisibility(View.INVISIBLE);

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Object item = parentView.getItemAtPosition(position);
                selectedSort = item.toString();
                System.out.println("SPINNER: " + item.toString() + ", a selectedSort je " + selectedSort);


                if(!spinnerSort.getSelectedItem().toString().equals(types_all_controller.get_FIRE_Sort_of_intervention().getName())) {
                    System.out.println("SAVE: preskočili smo požar dio " + types_all_controller.get_FIRE_Sort_of_intervention().getName().toString() + types_all_controller.get_FIRE_Sort_of_intervention().getName().toString().length() +  " selected sort= " + selectedSort);
                    repeatedSpinner.setEnabled(false);
                    sizeOfFire.setEnabled(false);
                    spatialSpread.setEnabled(false);
                    timeSpread.setEnabled(false);
                    smokeSpread.setEnabled(false);
                    outdoorSpread.setEnabled(false);
                    destroyedSpace.setEnabled(false);
                    System.out.println("SelectedSort prvi prolaz je: " + selectedSort);
                    notFire.setText("Rekli ste da ova intervencija nema veze s požarom stoga nije potrebno popuniti podatke o ovom koraku u izvještaju!");
                    verticalStepperForm.setActiveStepAsCompleted();
                }

                if(spinnerSort.getSelectedItem().toString().equals(types_all_controller.get_FIRE_Sort_of_intervention().getName())){
                    System.out.println("SAVE: je fireee");
                    notFire.setText(" ");
                    repeatedSpinner.setEnabled(true);
                    sizeOfFire.setEnabled(true);
                    spatialSpread.setEnabled(true);
                    timeSpread.setEnabled(true);
                    smokeSpread.setEnabled(true);
                    outdoorSpread.setEnabled(true);
                    destroyedSpace.setEnabled(true);
                }

                spinnerType.setAdapter(getTypeOfInterventionAdapter(item.toString()));
                spinnerType.setVisibility(View.VISIBLE);

                validate_MAIN_INFORMATIONA();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                spinnerType.setVisibility(View.INVISIBLE);
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

    /**
     * Methoda koja provjerava da li su odabrani sor, type i upisan text(više od 3 slova)
     */
    private boolean validate_MAIN_INFORMATIONA() {
        boolean titleIsCorrect = false;
        String description = interventionDescription.getText().toString();

        if (description.length() > 3 && !spinnerSort.getSelectedItem().toString().equals(NO_SELECTED) && !spinnerType.getSelectedItem().toString().equals(NO_SELECTED)) {
            titleIsCorrect = true;

            verticalStepperForm.setActiveStepAsCompleted();

        } else {
            String titleErrorString = "Potrebno je upisati opis intervencije  i odabrati vrstu i tip intervencije";
            verticalStepperForm.setActiveStepAsUncompleted(titleErrorString);
        }

        return titleIsCorrect;
    }

    /**
     * Methoda koja sprema sort požara
     */

    private void save_MAIN_INFORMATION() {
        if (validate_MAIN_INFORMATIONA()) {

            String title = interventionDescription.getText().toString();
            // insert in database
            intervencije.addDescriptionOfIntervention(title);

            if (spinnerSort.getSelectedItem().toString().equals(types_all_controller.get_FIRE_Sort_of_intervention().getName())) {
                intervencije.setThisInterventionAsFire();
                intervencije.getReports().addFireIntervention(Types_all_Controller.get_Intervention_typeByName(spinnerType.getSelectedItem().toString()));
                System.out.println("SAve FIRE step --> provjera: " + types_all_controller.get_FIRE_Sort_of_intervention().getName());
            }
            if (spinnerSort.getSelectedItem().toString().equals(types_all_controller.get_TRHNICAL_Sort_of_intervention().getName())) {
                intervencije.setThisInterventionAsTehnical();
                intervencije.getReports().addTehnicalInterventionDetails(Types_all_Controller.get_Intervention_typeByName(spinnerType.getSelectedItem().toString()));
                System.out.println("SAve tEHNICAL step");
            }
            if (spinnerSort.getSelectedItem().toString().equals(types_all_controller.get_OTHER_Sort_of_intervention().getName())) {
                intervencije.setThisInterventionAsOther();
                intervencije.getReports().addOtherInterventionDetails(Types_all_Controller.get_Intervention_typeByName(spinnerType.getSelectedItem().toString()));
                System.out.println("SAve other step");
            }
            prviUlaz_MAIN = false;
        }
    }

    /**
     * Methode za dohvačanje podataka za adapter kako bi se punili spinneri
     */

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

    /**
     * Methoda koja popunjava sa spinerima i zadaje uvijete biranja broja na stepu used resources
     */

    private View createUsedResourcesStep() {
        chooseTypeAndSort = new EditText(this);

        LayoutInflater inflate = LayoutInflater.from(getBaseContext());
        final LinearLayout v = (LinearLayout) inflate.inflate(R.layout.step_used_resources, null, false);
        final LinearLayout ll = v;

        /* Button koji omogućuje doavanje još resursa jer se tako traži u službenom izvještaju */

        prvi = new Button(this);
        prvi.setText("Dodaj resurs");
        prvi.setEnabled(false);

        // pocetak - prvi prikaz za odabir resursa

        spinnerFiremanPatrol = (Spinner) v.findViewById(R.id.sort_of_unit);
        spinnerFiremanPatrol.setAdapter(getFiremanPatrols());


        spinnerFiremanPatrol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!parent.getSelectedItem().toString().equals(NO_SELECTED)) {
                    spinnerVehicle = addSpinnerValue_listener_USED_RESOURCES_STEP(spinnerVehicle, v, R.id.vehicle, getVehicleAdapter(Fireman_patrol.getPatrolByName(parent.getSelectedItem().toString())), prvi);
                } else {
                    spinnerVehicle = addSpinnerValue_listener_USED_RESOURCES_STEP(spinnerVehicle, v, R.id.vehicle, getVehicleAdapter(Fireman_patrol.getPatrolByName(parent.getSelectedItem().toString())), prvi);

                }
                cotrolButtonAddAndValidate(prvi);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        //numberKeybor omogućava upisa samo brojeva i to je realizirano kroz metodu

        kmNumber = addTextChangeListenerWithValidation(v,R.id.km, prvi);//v.findViewById(R.id.km);// addTextChangeListenerWithValidation (kmNumber, v, R.id.km);

        numberKeybord(kmNumber);


        clockNumber = addTextChangeListenerWithValidation(v, R.id.clock, prvi);
        numberKeybord(clockNumber);


        numberOfFiremanParticipated = addTextChangeListenerWithValidation(v, R.id.number_of_firemen_in_truck, prvi);
        numberKeybord(numberOfFiremanParticipated);


        waterNumber = addTextChangeListenerWithValidation(v, R.id.water, prvi);
        numberKeybord(waterNumber);


        foamNumber = addTextChangeListenerWithValidation(v, R.id.foam, prvi);
        numberKeybord(foamNumber);


        powderNumber = addTextChangeListenerWithValidation(v, R.id.powder, prvi);
        numberKeybord(powderNumber);


        co2Number = addTextChangeListenerWithValidation(v, R.id.CO_2, prvi);
        numberKeybord(co2Number);


        ll.addView(prvi);


        LayoutInflater factory = LayoutInflater.from(this);
        final View myView = factory.inflate(R.layout.step_used_resources, null);

        Button b = new Button(this);
        b.setText("Dodaj resurs");

        Button neporebniResurs = new Button(this);

        addNewUsedResources(prvi, b, ll, myView, neporebniResurs, v);
//kraj
        return v;
    }


    /**
     * Methoda koja popunjava sa spinerima i zadaje uvijete biranja broja na stepu used resources nakon PRVOGG UPISANOG
     */

    private void addUsedResources(final Button prvi, final View v, final LinearLayout ll) {
        prviUlaz_USED_RESOURCES_STEP_NUM = true;

        LayoutInflater factory = LayoutInflater.from(this);
        final View myView = factory.inflate(R.layout.step_used_resources, null);

        spinnerFiremanPatrol = (Spinner) v.findViewById(R.id.sort_of_unit);
        spinnerFiremanPatrol.setAdapter(getFiremanPatrols());

        spinnerFiremanPatrol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             if (!parent.getSelectedItem().toString().equals(NO_SELECTED)) {
                 spinnerVehicle = addSpinnerValue_listener_USED_RESOURCES_STEP(spinnerVehicle, v, R.id.vehicle, getVehicleAdapter(Fireman_patrol.getPatrolByName(parent.getSelectedItem().toString())), prvi);
             }
             else {
                 spinnerVehicle = addSpinnerValue_listener_USED_RESOURCES_STEP(spinnerVehicle, v, R.id.vehicle, getVehicleAdapter(Fireman_patrol.getPatrolByName(parent.getSelectedItem().toString())), prvi);
                }
                cotrolButtonAddAndValidate(prvi);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        kmNumber = addTextChangeListenerWithValidation(v, R.id.km, prvi);//v.findViewById(R.id.km);// addTextChangeListenerWithValidation (kmNumber, v, R.id.km);
        numberKeybord(kmNumber);


        clockNumber = addTextChangeListenerWithValidation(v, R.id.clock, prvi);
        numberKeybord(clockNumber);


        numberOfFiremanParticipated = addTextChangeListenerWithValidation(v, R.id.number_of_firemen_in_truck,  prvi);
        numberKeybord(numberOfFiremanParticipated);


        waterNumber = addTextChangeListenerWithValidation(v, R.id.water,  prvi);
        numberKeybord(waterNumber);


        foamNumber = addTextChangeListenerWithValidation(v, R.id.foam,  prvi);
        numberKeybord(foamNumber);


        powderNumber = addTextChangeListenerWithValidation(v, R.id.powder,  prvi);
        numberKeybord(powderNumber);


        co2Number = addTextChangeListenerWithValidation(v, R.id.CO_2,  prvi);
        numberKeybord(co2Number);


        ll.addView(prvi);

        LayoutInflater inflate = LayoutInflater.from(getBaseContext());
        final LinearLayout addedVehicleContent = (LinearLayout) inflate.inflate(R.layout.step_used_resources, null, false);


        Button b = new Button(this);
        b.setText("Dodaj resurs");

        Button neporebniResurs = new Button(this);
        neporebniResurs.setText("Nepotrebni resurs");
        ll.addView(neporebniResurs);

        addNewUsedResources(prvi, b, ll, myView, neporebniResurs, v);
    }

    private boolean validate_USED_RESOURCES(Button gumbDodavanja) {
        boolean isCorrect = false;
        System.out.println("validateUSED_RESOURCES");


        if (validSpinner(spinnerVehicle) & validSpinner(spinnerFiremanPatrol) & isValidEditbox(co2Number) & isValidEditbox(powderNumber) & isValidEditbox(foamNumber) & isValidEditbox(waterNumber) & isValidEditbox(numberOfFiremanParticipated) & isValidEditbox(clockNumber) & isValidEditbox(kmNumber) ) {
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


    private Spinner addSpinnerValue_listener_USED_RESOURCES_STEP(Spinner spinner, View content, int id, ArrayAdapter<String> methodArray, final Button gumbBrisanja) {
        spinner = (Spinner) content.findViewById(id);
        spinner.setAdapter(methodArray);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Object item = parentView.getItemAtPosition(position);
                System.out.println("SPINNER_size of fire: " + item.toString());
                cotrolButtonAddAndValidate(gumbBrisanja);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                spinnerType.setVisibility(View.INVISIBLE);
                // your code here
            }
        });
        return spinner;
    }

    /**
     * Methoda koja onemogućuje spriječavanje predomišljanja u vezi resursa
     */

    private void cotrolButtonAddAndValidate(Button gumbBrisanja) {
        if(!validate_USED_RESOURCES(gumbBrisanja)){
            gumbBrisanja.setEnabled(false);
        }
        else {
            gumbBrisanja.setEnabled(true);
        }
    }

    /**
     * Methoda koja za određeni edittext određuje promijenu kada dođe do promijene texta
     */
    private EditText addTextChangeListenerWithValidation(View view, int id, final Button gumbBrisanja) {

        EditText editText = (EditText) view.findViewById(id);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cotrolButtonAddAndValidate(gumbBrisanja);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return editText;
    }

    /**
     * Methoda koja spema količinu utrošenih resursa (korigirano stepoima)
     */

    public void save_USED_RESOURCES() {
            kmText =kmNumber.getText().toString();
            waterText = waterNumber.getText().toString();
            powderText = powderNumber.getText().toString();
            foamText = foamNumber.getText().toString();
            co2Text = co2Number.getText().toString();
            numberOfFiremansText = numberOfFiremanParticipated.getText().toString();
            clockText = clockNumber.getText().toString();
            Fireman_patrol patrol = Fireman_patrol.getPatrolByName(spinnerFiremanPatrol.getSelectedItem().toString());
            Truck truck = patrol.getTruckByName(spinnerVehicle.getSelectedItem().toString());
            sumFireman += Integer.parseInt(numberOfFiremansText);
            co2Sum += Double.parseDouble(co2Text);
            powderSum += Double.parseDouble(powderText);
            foamSum += Double.parseDouble(foamText);
            // insert in database
            intervencije.getReports().addFiremanPatrolandTruck(Integer.parseInt(numberOfFiremansText),
                    Double.parseDouble(waterText),
                    Double.parseDouble(foamText), Double.parseDouble(powderText),
                    Double.parseDouble(co2Text), Double.parseDouble(kmText),
                    Double.parseDouble(clockText),
                    truck,
                    patrol
            );
        System.out.println("SAVE USED RESOURCE:  " + intervencije.getReports().getTrucksAndPatrols().get(0).getFireman_patrol().getName());

        prviUlaz_USED_RESOURCES_STEP_NUM = false;
    }

    /**
     * Methoda koja sprema resurs, zatim  briše gumb za dodavanje i dodaje novi view za popunjvavanje novog resussa ,
     * a ukoliko se klikne na gumb za poništavanje briše view za dodavanje resursa ukoliko se korisnik predomisli(skuži da mu ne treba još jedan)
     */
    private void addNewUsedResources(final Button prvi, final Button noviB, final LinearLayout ll, final View myView, final Button nepotrebni, final View stari) {
        prvi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                prviUlaz_USED_RESOURCES_STEP_NUM = false;
                save_USED_RESOURCES();
                ll.removeView(nepotrebni);
                ll.removeView(prvi);
                ll.addView(myView);
                addUsedResources(noviB, myView, ll);
                spremljenResrs = true;
            }
        });


        nepotrebni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll.removeView(stari);
                ll.removeView(nepotrebni);
                verticalStepperForm.setActiveStepAsUncompleted(" ");
                verticalStepperForm.setActiveStepAsCompleted();
            }
        });
    }

    /**
     * Methode koje dohvaćaju potrebne podatke i stavljaju ih u adapter tako da se popune spinneri
     */

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

    /**
     * Methode koje omogućuju samo brojeve na tipkovnici kod popunjavanja editText-a
     */

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

    /**
     * Methoda koja puni spinnere potrebnim podacima i omogućuje prikaz sao brojeva na tipkovnici za izbor
     */
    private View createOwnerAndMaterialCostStep() {
        chooseTypeAndSort = new EditText(this);

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

    /**
     * Methoda koja provjerava da li su upisani surface and superficies
     */
    private boolean validate_OWNER_AND_MATERIAL_COST() {
        System.out.println("ušao u VALIDATE owner and cost - save");
        boolean isCorrect = false;
        String surface = surfaceNumber.getText().toString();
        String superficies = superficiesNumber.getText().toString();

        if (surface.length() > 0 && superficies.length() > 0) {
            isCorrect = true;
            System.out.println("surface number: " + surface + "savee");
            System.out.println("superficies: " + superficies);

            verticalStepperForm.setActiveStepAsCompleted();
        } else {
            String titleErrorString = "Potrebno je upisati površinu objekata i vanjskog prostora!";
            verticalStepperForm.setActiveStepAsUncompleted(titleErrorString);
            System.out.println("surface number: " + surface + "savee");
            System.out.println("superficies: " + superficies + "savee");
        }

        return isCorrect;
    }

    /**
     * Methoda koja sprema surface and superficies u report (dodaje se odmah jer su atributi)
     */
    private void save_OWNER_AND_MATERIAL_COST() {
        if (validate_OWNER_AND_MATERIAL_COST()) {
            System.out.println("ušao u SAVE owner and cost - save");

            String surface = surfaceNumber.getText().toString();
            String superficies = superficiesNumber.getText().toString();

            // insert in database
            intervencije.addObjectSuperficies_ha(Double.parseDouble(superficies));
            intervencije.addObjectSurface_m2(Double.parseDouble(surface));
        }

        System.out.println("ovner cost saved ");
        prviUlaz_OWNER_AND_MATERIAL_STEP_NUM = false;
    }

    /**
     * Methoda koja priprema prikaz za popunjavanje posta intervencije
     */

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
                if(!prviUlaz_INTERVENTION_STEP_NUM){
                    promijenaINTERVENTION_STEP_NUM = true;
                }
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
                if(!prviUlaz_INTERVENTION_STEP_NUM){
                    promijenaINTERVENTION_STEP_NUM = true;
                }
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
                if(!prviUlaz_INTERVENTION_STEP_NUM){
                    promijenaINTERVENTION_STEP_NUM = true;
                }
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
                if(!prviUlaz_INTERVENTION_STEP_NUM){
                    promijenaINTERVENTION_STEP_NUM = true;
                }
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
                if(!prviUlaz_INTERVENTION_STEP_NUM){
                    promijenaINTERVENTION_STEP_NUM = true;
                }
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
                if(!prviUlaz_INTERVENTION_STEP_NUM){
                    promijenaINTERVENTION_STEP_NUM = true;
                }
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
                if(!prviUlaz_INTERVENTION_STEP_NUM){
                    promijenaINTERVENTION_STEP_NUM = true;
                }
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
                if(!prviUlaz_INTERVENTION_STEP_NUM){
                    promijenaINTERVENTION_STEP_NUM = true;
                }
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
                if(!prviUlaz_INTERVENTION_STEP_NUM){
                    promijenaINTERVENTION_STEP_NUM = true;
                }
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
                if(!prviUlaz_INTERVENTION_STEP_NUM){
                    promijenaINTERVENTION_STEP_NUM = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return interventionCostContent;
    }

    /**
     * Methoda koja provjerava dali su upisani cos-ovi (ne provjerava brojeve jer je već omogućen upis samo brojeva kroz tipkovnicu)
     */

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

    /**
     * Methoda koja priprema za spremanje količinu potrošenih resursa kako bi se ukupni trošak izračunao množnjem koje je predviđeno za tu godinu te jvatrogasne edinice
     */

    private void save_INTERVENTION_COST() {
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

        if (validate_INTERVENTION_COST()) {
            intervencije.getReports().addConsumption(apsorbent,automaticLadder,co2Sum,commandVehicle,
                  //  0, id2
                     powderSum,//fire_extinguisher
                     sumFireman, //fire_fighter
                    foamSum,
                    insurance, navalVehicle, powerPump, roadTanker,specialVehicle,technicalVehicle,transportVehicle);
            System.out.println("SAVE_COST + " + intervencije.getReports().getConsumption().getNavalVehicle());

            // insert in database
            prviUlaz_INTERVENTION_STEP_NUM = false;
        }
    }

    /**
     * Methoda koja kreira dio za dodavanje opisa reporta i omogućuje vaidaciju nakon upisa u edit text
     */
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

    /**
     * Methoda koja provjerava dal opis ima više od 5 slova i nakon više od 5 slova omogućuje kretanje dalje
     */
    private boolean validate_DESCRIPTION_STEP_HELPER() {
        boolean isCorrect = false;
        String surface = surfaceNumber.getText().toString();
        String superficies = superficiesNumber.getText().toString();

        if (descriptionEditText.length() > 5) {
            isCorrect = true;
            verticalStepperForm.setActiveStepAsCompleted();

        } else {
            String titleErrorString = "Potrebno je popuniti polje! ";
            verticalStepperForm.setActiveStepAsUncompleted(titleErrorString);
        }

        return isCorrect;

    }

    /**
     * Methoda koja sprema atribut opisa u report (nije potrebno spremati kasnije jer je sa samim dodavanjem reporta ok spemiti i opis, kod novog spremanja se upsata samo)
     *
     */
    private void save__DESCRIPTION_STEP_HELPER() {
        intervencije.addHelpers(descriptionEditText.getText().toString());

        System.out.println("HELPERS: " + intervencije.getReports().getHelp());
        prviUlaz_DESCRIPTION_HELPER_STEP_NUM = false;
    }

    /**
     * Method  koja stvara SpinnerAdaper uz omoć kojeć će se u spinneru prikazati svi truckovi
     */

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


    /**
     * Methoda koja ima smpinner iz kojeg se biraju vvatrogasci koji su sudjelovali u intervenciji i dodaju se u bazu
     */
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



    /**
     * Methoda koja kreira korak kod kojega se
     *
     */
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

    /**
     * Method  koja pita da li zbilja želi osoba izaći bezkakvog spremanja
     */
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

    /**
     * Methoda koja omogućuje spremanje svih stavaka u report (ne sprema se ranije jer sam na ovaj način omogućila update svih polja
     *
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (spinnerSort.getSelectedItem().toString().equals(types_all_controller.get_FIRE_Sort_of_intervention().getName())) {
            intervencije.getReports().saveFireInterventionDetails();
            intervencije.setThisInterventionAsFire();
            System.out.println("SAve FIRE step u ONSAVEEE --> provjera: " + types_all_controller.get_FIRE_Sort_of_intervention().getName());
        }

        if (spinnerSort.getSelectedItem().toString().equals(types_all_controller.get_TRHNICAL_Sort_of_intervention().getName())) {
            intervencije.getReports().saveTehnicalInterventionDetails();
            intervencije.setThisInterventionAsTehnical();
            System.out.println("SAve tEHNICAL step u ONSAVEEE ");
        }

        if (spinnerSort.getSelectedItem().toString().equals(types_all_controller.get_OTHER_Sort_of_intervention().getName())) {
            intervencije.getReports().saveOtherInterventionDetails();
           intervencije.setThisInterventionAsOther();
            System.out.println("SAve other step u ONSAVEEE ");
        }

        intervencije.getReports().getConsumption().save();
        intervencije.getReports().save();
        intervencije.save();

        System.out.println("Spremamm - save u save instancestate");

        super.onSaveInstanceState(savedInstanceState);
    }

    /*
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        System.out.println("Ušao u restoree - save");

        if(!prviUlaz_MAIN){
            if (spinnerSort.getSelectedItem().toString().equals(types_all_controller.get_FIRE_Sort_of_intervention().getName())) {
                intervencije.getReports().saveFireInterventionDetails();
                System.out.println("SAve FIRE step u ONSAVEEE --> provjera: " + types_all_controller.get_FIRE_Sort_of_intervention().getName());
            }

            if (spinnerSort.getSelectedItem().toString().equals(types_all_controller.get_TRHNICAL_Sort_of_intervention().getName())) {
                intervencije.getReports().saveTehnicalInterventionDetails();
                System.out.println("SAve tEHNICAL step u ONSAVEEE ");
            }

            if (spinnerSort.getSelectedItem().toString().equals(types_all_controller.get_OTHER_Sort_of_intervention().getName())) {
                intervencije.getReports().saveOtherInterventionDetails();
                System.out.println("SAve other step u ONSAVEEE ");
            }
        }


       if(prviUlaz_INTERVENTION_STEP_NUM){
           intervencije.getReports().getConsumption().save();
           intervencije.getReports().save();
           intervencije.save();
       }



        if (savedInstanceState.containsKey(STATE_TITLE)) {
            String title = savedInstanceState.getString(STATE_TITLE);
        }

        if (savedInstanceState.containsKey(STATE_DESCRIPTION)) {
            String description = savedInstanceState.getString(STATE_DESCRIPTION);
            descriptionEditText.setText(description);
        }

        super.onRestoreInstanceState(savedInstanceState);
    }
*/

}
