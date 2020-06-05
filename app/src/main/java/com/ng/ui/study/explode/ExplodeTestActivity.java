package com.ng.ui.study.explode;

import android.animation.Animator;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ng.ui.MainActivity;
import com.ng.ui.R;

public class ExplodeTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_explode);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ParticleView particleAnimator = new ParticleView(ExplodeTestActivity.this,3000);
        particleAnimator.setOnAnimationListener(new ParticleView.OnAnimationListener() {
            @Override
            public void onAnimationStart(View view, Animator animation) {
                view.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onAnimationEnd(View view, Animator animation) {

            }
        });
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                particleAnimator.boom(v);//getWindow().getDecorView().findViewById(android.R.id.content)
            }
        });






    }
}
