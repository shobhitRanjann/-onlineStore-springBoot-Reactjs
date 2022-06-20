
# Online Store

This project idea is online store, to build a online store where
user can buy new products. I have not made it look like flipkart
or amazon. This is made using bootstrap, in future it may look like
that or more better than them.
Here in this project, you can 
* add new products.
* Tell the type of products
* payment will be made according to price you will mention.
* Login/ Signup

# Use Case

You can use this project for college project or if you want to build
personal online store.

## Environment Variables

To run this project, you will need to add the following environment.

`Java 8 or ++`

`My Sql /  Xampp Server`

`Postman`  <'for API check'>

`Razorpay Account` click here to create - [Razorpay](https://razorpay.com/)


## API Reference

#### Get all items

```http
  GET /all
```

#### Get item by Id

```http
  GET /allstore/${id}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `Content-Type` | `applicaion/json` | **Required**. (in body) |
| `Authorization`      | `Bearer <token>` | **Required**. you will get after successful login |

#### Login User And Generate JWT

 ```http
  POST /registerapii
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `username` | `string` | **Required**. your gmail |
| `password`      | `string` | **Required**. your password |


 ```http
   POST /process_register
  ```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `email` | `string` | **Required**. your gmail |
| `password`      | `string` | **Required**. your password |
| `firstName`      | `string` | **Required**. your password |
| `lastName`      | `string` | **Required**. your password |


Similarly you can check other api in controller, for reference i am giving you this link, from where you can understand how api works in [spring boot](https://spring.io/guides/tutorials/rest/)

## Important!

Frontend for this project is -> [here](https://github.com/shobhitRanjann/onlineStore-reactJs)
it is made in reactJs. All the steps to make it run are available there.

Happy Coding 😊



## Authors

- [Shobhit Ranjan](https://github.com/shobhitRanjann/)

Contribution guidelines for this project