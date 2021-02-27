package com.example.android.politicalpreparedness.repository

import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election

/**
 * The repository class containing operations for use with both network and database operations.
 */
class ElectionsRepository(private val electionDatabase: ElectionDatabase) {

    /**
     * Retrieves upcoming elections from the Google Civics API.
     */
    suspend fun getUpcomingElections(): List<Election> {
        return CivicsApi.retrofitService.getElections().elections
    }
}