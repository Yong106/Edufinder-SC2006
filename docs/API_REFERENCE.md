# API Reference

This document provides an overview of all API endpoint available in the project. It includes usage examples, request/response formats, and security.

## Authentication & Security

- Some endpoints are protected and require user authentication via an **HTTP-only cookie**.
- The authentication cookie is **automatically issued** upon successful **login** or **signup**. Clients do **not** need to include any additional headers in subsequent requests.
- Since the cookie is **HTTP-only** and inaccessible to client-side scripts, authentication status can be verified by calling a protected endpoint:
  - A **401 Unauthorized** response indicates the user is **not authenticated**.
  - Any successful response indicates the user is **authenticated**.

## General Response Status

All API responses include an appropriate HTTP status code to indicate the outcome of the request.
Common status codes are listed below. Some standard codes may not be repeated in individual endpoint documentation.

### Success:
- `200 OK`
  - The request was successful, and a response body (if applicable) is returned.
- `204 NO CONTENT`
  - The request was successful, but no content is returned in the response body.

### Error:
- `400 BAD REQUEST`:
  - The request could not be processed due to client-side errors, such as:
    - Malformed JSON request body
    - Missing required fields
    - Invalid data format (e.g., incorrect email format)
- `401 UNAUTHENTICATED`:
  - Authentication is required or the provided authentication cookie is invalid.
- `403 FORBIDDED`:
  - The client is authenticated but does not have sufficient permission to perform the operation.
    (In some cases, `404 NO FOUND` may be returned instead to avoid exposing resource existence.)
- `404 NOT FOUND`:
  - The requested resource or endpoint does not exist, or the client lacks permission and the resource should not be exposed.
- `409 CONFLICT`:
  - The request conflicts with the current server state (e.g. attempting to unsave a resource that is not currently saved).
- `500 INTERNAL SERVER ERROR`:
  - An unexpected error occurred on the server.
    Try again later or contact support if the problem persists.

## General Error Response Format

Error responses follow a consistent JSON structure to indicate the cause and details of a failed request.

### Custom Exception
Errors handled by the application’s custom `GlobalExceptionHandler` contain the following fields:
- `status`: The HTTP status code of the response.
- `error`: The standard HTTP status name.
- `message`: A human-readable explanation of the error.
- `timestamp`: The time at which the error occurred (in ISO 8601 format).

### Default Spring Boot Errors

If the error is not handled by the custom exception handler, Spring Boot automatically generates a default error response.
These responses may include additional fields:
- `path`: The API endpoint that caused the error.
- `trace`: Stack trace information (typically shown only in development mode).

**Example**:
```json
{
    "timestamp": "2025-11-04T20:57:19.445175",
    "message": "Access denied.",
    "error": "Not Found",
    "status": 404
}
```

    
## API Endpoint

## School

### GetAllSchools
- **Path**: `GET /api/schools`
- **Auth Required**: No
- **Success Code**: `200 OK`
- **Details**: Returns all available schools.

**Request Format**:
Not Required

