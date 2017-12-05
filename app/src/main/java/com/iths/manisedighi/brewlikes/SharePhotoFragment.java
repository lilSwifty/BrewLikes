package com.iths.manisedighi.brewlikes;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import java.util.Arrays;


/**
 * Created by emmapersson on 2017-11-28.
 * A class that handles the sharing of the photo to facebook
 */

public class SharePhotoFragment extends Fragment {

    private static final String TAG = "SharePhotoFragment";
    private static final String PERMISSION = "publish_actions";

    private CallbackManager callbackManager;
    private ShareButton btnShare;
    private ShareDialog shareDialog;
    private boolean canPresentShareDialogWithPhotos;
    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
            Log.d("HelloFacebook", "Canceled");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
            String title = getString(R.string.error);
            String alertMessage = error.getMessage();
            showResult(title, alertMessage);
        }

        @Override
        public void onSuccess(Sharer.Result result) {
            Log.d("HelloFacebook", "Success!");
            if (result.getPostId() != null) {
                String title = getString(R.string.success);
                String id = result.getPostId();
                String alertMessage = getResources().getString(R.string.successfully_posted_post) + id;
                showResult(title, alertMessage);
            }
        }

        private void showResult(String title, String alertMessage) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(alertMessage)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: starts");

        FacebookSdk.getSdkVersion();

        callbackManager = CallbackManager.Factory.create();

        shareDialog = new ShareDialog(this);                //kan ta bort för dem verkar inte fylla någon funktion
        shareDialog.registerCallback(callbackManager, shareCallback); // -||-


/*      shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });*/
    }

    /**
     * A method that makes the fragment instantiate and return its user
     * interface view ShareButton.
     * @param inflater - Object to help to inflate the view
     * @param container - parent ViewGroup from InfoActivity's layout
     * @param savedInstanceState - containing data about the previous instance of the fragment
     * @returns the view ShareButton
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.share_fragment, container, false);
    }

    /**
     * A method being called right after onCreateView has returned ShareButton view
     * @param view - ShareButton
     * @param savedInstanceState - containing data about the previous instance of the fragment
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        setSharePhoto(view);
    }

    /**
     * A method that builds the photo to share on facebook
     * @param view - ShareButton
     */
    private void setSharePhoto(View view){
        Log.d(TAG, "setSharePhoto: ");
        Bitmap image = BitmapFactory.decodeFile(getArguments().getString("photoPath"));
        SharePhoto sharePhoto = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
        SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                .addPhoto(sharePhoto)
                .setShareHashtag(new ShareHashtag.Builder()
                        .setHashtag("#BrewLikes")
                        .build())
                .build();

        if (canPresentShareDialogWithPhotos) {
            Log.d(TAG, "setSharePhoto: showing share dialog");
            shareDialog.show(sharePhotoContent);
        } else if (hasPublishPermission()) {
            ShareApi.share(sharePhotoContent, shareCallback);
        }

        btnShare = view.findViewById(R.id.btnShare);

        btnShare.setShareContent(sharePhotoContent);
        canPresentShareDialogWithPhotos = ShareDialog.canShow(SharePhotoContent.class);
/*        btnShare.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.d(TAG, "onSuccess: ");
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: ");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError: "+error.getMessage());
            }
        });
        /*if(btnShare.isActivated()) {
            shareDialog.show(sharePhotoContent);
        }*/ //TODO Var ska denna förbaskade callbackskiten va=???
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: ");
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private boolean hasPublishPermission() {
        Log.d(TAG, "hasPublishPermission: ");
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && accessToken.getPermissions().contains("publish_actions");
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
            callbackManager.onActivityResult(requestCode, resultCode, data);
//            shareDialog.registerCallback(callbackManager, callback);
//        ShareInternalUtility.registerSharerCallback(requestCode ,callbackManager, callback); ------?????
        //TODO this call is were it's mess things up
    }*/
}

