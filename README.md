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