**Response Format**:
```json
{
  "schools": [
    {
      "id": 1,
      "name": "ADMIRALTY PRIMARY SCHOOL",
      "ccas": [
        {
          "name": "ART AND CRAFTS",
          "type": "CLUBS AND SOCIETIES"
        },
        {
          "name": "CHINESE DANCE",
          "type": "VISUAL AND PERFORMING ARTS"
        }
      ],
      "subjects": [
        "Art",
        "Urdu Language"
      ],
      "programmes": [
        "Art Elective Programme",
        "Engineering and Tech Programme and Scholarship"
      ],
      "level": "PRIMARY",
      "natureCode": "CO-ED SCHOOL",
      "type": "GOVERNMENT SCHOOL",
      "sessionCode": "FULL DAY",
      "location": "WOODLANDS",
      "address": "11 WOODLANDS CIRCLE   ",
      "postalCode": "738907",
      "nearbyMrtStation": "Admiralty Station",
      "nearbyBusStation": "TIBS 965, 964, 913",
      "website": "https://admiraltypri.moe.edu.sg/",
      "email": "ADMIRALTY_PS@MOE.EDU.SG",
      "phoneNumber": "63620598",
      "faxNumber": "63627512",
      "minCutOffPoint": null,
      "maxCutOffPoint": null,
      "motherTongue1": "CHINESE",
      "motherTongue2": "MALAY",
      "motherTongue3": "TAMIL",
      "sapInd": false,
      "autonomousInd": false,
      "giftedInd": false,
      "ipInd": false
    },
    {
      "id": 2,
      "name": "ADMIRALTY SECONDARY SCHOOL",
      "ccas": [
        {
          "name": "ART AND CRAFTS",
          "type": "CLUBS AND SOCIETIES"
        },
        {
          "name": "BADMINTON",
          "type": "PHYSICAL SPORTS"
        }
      ],
      "subjects": [
        "Additional Mathematics",
        "Arabic Language as 3rd Lang"
      ],
      "programmes": [],
      "level": "SECONDARY (S1-S5)",
      "natureCode": "CO-ED SCHOOL",
      "type": "GOVERNMENT SCHOOL",
      "sessionCode": "SINGLE SESSION",
      "location": "WOODLANDS",
      "address": "31 WOODLANDS CRESCENT   ",
      "postalCode": "737916",
      "nearbyMrtStation": "ADMIRALTY MRT",
      "nearbyBusStation": "904",
      "website": "http://www.admiraltysec.moe.edu.sg",
      "email": "Admiralty_SS@moe.edu.sg",
      "phoneNumber": "63651733",
      "faxNumber": "63652774",
      "minCutOffPoint": null,
      "maxCutOffPoint": null,
      "motherTongue1": "CHINESE",
      "motherTongue2": "MALAY",
      "motherTongue3": "TAMIL",
      "sapInd": false,
      "autonomousInd": false,
      "giftedInd": false,
      "ipInd": false
    }
    ]
}
```

**Note:**
Most of the response fields correspond to fields in data.gov.sg. Visit data.gov.sg to know the exact meaning or possible value of fields.

## Authentication

### Login
- **Path**: `GET /api/auth/login`
- **Auth Required**: No
- **Success Code**: `200 OK`
- **Details**: Login and get user information

**Request Format**:
```json
{
  "username": "john",
  "password": "John1234@"
}
```

**Response Format**:
```json
{
  "id": 1,
  "username": "john",
  "role": "USER",
  "postalCode": "123456"
}
```
- `Role`: User role, either `USER` or `ADMIN`.

**Note**:
Authentication cookie with be automatically issued.

**Error Code**:
- `401 Unauthorised`:
  - Invalid credentials

### Signup
- **Path**: `GET /api/auth/signup`
- **Auth Required**: No
- **Success Code**: `200 OK`
- **Details**: Signup and get user information

**Request Format**:
```json
{
  "username": "john",
  "password": "John1234@"
}
```

**Response Format**:
```json
{
  "id": 1,
  "username": "john",
  "role": "USER",
  "postalCode": "123456"
}
```
- `Role`: User role, either `USER` or `ADMIN`.

**Note**:
Authentication cookie with be automatically issued.

**Error Code**:
- `400 BAD REQUEST`
  - Password doesn't meet minimum security policy:
    - At least 1 uppercase
    - At least 1 lowercase
    - At least 1 digit
    - At least 1 special symbol
    - At least 8 characters
- `409 CONFLICT`
  - Username already taken by another user

### Logout
- **Path**: `GET /api/auth/logout`
- **Auth Required**: No
- **Success Code**: `204 NO CONFLICT`
- **Details**: Logout

**Request Format**: Not Required

**Response Format**: Not Returned

**Note**:
Authentication cookie with be removed.

## User

### EditUser
- **Path**: `GET /api/users`
- **Auth Required**: Yes
- **Success Code**: `200 OK`
- **Details**: Edit user profile.

**Request Format**:
```json
{
  "postalCode": "908765"
}
```

**Response Format**:
```json
{
  "id": 1,
  "username": "john",
  "role": "USER",
  "postalCode": "908765"
}
```


