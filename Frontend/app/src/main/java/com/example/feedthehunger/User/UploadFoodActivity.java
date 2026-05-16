package com.example.feedthehunger.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.feedthehunger.ApiClient;
import com.example.feedthehunger.ApiService;
import com.example.feedthehunger.R;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadFoodActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    EditText up_food_type, up_description, up_address;
    Button upload_imagebtn, submit_foodbtn;
    ImageView iv_food_image;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        up_food_type = findViewById(R.id.up_food_type);
        up_description = findViewById(R.id.up_description);
        up_address = findViewById(R.id.up_address);
        iv_food_image = findViewById(R.id.iv_food_image);
        upload_imagebtn = findViewById(R.id.upload_imagebtn);
        submit_foodbtn = findViewById(R.id.submit_foodbtn);

        upload_imagebtn.setOnClickListener(v -> chooseImage());

        submit_foodbtn.setOnClickListener(v -> {
            if (imageUri == null) {
                Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String userFcmToken = task.getResult();
                            uploadFoodData(imageUri, userFcmToken);
                        } else {
                            Toast.makeText(this, "FCM token error", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            iv_food_image.setImageURI(imageUri);
            iv_food_image.setVisibility(ImageView.VISIBLE);
        }
    }

    private void uploadFoodData(Uri imageUri, String userFcmToken) {
        try {
            File file = new File(getRealPathFromURI(imageUri));

            String mimeType = getContentResolver().getType(imageUri);
            if (mimeType == null ||
                    !(mimeType.equals("image/jpeg") || mimeType.equals("image/png") || mimeType.equals("image/webp"))) {
                mimeType = "image/jpeg";
            }

            RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

            RequestBody foodTypePart = RequestBody.create(
                    MediaType.parse("text/plain"),
                    up_food_type.getText().toString().trim()
            );

            RequestBody descriptionPart = RequestBody.create(
                    MediaType.parse("text/plain"),
                    up_description.getText().toString().trim()
            );

            RequestBody addressPart = RequestBody.create(
                    MediaType.parse("text/plain"),
                    up_address.getText().toString().trim()
            );

            SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
            String userIdValue = sp.getString("user_id", "");

            RequestBody userIdPart = RequestBody.create(
                    MediaType.parse("text/plain"),
                    userIdValue
            );

            RequestBody tokenPart = RequestBody.create(
                    MediaType.parse("text/plain"),
                    userFcmToken
            );

            ApiService service = ApiClient.getClient().create(ApiService.class);

            Call<ResponseBody> call = service.uploadFood(
                    foodTypePart,
                    descriptionPart,
                    addressPart,
                    userIdPart,
                    tokenPart,
                    imagePart
            );

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String jsonString = response.body().string();
                            JSONObject jsonObject = new JSONObject(jsonString);

                            Toast.makeText(UploadFoodActivity.this,
                                    "Food details & image uploaded successfully!",
                                    Toast.LENGTH_LONG).show();

                            Log.i("UPLOAD", "Food uploaded: " + jsonObject.toString());

                            up_food_type.setText("");
                            up_description.setText("");
                            up_address.setText("");
                            iv_food_image.setImageDrawable(null);
                            UploadFoodActivity.this.imageUri = null;

                        } catch (IOException | JSONException e) {
                            Log.e("UPLOAD", "Error parsing success response: " + e.getMessage());
                        }
                    } else {
                        Log.e("UPLOAD", "Server error: " + response.code());
                        Toast.makeText(UploadFoodActivity.this,
                                "Server error: " + response.code(),
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("UPLOAD", "Upload failed: " + t.getMessage());
                    Toast.makeText(UploadFoodActivity.this,
                            "Upload failed: " + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Log.e("UPLOAD", "Error: " + e.getMessage());
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }

        return null;
    }
}