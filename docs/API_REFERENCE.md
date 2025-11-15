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
- **Admin Required**: No
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

### EditSchoolCutOffPoint
- **Path**: `PUT /api/schools/{schoolId}`
- **Auth Required**: No
- **Admin Required**: Yes
- **Success Code**: `204 NO CONTENT`
- **Details**: Edit school cut off point.

**Request Format**:
```json
{
  "minCutOffPoint": 6,
  "maxCutOffPoint": 30
}
```
- `minCutOffPoint`: `Integer` between `4-32`
- `maxCutOffPoint`: `Integer` between `4-32`

**Response Format**: Not Returned

**Error Code**:
- `400 BAD REQUEST`
    - `minCutOffPoint` is not `Integer` between `4-32`
    - `maxCutOffPoint` is not `Integer` between `4-32`
    - `maxCutOffPoint` is smaller than `minCutOffPoint`
- `404 NOT FOUND`
    - School not found

## Authentication

### Login
- **Path**: `GET /api/auth/login`
- **Auth Required**: No
- **Admin Required**: No
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
- **Admin Required**: No
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
- **Path**: `PUT /api/users`
- **Auth Required**: Yes
- **Admin Required**: No
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
- `Role`: User role, either `USER` or `ADMIN`.

**Error Code**:
- `400 BAD REQUEST`
    - Postal code doesn't consist of 6 digits

### EditUserRole
- **Path**: `PUT /api/users/{userId}`
- **Auth Required**: Yes
- **Admin Required**: Yes
- **Success Code**: `200 OK`
- **Details**: Edit user role.

**Request Format**:
```json
{
  "role": "USER"
}
```
- `role`: User role, either `USER` or `ADMIN`.

**Response Format**:
```json
{
  "id": 1,
  "username": "john",
  "role": "USER",
  "postalCode": "908765"
}
```
- `role`: User role, either `USER` or `ADMIN`.

**Error Code**:
- `400 BAD REQUEST`
    - `role` is neither `USER` nor `ADMIN`
- `404 NOT FOUND`
    - User not found
- `409 CONFLICT`
    - User is the last `ADMIN` and is demoting to `USER` 

### GetSavedSchoolIds
- **Path**: `GET /api/users/saved-schools`
- **Auth Required**: Yes
- **Admin Required**: No
- **Success Code**: `200 OK`
- **Details**: Get list of id of user saved schools.

**Request Format**: Not Required

**Response Format**:
```json
{
    "savedSchoolIds": [
        1,
        2
    ]
}
```

### AddSavedSchool
- **Path**: `POST /api/users/saved-schools`
- **Auth Required**: Yes
- **Admin Required**: No
- **Success Code**: `204 NO CONTENT`
- **Details**: Add a school to user's saved school

**Request Format**: 
```json
{
    "schoolId": 2
}
```

**Response Format**: Not Returned

**Error Code**:
- `404 NOT FOUND`
  - School not found.
- `409 CONFLICT`
  - User already save this specific school.
  

### RemoveSavedSchool
- **Path**: `DELETE /api/users/saved-schools`
- **Auth Required**: Yes
- **Admin Required**: No
- **Success Code**: `204 NO CONTENT`
- **Details**: Remove a school from user's saved school

**Request Format**:
```json
{
    "schoolId": 2
}
```

**Response Format**: Not Returned

**Error Code**:
- `409 CONFLICT`
    - School is not in user's saved school.

## Comment

### GetCommentsBySchoolId
- **Path**: `GET /api/schools/{schoolId}/comments`
- **Auth Required**: No
- **Admin Required**: No
- **Success Code**: `200 OK`
- **Details**: Get all comments, include relevant replies and vote summary of a school.

**Request Format**: Not Required

