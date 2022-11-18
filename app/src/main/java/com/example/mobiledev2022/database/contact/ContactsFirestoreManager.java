package com.example.mobiledev2022.database.contact;

import static com.example.mobiledev2022.database.contact.ContactsFirestoreDBContract.COLLECTION_NAME;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ContactsFirestoreManager {
    private static ContactsFirestoreManager contactsFirestoreManager;
    private CollectionReference contactsCollectionReference;
    public static ContactsFirestoreManager newInstance(){
        if (contactsFirestoreManager == null){
            contactsFirestoreManager = new ContactsFirestoreManager();
        }
        return contactsFirestoreManager;
    }
    private FirebaseFirestore firebaseFirestore;
    private ContactsFirestoreManager(){
        firebaseFirestore = FirebaseFirestore.getInstance();
        contactsCollectionReference = firebaseFirestore.collection(COLLECTION_NAME);
    }
    public void createDocument(Contact contact){
        contactsCollectionReference.add(contact);
    }
    public void getAllContacts (OnCompleteListener<QuerySnapshot> onCompleteListener){
        contactsCollectionReference.get().addOnCompleteListener(onCompleteListener);
    }
    public void updateContact(Contact contact){
        String documentId = contact.getDocumentId();
        DocumentReference documentReference = contactsCollectionReference.document(documentId);
        documentReference.set(contact);
    }
    public void deleteContact(String documentId){
        DocumentReference documentReference = contactsCollectionReference.document(documentId);
        documentReference.delete();
    }
    public void sendContactsBulk() {

        // Create a new Contact document map of values and add it to the collection
        createDocument(new Contact("Jack", "Miller", "jmiller@gmail.com"));

        // Create a new Contact document map of values and add it to the collection
        createDocument(new Contact("Michael", "Johnson", "m_johnson@gmail.com"));

        // Create a new Contact document map of values and add it to the collection
        createDocument(new Contact("Chris", "Stanley", "chrisstnl@gmail.com"));

        // Create a new Contact document map of values and add it to the collection
        createDocument(new Contact("Jane", "Smith", "jsmith@gmail.com"));
    }
    // TODO: 2.3 Creating a Database Manager
}
