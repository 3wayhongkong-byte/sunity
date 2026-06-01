package com.propertymanager

import android.app.Application
import com.propertymanager.data.PropertyDatabase

class PropertyManagerApp : Application() {
    val database by lazy { PropertyDatabase.getDatabase(this) }
}