**Response Format**:
```json
{
  "comments": [
    {
      "id": 3,
      "username": "user1",
      "content": "new comment",
      "createdAt": "2025-10-29T12:04:15",
      "replies": [
        {
          "id": 2,
          "username": "user1",
          "content": "new reply",
          "createdAt": "2025-10-31T22:39:01"
        },
        {
          "id": 3,
          "username": "user1",
          "content": "new reply",
          "createdAt": "2025-11-01T19:10:53"
        }
      ],
      "voteSummary": {
        "userVoteType": "DOWNVOTE",
        "upvoteCount": 0,
        "downvoteCount": 1
      }
    },
    {
      "id": 4,
      "username": "user1",
      "content": "new comment",
      "createdAt": "2025-10-29T12:33:40",
      "replies": [],
      "voteSummary": {
        "userVoteType": "NOVOTE",
        "upvoteCount": 0,
        "downvoteCount": 0
      }
    }
  ]
}
```
- `userVoteType`: `NOVOTE`, `UPVOTE` or `DOWNVOTE` indicating logged-in user vote type. It is always `NOVOTE` if user is anonymous.

**Error Code**:
- `404 NOT FOUND`
    - School not found.

### CreateComment
- **Path**: `POST /api/schools/{schoolId}/comments`
- **Auth Required**: Yes
- **Admin Required**: No
- **Success Code**: `200 OK`
- **Details**: Create comment for a school.

**Request Format**: 
```json
{
  "content": "new comment"
}
```

**Response Format**:
```json
{
    "id": 13,
    "username": "user1",
    "content": "new comment",
    "createdAt": "2025-11-05T16:59:59.405814",
    "replies": [],
    "voteSummary": {
        "userVoteType": "NOVOTE",
        "upvoteCount": 0,
        "downvoteCount": 0
    }
}
```
- `userVoteType`: `NOVOTE`, `UPVOTE` or `DOWNVOTE` indicating logged-in user vote type.

**Error Code**:
- `404 NOT FOUND`
    - School not found.

### DeleteComment
- **Path**: `DELETE /api/comments/{commentId}`
- **Auth Required**: Yes
- **Admin Required**: No
- **Success Code**: `204 NO CONTENT`
- **Details**: Delete a comment.

**Request Format**: Not Required

**Response Format**: Not Returned

**Error Code**:
- `404 NOT FOUND`
    - Comment not found
    - Requested user is not owner of comment


**Reply**

### CreateComment
- **Path**: `POST /api/comments/{commentId}/replies`
- **Auth Required**: Yes
- **Admin Required**: No
- **Success Code**: `200 OK`
- **Details**: Create reply for a comment.

**Request Format**:
```json
{
  "content": "new reply"
}
```

**Response Format**:
```json
{
  "id": 6,
  "username": "user1",
  "content": "new reply",
  "createdAt": "2025-11-05T17:07:01.639182"
}
```

**Error Code**:
- `404 NOT FOUND`
    - Comment not found.


### DeleteReply
- **Path**: `DELETE /api/replies/{replyId}`
- **Auth Required**: Yes
- **Admin Required**: No
- **Success Code**: `204 NO CONTENT`
- **Details**: Delete a reply.

**Request Format**: Not Required

**Response Format**: Not Returned

**Error Code**:
- `404 NOT FOUND`
    - Reply not found.
    - Requested user is not owner of reply.


## Vote

### SetVote
- **Path**: `PUT /api/comments/{commentId}/votes`
- **Auth Required**: Yes
- **Admin Required**: No
- **Success Code**: `200 NO CONTENT`
- **Details**: Create or edit vote of user for a comment.

**Request Format**:
```json
{
  "voteType": "DOWNVOTE"
}
```
- `voteType`: `NOVOTE`, `UPVOTE` or `DOWNVOTE`

**Response Format**: Not Returned

**Error Code**:
- `400 BAD REQUEST`
  - `voteType` is not `NOVOTE`, `UPVOTE` or `DOWNVOTE`.
- `404 NOT FOUND`
  - Comment not found.