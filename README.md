1.Open a account in https://openweathermap.org/
2.Generate the api_key for the user
3. update the **openweathermap.api.key** property in the application.properties with the newly created api_key (api_key activation might take some time)
4.create a mysql database and provide the database details in spring.datasource.url property 
5. update the relavant username and password for mysql database in properties 
   **spring.datasource.username**
   **spring.datasource.password**
6. start the application 
7. make a post request to the API http://localhost:8080/app/weather with body has 
{
"postalCode":<US_postal_code>,
"user":<user>
}
8. to get history of user request use http://localhost:8080/app/history?user=<user>
9. to get history of user request by postal code use http://localhost:8080/app/history?postalCode=<pincode>
