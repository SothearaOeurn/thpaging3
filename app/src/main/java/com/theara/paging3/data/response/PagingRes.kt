/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.theara.paging3.data.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Paging")
class PagingRes {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @field:SerializedName("id")
    var id: Long? = null

    @ColumnInfo(name = "name")
    @field:SerializedName("name")
    var name: String? = null

    @ColumnInfo(name = "full_name")
    @field:SerializedName("full_name")
    var fullName: String? = null

    @ColumnInfo(name = "description")
    @field:SerializedName("description")
    var description: String? = null

    @ColumnInfo(name = "html_url")
    @field:SerializedName("html_url")
    var url: String? = null

    @ColumnInfo(name = "stargazers_count")
    @field:SerializedName("stargazers_count")
    var stars: Int? = null

    @ColumnInfo(name = "forks_count")
    @field:SerializedName("forks_count")
    var forks: Int? = null

    @ColumnInfo(name = "language")
    @field:SerializedName("language")
    var language: String? = null
}
