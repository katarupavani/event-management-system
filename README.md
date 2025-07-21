event-management-system – Feedback API

Add Feedback URL: /feedbacks/add Method: POST Request Body: Feedback (includes user ID, event ID, rating, and comments) Response: ResponseData containing the saved feedback or an error message

Get Feedback by ID URL: /feedbacks/{id} Method: GET Path Variable: id (integer) Response: ResponseData containing the feedback details or an error message

Get All Feedbacks URL: /feedbacks Method: GET Response: ResponseData containing a list of all feedback entries

Get Feedbacks by User ID URL: /feedbacks/user/{userId} Method: GET Path Variable: userId (integer) Response: ResponseData containing all feedbacks submitted by the specified user

Get Feedbacks by Event ID URL: /feedbacks/event/{eventId} Method: GET Path Variable: eventId (integer) Response: ResponseData containing all feedbacks for the specified event

Update Feedback URL: /feedbacks/update/{id} Method: PUT Path Variable: id (integer) Request Body: FeedbackEntity (includes updated rating and/or comments) Response: ResponseData containing updated feedback details or an error message

Delete Feedback URL: /feedbacks/{id} Method: DELETE Path Variable: id (integer) Response: ResponseData with a success or failure message
POST: http://localhost:8080/bookings/create is for event booking.

GET: http://localhost:8080/bookings/1  is for fetching data by id.

GET: http://localhost:8080/bookings is for fetching all data.

DELETE: http://localhost:8080/bookings/1 is for deleting data by id.

GET: http://localhost:8080/bookings/user/1 is for fetching data based on userid.

GET: http://localhost:8080/bookings/event/1 is for fetching data based on eventid.

GET: http://localhost:8080/bookings/date/{date} is for fetching data based on date.

POST: http://localhost:8080/events/create
DELETE:http://localhost:8080/events/100
GET:http://localhost:8080/events/upcoming
GET:http://localhost:8080/events/category/Music


