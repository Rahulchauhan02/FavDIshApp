package com.mylearninghub.favdish.model.repository

import androidx.annotation.WorkerThread
import com.mylearninghub.favdish.model.dao.FavDishDao
import com.mylearninghub.favdish.model.FavDishEntity


class FavDishRepository(val favDishDao: FavDishDao) {

    @WorkerThread
    suspend fun insert(favDishEntity: FavDishEntity)
    {
        favDishDao.insertFavDish(favDishEntity)
    }
}