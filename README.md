# Getting Started 
    In the application, users can register and create, get their own wishlist.

### Technology Stack
    Java 8
    Spring Boot 2.5
    Spring Data Couchbase
    Couchbase
    Swagger 2
    Maven
    Docker
    Mockito & Junit
    
### Clone Repository

    git clone https://github.com/vturkoglu/wunderlist.git

## Build app 

    1-) Before you build/run the app, you must first run couchbase server via following command;
        docker-compose up coucbase 
        go to localhost:8091 and cofigure couchbase server with following instractions;
        - cluster name : 127.0.0.1
        - username: Administator
        - password: password
        - (create) bucket: wunderlist

        Build the project via maven;
        mvn clean install -U

    2-) You can run the app following command;
        mvn spring-boot:run

## Running Test Cases

## Register
        ---Register Request---
        curl -i -X POST \
        -H "Content-Type:application/json" \
        -d \
        '{
            "username" : "user1",
            "password": "12345"
        }' \
        'http://localhost:8080/api/user/signup'

        ---Register Response---
        {
            "username": "user1",
            "token": "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyIjoibWVtYXRpMiJ9.VXywqyggY35Erhaahp4HzFEJ1ujdSQrJXEvbA-tk56qA7OP8Sf68_8jUKaa4Py7RE8C7Ktl38cbw4i7CvO2L5g",
            "wishlist": null,
            "links":[]
        }

## Login
        ----Login Request---
        curl -i -X POST \
        -H "Content-Type:application/json" \
        -d \
        '{
            "username" : "user1",
            "password": "12345"
        }' \
        'http://localhost:8080/api/user/login'

        ---Login Reponse---
        Token : eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyIjoibWVtYXRpMiJ9.VXywqyggY35Erhaahp4HzFEJ1ujdSQrJXEvbA-tk56qA7OP8Sf68_8jUKaa4Py7RE8C7Ktl38cbw4i7CvO2L5g

## Add Wishlist
        ---Wishlist Add Request---
        curl -i -X POST \
        -H "Content-Type:application/json" \
        -H "Authentication:Bearer eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyIjoibWVtYXRpMiJ9.VXywqyggY35Erhaahp4HzFEJ1ujdSQrJXEvbA-tk56qA7OP8Sf68_8jUKaa4Py7RE8C7Ktl38cbw4i7CvO2L5g" \
        -d \
        '{
            "title" : "Wishlist-1",
            "description" : "asdfdfdsf sdfds fsdf sdfs f"
        }' \
        'http://localhost:8080/api/wishlist/user1/add'

## Get Wishlist 
        ---Get Own Wishlist Request---
        curl -i -X GET \
        -H "Authentication:Bearer eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyIjoibWVtYXRpMiJ9.VXywqyggY35Erhaahp4HzFEJ1ujdSQrJXEvbA-tk56qA7OP8Sf68_8jUKaa4Py7RE8C7Ktl38cbw4i7CvO2L5g" \
        'http://localhost:8080/api/wishlist/user1/get'

## Swagger
    You can also monitoring and test/run methods via swagger.

http://localhost:8080/swagger-ui.html

