# Money Transfer Anemic

This project is an example of layered architecture. All classes are organized into 3 layers:
1. Presentation layer. Usually contains rest controllers, queue message listeners and all sorts of input adapters. We leave it outside the scope of the solution, let's just imagine that it exists somewhere.
2. Application layer. The `service` package, where we put all the application logic.
3. Persistence layer. `dao` and `entity` packages with Spring Data repositories and JPA entities.

It's important to keep the right direction of dependencies between layers. For example classes from 
the application layer can use classes from the Persistence layer, but not from the presentation layer.

## Unit tests
The `MoneyTransferServiceUnitTest` class contains unit tests for the `MoneyTransferService`. 
We are using the `Mockito` library to mock all class dependencies. 

## Integration tests
The `MoneyTransferServiceIntegrationTest` class contains integration tests. 
We are using in-memory version of H2 as the application database. For real applications, we will most likely use something like PostgreSQL and run tests with using the TestContainers library.
