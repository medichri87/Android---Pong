package com.example.cjm.application1.PingPong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cjm.application1.R;

/**
 * Created by cj on 1/9/2015.
 */
public class Instructions extends Activity{

    private Button back;
    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructions);
        back = (Button) findViewById(R.id.btn_instruction_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Menu.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
