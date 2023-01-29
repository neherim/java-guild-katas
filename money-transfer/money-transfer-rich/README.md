# Money Transfer Rich

This project is an example of onion architecture. We moved all domain logic to the `domain` package.
It's important to keep domain logic pure, separated from any I/O operations. Classes from the `domain` package should
only depend on primitive types or other domain classes.

All orchestration, such as loading objects from a database or interactions with external services, we put in the `application` package.
A typical method from the `applcication` package consists of 3 parts:
1. Load the domain object from the repository.
2. Call the domain object method.
3. Save the domain object to the repository.

### DDD
The `Account` class is the aggregate root in terms of DDD (Domain Driven Design). 
It contains all the information about a current balance and an amount of money reserved on it. 
All operations with the account balance or reserved money go through the `Account` class. 
Thanks to that, we have only one place where the balance value could change, and we can implement all business rules in it. 
In contrast to the anemic model, where the `Account` class has only getters and setters without logic. 
So, the balance value can be changed from any class without proper validation of business rules.

### Unit test
Domain logic is the most important part of any application, and we want to test this part as best we can. 
Domain model purity makes writing tests much easier, you don't need to use mocks or Spring context in your tests.
Thanks to that, we can write a lot of small and fast unit tests for the `domain` classes and just a few tests for the `application` layer. 

### Integration test
The `application` layer does not contain important business rules or conditions at all. 
So it will be enough to write one simple integration test to have 100% coverage on it. 
We don't need to use mocks here either, because if we mock all the dependencies of the `MoneyTransferService` class, 
then we have nothing to test.
In our example we use the H2 in-memory database for integration tests, but in the real world you'll 
need something like a `PostgreSQL` with a `TestContainers` library.