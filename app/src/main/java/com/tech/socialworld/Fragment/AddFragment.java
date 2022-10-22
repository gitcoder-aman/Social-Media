package com.tech.socialworld.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.tech.socialworld.Model.UserModel;
import com.tech.socialworld.R;
import com.tech.socialworld.databinding.FragmentAddBinding;

import java.io.IOException;
import java.util.Objects;


public class AddFragment extends Fragment {

    FragmentAddBinding binding;
    ProgressDialog dialog;
    FirebaseAuth auth;
    FirebaseDatabase database;

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(inflater, container, false);

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Opening gallery");

        //set profileImage , name,profession
        database.getReference().child("Users")
                        .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            UserModel userModel = snapshot.getValue(UserModel.class);
                            Picasso.get()
                                    .load(userModel.getProfile())
                                    .placeholder(R.drawable.placeholder)
                                    .into(binding.profileImage);

                            binding.name.setText(userModel.getName());
                            binding.profession.setText(userModel.getProfession());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        binding.postDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String description = binding.postDescription.getText().toString();
                if(!description.isEmpty()){
                    binding.postBtn.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.follow_btn_bg));
                    binding.postBtn.setTextColor(requireContext().getResources().getColor(R.color.white)); //requireContext place to getContext
                    binding.postBtn.setEnabled(true);
                }else{
                    binding.postBtn.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.follow_active_btn));
                    binding.postBtn.setTextColor(requireContext().getResources().getColor(R.color.grey)); //requireContext place to getContext
                    binding.postBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                launchSomeActivity.launch(intent);
            }
        });
        return binding.getRoot();
    }

    ActivityResultLauncher<Intent> launchSomeActivity =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        dialog.dismiss();
                        Intent data = result.getData();

                        if (data != null && data.getData() != null) {
                            Uri selectImageUri = data.getData();
                            Bitmap selectedImageBitmap = null;

                            try {
                                selectedImageBitmap = MediaStore.Images.Media.getBitmap(AddFragment.this.requireContext().getContentResolver(), selectImageUri);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            binding.postImage.setImageBitmap(selectedImageBitmap);
                            binding.postImage.setVisibility(View.VISIBLE);

                            binding.postBtn.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.follow_btn_bg));
                            binding.postBtn.setTextColor(requireContext().getResources().getColor(R.color.white)); //requireContext place to getContext
                            binding.postBtn.setEnabled(true);

//                            final StorageReference reference = storage.getReference().child("child_photo").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
//                            reference.putFile(selectImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                @Override
//                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                    Toast.makeText(getContext(), "Cover photo saved", Toast.LENGTH_SHORT).show();
//
//                                    //Cover photo store in database
//                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                        @Override
//                                        public void onSuccess(Uri uri) {
//                                            database.getReference().child("Users").child(Objects.requireNonNull(auth.getUid())).child("coverPhoto").setValue(uri.toString());
//                                        }
//                                    });
//                                }
//                            });
                        }
                    }
                    dialog.dismiss();
                }
            });
}