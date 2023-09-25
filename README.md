# Stock Market Data Application

A spring boot application that makes requests to Robinhood(or other data providers) for market and instrument data and updates the table data in the database, and provides the user with an endpoint to get database data and sync data with the ones in data providers.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Development Notes
* Integration and unit tests has been developed for nearly all services and controllers. Test containers are used for integration tests so you will need Docker to run integration tests.
* Response of GetInstrumentBySymbol method in InstrumentService is cached. These caches will be evited if you sync instrument data (request to the api/instruments/sync) for data consistency.
* If you stop the containers after running them and start them again, the application container will give an error because it will try to insert the data in the instrument table into the database again (the spring application will try to run the data.sql file every time it starts). For this reason, before starting the application again, you need to run the 'docker-compose down' command and delete the existing containers.

### Prerequisites

Application itself, PostgreSQL and Redis (for caching) databases are containerized by using Docker Compose and provided connection to these containers with each other. So you need to have Docker in your computer.

### Installation

A step by step guide that will tell you how to get the development environment up and running.

1. Clone the repo
   ```sh
   git clone https://github.com/shintalha/Stock-Market-Data-Application.git
   ```
2. Run dockerized app
   ```sh
   docker-compose up
   ```

## Usage

Go to the swagger ui screen
   ```sh

   http://localhost:8080/swagger-ui/index.html
```

![Screenshot 2023-09-25 at 20 12 47](https://github.com/shintalha/Stock-Market-Data-Application/assets/97956471/40f0dfec-529c-4c0b-9b62-7fbc4d2b5fa3)


