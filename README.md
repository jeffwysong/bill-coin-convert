## Bill to Coin Converter Api

#### How to start the application
1. Clone this repo.
1. In the root directory of the project run:
`./mvnw spring-boot:run`

#### Ways to interact with the REST Api
1. Use the swagger ui:
    1. Open up a web browser and go to: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
    
1.  Use curl:
    1.  Open a command prompt that has curl installed.
    1.  Run `curl -X GET "http://localhost:8080/inventory" -H  "accept: application/json"`
     to see the machines inventory.
    1. Run `curl -X POST "http://localhost:8080/convert" -H  "accept: application/json" -H  "Content-Type: application/json" -d "[  {    \"billValue\": 2,    \"numProvided\": 1  },  {    \"billValue\": 5,    \"numProvided\": 2  }]"`
    to convert 12 dollars into cents.  Fill free to add as many billValue and numProvided sets as you like.
    1. If you prefer just inputting a number as a parameter, then run:
    `curl -X POST "http://localhost:8080/convert/7" -H  "accept: application/json"` 
    to convert $7 for example.
    1. if you want to Re-initialize the machine, run:
    `curl -X PUT "http://localhost:8080/initialize" -H  "accept: application/json" -H  "Content-Type: application/json" -d "{  \"number\": 100}"`.
    You can put any positive value for the number.
    
    
####  Things left to do:
* Validate /convert endpoint request body matches valid bills.
* Remove feature of adding negative dollars adds coins to machine.
* Add HATEOAS links to:
    * the convert response for inventory and initialize.
    * the inventory response for convert and initialize.
    * the initialize response for inventory and convert.
* Build UI
