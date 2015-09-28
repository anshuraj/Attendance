package com.deadlydespo.attendance;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    public final String url = "http://117.239.20.150/attendance/student/stumaind.php";
    //public final String url = "http://2.0.0.168/anshu/student/stumain.php";   // Test url
    WebView myWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.myWebView = (WebView) findViewById(R.id.webview);

        ((Tracking) getApplication()).startTracking();

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new WebClientClass());
        myWebView.loadUrl(url);
    }

    public class WebClientClass extends WebViewClient {
        ProgressDialog pd = null;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            pd = new ProgressDialog(MainActivity.this);
            //pd.setTitle("Please wait");
            pd.setMessage("Please wait loading... ");
            pd.show();
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (pd.isShowing() && pd!=null)
            {
                pd.cancel();
            }
        }
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Log.i("error--> ", "error code: " + errorCode);
            if(errorCode == -2) {
                view.loadData("","",null);
                pd.cancel();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font color='#46CAF0'><b>Error</b></font>"));
                builder.setMessage(Html.fromHtml("<font color='#000'>Server not available.</font>"));
                builder.setPositiveButton(Html.fromHtml("<font color='#000'><b>Retry</b></font>"), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myWebView.loadUrl(url);
                    }
                });
                builder.setNegativeButton(Html.fromHtml("<font color='#000'><b>Exit</b></font>"), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                        System.exit(0);
                    }
                });
                AlertDialog alert=builder.create();
                alert.show();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.myWebView.canGoBack()) {
            this.myWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    //This function will save the state (reloading) on changing screen orientation
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }
}
