**Organizer**
**Create a new organizer**
**URL** : POST(method)	/organizers/create
RequestBody : Organizer DTO
ResponseData with created OrganizerEntity

**Get user by ID**
**URL**: GET(method)	/users/{id}
RequestBody : Path variable id
ResponseData with user or error

**Get all users**
**URL**: GET(method)	/users
ResponseData with List<UserEntity>

**Delete user by ID**
**URL**: DELETE(method)	/users/{id}	
ResponseData with status

**UserRegistration**
**Register a new user**
**URL**: POST(Method)	/users/create		
RequestBody:User DTO	
ResponseData with UserEntity

**Get user by ID**
**URL**: GET(Method)	/users/{id}		
RequestBody: Path variable id	
ResponseData with user or error

**Get all users**
**URL**: GET(Method)	/users	
ResponseData with List<UserEntity>

**Delete user by ID**
**URL**: DELETE(Method)	/users/{id}		
RequestBody:Path variable id	
ResponseData with status



