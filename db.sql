USE master
GO

IF EXISTS (SELECT * FROM sys.databases WHERE name='iNewLookS')
	DROP DATABASE iNewLookS
	GO
	
CREATE DATABASE iNewLookS
GO

USE iNewLookS
GO

CREATE TABLE Account
(
id int IDENTITY(1,1) PRIMARY KEY,
username nvarchar(16) UNIQUE,
pwd varchar(32),
name nvarchar(32),
email varchar(64),
[address] nvarchar(128),
company nvarchar(64),
[admin] bit,
)
GO

CREATE TABLE Elevator
(
id int IDENTITY(1,1) PRIMARY KEY,
name nvarchar(128),
baseprice money,
floorprice money,
[description] nvarchar(3000),
maxWeight int,
maxHuman int,
country nvarchar(200),
)
GO

CREATE TABLE Request
(
id int IDENTITY(1,1) PRIMARY KEY,
userId int FOREIGN KEY REFERENCES Account(id),
elevatorId int FOREIGN KEY REFERENCES Elevator(id),
floorCount int,
systemCount int,
[address] nvarchar(3000),
totalPrice money,
done bit,
processing bit,
postDate datetime,
finishDate datetime,
)
GO

CREATE TABLE Task
(
id int IDENTITY(1,1),
requestId int FOREIGN KEY REFERENCES Request(id),
toDo nvarchar(1024),
done bit,
)

CREATE TABLE Feedback
(
id int IDENTITY(1,1),
userId int FOREIGN KEY REFERENCES Account(id),
senderName nvarchar(128),
senderEmail varchar(64),
[description] nvarchar(1000),
rate int,
problem nvarchar(3000),
comment nvarchar(3000),
[read] bit,
)
GO

INSERT INTO Account(username,pwd,name,email,[address],company,[admin]) VALUES
('admin','1','Pham Viet Hung', 'email@gmail.com', '67LCU', 'None', 1),
('a','a','Pham Viet Hung', 'email@gmail.com', '67LCU', 'None', 1)
GO

INSERT INTO Elevator(name, baseprice, floorprice, [description], maxWeight, maxHuman, country) VALUES
('E2014',39999,5000,'E2014 Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque tristique risus eu hendrerit mattis. Maecenas varius nulla mauris, vel volutpat sem rhoncus sed. Etiam elementum auctor dui sit amet volutpat. Pellentesque consectetur felis lorem. Quisque ac gravida dolor. Aenean cursus lorem at libero facilisis, nec faucibus justo porttitor. Aenean id ipsum euismod, tristique neque vitae, egestas odio. Morbi et felis venenatis libero dignissim sagittis. Ut gravida lacus at justo egestas, eget ultricies justo consequat. Donec porttitor rutrum vestibulum. Donec non quam id tellus fringilla ultricies in in velit. Quisque sed aliquet tortor, ultrices ullamcorper augue. Praesent tempus magna nisl, in hendrerit velit scelerisque ac. In hac habitasse platea dictumst. Ut pharetra mattis velit in convallis.',1000,11, 'USA'),
('E2013 child',49999,3500,'E2013 for child Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque tristique risus eu hendrerit mattis. Maecenas varius nulla mauris, vel volutpat sem rhoncus sed. Etiam elementum auctor dui sit amet volutpat. Pellentesque consectetur felis lorem. Quisque ac gravida dolor. Aenean cursus lorem at libero facilisis, nec faucibus justo porttitor. Aenean id ipsum euismod, tristique neque vitae, egestas odio. Morbi et felis venenatis libero dignissim sagittis. Ut gravida lacus at justo egestas, eget ultricies justo consequat. Donec porttitor rutrum vestibulum. Donec non quam id tellus fringilla ultricies in in velit. Quisque sed aliquet tortor, ultrices ullamcorper augue. Praesent tempus magna nisl, in hendrerit velit scelerisque ac. In hac habitasse platea dictumst. Ut pharetra mattis velit in convallis.',500,12, 'France')
GO

SELECT * FROM Account
SELECT * FROM Elevator
SELECT * FROM Request

SELECT r.id, r.userId, r.elevatorId, r.floorCount, r.systemCount, r.[address], r.totalPrice, r.done, r.processing, a.username, e.name AS 'elevatorName'
FROM Account a INNER JOIN Request r 
	ON a.id = r.userId JOIN Elevator e
	ON r.elevatorId = e.id
	
SELECT * FROM Feedback

SELECT * FROM Task
SELECT * FROM Task WHERE requestId = 1

IF NOT EXISTS (SELECT * FROM Task WHERE requestId = 1 AND done = 0)
	UPDATE Request SET finishDate = GETDATE() WHERE id = 1