package lk.iot.lmsrealtime1.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import java.util.ArrayList;

import lk.iot.lmsrealtime1.R;
import lk.iot.lmsrealtime1.adapter.MenuAdapter;
import lk.iot.lmsrealtime1.data.FirebaseDAO;
import lk.iot.lmsrealtime1.model.MyMenu;
public class MainActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    RecyclerView rvMenuItem;
    MenuAdapter adapter;
    Drawer result = null;
    AccountHeader headerResult = null;
    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    //FirebaseFirestore fStore;
    String userID;
    LinearLayout progressBar;


    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolBar =  findViewById( R.id.tb_main );
        progressBar =  findViewById( R.id.progressBar );

        fAuth = FirebaseAuth.getInstance();
        //fStore = FirebaseFirestore.getInstance();
        userID = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";
        mDatabase = FirebaseDatabase.getInstance().getReference();
        rvMenuItem = (RecyclerView) findViewById( R.id.rvMenuItem );
        //layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager = new GridLayoutManager( MainActivity.this, 2, RecyclerView.VERTICAL, false );
        rvMenuItem.setLayoutManager( layoutManager );
        rvMenuItem.setHasFixedSize( true );
        String[] menuLabels = {"Appliance", "Manual Control", "ScheduleActivity", "Cost & Power","Customer Info"};
        insertNavigationDrawer( savedInstanceState );

        int[] menuImages = {
                R.drawable.setting,
                R.drawable.control,
                R.drawable.schedule,
                R.drawable.cost_power,
                R.drawable.customer_info,

        };
        ArrayList<MyMenu> arrayList = new ArrayList<>();

        for (int i = 0; i < menuLabels.length; i++) {
            String menuName = menuLabels[i];
            int img = menuImages[i];
            arrayList.add( new MyMenu( menuName, img ) );
        }
        adapter = new MenuAdapter( getApplicationContext(), arrayList );
        rvMenuItem.setAdapter( adapter );


      //  downloadFromFirebase();


    }

    void downloadFromFirebase(){

        progressBar.setVisibility(View.VISIBLE);
        new FirebaseDAO(MainActivity.this).getCategory("category1");
        new FirebaseDAO(MainActivity.this).getCategory("category2");
        new FirebaseDAO(MainActivity.this).getCategory("category3");
       // new FirebaseDAO(MainActivity.this).getManualControl();
       // new FirebaseDAO(MainActivity.this).getAutomaicSchedule();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        }, 6600);
      //  new ManualControlDAO(MainActivity.this).insertNewLabel(userID);

    }
    @Override
    protected void onResume() {
        super.onResume();
       // downloadFromFirebase();
    }

    private void insertNavigationDrawer(Bundle savedInstanceState) {
        headerResult = new AccountHeaderBuilder()
                .withActivity( this )
                .withHeaderBackground( R.drawable.dr_back )
                .withTranslucentStatusBar( true )

                .withOnAccountHeaderListener( new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {

                        return false;
                    }
                } )
                .withSavedInstance( savedInstanceState )
                .build();

        //Drawer
        DrawerBuilder drawerBuilder = new DrawerBuilder();
        drawerBuilder.withActivity(this);
        drawerBuilder.withToolbar(mToolBar);
        drawerBuilder.withDisplayBelowStatusBar(false);
        drawerBuilder.withActionBarDrawerToggleAnimated(true);
        drawerBuilder.withDrawerGravity(Gravity.LEFT);
        drawerBuilder.withSavedInstance(savedInstanceState);
        drawerBuilder.withSelectedItem(0);
        drawerBuilder.withTranslucentStatusBar(false);
        drawerBuilder.withAccountHeader(headerResult);
        drawerBuilder.addDrawerItems(
                new PrimaryDrawerItem().withName("Home").withIcon(R.drawable.ic_home_black).withIdentifier(1).withSelectedColor(3),
                new PrimaryDrawerItem().withName("Appliance").withIcon(R.drawable.setting).withIdentifier(5).withBadgeStyle(new BadgeStyle(Color.RED, Color.RED)),
                new DividerDrawerItem(),
                new PrimaryDrawerItem().withName("Load Management").withIdentifier(6),
                new SecondaryDrawerItem().withName("About Us").withIcon(R.drawable.ic_about_us).withTag("Bullhorn"),
                new SecondaryDrawerItem().withName("Logout").withIcon(R.drawable.ic_exit)
        );
        drawerBuilder.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {


                if (((Nameable) drawerItem).getName().getText(MainActivity.this).equals("Home")) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

                if (((Nameable) drawerItem).getName().getText(MainActivity.this).equals("Appliance")) {
                    //Sync Activity
                    Intent intent = new Intent(getApplicationContext(), HomeApplianceActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }

                if (((Nameable) drawerItem).getName().getText(MainActivity.this).equals("Logout")) {
                    displayStatusMessage("Are you sure want to  exit?",3,0);

                }

                return false;
            }
        });
        result = drawerBuilder.build();//set the AccountHeader  created earlier for the header
    }

    @Override
    public void onBackPressed() {


        displayStatusMessage("Are you sure want to  exit?",3,0);

    }

    private void displayStatusMessage(String s, int colorValue, final int id) {

        AlertDialog.Builder builder = null;
        View view = null;
        TextView tvOk, tvMessage,tvCancel;
        ImageView imageView;
        int defaultColor = R.color.textGray;
        int successColor = R.color.successColor; // 1
        int errorColor = R.color.errorColor; // 2
        int warningColor = R.color.warningColor; // 3

        int success = R.drawable.ic_success;
        int error_image = R.drawable.ic_error;
        int warning_image = R.drawable.ic_warning;
        //1,2,3

        int color = defaultColor;
        int img = success;
        if (colorValue == 1) {
            color = successColor;
            img = success;

        } else if (colorValue == 2) {
            color = errorColor;
            img = error_image;

        } else if (colorValue == 3) {
            color = warningColor;
            img = warning_image;
        }

        builder = new AlertDialog.Builder(MainActivity.this);
        view = getLayoutInflater().inflate(R.layout.layout_for_custom_message, null);

        tvOk = (TextView) view.findViewById(R.id.tvOk);
        tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        imageView = (ImageView)view.findViewById(R.id.iv_status);
        tvCancel =(TextView) view.findViewById(R.id.tvCancel);

        tvMessage.setTextColor(getResources().getColor(color));
        tvMessage.setText(s);
        imageView.setImageResource(img);


        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        tvCancel.setVisibility(View.VISIBLE);
        tvOk.setOnClickListener(    new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id==0){
                   // System.exit(0);
                   // finish();
                    alertDialog.dismiss();

                        FirebaseAuth.getInstance().signOut();//logout
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        finish();

                }else{
                    alertDialog.dismiss();
                }


            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }
}
