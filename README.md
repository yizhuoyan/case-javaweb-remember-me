# remember me 

users like we could remember who them are when them had checked the remember-me-checkbox.

# implementation

we create a cookie named "REMENMBER-ME" when them sign on with checked the remember-me-checkbox.

the "REMENMBER-ME" cookie value is a token which we had persisted in the db with the account record;


when they session time out and want to get any protected resource them should take the cookie with the request. 
we first check the token from db.
then check the timeout
then check the user-agent is the samething 