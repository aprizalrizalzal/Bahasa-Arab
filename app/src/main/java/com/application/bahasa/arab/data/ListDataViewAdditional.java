package com.application.bahasa.arab.data;

import java.util.ArrayList;
import java.util.List;

public class ListDataViewAdditional {

    public static List<DataModelAdditional> listDataModelAdditionals(){
        ArrayList<DataModelAdditional> dataModelAdditionalArrayList = new ArrayList<>();
        dataModelAdditionalArrayList.add(new DataModelAdditional(
                "ba17",
                "Kosa Kata Pendukung",
                "Halaman 379/386",
                "https://firebasestorage.googleapis.com/v0/b/bahasa-6a723.appspot.com/o/bahasa-arab%2Fcover%2F17.jpg?alt=media&token=64c71c75-fd60-461f-9e2f-1c7d9e0a01cc",
                "https://firebasestorage.googleapis.com/v0/b/bahasa-6a723.appspot.com/o/bahasa-arab%2Fcontent%2Fkosa-kata-pendukung.pdf?alt=media&token=e0b68f5e-031f-4335-a6b9-807518760aca",
                ""));
        dataModelAdditionalArrayList.add(new DataModelAdditional(
                "ba18",
                "Daftar Kosa Kata Setiap Unit",
                "Halaman 387/392",
                "https://firebasestorage.googleapis.com/v0/b/bahasa-6a723.appspot.com/o/bahasa-arab%2Fcover%2F18.jpg?alt=media&token=09ca9727-25d0-4c09-8b6d-fb77f9276370",
                "https://firebasestorage.googleapis.com/v0/b/bahasa-6a723.appspot.com/o/bahasa-arab%2Fcontent%2Fdaftar-kosa-kata-setip-unit.pdf?alt=media&token=70735701-5f11-45f4-adbe-942d4f8e6102",
                ""));
        dataModelAdditionalArrayList.add(new DataModelAdditional(
                "ba19",
                "Daftar Kosa Kata Buku",
                "Halaman 393/403",
                "https://firebasestorage.googleapis.com/v0/b/bahasa-6a723.appspot.com/o/bahasa-arab%2Fcover%2F19.jpg?alt=media&token=33ce1355-4241-42d2-b73f-60d791cd5670",
                "https://firebasestorage.googleapis.com/v0/b/bahasa-6a723.appspot.com/o/bahasa-arab%2Fcontent%2Fdaftar-kosa-kata-buku.pdf?alt=media&token=42f26fc9-3f4f-4cf7-b9ac-5eab40065585",
                ""));
        return dataModelAdditionalArrayList;
    }
}
