# meduva
## Table of contents
* [General](#general)
* [Technologies](#technologies)
* [Running](#running)
* [Preview](#preview)

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

## Preview
Welcome page:

![image](https://user-images.githubusercontent.com/54111855/150813121-eabbd726-ef8c-4980-b195-202fe8e1de95.png)

Login page:

![image](https://user-images.githubusercontent.com/54111855/150813347-cc4fcb86-a9fc-4df2-9e06-60c0b259dfc1.png)

Admin profile page:

![image](https://user-images.githubusercontent.com/54111855/150813515-dcb1480c-1155-4b16-9d30-1bb79cd5d97e.png)

Making an appointment:

![image](https://user-images.githubusercontent.com/54111855/150813882-dcdaa879-c011-4d85-b0dd-ac6681bc440c.png)

Selecting visit term:

![image](https://user-images.githubusercontent.com/54111855/150815035-dedd8ab1-eba6-4684-a3cd-1cb18ff9821b.png)

![image](https://user-images.githubusercontent.com/54111855/150814911-65a5707b-5794-4ff3-b088-4f824fd67ac8.png)

Schedule view:

![image](https://user-images.githubusercontent.com/54111855/150816341-2d4c9743-33ef-4128-b91f-d74be946ec5d.png)

Adding equipment:

![image](https://user-images.githubusercontent.com/54111855/150817394-283ae555-2176-48e7-b9c8-a9b5fa25e591.png)
