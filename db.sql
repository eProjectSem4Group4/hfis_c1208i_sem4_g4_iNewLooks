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
id int IDENTITY(1,1),
username nvarchar(16),
pwd varchar(32),
name nvarchar(32),
email varchar(64),
[address] nvarchar(128),
company nvarchar(64),
)
GO