package info.zhegui.share;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private ListView listView;
    private Button btnShare;
    private java.util.List<ResolveInfo> listApp;

    private final int WHAT_SHOW_SHARE_APP=101;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case WHAT_SHOW_SHARE_APP:
                    listView.setAdapter(new MyAdapter());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=(ListView)findViewById(R.id.listView);
        btnShare=(Button)findViewById(R.id.btn_share);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnShare.setEnabled(false);

//                new Thread(){
//                    public void run(){

//                        listApp= showAllShareApp();
//                listView.setAdapter(new MyAdapter());
//                    }
//                }.start();
            }
        });
    }

    private void share(ResolveInfo appInfo) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        if (appInfo != null) {
            sendIntent
                    .setComponent(new ComponentName(
                            appInfo.activityInfo.packageName,
                            appInfo.activityInfo.name));
        }
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share"));
    }

    private java.util.List<ResolveInfo> showAllShareApp() {
        java.util.List<ResolveInfo> mApps = new ArrayList<ResolveInfo>();
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        intent.setType("text/plain");
        PackageManager pManager = getPackageManager();
        mApps = pManager.queryIntentActivities(intent,
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        return mApps;
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return listApp.size();
        }

        @Override
        public Object getItem(int position) {
            return listApp.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder=null;
            if(convertView==null){
                holder=new ViewHolder();
                convertView= LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_share_app, parent, false);
                holder.ivLogo= (ImageView) convertView.findViewById(R.id.iv_logo);
                holder.tvAppName= (TextView) convertView.findViewById(R.id.tv_app_name);
                holder.tvPackageName= (TextView) convertView.findViewById(R.id.tv_app_package_name);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }
            ResolveInfo appInfo=listApp.get(position);
            holder.tvAppName.setText(appInfo.activityInfo.packageName);
            holder.tvPackageName.setText(appInfo.activityInfo.packageName);
            return null;
        }
    }

    static class ViewHolder{
        ImageView ivLogo;
        TextView tvAppName;
        TextView tvPackageName;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
