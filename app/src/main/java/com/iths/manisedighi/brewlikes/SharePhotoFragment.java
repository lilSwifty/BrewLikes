package com.iths.manisedighi.brewlikes;

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

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;


/**
 * Created by emmapersson on 2017-11-28.
 * A class that handles the sharing of the photo to facebook
 */

public class SharePhotoFragment extends Fragment {

    private static final String TAG = "SharePhotoFragment";
    private CallbackManager callbackManager;
    ShareDialog shareDialog;
    private ShareButton btnShare;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.getSdkVersion();

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
//        shareDialog.registerCallback(callbackManager, callback);

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
        setSharePhoto(view);
    }

    /**
     * A method that builds the photo to share on facebook
     * @param view - ShareButton
     */
    private void setSharePhoto(View view){

        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.brewlikes_placeholder_logo2);
        SharePhoto sharePhoto = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
        SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                .addPhoto(sharePhoto)
                .setShareHashtag(new ShareHashtag.Builder()
                        .setHashtag("#BrewLikes")
                        .build())
                .build();

        btnShare = view.findViewById(R.id.btnShare);
        btnShare.setShareContent(sharePhotoContent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
//          shareDialog.registerCallback(callbackManager, callback);
        //TODO this call is were it's mess things up
    }

    private FacebookCallback<Sharer.Result> callback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onSuccess(Sharer.Result result) {
            Log.d(TAG, "onSuccess: successfully upload"+result);
            Toast.makeText(getContext(), "Successfully upload", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Log.d(TAG, "onCancel: upload cancelled");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d(TAG, "onError: "+error.getMessage());
        }
    };
}

