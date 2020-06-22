package com.machacode.oscarmorquecho.ledtext;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.machacode.oscarmorquecho.ledtext.adapters.caConfigurations;
import com.machacode.oscarmorquecho.ledtext.adapters.caFonts;
import com.machacode.oscarmorquecho.ledtext.interfaces.onClickActions;
import com.machacode.oscarmorquecho.ledtext.models.Font;
import com.machacode.oscarmorquecho.ledtext.models.LedText;
import com.machacode.oscarmorquecho.ledtext.models.LedTextRealm;
import com.machacode.oscarmorquecho.ledtext.utils.ScrollTextView;
import com.machacode.oscarmorquecho.ledtext.utils.TextWatcherLedText;
import com.machacode.oscarmorquecho.ledtext.utils.Utilities;

import net.margaritov.preference.colorpicker.ColorPickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.machacode.oscarmorquecho.ledtext.utils.Utilities.CUSTOM;
import static com.machacode.oscarmorquecho.ledtext.utils.Utilities.DEFAULT;

public class MainActivity extends AppCompatActivity implements Animation.AnimationListener, onClickActions {
    //region Variables
    private Realm mRealm;
    private ScrollTextView tvTextDisplayed;
    private int scroll_pos;
    private float STEP_TEXT_SIZE = 10;
    private boolean StopResume = false;
    private Button btnStop;
    private Button btnStart;
    private TextView tvFuente;
    private TextView tvSpeedInNumber;
    private SeekBar sbTextSpeed;
    private Animation animBlink;
    private RelativeLayout rlColor;
    private RelativeLayout rlBackgroundColor;
    private boolean clickScreen = true;
    private MaterialToolbar toolbar;

    private LedText ledText;
    private DrawerLayout drawerLayout;
    private ColorPickerDialog colorPickerDialog;
    private int colorSelected = Color.parseColor("#33b5e5");
    private int backgroundSelected = Color.parseColor("#000000");

