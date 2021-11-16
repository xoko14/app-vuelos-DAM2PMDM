package com.xoquin.vista_ej13.dao

interface DAO<T> {
    fun getOne(): T
    fun getAll(): List<T>
}