package com.isi.isilibrary.cfbuilder

import java.util.*

class PersonalData(
    name: String,
    surname: String,
    dd: String?,
    mm: String?,
    yy: String?,
    gender: Boolean,
    birthplace: String?
) {
    var name: String? = null
        private set
    var surname: String? = null
        private set
    var dd: String? = null
    var mm: String? = null
    var yy: String? = null
    var birthplace: String? = null
    var isGender //true-->Male
            = false

    init {
        try {
            setName(name)
            setSurname(surname)
            this.dd = dd
            this.mm = mm
            this.yy = yy
            this.birthplace = birthplace
            isGender = gender
        } catch (e: Error) {
            throw RuntimeException("Something goes wrong in new PersonalData", e)
        }
    }

    fun setName(name: String) {
        this.name = name.uppercase(Locale.getDefault())
    }

    fun setSurname(surname: String) {
        this.surname = surname.uppercase(Locale.getDefault())
    }
}