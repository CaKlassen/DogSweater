package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FacebookShare extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_share);

        openActiveSession(this,true, Arrays.asList(""),new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                if (state == SessionState.OPENED) {
                    Log.d("Facebook", "SessionState.OPENED");
                    Session.OpenRequest openRequest = new Session.OpenRequest(FacebookShare.this);
                    openRequest.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);
                    session.requestNewPublishPermissions(
                            new Session.NewPermissionsRequest(FacebookShare.this, Arrays.asList("publish_actions")));
                }
                else if (state == SessionState.OPENED_TOKEN_UPDATED) {
                    Log.d( "Facebook", "Publishing" );
                }
                else if (state == SessionState.CLOSED_LOGIN_FAILED) {
                    Log.d( "Facebook", "SessionState.CLOSED_LOGIN_FAILED" );
                    session.closeAndClearTokenInformation();
                    // Possibly finish the activity
                }
                else if (state == SessionState.CLOSED) {
                    Log.d( "Facebook", "SessionState.CLOSED" );
                    session.close();
                    // Possibly finish the activity
                }
            }});
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //Create menu
        MenuHelper m = new MenuHelper(getApplicationContext(), this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_facebook_share, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, imageReturnedIntent);
    }

    private static Session openActiveSession(Activity activity, boolean allowLoginUI, List permissions, Session.StatusCallback callback) {
        Session.OpenRequest openRequest = new Session.OpenRequest(activity).setPermissions(permissions).setCallback(callback);
        Session session = new Session.Builder(activity).build();
        if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState()) || allowLoginUI) {
            Session.setActiveSession(session);
            session.openForRead(openRequest);
            return session;
        }
        return null;
    }

    public void publish(View v) {
        final Bitmap bi;

        try{
            bi = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse( getIntent().getStringExtra("ImageURI") ) );
        }
        catch( FileNotFoundException e ) {
            Log.e( "FacebookShare", e.getMessage(), e );
            return;
        }
        catch( IOException e ) {
            Log.e( "FacebookShare", e.getMessage(), e );
            return;
        }

        final Request.Callback callback = new Request.Callback(){
            @Override
            public void onCompleted( Response r ) {
                Log.d( "Request.Callback.Response", ""+r );
            }
        };


        Request request = Request.newMyUploadPhotoRequest(Session.getActiveSession(), bi, getString(R.string.share_caption), callback);
        request.executeAsync();
    }
}
