package com.mylearninghub.favdish.model.dao

import androidx.room.Dao
import androidx.room.Insert
import com.mylearninghub.favdish.model.FavDishEntity

@Dao
interface FavDishDao {
@Insert
suspend fun insertFavDish(favDishEntity: FavDishEntity)
}