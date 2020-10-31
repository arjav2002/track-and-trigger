package com.oopcows.trackandtrigger.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Any;

import java.util.HashMap;
import java.util.Map;

import static com.oopcows.trackandtrigger.helpers.CowConstants.USERS_TABLE_NAME;

public class FireBaseHelper {
    FirebaseFirestore db;

    public boolean checkIfUserExists(String gmail) {

        db = FirebaseFirestore.getInstance();
        return db.collectionGroup(USERS_TABLE_NAME)
                .whereEqualTo("gmailId", gmail)
                .get()
                .isSuccessful();
    }
}
