package com.example.cjm.application1.PingPong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.cjm.application1.R;

/**
 * Created by cj on 1/9/2015.
 */
public class Settings extends Activity implements RadioGroup.OnCheckedChangeListener {
    private static Context settingsContext;
    private Button back;
    private RadioGroup speed, number;
    private static boolean isSettingsAltered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        isSettingsAltered = true;

        createRadioGroupsAndButtons();
        settingsContext = getApplicationContext();

        this.speed.setOnCheckedChangeListener(this);
        this.number.setOnCheckedChangeListener(this);

        SharedPreferences sp = getSharedPreferences("SHARED", MODE_MULTI_PROCESS);
        SharedPreferences.Editor spEdit = sp.edit();

        spEdit.putInt("RG1", speed.getCheckedRadioButtonId());
        spEdit.putInt("RG2", number.getCheckedRadioButtonId());
        spEdit.commit();

        this.back = (Button) findViewById(R.id.btn_settings_back);

        this.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, Menu.class);
                startActivity(intent);
            }
        });
    }

    public static boolean isSettingsAltered() {
        return isSettingsAltered;
    }

    public static Context getSettingsContext() {
        return settingsContext;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("Shared", MODE_PRIVATE);
        int grp2 = sharedPreferences.getInt("RG1", 1);
        int grp3 = sharedPreferences.getInt("RG2", 1);

        speed.check(grp2);
        number.check(grp3);
    }

    private void createRadioGroupsAndButtons() {
        this.speed = (RadioGroup) findViewById(R.id.radiogroup_ball_speed);
        this.number = (RadioGroup) findViewById(R.id.radiogroup_number_to_game_over);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int grp1 = speed.getCheckedRadioButtonId();
        int grp2 = number.getCheckedRadioButtonId();

        SharedPreferences settingsPreferences = getSharedPreferences("SHARED", MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = settingsPreferences.edit();
        editor.putInt("RG1", grp1);
        editor.putInt("RG2", grp2);

        editor.commit();
    }
}
