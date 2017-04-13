package biz.sendyou.senduandroid.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.List;

import biz.sendyou.senduandroid.ActivityManager;
import biz.sendyou.senduandroid.Fragment.AddressBookFragment;
import biz.sendyou.senduandroid.Fragment.CardSelectDialogFragment;
import biz.sendyou.senduandroid.Fragment.CashFragment;
import biz.sendyou.senduandroid.Fragment.CreateCardFragment;
import biz.sendyou.senduandroid.Fragment.DrawFragment;
import biz.sendyou.senduandroid.Fragment.FrontFragment;
import biz.sendyou.senduandroid.Fragment.OrderCardFragment;

import biz.sendyou.senduandroid.Fragment.OrderFinishFragment;
import biz.sendyou.senduandroid.Fragment.SelectTemplateFragment;
import biz.sendyou.senduandroid.Fragment.SendCheckFragment;
import biz.sendyou.senduandroid.Fragment.SettingFragment;
import biz.sendyou.senduandroid.Fragment.SignInFragment;
import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.Service.Usr;
import biz.sendyou.senduandroid.UserInfoManager;
import biz.sendyou.senduandroid.datatype.Address;
import biz.sendyou.senduandroid.datatype.CardTemplate;
import biz.sendyou.senduandroid.thread.TemplateDownloadThread;

public class NavigationDrawerActivity extends AppCompatActivity
        implements CashFragment.OnFragmentInteractionListener,OrderFinishFragment.OnFragmentInteractionListener,NavigationView.OnNavigationItemSelectedListener,SignInFragment.OnFragmentInteractionListener,FrontFragment.OnFragmentInteractionListener, AddressBookFragment.OnListFragmentInteractionListener, CreateCardFragment.OnFragmentInteractionListener,SelectTemplateFragment.OnListFragmentInteractionListener, OrderCardFragment.OnFragmentInteractionListener, CardSelectDialogFragment.OnFragmentInteractionListener, DrawFragment.OnFragmentInteractionListener {

    private final String TAG = "NavigationDrawer";
    final int DEFAULT_LODING_COUNT = 12;
    private long backKeyPressedTime = 0;
    private Toast toast;
    private NavigationDrawerActivity navigationDrawerActivity;
    public static GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        setUniversalImageLoader();

        UserInfoManager.getInstance().setEmail("enoxaiming@naver.com");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);

        TextView usrName = (TextView) view.findViewById(R.id.username);
        usrName.setText(UserInfoManager.getInstance().getUserName());
        TextView place = (TextView) view.findViewById(R.id.textView3);
        place.setText(UserInfoManager.getInstance().getJusoAddress());
        TextView num = (TextView) view.findViewById(R.id.textView);
        num.setText(UserInfoManager.getInstance().getNumAddress());

        ImageView btn = (ImageView) view.findViewById(R.id.imageView9);

        /*
        * 결국 이 loginActivity를 종료하고 싶은거 아님? 왜 여기서 이렇게 액티비티 객체 자체를 저장하고 종료하는거..?
        * 액티비티 호출하기 전에 종료하면ㄷ안댐?
        LoginActivity loginActivity = LoginActivity.loginActivity;
        loginActivity.finish();
        */

        navigationDrawerActivity = this;

        ActivityManager.getInstance().setNavigationDrawerAcitivity(navigationDrawerActivity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("SendU");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null) {
            changeFragmentToFront();
        }
    }

    public void setToolBarTitle(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                showGuide();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                navigationDrawerActivity.finish();
                toast.cancel();
            }
            super.onBackPressed();
        }

    }

    public void setUniversalImageLoader(){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(Usr.getContext())
                .threadPriority(Thread.NORM_PRIORITY -2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs()
                .build();

        ImageLoader.getInstance().init(config);
    }

    public void showGuide() {
        toast = Toast.makeText(navigationDrawerActivity,
                "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_order) {
            // Handle the camera action
            changeFragmentToSelectTemplate();
        } else if (id == R.id.nav_address) {
            DialogHtmlView();
        } else if (id == R.id.nav_storage) {
            DialogHtmlView();
        } else if (id == R.id.nav_sendcheck) {
            changeFragmentToSendCheck();
        } else if (id == R.id.nav_settings) {
            changeFragmentToSetting();
        } else if (id == R.id.nav_logout) {
            Usr user = (Usr) getApplicationContext();
            if (user.getFacebookToken() != null) {
                LoginManager.getInstance().logOut();
                intentActivty(NavigationDrawerActivity.this, LoginActivity.class);
            }
            else if (user.getId() != null) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.w(TAG, "Google Logout");
                        intentActivty(NavigationDrawerActivity.this, LoginActivity.class);
                    }
                });
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //TODO Refactor methods
    private void changeFragmentToAddressBook(){
        AddressBookFragment addressBookFragment = AddressBookFragment.newInstance(1);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrameLayout,addressBookFragment);
        fragmentTransaction.commit();
    }

    private void changeFragmentToFront(){
        FrontFragment mFrontFragment = FrontFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrameLayout,mFrontFragment);
        fragmentTransaction.commit();
    }

    private void changeFragmentToSendCheck(){
        SendCheckFragment mSendCheckFragment = SendCheckFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrameLayout,mSendCheckFragment);
        fragmentTransaction.commit();
    }

    private void changeFragmentToSetting() {
        SettingFragment mSettingFragment = SettingFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrameLayout,mSettingFragment);
        fragmentTransaction.commit();

    }

    private void changeFragmentToSelectTemplate() {
        Log.w(TAG, "start s3 connect");
        TemplateDownloadThread templateDownloadThread = new TemplateDownloadThread();

        templateDownloadThread.start();
        try {
            templateDownloadThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<CardTemplate> templates = new ArrayList<>();
        List<String> thumbUrls = templateDownloadThread.getThumb_keys();
        //String result = SendByHttp(); // 메시지를 서버에 보냄
        //String[][] parsedData = jsonParserList(result);

        for(int i =0; i <thumbUrls.size(); i++){

            if(thumbUrls.get(i).contains(".jpg") || thumbUrls.get(i).contains("png")){
            CardTemplate cardTemplate = new CardTemplate();
            cardTemplate.setUrl(thumbUrls.get(i));

            templates.add(cardTemplate);
            }
        }

        SelectTemplateFragment mSelectTemplateFragment = SelectTemplateFragment.newInstance(templates ,2);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrameLayout, mSelectTemplateFragment);
        fragmentTransaction.commit();
    }

    private void changeFragmentToCreateCrad() {
        CreateCardFragment mCreateCardFragment = CreateCardFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrameLayout, mCreateCardFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onListFragmentInteraction(Address item) {

    }

    @Override
    public void onListFragmentInteraction(CardTemplate item) {

    }

    public void intentActivty(Context packageContext, Class cls) {
        Intent mIntent = new Intent(packageContext, cls);
        startActivity(mIntent);
        finish();
    }

    private void DialogHtmlView() {
        AlertDialog.Builder ab = new AlertDialog.Builder(NavigationDrawerActivity.this);
        ab.setMessage("준비중인 기능입니다!");
        ab.setPositiveButton("ok", null);
        ab.show();
    }
}
