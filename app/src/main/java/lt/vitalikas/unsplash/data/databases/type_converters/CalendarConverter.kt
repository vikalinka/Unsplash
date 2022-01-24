package lt.vitalikas.unsplash.data.databases.type_converters

import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter

@RequiresApi(Build.VERSION_CODES.N)
class CalendarConverter {

    @TypeConverter
    fun fromCalendarToLong(calendar: Calendar): Long = calendar.time.time

    @TypeConverter
    fun longToCalendar(long: Long): Calendar = Calendar.getInstance().apply { timeInMillis = long }
}