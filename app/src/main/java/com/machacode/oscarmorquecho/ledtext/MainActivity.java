package com.machacode.oscarmorquecho.ledtext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.machacode.oscarmorquecho.ledtext.adapters.caFonts;
import com.machacode.oscarmorquecho.ledtext.interfaces.onClickActions;
import com.machacode.oscarmorquecho.ledtext.models.Font;
import com.machacode.oscarmorquecho.ledtext.models.LedText;
import com.machacode.oscarmorquecho.ledtext.utils.ScrollTextView;

import net.margaritov.preference.colorpicker.ColorPickerDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements Animation.AnimationListener, onClickActions {
    private static final int DEFAULT = 1;
    private static final int CUSTOM = 2;

    private Activity activity;
    private ScrollTextView tvTextDisplayed;
    private int TEXT_SIZE = 20;
    private float STEP_TEXT_SIZE = 10;
    private boolean StopResume = false;
    private Button btnLeftDirection;
    private Button btnRightDirection;
    private Button btnStop;
    private Button btnMinus;
    private Button btnPlus;
    private Button btnStart;
    private Button btnColor;
    private Button btnBackgroundColor;
    private EditText etText;
    private TextView tvFuente;
    private TextView tvSpeedInNumber;
    private SwitchCompat swtBlink;
    private SwitchCompat swtPoliceSiren;
    private SeekBar sbTextSpeed;
    private Animation animBlink;
    private RelativeLayout rlMainActivity;
    private RelativeLayout rlColor;
    private RelativeLayout rlBackgroundColor;
    private boolean clickScreen = true;
    private Toolbar toolbar;
    private ActionBar actionBar;

    private LedText text;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private NavigationView navigationViewRight;

    private ColorPickerDialog colorPickerDialog;
    private int colorSelected = Color.parseColor("#33b5e5");
    private int backgroundSelected = Color.parseColor("#000000");

    private caFonts caFonts;

    private View.OnClickListener onClick_toolbar = new View.OnClickListener(){
        public void onClick(View v) {
            openNavigationView();
        }
    };
    private View.OnClickListener onClick_rlMainActivity = new View.OnClickListener(){
        public void onClick(View v) {
            clickScreen();
        }
    };
    private View.OnClickListener onClick_btnPlus = new View.OnClickListener(){
        public void onClick(View v) { enlarge_minimize_Text("enlarge");
        }
    };
    private View.OnClickListener onClick_btnMinus = new View.OnClickListener(){
        public void onClick(View v) { enlarge_minimize_Text(""); }
    };

    private ColorPickerDialog.OnColorChangedListener onColorChanged_btnColor = new ColorPickerDialog.OnColorChangedListener(){
        public void onColorChanged(int color) {
            colorSelected = color;
            rlColor.setBackgroundColor(colorSelected);
            text.setColorText(colorSelected);
        }
    };

    private View.OnClickListener onClick_btnColor = new View.OnClickListener(){
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
            text.setColorBackgroundColor(backgroundSelected);
        }
    };

    private View.OnClickListener onClick_btnBackgroundColor = new View.OnClickListener(){
        public void onClick (View  v){
            colorPickerDialog = new ColorPickerDialog(MainActivity.this, backgroundSelected);
            colorPickerDialog.setAlphaSliderVisible(true);
            colorPickerDialog.setHexValueEnabled(true);
            colorPickerDialog.setTitle(R.string.select_a_color);
            colorPickerDialog.setOnColorChangedListener(onColorChanged_btnBackgroundColor);
            colorPickerDialog.show();
        }
    };

    private TextWatcher etTextTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            text.setText(s.toString());
        }
    };

    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener(){
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            switch(id){
                case R.id.account:
                    Toast.makeText(MainActivity.this, "My Account",Toast.LENGTH_SHORT).show();

                default:
                    return true;
            }
        }
    };

    private View.OnClickListener onClick_btnStop = new View.OnClickListener(){
        public void onClick(View v) {
            changeTextBtnStop(text.stopMarquee());
            StopResume = !StopResume;
        }
    };

    private SeekBar.OnSeekBarChangeListener sbChange_sbSpeed = new SeekBar.OnSeekBarChangeListener(){
        public void onProgressChanged (SeekBar seekBar,int progress, boolean fromUser){
            if(progress == 0) {
                progress = 100;
            }

            sbTextSpeed.setProgress(progress);
            String speed = " " + String.valueOf(progress);
            tvSpeedInNumber.setText(speed);
            text.setSpeed(progress);
        }

        public void onStartTrackingTouch (SeekBar seekBar){

        }

        public void onStopTrackingTouch (SeekBar seekBar){

        }
    };

    private CompoundButton.OnCheckedChangeListener switchCompatOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener(){
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                tvTextDisplayed.startAnimation(animBlink);
            }else{
                tvTextDisplayed.clearAnimation();
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener switchPoliceSirenOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener(){
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){

            }else{
            }
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);

        animBlink = AnimationUtils.loadAnimation(this, R.anim.blink);
        navigationView = findViewById(R.id.nv_left_menu);
        navigationViewRight = findViewById(R.id.nv_right_menu);
        rlMainActivity = findViewById(R.id.rlMainActivity);
        tvTextDisplayed = findViewById(R.id.tvTextDisplayed);
        etText = findViewById(R.id.etText);
        btnLeftDirection = findViewById(R.id.btnLeftDirection);
        btnRightDirection = findViewById(R.id.btnRightDirection);
        btnStop = findViewById(R.id.btnStop);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        btnColor = findViewById(R.id.btnColor);
        btnBackgroundColor = findViewById(R.id.btnBackgroundColor);
        tvSpeedInNumber = findViewById(R.id.tvSpeedInNumber);
        swtBlink = findViewById(R.id.swtBlink);
        swtPoliceSiren = findViewById(R.id.swtPoliceSiren);
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
        etText.addTextChangedListener(etTextTextWatcher);
        swtBlink.setOnCheckedChangeListener(switchCompatOnCheckedChangeListener);
        swtPoliceSiren.setOnCheckedChangeListener(switchPoliceSirenOnCheckedChangeListener);
        animBlink.setAnimationListener(this);

        drawerLayout = findViewById(R.id.activity_main);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        etText.setWidth((size.x) / 2);
        etText.setLeft(((size.x) / 2) / 2);
        tvTextDisplayed.setHeight((size.y / 10) * 4);
        text = new LedText(tvTextDisplayed, "Holaaaaaaa", 20, true, TEXT_SIZE, backgroundSelected, colorSelected);
        sbTextSpeed.setProgress(5000);

        caFonts = new caFonts(this);
        RecyclerView lvMenuTickets = findViewById(R.id.rvFonts);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        //lvMenuTickets.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lvMenuTickets.setLayoutManager(mLayoutManager);
        lvMenuTickets.setItemAnimator(new DefaultItemAnimator());
        lvMenuTickets.setAdapter(caFonts);

        List<Font> lsFonts = new ArrayList<>();
        lsFonts.add(new Font("Sans Serif", "sans_serif", DEFAULT));
        lsFonts.add(new Font("Monospace", "monospace", DEFAULT));
        lsFonts.add(new Font("Serif", "serif", DEFAULT));
        lsFonts.add(new Font("Default Bold", "default_bold", DEFAULT));
        lsFonts.add(new Font("Default", "default", DEFAULT));
        lsFonts.add(new Font("Turnaround", "fonts/Turnaround.ttf", CUSTOM));

        caFonts.setNewList(lsFonts);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.account:
                Toast.makeText(MainActivity.this, "My Account",Toast.LENGTH_SHORT).show();

            default:
                return true;
        }
    }

    protected void enlarge_minimize_Text(String action){
        text.setSize(action);
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
                toolbar.animate().translationY(-112).setDuration(600L).withEndAction(new Runnable(){
                    @Override
                    public void run() {
                        ab.hide();
                    }
                }).start();
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
        if (animation == animBlink) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_options:
                drawerLayout.openDrawer(GravityCompat.END);
                break;
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
        if(TYPE_FONT == DEFAULT){
            Typeface typeface = Typeface.SANS_SERIF;
            switch(fontPath){
                case "sans_serif":
                    typeface = Typeface.SANS_SERIF;
                    break;

                case "monospace":
                    typeface = Typeface.MONOSPACE;
                    break;

                case "serif":
                    typeface = Typeface.SERIF;
                    break;

                case "default_bold":
                    typeface = Typeface.DEFAULT_BOLD;
                    break;

                case "default":
                    typeface = Typeface.DEFAULT;
                    break;
            }
            tvTextDisplayed.setTypeface(typeface);
        }else if(TYPE_FONT == CUSTOM){
            tvTextDisplayed.setTypeface(Typeface.createFromAsset(getAssets(), fontPath));
        }
    }
}