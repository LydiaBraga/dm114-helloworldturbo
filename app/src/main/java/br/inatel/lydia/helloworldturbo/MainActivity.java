package br.inatel.lydia.helloworldturbo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import br.inatel.lydia.helloworldturbo.fragments.GCMFragment;
import br.inatel.lydia.helloworldturbo.fragments.ListaPedidosFragment;
import br.inatel.lydia.helloworldturbo.fragments.OrdersFragment;
import br.inatel.lydia.helloworldturbo.fragments.SettingsFragment;
import br.inatel.lydia.helloworldturbo.fragments.Tela1Fragment;
import br.inatel.lydia.helloworldturbo.models.OrderInfo;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private OrderInfo orderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = this.getIntent();
        if (intent.hasExtra("orderInfo")) {
            orderInfo = (OrderInfo) intent.getSerializableExtra("orderInfo");

            if (orderInfo != null) {
                displayFragment(R.id.nav_gcm);
            }
        } else if (savedInstanceState == null) {
            displayFragment(R.id.nav_tela1);
        }

        createNotificationChannel();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displayFragment(item.getItemId());
        return true;
    }

    private void displayFragment(int fragmentId) {
        Class fragmentClass;
        Fragment fragment = null;
        int backStackEntryCount;
        backStackEntryCount = getFragmentManager().getBackStackEntryCount();

        for (int j = 0; j < backStackEntryCount; j++) {
            getFragmentManager().popBackStack();
        }

        try {
            switch (fragmentId) {
                case R.id.nav_tela1:
                    fragmentClass = Tela1Fragment.class;
                    fragment = (Fragment) fragmentClass.newInstance();
                    break;
                case R.id.nav_tela2:
                    fragmentClass = OrdersFragment.class;
                    fragment = (Fragment) fragmentClass.newInstance();
                    break;
                case R.id.nav_settings:
                    fragmentClass = SettingsFragment.class;
                    fragment = (Fragment) fragmentClass.newInstance();
                    break;
                case R.id.nav_gcm:
                    fragmentClass = GCMFragment.class;
                    fragment = (Fragment) fragmentClass.newInstance();
                    if (orderInfo != null) {
                        Bundle args = new Bundle();
                        args.putSerializable("orderInfo", orderInfo);
                        fragment.setArguments(args);
                        orderInfo = null;
                    }
                    break;
                default:
                    fragmentClass = Tela1Fragment.class;
                    fragment = (Fragment) fragmentClass.newInstance();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.hasExtra("orderInfo")) {
            orderInfo = (OrderInfo) intent.getSerializableExtra("orderInfo");

            if (orderInfo != null) {
                displayFragment(R.id.nav_gcm);
            }
        }
        super.onNewIntent(intent);
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", "SiecolaVendas", importance);
            channel.setDescription("Orders");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
