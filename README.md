# Insta Test

## Overview

This application is designed to facilitate testing of backend APIs. It allows users to perform GET and POST requests, manage request headers dynamically, cache requests/responses locally, and handle various network scenarios such as offline connectivity.

### Architecture

The application follows a structured approach using Clean Architecture with layers divided into:

- **Presentation (UI)**: Jetpack Compose for building the UI, LiveData for observing data changes.
- **Domain (Use Cases)**: Business logic encapsulated in Use Cases.
- **Data (Repositories)**: SQLite for local caching of API requests/responses.

### Features Implemented

1. **Dynamic URL Entry and Header Management**
    - Users can enter a URL and add multiple headers dynamically for each request.

2. **GET/POST Requests**
    - Supports both GET and POST request methods.
    - POST requests can handle two types of request bodies: JSON string and multipart/file uploads.

3. **Response Display**
    - After each request, the application displays:
        - Request URL
        - Response code
        - Error message (if any)
        - Request and Response headers
        - Request body or query parameters (depending on request type)
        - Response body

4. **Offline Handling**
    - Detects and notifies users of offline status, preventing requests when the device is not connected.

5. **Caching**
    - Stores executed requests and their responses locally using SQLite.
    - Supports viewing, sorting, and filtering cached requests/responses:
        - Sort by execution time
        - Filter by request type (GET/POST)
        - Filter by response status (Success/Failure)

### Technologies Used

- **Jetpack Compose**: Declarative UI toolkit for building native Android UIs.
- **LiveData**: Lifecycle-aware observable data holder class.
- **SQLite**: Local database for caching network requests/responses.
## Fake Server for testing
## GET Getsuccess?id=99
https://fab51db5-7ad2-43c4-a43a-9cdbb80f2079.mock.pstmn.io/Getsuccess?id=99

## GET GetFailed?id=99
https://fab51db5-7ad2-43c4-a43a-9cdbb80f2079.mock.pstmn.io/GetFailed?id=99

//////////////////////////
## POST addUserTrue
https://fab51db5-7ad2-43c4-a43a-9cdbb80f2079.mock.pstmn.io/addUserTrue

{"phone":"01010101010","name":"New Ahmed"}

//////////////////////////
## POST addUserFalse
https://fab51db5-7ad2-43c4-a43a-9cdbb80f2079.mock.pstmn.io/addUserFalse

{"phone":"01010101010","name":"New Ahmed"}

## AUTH
x-api-key
PMAK-668c143dafe0550001c19072-855a7e5541ecfe655d3ed55cd4c9d8ddb6
