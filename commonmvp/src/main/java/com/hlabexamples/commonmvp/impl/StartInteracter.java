package com.hlabexamples.commonmvp.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hlabexamples.commonmvp.base.mvp.callback.ICallbackListener;
import com.hlabexamples.commonmvp.data.LoginModel;
import com.hlabexamples.commonmvp.data.SignupModel;
import com.hlabexamples.commonmvp.data.UserModel;

import static com.hlabexamples.commonmvp.impl.DBFields.USERS;

/**
 * Created by H.T. on 04/12/17.
 */

public class StartInteracter {
    private final String TAG = StartInteracter.class.getSimpleName();
    private final FirebaseAuth mAuth;
    private final DatabaseReference mDatabase;

    public StartInteracter() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void doSignup(final SignupModel loginModel, @NonNull  final ICallbackListener<String> callbackListener) {
        mAuth.createUserWithEmailAndPassword(loginModel.getUsername(), loginModel.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            writeNewUser(user.getUid(), loginModel.getUsername());
                            callbackListener.onSuccess(user.getEmail());
                        } else
                            callbackListener.onFailure(new Exception("User can not be created"));
                    } else {
                        callbackListener.onFailure(task.getException());
                    }
                });
    }

    private void writeNewUser(String userId, String email) {
        UserModel user = new UserModel(userId, email);
        mDatabase.child(USERS).child(userId).setValue(user);
    }

    public void doLogin(final LoginModel loginModel, @NonNull final ICallbackListener<String> callbackListener) {
        mAuth.signInWithEmailAndPassword(loginModel.getUsername(), loginModel.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "loginUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        callbackListener.onSuccess(user != null ? user.getEmail() : "");
                    } else {
                        callbackListener.onFailure(task.getException());
                    }
                });
    }

    public void doGoogleLogin(GoogleSignInAccount account, final ICallbackListener<String> callbackListener) {

        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            writeNewUser(user.getUid(), user.getEmail());
                            callbackListener.onSuccess(user.getEmail());
                        } else
                            callbackListener.onFailure(new Exception("User can not be created"));

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        callbackListener.onFailure(task.getException());
                    }
                });
    }
}

