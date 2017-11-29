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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
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
    private Spinner categorySpinner;
    private TextView tasteRateNumber;
    private TextView priceRateNumber;
    private Uri selectedImage;

    static final int REQUEST_TAKE_PHOTO = 1337;
    static final int RESULT_LOAD_IMAGE = 2;
    static final int REQUEST_CODE = 7175;

    private static final String TAG = "RankingActivity";

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
    }

    /**
     * The method that does all the work with saving the rankings and put them into the database/infoviews.
     * @param view
     */
    public void onSaveButtonClick(View view){
        String name = saveBeerName();
        String comment = saveBeerComment();


        double taste = saveTaste();
        double price = savePrice();
        Category category = new Category();
        category = (Category) categorySpinner.getSelectedItem();
        int categoryId = (int) category.getId();
        String picture = "";

        if (mCurrentPhotoPath == null && selectedImage != null){
            picture = selectedImage.toString();
        }else if (selectedImage == null && mCurrentPhotoPath != null){
            picture = mCurrentPhotoPath;
        }

        if (name.isEmpty()) {
            makeToast("You need fill all columns");
        }else if (comment.isEmpty()){
            makeToast("You need to describe the beer");
        }else if (picture.isEmpty()){
            makeToast("You need to take a picture of your beer");
        }else if(category.toString().equals("none")){
            makeToast("Please choose category");
        }else{
            Beer beer = new Beer(name, categoryId, price, taste, comment, picture);
            dbHelper.addBeer(beer);
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
        }

        /*
        else if (tasteRateNumber.getText().charAt(0) != 0){
            makeToast("Please rate the price of this beer");
        }else if (priceRateNumber.getText().charAt(0) != 0){
            makeToast("Please rate the taste of this beer");
        }

         */






        // TODO: skicka personen till den activityn som vi vill
    }
    private void onMappingClick(View view){
        // TODO: skicka personen till moas map där man kan logga in, ändra färgen på map-pinnen ifall man har checkat in till ölfärgad.
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
     * @return an int with the price rate.
     */
    private double savePrice(){
        int price = priceRateNumber.getText().charAt(0);
        double doublePrice = (double) price;
        return doublePrice;
    }

    /**
     * A method to get the taste rate.
     * @return an int with the price rate.
     */
    private double saveTaste(){
        int taste = tasteRateNumber.getText().charAt(0);
        double doubleTaste = (double)taste;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(RankingActivity.this);
        builder.setMessage("Please choose an alternative").setCancelable(false)
                .setPositiveButton("Take photo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dispatchTakePictureIntent();

                    }
                })
                .setNeutralButton("Upload image", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, RESULT_LOAD_IMAGE);

                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Time to brew...");
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
                photoFile = createImageFile();
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
            scalePicture();
            addPictureToGallery();
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


    String mCurrentPhotoPath;

    public File createImageFile() throws IOException {
        // Create a name for the image file
        String dateStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + dateStamp + "_";
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



    public void scalePicture() {
        //The dimensions of the View
        int targetW = beerImage.getWidth();
        int targetH = beerImage.getHeight();


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
        beerImage.setImageBitmap(bitmap);

        //return bitmap;
    }




    public void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}
