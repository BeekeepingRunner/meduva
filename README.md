# meduva
## Table of contents
* [General](#general)
* [Technologies](#technologies)
* [Running](#running)
* [Functionalities](#functionalities)

## General
A uniwersal web application for health care units - places like medical, physioterapeutic, rehabilitation or wellness centres. The app includes visit, schedules and facility management. It enables services to be defined and linked to rooms, employees and equipment. Registered clients and workers can plan their visits. Available dates of visits are determined based on properly set working hours of individual employees and the availability of equipment and rooms. It is also possible to view schedules and define unavailability hours for workers, rooms and equipment.

## Technologies
Project is created with:
* Java: 11
* Spring Boot: 2.5.3
* Angular: 12.0.1
* MySQL

## Running
* To run the project you need to download it from the github repository
* You also have to have an existing MySQL connection

### Running servers
Now you need to start the frontend and backend servers.

#### Frontend
Before you start the frontend server, inside the  _/meduva-frontend_ install all necessary dependencies with the following command:
```
$ npm install
```
After that, to start the server type the following:
```
$ ng serve
```
Frontend server works on localhost:4200

#### Backend
Before starting the backend server, you need to set environmental variables associated with your MySQL database, because they are used for connection in the application.properties file:

![image](https://user-images.githubusercontent.com/54111855/150802395-812a59bf-8b41-4ee4-b940-6057f0e7f843.png)

To start the backend server, type the following command inside the _/meduva-backend_ folder:
```
$ mvn spring-boot:run
```
Backend server share its API on localhost:8080/api

## Functionalities
The application consists of several main functional modules which, when combined with each other, allow for the automation of work. Each of them has been wrapped in a graphic design, allowing for easier navigation and operation.

#### Welcome page:
![image](https://user-images.githubusercontent.com/54111855/150813121-eabbd726-ef8c-4980-b195-202fe8e1de95.png)

#### Admin profile page:
![image](https://user-images.githubusercontent.com/54111855/150813515-dcb1480c-1155-4b16-9d30-1bb79cd5d97e.png)

The list of modules is presented below: <br />
* Registration for the visit
* Schedule management
* Facility management
* User management
* Authentication and autorization

### Registration for the visit
Registration for an appointment is one of the most important modules and the essence of the application. The visit can be planned according to various points of reference. The client can register himself, the specialist who performs the service can register the client, and the receptionist can arrange the client's visit with the specialist. 

#### Making an appointment:
![image](https://user-images.githubusercontent.com/54111855/150813882-dcdaa879-c011-4d85-b0dd-ac6681bc440c.png)

It is implemented based on an algorithm that automatically generates available dates, taking into account the available resources. These resources are rooms, equipment and employees competent to perform the service. The customer can choose an interesting date from those available on a given day.

#### Selecting visit term:
![image](https://user-images.githubusercontent.com/54111855/150815035-dedd8ab1-eba6-4684-a3cd-1cb18ff9821b.png)
![image](https://user-images.githubusercontent.com/54111855/150814911-65a5707b-5794-4ff3-b088-4f824fd67ac8.png)

### Schedule management
Schedule management consists of three parts: managing the schedules of rooms, devices and employees. The data related to these modules are used by the visit planning algorithm to determine an appropriate, collision-free date. Users with the role of a receptionist or administrator can directly manage the schedules of individual resources.

#### Schedule view:
![image](https://user-images.githubusercontent.com/54111855/150816341-2d4c9743-33ef-4128-b91f-d74be946ec5d.png)

### Facility management
Managing the facility, as well as managing schedules, is based on three main modules: users, rooms and services configuration. Each of them can be used separately and together, based on a special facility configuration wizard. It is a tool created to combine the above functionalities in a user-friendly way (especially in the case of initial system preparation). It allows for full personalization using the "step by step" method. It also includes the possibility of configuring connections between elements representing a given module.

#### Adding equipment:
![image](https://user-images.githubusercontent.com/54111855/150817394-283ae555-2176-48e7-b9c8-a9b5fa25e591.png)

### User management
The user management module can be seen at the beginning of using the application, as logging and registration can be treated as part of this module. They were implemented on the basis of JSON Web Token (JWT).

#### Login page:
![image](https://user-images.githubusercontent.com/54111855/150813347-cc4fcb86-a9fc-4df2-9e06-60c0b259dfc1.png)

Once the profile is created, it can be managed by the user and the system administrator. The account may be deleted or edited, and the editing is based on the change of personal data or login details, as well as the possibility of changing the user's role in the system. 
The application also offers support for users who are not logged in by adding their most important data to the system. This is useful when the customer does not have an account on the site, but wants to use the facility's services (e.g. in the case of a telephone connection with the receptionist).

#### New client:
![image](https://user-images.githubusercontent.com/79913325/150968925-9320e8fa-a080-4700-abcd-e4a4aaa28443.JPG)

