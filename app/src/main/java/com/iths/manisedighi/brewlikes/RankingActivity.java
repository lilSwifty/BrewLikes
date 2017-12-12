package com.iths.manisedighi.brewlikes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class RankingActivity extends AppCompatActivity {
    private ImageView beerImage;
    private TextView tasteText;
    private SeekBar tasteBar;
    private TextView priceText;
    private SeekBar priceBar;
    private EditText beerComment;
    private Button saveButton;
    private TextView awfulText;
    private TextView perfectText;
    private TextView expensiveText;
    private TextView cheapText;
    private EditText beerName;
    private TextView categoryText;
    private Button editButton;
    private Button discardButton;
    private ImageButton mapButton;
    private Spinner categorySpinner;
    private TextView tasteRateNumber;
    private TextView priceRateNumber;
    private Uri selectedImage;
    private String location = null;
    private double lat = 0.0;
    private double lng = 0.0;
    private LatLng latLng;
    static final int REQUEST_TAKE_PHOTO = 1337;
    static final int RESULT_LOAD_IMAGE = 217;
    static final int REQUEST_CODE = 7175;
    private String name;
    private String comment;
    private double taste;
    private double price;
    private int categoryId;
    private String picture;
    private Category category = new Category();
    private static final String TAG = "RankingActivity";
    private String address;
    private String phoneNumber;
    private String website;
    private String smallPicture;
    private Bitmap litenBild;
    private Bitmap storBild;
    private String mCurrentPhotoPath;
    private TextView checkInText;
    DBHelper dbHelper = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        findViews();
        cameraLauncher();
        setSeekbars(tasteBar,tasteRateNumber);
        setSeekbars(priceBar,priceRateNumber);
        findBeerCategories();
    }

    /**
     * A method to set the seekbars and also show the number which the person has rated.
     */
    public void setSeekbars(SeekBar seekBar, final TextView textView){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /**
     * A method to fill the Spinner with the categories.
     */
    public void findBeerCategories(){
        List categoryList = dbHelper.getAllCategories();
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this,android.R.layout.simple_spinner_item,categoryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }
    /**
     * A method to find the views.
     */
    private void findViews(){
        beerImage = findViewById(R.id.beerImage);
        tasteText = findViewById(R.id.tasteText);
        tasteBar = findViewById(R.id.tasteBar);
        priceText = findViewById(R.id.priceText);
        priceBar = findViewById(R.id.priceBar);
        beerComment = findViewById(R.id.beerComment);
        saveButton = findViewById(R.id.saveButton);
        awfulText = findViewById(R.id.awfulText);
        perfectText = findViewById(R.id.perfectText);
        expensiveText = findViewById(R.id.expensiveText);
        cheapText = findViewById(R.id.cheapText);
        beerName = findViewById(R.id.beerName);
        categoryText = findViewById(R.id.categoryText);
        editButton = findViewById(R.id.editButton);
        discardButton = findViewById(R.id.discardButton);
        categorySpinner = findViewById(R.id.categorySpinner);
        tasteRateNumber = findViewById(R.id.tasteRateNumber);
        priceRateNumber = findViewById(R.id.priceRateNumber);
        mapButton = findViewById(R.id.mapButton);
        checkInText = findViewById(R.id.checkInText);
        beerComment.setElevation(0);
        beerName.setElevation(0);
    }

    /**
     * The method that does all the work with saving the rankings and put them into the database/infoviews.
     * @param view
     */
    public void onSaveButtonClick(View view){
        name = saveBeerName();
        comment = saveBeerComment();
        taste = saveTaste();
        price = savePrice();
        category = (Category)categorySpinner.getSelectedItem();
        categoryId = (int) category.getId();

        if (name.isEmpty()) {
            makeToast(getApplicationContext().getString(R.string.nameTheBeer));
        }else if (comment.isEmpty()){
            makeToast(getApplicationContext().getString(R.string.commentTheBeer));
        }else if (picture.isEmpty()){
            makeToast(getApplicationContext().getString(R.string.pictureTheBeer));
        }else if(category.getName().equals("Unknown")){
            final AlertDialog.Builder builder = new AlertDialog.Builder(RankingActivity.this);
            builder.setIcon(R.drawable.brewlikes_main_image);
            builder.setMessage(getApplicationContext().getString(R.string.saveToUnknown)).setCancelable(false)
                    .setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey (DialogInterface dialog, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_BACK &&
                                    event.getAction() == KeyEvent.ACTION_UP &&
                                    !event.isCanceled()) {
                                finish();
                                return true;
                            }
                            return false;
                        }
                    })
                    .setPositiveButton(getApplicationContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            addBeer();

                        }
                    })
                    .setNegativeButton(getApplicationContext().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;

                        /*
                        //For later use, if we want to upload image from gallery
                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, RESULT_LOAD_IMAGE);
                        */

                        }
                    });
            AlertDialog alert = builder.create();
            alert.setTitle(getApplicationContext().getString(R.string.lastChange));
            alert.show();
        }else{
            addBeer();
        }
    }

    /**
     * A method to add a beer and send the user to the info activity.
     */
    public void addBeer(){
        Beer beer = new Beer(name, categoryId, price, taste, comment, picture, location,lat,lng,address,phoneNumber,website,smallPicture);
        dbHelper.addBeer(beer);
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("BeerID", beer.getId());
        intent.putExtra("info",1);
        startActivity(intent);
        finish();
    }

    /**
     * A method to send the user to the gps-menu where the user can choose a place to log in to.
     * @param view
     */
    public void onCheckinClick(View view){
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("ID", 1);
        startActivityForResult(intent, 1 );
    }

    /**
     * A method to restart the camera and give the user a chance to take a new picture.
     * @param view
     */
    public void onEditButtonClick(View view){
        cameraLauncher();
    }

    /**
     * A method to send the user back to the main page of the app if he/she doesn't want to rank a beer anymore.
     * @param view
     */
    public void onDiscardButtonClick(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    /**
     * A method to get the price rate.
     * @return a double with the price rate.
     */
    private double savePrice(){
        double doublePrice = Double.parseDouble(priceRateNumber.getText().toString());
        return doublePrice;
    }

    /**
     * A method to get the taste rate.
     * @return a double with the price rate.
     */
    private double saveTaste(){
        double doubleTaste = Double.parseDouble(tasteRateNumber.getText().toString());
        return doubleTaste;
    }

    /**
     * Saving the name of the beer.
     * @return a String for the name of the beer.
     */
    private String saveBeerName(){
        return beerName.getText().toString();
    }

    /**
     * Saving the comment for the beer.
     * @return a String for the comment to the beer.
     */
    private String saveBeerComment(){
        return beerComment.getText().toString();
    }

    /**
     * starts a dialog with options take/upload picture
     */
    public void cameraLauncher() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(RankingActivity.this);
        builder.setIcon(R.drawable.brewlikes_main_image);
        builder.setMessage(getApplicationContext().getString(R.string.chooseAlternative)).setCancelable(false)
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey (DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK &&
                                event.getAction() == KeyEvent.ACTION_UP &&
                                !event.isCanceled()) {
                            finish();
                            return true;
                        }
                        return false;
                    }
                })
                .setPositiveButton(getApplicationContext().getString(R.string.takePhoto), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dispatchTakePictureIntent();

                    }
                })
                .setNegativeButton(getApplicationContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();

                        /*
                        //For later use, if we want to upload image from gallery
                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, RESULT_LOAD_IMAGE);
                        */

                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle(getApplicationContext().getString(R.string.timeToBrew));
        alert.show();
    }

    /**
     * To create and invoke the Intent for the picture. First, ensure that there's a camera activity to handle the intent.
     */

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // To create a file to put the picture in
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = createImageFile("");
            } catch (IOException ioException) {
                //Error occured while creating the File
                makeToast("Cannot create file");
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.iths.manisedighi.brewlikes.FileProvider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                Log.d(TAG, "dispatchTakePictureIntent: this works");
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap beerPhoto = (Bitmap) extras.get("data");
            beerImage.setImageBitmap(beerPhoto);

        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK){

            storBild = scalePicture(960,960);

            litenBild=scalePicture(500,500);

            try {
                File fbig = createImageFile("big");
                File fsmall = createImageFile("small");

                saveBitmapToFile(storBild, fbig);
                saveBitmapToFile(litenBild, fsmall);

                picture = fbig.getAbsolutePath();
                smallPicture = fsmall.getAbsolutePath();

            } catch (IOException e) {
                e.printStackTrace();
            }



            //scalePicture();
            beerImage.setImageBitmap(storBild);

            Log.d(TAG, "onActivityResult: " + storBild + "   " +litenBild);

        } else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK){
            //TODO find path to gallery

            selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.beerImage);

            Bitmap bmp = null;

            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            imageView.setImageBitmap(bmp);
        } else if(requestCode == 1 && resultCode == 1){
            location=data.getStringExtra("location");
            latLng=new LatLng(data.getDoubleExtra("lat",0.0),data.getDoubleExtra("lng",0.0));
            lat=latLng.latitude;
            lng=latLng.longitude;
            website=data.getStringExtra("website");
            address=data.getStringExtra("address");
            phoneNumber=data.getStringExtra("phoneNumber");
            mapButton.setImageResource(R.drawable.ic_location_beer);
            mapButton.setBackgroundColor(getResources().getColor(R.color.blackbrew));
            checkInText.setVisibility(View.INVISIBLE);
        }

    }

    private void saveBitmapToFile(Bitmap bitmap, File file) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    /**
     * Creates a collision-resistant name for the image file
     * @return the image with the new name
     * @throws IOException - if something goes wrong
     */
    public File createImageFile(String postfix) throws IOException {
        // Create a name for the image file
        String dateStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + dateStamp + "_" + postfix;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        //within parentheses: prefix, suffix, directory
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        //Save the file, path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d(TAG, "createImageFile: this works");
        return image;
    }
    /**
     * Adds the picture to the gallery
     */
    public void addPictureToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        Log.d(TAG, "addPictureToGallery: this works");
    }

    private Bitmap scalePicture(int targetW, int targetH ) {
        //The dimensions of the bitmap

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        //Decides how much to scale down the picture
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        //Decodes the image into Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        //beerImage.setImageBitmap(bitmap);

        return bitmap;
    }
    public void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}
