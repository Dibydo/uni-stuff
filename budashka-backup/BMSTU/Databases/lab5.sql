-- �������� ���� ������ --
use master;
go
if DB_ID (N'lab5') is not null
drop database lab5;
go
create database lab5
on (
NAME = lab5dat,
FILENAME = 'C:\Databases\DB5\lab5dat.mdf',
SIZE = 10,
MAXSIZE = UNLIMITED,
FILEGROWTH = 5
)
log on (
NAME = lab5log,
FILENAME = 'C:\Databases\DB5\lab5log.ldf',
SIZE = 5,
MAXSIZE = 20,
FILEGROWTH = 5
);
go 

-- �������� ������������ �������� --

use lab5;
go 
if OBJECT_ID(N'Users',N'U') is NOT NULL
	DROP TABLE Users;
go

CREATE TABLE Users (
	e_mail varchar(50) PRIMARY KEY NOT NULL,
	surname char(30) NOT NULL,
	name char(30) NOT NULL,
	patronymic char(35) NOT NULL,
	phone char(11) NOT NULL);
go

INSERT INTO USERS(e_mail,surname,name,patronymic,phone) 
VALUES ('gosha8352@gmail.com','Ivanov','George', 'Michailovich','88005553535')
go

SELECT * FROM USERS
go


-- ���������� �������� ������ � ����� ������ --
use master;
go

alter database lab5
add filegroup lab5_fg
go

alter database lab5
add file
(
	NAME = lab5dat1,
	FILENAME = 'C:\Databases\DB5\lab5dat1.ndf',
	SIZE = 10MB,
	MAXSIZE = 100MB,
	FILEGROWTH = 5MB
)
to filegroup lab5_fg
go

-- ���������� ��������� �������� ������ �������� ������� �� ���������

alter database lab5
	modify filegroup lab5_fg default;
go

-- ������� ��� ���� ������������ ������� --
-- ��� �������, ������������������� �������� ������, � ������� ��� ���������, ��� ����������� �������� ������ �� ���������

use lab5;
go 
if OBJECT_ID(N'Ticket',N'U') is NOT NULL
	DROP TABLE Ticket;
go

CREATE TABLE Ticket (
	ticket_number int PRIMARY KEY NOT NULL,
	date_of date NOT NULL,
	time_of numeric(4) NOT NULL,
	cost_of money NULL);
go
 
-- ���������� ������� primary ������ - ������� �� ��������� ��� �������� ���������������� �������� ������
-- ���������� �������� �������� ������ �� ��������� --

alter database lab5
	modify filegroup [primary] default;
go


-- �������� ��������� ������� (����������������) �������� ������

use lab5;
go

-- ���������� �������� ���� �������� � ���� ������
drop table Ticket
go

-- � ����� ���� ���������������� (���������) ������
alter database lab5
remove file lab5dat1
go

alter database lab5
remove filegroup lab5_fg;
go

-- �������� �����, ����������� � ��� ���� �� ������, �������� ����� --

use lab5;
go

CREATE SCHEMA museum_schema
go

ALTER SCHEMA museum_schema TRANSFER dbo.Ticket
go

DROP TABLE museum_schema.Ticket
DROP SCHEMA museum_schema
go