    //region Variables Listeners
    private View.OnClickListener onClick_toolbar = v -> openNavigationView();
    private View.OnClickListener onClick_rlMainActivity = v -> clickScreen();
    private View.OnClickListener onClick_btnPlus = v -> enlarge_minimize_Text("enlarge");
    private View.OnClickListener onClick_btnMinus = v -> enlarge_minimize_Text("");
    private ColorPickerDialog.OnColorChangedListener onColorChanged_btnColor = new ColorPickerDialog.OnColorChangedListener(){
        public void onColorChanged(int color) {
            colorSelected = color;
            rlColor.setBackgroundColor(colorSelected);
            ledText.setColorText(colorSelected);
        }
    };
    private View.OnClickListener onClick_btnColor = new View.OnClickListener() {
        public void onClick (View  v){
            colorPickerDialog = new ColorPickerDialog(MainActivity.this, colorSelected);
            colorPickerDialog.setAlphaSliderVisible(false);
            colorPickerDialog.setHexValueEnabled(false);
            colorPickerDialog.setTitle(R.string.select_a_color);
            colorPickerDialog.setOnColorChangedListener(onColorChanged_btnColor);
            colorPickerDialog.show();
        }
    };
    private ColorPickerDialog.OnColorChangedListener onColorChanged_btnBackgroundColor = new ColorPickerDialog.OnColorChangedListener(){
        public void onColorChanged(int color) {
            backgroundSelected = color;
            rlBackgroundColor.setBackgroundColor(backgroundSelected);
            ledText.setColorBackgroundColor(backgroundSelected);
        }
    };
    private View.OnClickListener onClick_btnBackgroundColor = new View.OnClickListener() {
        public void onClick (View  v){
            colorPickerDialog = new ColorPickerDialog(MainActivity.this, backgroundSelected);
            colorPickerDialog.setAlphaSliderVisible(true);
            colorPickerDialog.setHexValueEnabled(true);
            colorPickerDialog.setTitle(R.string.select_a_color);
            colorPickerDialog.setOnColorChangedListener(onColorChanged_btnBackgroundColor);
            colorPickerDialog.show();
        }
    };
    /*private TextWatcher etTextTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            ledText.setText(s.toString());
        }
    };*/
    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = item -> {
        int id = item.getItemId();
        if (id == R.id.save_options) {
            Toast.makeText(MainActivity.this, "My Account", Toast.LENGTH_SHORT).show();
        }
        return true;
    };
    private View.OnClickListener onClick_btnStop = new View.OnClickListener() {
        public void onClick(View v) {
            changeTextBtnStop(ledText.stopMarquee());
            StopResume = !StopResume;
        }
    };
    private SeekBar.OnSeekBarChangeListener sbChange_sbSpeed = new SeekBar.OnSeekBarChangeListener() {
        public void onProgressChanged (SeekBar seekBar,int progress, boolean fromUser){
            if(progress == 0) {
                progress = 100;
            }

            sbTextSpeed.setProgress(progress);
            String speed = " " + progress;
            tvSpeedInNumber.setText(speed);
            ledText.setSpeed(progress);
        }

        public void onStartTrackingTouch (SeekBar seekBar){

        }

        public void onStopTrackingTouch (SeekBar seekBar){

        }
    };
    private CompoundButton.OnCheckedChangeListener switchCompatOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener(){
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked)
                tvTextDisplayed.startAnimation(animBlink);
            else
                tvTextDisplayed.clearAnimation();
        }
    };
    private CompoundButton.OnCheckedChangeListener switchPoliceSirenOnCheckedChangeListener = (buttonView, isChecked) -> {
        if(isChecked){

        }else{
        }
    };
    private View.OnClickListener onClick_SaveCurrentConfiguration = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LedTextRealm ledTextRealm = new LedTextRealm();

            if (ledText != null) {
                Realm realm = Utilities.createRealmInstance(MainActivity.this);
                ledTextRealm.setId(Utilities.getNextIdLedTextRealm(realm));

                int backg = ledText.getColorBackgroundColor();
                ledTextRealm.setText(ledText.getText());
                ledTextRealm.setSpeed(ledText.getSpeed());
                ledTextRealm.setDirection(ledText.isDirection());
                ledTextRealm.setSize(ledText.getSize());
                ledTextRealm.setColorBackgroundColor(backg);
                ledTextRealm.setColorText(ledText.getColorText());
                ledTextRealm.setCreated_at(Calendar.getInstance().getTime());

                mRealm.executeTransaction(mRealm -> {
                    mRealm.copyToRealmOrUpdate(ledTextRealm);
                    loadRvSavedConfiguration();
                });
            }
        }
    };
    //endregion
    //endregion

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);
        mRealm = Realm.getDefaultInstance();

        initToolbar();
        initDrawer();
        initAll();
    }

    private void initAll(){
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);

        animBlink = AnimationUtils.loadAnimation(this, R.anim.blink);
        NavigationView navigationView = findViewById(R.id.nv_left_menu);
        NavigationView navigationViewRight = findViewById(R.id.nv_right_menu);
        RelativeLayout rlMainActivity = findViewById(R.id.rlMainActivity);
        tvTextDisplayed = findViewById(R.id.tvTextDisplayed);
        TextInputEditText etText = findViewById(R.id.etText);
        Button btnLeftDirection = findViewById(R.id.btnLeftDirection);
        Button btnRightDirection = findViewById(R.id.btnRightDirection);
        btnStop = findViewById(R.id.btnStop);
        Button btnMinus = findViewById(R.id.btnMinus);
        Button btnPlus = findViewById(R.id.btnPlus);
        Button btnColor = findViewById(R.id.btnColor);
        Button btnBackgroundColor = findViewById(R.id.btnBackgroundColor);
        tvSpeedInNumber = findViewById(R.id.tvSpeedInNumber);
        SwitchMaterial swtBlink = findViewById(R.id.swtBlink);
        SwitchMaterial swtPoliceSiren = findViewById(R.id.swtPoliceSiren);
        sbTextSpeed = findViewById(R.id.sbTextSpeed);
        rlColor = findViewById(R.id.rlColor);
        rlBackgroundColor = findViewById(R.id.rlBackgroundColor);

        toolbar.setNavigationOnClickListener(onClick_toolbar);
        navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
        navigationViewRight.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
        rlMainActivity.setOnClickListener(onClick_rlMainActivity);
        btnStop.setOnClickListener(onClick_btnStop);
        btnColor.setOnClickListener(onClick_btnColor);
        btnPlus.setOnClickListener(onClick_btnPlus);
        btnMinus.setOnClickListener(onClick_btnMinus);
        btnBackgroundColor.setOnClickListener(onClick_btnBackgroundColor);
        sbTextSpeed.setOnSeekBarChangeListener(sbChange_sbSpeed);
        /*etText.addTextChangedListener(etTextTextWatcher);*/
        swtBlink.setOnCheckedChangeListener(switchCompatOnCheckedChangeListener);
        swtPoliceSiren.setOnCheckedChangeListener(switchPoliceSirenOnCheckedChangeListener);
        animBlink.setAnimationListener(this);

        etText.setWidth((size.x) / 2);
        etText.setLeft(((size.x) / 2) / 2);
        tvTextDisplayed.setHeight((size.y / 10) * 4);
        int TEXT_SIZE = 20;
        ledText = new LedText(tvTextDisplayed, "Holaaaaaaa", 20, true, TEXT_SIZE, backgroundSelected, colorSelected);
        sbTextSpeed.setProgress(5000);

       /* Animation animationToRight = new TranslateAnimation(-400,400, 0, 0);
        animationToRight.setDuration(12000);
        animationToRight.setRepeatMode(Animation.RESTART);
        animationToRight.setRepeatCount(Animation.INFINITE);
        tvTextDisplayed.setAnimation(animationToRight);*/

        initRecyclerListOfFonts();
        etText.addTextChangedListener(new TextWatcherLedText(this, etText, ledText));
    }

    private void initToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_options);
        }
    }

    private void initDrawer(){
        drawerLayout = findViewById(R.id.activity_main);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        initRightDrawer();
    }

    private void initRightDrawer(){
        findViewById(R.id.btnSaveCurrentConfiguration).setOnClickListener(onClick_SaveCurrentConfiguration);
        loadRvSavedConfiguration();
    }

    private void loadRvSavedConfiguration(){
        RealmResults<LedTextRealm> allLedTextConfigurations = mRealm.where(LedTextRealm.class).findAll();
        List<LedTextRealm> lsLedTextRealm = new ArrayList<>(allLedTextConfigurations);

        caConfigurations caConfigurations = new caConfigurations(this,this);
        RecyclerView lvMenuTickets = findViewById(R.id.rvSavedConfigurations);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        lvMenuTickets.setLayoutManager(mLayoutManager);
        lvMenuTickets.setItemAnimator(new DefaultItemAnimator());
        lvMenuTickets.setAdapter(caConfigurations);

        caConfigurations.setNewList(lsLedTextRealm);
    }

    private void initRecyclerListOfFonts(){
        com.machacode.oscarmorquecho.ledtext.adapters.caFonts caFonts = new caFonts(this, this);
        RecyclerView lvMenuTickets = findViewById(R.id.rvFonts);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        lvMenuTickets.setLayoutManager(mLayoutManager);
        lvMenuTickets.setItemAnimator(new DefaultItemAnimator());
        lvMenuTickets.setAdapter(caFonts);

        List<Font> lsFonts = new ArrayList<>();
        lsFonts.add(new Font(1, "Sans Serif", "sans_serif", DEFAULT));
        lsFonts.add(new Font(2, "Monospace", "monospace", DEFAULT));
        lsFonts.add(new Font(3, "Serif", "serif", DEFAULT));
        lsFonts.add(new Font(4, "Default Bold", "default_bold", DEFAULT));
        lsFonts.add(new Font(5, "Default", "default", DEFAULT));
        lsFonts.add(new Font(6, "Turnaround", "fonts/Turnaround.ttf", CUSTOM));

        caFonts.setNewList(lsFonts);
    }

    protected void enlarge_minimize_Text(String action){
        ledText.setSize(action);
    }

    protected void openNavigationView(){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    protected void changeTextBtnStop(boolean stopResume){
        btnStop.setText((stopResume) ? R.string.resume : R.string.stop);
    }

    public void clickScreen(){
        if(clickScreen){
            hideActionBar();
        }else{
            showActionBar();
        }
        clickScreen = !clickScreen;
    }

    protected void hideActionBar(){
        final ActionBar ab = getSupportActionBar();
        if (ab != null && ab.isShowing()) {
            if(toolbar != null) {
                toolbar.animate().translationY(-112).setDuration(600L).withEndAction(ab::hide).start();
            } else {
                ab.hide();
            }
        }
    }

    protected void showActionBar(){
        ActionBar ab = getSupportActionBar();
        if (ab != null && !ab.isShowing()) {
            ab.show();
            if(toolbar != null) {
                toolbar.animate().translationY(0).setDuration(600L).start();
            }
        }
    }

    public void onAnimationEnd(Animation animation){
        // if (animation == animBlink) { }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_options) {
            drawerLayout.openDrawer(GravityCompat.END);
        }
        return true;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onClick(String fontPath, int TYPE_FONT) {
        tvTextDisplayed.setTypeface(Utilities.getTypeFace(fontPath, TYPE_FONT, this));
    }

    @Override
    public void onClickConfiguration(LedTextRealm ledTextRealm) {
        ledText = new LedText(  tvTextDisplayed,
                                ledTextRealm.getText(),
                                ledTextRealm.getSpeed(),
                                ledTextRealm.isDirection(),
                                ledTextRealm.getSize(),
                                ledTextRealm.getColorBackgroundColor(),
                                ledTextRealm.getColorText());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("ledText", ledText.getText());
        outState.putInt("size", ledText.getSize());
        outState.putInt("speed", ledText.getSpeed());
        outState.putInt("colorText", ledText.getColorText());
        outState.putInt("colorBackground", ledText.getColorBackgroundColor());
        outState.putBoolean("direction", ledText.isDirection());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String text = savedInstanceState.getString("ledText");
        int size = savedInstanceState.getInt("size");
        int speed = savedInstanceState.getInt("speed");
        int colorText = savedInstanceState.getInt("colorText");
        int colorBackground = savedInstanceState.getInt("colorBackground");
        boolean direction = savedInstanceState.getBoolean("direction");

        ledText = new LedText(tvTextDisplayed, text, speed, direction, size, colorBackground, colorText);
    }
}