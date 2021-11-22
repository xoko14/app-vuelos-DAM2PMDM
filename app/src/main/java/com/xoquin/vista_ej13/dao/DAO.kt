package com.xoquin.vista_ej13.dao

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QuerySnapshot

interface DAO<T> {
    fun convert(documents: QuerySnapshot):List<T>
}