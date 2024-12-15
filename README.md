1.Open a account in https://openweathermap.org/ <br>
2.Generate the api_key for the user <br>
3. update the **openweathermap.api.key** property in the application.properties with the newly created api_key (api_key activation might take some time) <br>
4.create a mysql database and provide the database details in spring.datasource.url property <br>
5. update the relavant username and password for mysql database in properties  <br>
   **spring.datasource.username** <br>
   **spring.datasource.password** <br>
6. start the application  <br>
7. make a post request to the API http://localhost:8080/app/weather with body has  <br>
{ <br>
"postalCode":<US_postal_code>, <br>
"user":<user> <br>
} <br>
8. to get history of user request use http://localhost:8080/app/history?user=<user> <br>
9. to get history of user request by postal code use http://localhost:8080/app/history?postalCode=<pincode> <br>
