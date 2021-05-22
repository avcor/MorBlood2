package com.unisparc.morblood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.jgabrielfreitas.core.BlurImageView;
import com.unisparc.morblood.LoginRegister;
import com.unisparc.morblood.fragments.DonorFragment;
import com.unisparc.morblood.fragments.RecipientFragment;
import com.unisparc.morblood.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    String TAG = "mainActivity";

    final String MYPREF = "mypref";
    int AUTHUI_REQUEST_CODE = 10001;

    BlurImageView blurImageView;
    Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
        private DonorFragment donorFragment;
        private RecipientFragment recipientFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolBar);
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
                case R.id.requestList_Menu :
                    Intent intent = new Intent(this, RecipientRequestAccept.class);
                    startActivity(intent);
                    return true;
                case R.id.profile_Menu :
                    Toast.makeText(this, "not now boi", Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    return false;
            }
        });
//        BlurView blurView = findViewById(R.id.blurview);
//        float radius = 10f;
//
//        View decorView = getWindow().getDecorView();
//        //ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
//        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
//        //set drawable to draw in the beginning of each blurred frame (Optional).
//        //can be used in case your layout has a lot of transparent space and your content
//        //gets kinda lost after after blur is applied.
//        Drawable windowBackground = decorView.getBackground();
//
//        blurView.setupWith(rootView)
//                .setFrameClearDrawable(windowBackground)
//                .setBlurAlgorithm(new RenderScriptBlur(this))
//                .setBlurRadius(radius)
//                .setBlurAutoUpdate(true)
//                .setHasFixedTransformationMatrix(true);

//        blurImageView.setBlur(20);
        donorFragment = new DonorFragment();
        recipientFragment = new RecipientFragment();

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),1);
        viewPagerAdapter.addFragment(recipientFragment,"recipient");
        viewPagerAdapter.addFragment(donorFragment,"donor");
        viewPager.setAdapter(viewPagerAdapter);


//        autosearchList = new ArrayList<>(Arrays.asList(bloog_grp));
//        trial(autosearchList);
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmenttitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            fragmenttitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position );
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmenttitle.get(position);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
        }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if(firebaseAuth.getCurrentUser() == null){
//            List<AuthUI.IdpConfig> provider1 = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());
//
//            Intent intent = AuthUI.getInstance()
//                    .createSignInIntentBuilder()
//                    .setAvailableProviders(provider1)
//                    .setAlwaysShowSignInMethodScreen(true)
//                    .setIsSmartLockEnabled(false)
//                    .build();
//
//            startActivityForResult(intent,AUTHUI_REQUEST_CODE);

            Intent intent =  new Intent(this, LoginRegister.class);
            startActivityForResult(intent,AUTHUI_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == AUTHUI_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                Toast.makeText(this,"ok ", Toast.LENGTH_LONG).show();
                // this will start new data_onboard activity (new version one)
                Intent intent = new Intent(getApplicationContext(), data_onboard.class);
                startActivity(intent);
            }
            else{
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if(response == null){
                    // user cancelled the sign in
                    Toast.makeText(this,"You cancelled the sign in ", Toast.LENGTH_LONG).show();
                }else {
                    // response.getError
                    Toast.makeText(this,"Error, try again",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Warning!!!");
        alertDialogBuilder.setIcon(R.drawable.ic_baseline_warning_24);
        alertDialogBuilder.setMessage("Where you want to go? \n All your data will be lost!");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Exit", (dialogInterface, i) -> {
            finish();
            System.exit(0);
        });

        alertDialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> Toast.makeText(getApplicationContext(),"Cancelled",Toast.LENGTH_LONG).show());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



}