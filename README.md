# shopping-cart
This is a REST api implementation of a rudimentary shopping cart application.
There is no UI available, however api endpoints can be called via postman.

## Functionality:
A user can add an item in as to the shopping cart to be able to order it later
  - If the quantity of the item > available quantity of available product, the item will not be added to the shopping cart.
  - If the quantity of the item < available quantity of available product, the item will be added to the shopping cart with the available quantity.
  - If an item is added to the shopping cart that is already in the shopping cart, the item count in the shopping cart will be increased by the added number.
  - All contents of a shopping cart can be displayed based on the shopping cart ID to check the contents.
  - The output contains all items (item, quantity, price)
  - The output displays the total price, which is calculated from the sum of the item prices.
  - An item can be deleted from the shopping cart.
  - As a customer I would like to be able to empty the complete shopping car

### Usage:
Application can be started by running docker-compose file as following:

`docker-compose up`

which will runn the application and will be available on `localhost:8080`

### Api Endpoints:
Following api endpoints are available to test 

- Adding a new cart  POST `localhost:8080/api/carts`
- Adding a new Item into a cart POST `localhost:8080/api/carts/1/items?productId=1&quantity=4`
- Removing an item from cart DELETE `localhost:8080/api/carts/items/1`
- Empty whole cart DELETE `localhost:8080/api/carts/1/empty`
- Display contents of the cart GET `localhost:8080/api/carts/1/items`

### Start-up data
There is H2 in-memory database is used and is populated with following data at start-up.

- one cart with and id=1
- Five products with ids 1 to 5 with each having a quantity of 5.

