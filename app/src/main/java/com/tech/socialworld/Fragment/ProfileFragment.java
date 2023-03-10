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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.tech.socialworld.Adapter.FollowersAdapter;
import com.tech.socialworld.Model.FollowModel;
import com.tech.socialworld.Model.UserModel;
import com.tech.socialworld.R;
import com.tech.socialworld.databinding.FragmentProfileBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    ArrayList<FollowModel> list;
    FragmentProfileBinding binding;
    ProgressDialog dialog;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        list = new ArrayList<>();

        FollowersAdapter adapter = new FollowersAdapter(list, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.followersRV.setLayoutManager(linearLayoutManager);
        binding.followersRV.setAdapter(adapter);

        database.getReference().child("Users")
                .child(Objects.requireNonNull(auth.getUid()))
                .child("followers").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            FollowModel followModel = dataSnapshot.getValue(FollowModel.class);
                            list.add(followModel);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        //Cover photo set from database and set all Text from database
        //Fetch User data from database
        database.getReference().child("Users").child(Objects.requireNonNull(auth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    Picasso.get()
                            .load(Objects.requireNonNull(userModel).getCoverPhoto())
                            .placeholder(R.drawable.placeholder)
                            .into(binding.image);

                    Picasso.get()
                            .load(userModel.getProfile())
                            .placeholder(R.drawable.man)
                            .into(binding.profileImage);

                    binding.username.setText(userModel.getName());
                    binding.profession.setText(userModel.getProfession());
                    binding.followersNo.setText(userModel.getFollowerCount() + "");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Opening gallery");

        binding.changeCoverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                launchSomeActivity1.launch(intent);
            }
        });
        binding.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                launchSomeActivity2.launch(intent);
            }
        });
        return binding.getRoot();
    }

    //CoverPhoto upload
    ActivityResultLauncher<Intent> launchSomeActivity1 =
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
                                selectedImageBitmap = MediaStore.Images.Media.getBitmap(ProfileFragment.this.requireContext().getContentResolver(), selectImageUri);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            binding.image.setImageBitmap(selectedImageBitmap);
                            final StorageReference reference = storage.getReference()
                                    .child("child_photo")
                                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
                            reference.putFile(selectImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(getContext(), "Cover photo saved", Toast.LENGTH_SHORT).show();

                                    //Cover photo store in database
                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            database.getReference().child("Users").child(Objects.requireNonNull(auth.getUid())).child("coverPhoto").setValue(uri.toString());
                                        }
                                    });
                                }
                            });
                        }
                    }
                    dialog.dismiss();
                }
            });
    //profile image upload
    ActivityResultLauncher<Intent> launchSomeActivity2 =
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
                                selectedImageBitmap = MediaStore.Images.Media.getBitmap(ProfileFragment.this.requireContext().getContentResolver(), selectImageUri);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            binding.profileImage.setImageBitmap(selectedImageBitmap);

                            final StorageReference reference = storage.getReference().child("profile_image").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
                            reference.putFile(selectImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(getContext(), "Profile photo saved", Toast.LENGTH_SHORT).show();

                                    //Cover photo store in database
                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            database.getReference().child("Users").child(Objects.requireNonNull(auth.getUid())).child("profile").setValue(uri.toString());
                                        }
                                    });
                                }
                            });
                        }
                    }
                    dialog.dismiss();
                }
            });
}