package com.chase.challenge.schoolsapp.network

import com.chase.challenge.schoolsapp.data.SchoolList
import com.chase.challenge.schoolsapp.data.SchoolListItem
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class NYCSchoolsRestClientTest {

    @Mock
    private lateinit var nycSchoolsApi: NYCSchoolsApi

    private lateinit var nycSchoolsRestClient: NYCSchoolsRestClient

    @Before
    fun setup(){
        MockitoAnnotations.openMocks(this)
        nycSchoolsRestClient = NYCSchoolsRestClient()
        //Initializing mock object for cityOfNewYorkApi
        nycSchoolsRestClient.cityOfNewYorkApi = nycSchoolsApi
    }

    @Test
    fun testFetchSchoolsApi() = runBlocking{
        val schoolListItemOne = SchoolListItem(
            dbn = "21K728",
            school_name = "Liberation Diploma Plus High School",
            phone_number = "718-946-6812",
            website = "www.theclintonschool.net",
            overview_paragraph = "The mission of Liberation Diploma Plus High School",
        )
        val schoolListItemTwo = SchoolListItem(
            dbn = "21K729",
            school_name = "Liberation Diploma Plus High School",
            phone_number = "718-946-6812",
            website = "www.theclintonschool.net",
            overview_paragraph = "The mission of Liberation Diploma Plus High School",
        )
        val schoolList  = SchoolList()
        schoolList.add(schoolListItemOne)
        schoolList.add(schoolListItemTwo)

        //Mock the API Response
        Mockito.`when`(nycSchoolsApi.getListOfSchools()).thenReturn(schoolList)

        //Call API using test
        val result = nycSchoolsRestClient.getListOfSchools()

        Mockito.verify(nycSchoolsApi).getListOfSchools()

        assert(2 == result!!.size)
    }
}