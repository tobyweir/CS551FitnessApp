package com.example.cs551fitnessapp.repository

import com.example.cs551fitnessapp.database.UserAppointmentDao

class EventRepository(private val dao: UserAppointmentDao) {

    val eventsFlow = dao.getAllEvents()

}