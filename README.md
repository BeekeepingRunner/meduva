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

## Running
To run the project you need download this project from the github repository. You are able to do it in three ways:
* Cloning the repository by git clone
* Downloading the ZIP file
* Opening with GitHub Desktop (it is required download and install the github desktop software)
### Cloning the repository by git clone
* Using HTTPS
```
$ git clone https://github.com/urgeorge/meduva.git
```
* Using SSH key
```
$ git clone git@github.com:urgeorge/meduva.git
```
* Using GitHub CLI
```
$ gh repo clone urgeorge/meduva
```
### Running servers
Then you need to start the frontend and backend servers. You can achieve this by typing and executing the following commands inside the proper server folder:
#### Frontend
```
$ ng serve
```
#### Backend
```
$ mvn spring-boot:run
```
