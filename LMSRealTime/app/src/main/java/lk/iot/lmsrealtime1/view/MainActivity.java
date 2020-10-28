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
    String userID;
    LinearLayout progressBar;


    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set ui
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //map ui elements to these variables
        // so we can access the data which user has inserted
        mToolBar =  findViewById( R.id.tb_main );
        progressBar =  findViewById( R.id.progressBar );
        rvMenuItem = (RecyclerView) findViewById( R.id.rvMenuItem );

        //initialize firebase database
        fAuth = FirebaseAuth.getInstance();

        //get userId from database
        //if userId is not available assign the value to zero
        userID = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";

        //initialize firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Divide the page to two columns
        LinearLayoutManager layoutManager = new GridLayoutManager( MainActivity.this, 2, RecyclerView.VERTICAL, false );
        rvMenuItem.setLayoutManager( layoutManager );
        rvMenuItem.setHasFixedSize( true );


        //insert drawer then user can click logout button
        insertNavigationDrawer( savedInstanceState );


       //insert menus and images to the menulist
        ArrayList<MyMenu> menuList = new ArrayList<>();
        menuList.add(new MyMenu("Appliance",R.drawable.setting));
        menuList.add(new MyMenu("Manual Control",R.drawable.control));
        menuList.add(new MyMenu("ScheduleActivity",R.drawable.schedule));
        menuList.add(new MyMenu("Cost & Power",R.drawable.cost_power));
        menuList.add(new MyMenu("Customer Info",R.drawable.customer_info));

       //initialize adapter
        adapter = new MenuAdapter( getApplicationContext(), menuList );

        //set that list to recycle view
        rvMenuItem.setAdapter( adapter );
    }

  //create drawer in left side
    private void insertNavigationDrawer(Bundle savedInstanceState) {
        //build left side drawer
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

        //set Lables inside the drawer with images
        drawerBuilder.addDrawerItems(
                new PrimaryDrawerItem().withName("Home").withIcon(R.drawable.ic_home_black).withIdentifier(1).withSelectedColor(3),
                new PrimaryDrawerItem().withName("Appliance").withIcon(R.drawable.setting).withIdentifier(5).withBadgeStyle(new BadgeStyle(Color.RED, Color.RED)),
                new DividerDrawerItem(),
                new PrimaryDrawerItem().withName("Load Management").withIdentifier(6),
                new SecondaryDrawerItem().withName("About Us").withIcon(R.drawable.ic_about_us).withTag("Bullhorn"),
                new SecondaryDrawerItem().withName("Logout").withIcon(R.drawable.ic_exit)
        );

        //when we click drawer bar item
        drawerBuilder.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {


                //if user click home from drawer
                if (((Nameable) drawerItem).getName().getText(MainActivity.this).equals("Home")) {
                    //navigate to home page
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    //close this activity
                    finish();
                }

                //if user click Appliance from drawer
                if (((Nameable) drawerItem).getName().getText(MainActivity.this).equals("Appliance")) {
                    //navigate to homeAppliance page
                    Intent intent = new Intent(getApplicationContext(), HomeApplianceActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    //close this activity
                    finish();

                }
                //if user click logout from drawer
                if (((Nameable) drawerItem).getName().getText(MainActivity.this).equals("Logout")) {

                    //ask logout confirmation message
                    //if yes it will close this app
                    displayStatusMessage("Are you sure want to  exit?");

                }

                return false;
            }
        });
        //create drawer
        result = drawerBuilder.build();
    }

    //when user click back button ,
    @Override
    public void onBackPressed() {

        //ask logout confirmation message
        //if yes it will close this app
        displayStatusMessage("Are you sure want to  exit?");

    }

    //Ask confirmation message
    private void displayStatusMessage(String s) {

        AlertDialog.Builder builder = null;
        View view = null;
        TextView tvOk, tvMessage,tvCancel;
        ImageView imageView;

        int color =  R.color.warningColor;
        int img = R.drawable.ic_warning;

        builder = new AlertDialog.Builder(MainActivity.this);
        view = getLayoutInflater().inflate(R.layout.layout_for_custom_message, null);

        tvOk = (TextView) view.findViewById(R.id.tvOk);
        tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        imageView = (ImageView)view.findViewById(R.id.iv_status);
        tvCancel =(TextView) view.findViewById(R.id.tvCancel);

        tvMessage.setTextColor(getResources().getColor(color));
        tvMessage.setText(s);
        imageView.setImageResource(img);

        //set those variables in layout_for_custom_message layout
        builder.setView(view);

        //create the builder
        final AlertDialog alertDialog = builder.create();

        //show alertDialog
        alertDialog.show();
        tvCancel.setVisibility(View.VISIBLE);

        //if user clicks ok for logout
        tvOk.setOnClickListener(    new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //dismiss the alertDialog
                alertDialog.dismiss();

                //signout from firebase
                FirebaseAuth.getInstance().signOut();//logout

                //navigate to login page
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));

                //close this activity
                finish();


            }
        });

        //if user click cancel for logout
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dismiss the alert box
                alertDialog.dismiss();
            }
        });

    }
}
