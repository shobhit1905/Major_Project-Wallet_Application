MAJOR PROJECT Structure : WALLET Application

Features :

1. Add money
2. Withdraw money
3. Send money
4. Recharge/Bill payment
5. Get history
6. Get transaction details with transaction id
7. Get balance
8. Notifications in mail


Services :

1. User service
2. Transaction service
3. Wallet service
4. Notification service



API’s Required :

1 :- User Service :-
	Create account
	Get details
	Update account
	Delete account

2 :- 	Wallet service :-
	  Get balance
	  Create wallet
	  Update wallet
	  Update configuration of the wallet
	
3 :- 	Transaction Service :-
	  Initiate transaction
	  Recharge / Bill payment
	  Get transaction details
	  Get history

4 :- 	Notification Service :- 
	  Send Mail


Entities : 

1. User 
	- user id : primary key
	- user name : not null
	- user email : not null
	- user mobile : not null , unique , validated
	- user address : not null , validated
 	- user pancard number
	- created date
	- updated date


2. Wallet 
	- wallet id : primary key
	- user id : not null , foreign key
	- balance : not null , not < 0
	- created date
	- updated data
	- daily limit
	- daily transaction limit


3. Transaction 
	- transaction id : primary key
	- wallet id 
	- receiver id 
	- amount 
	- senders id
 	- payment status : ENUM(successful , pending , failed)
	- payment date
	- remark

4. User Authentication 
	- username
	- password 
	- userid
