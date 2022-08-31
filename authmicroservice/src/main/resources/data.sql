drop table if exists user;

create Table user(
	empid int primary key auto_increment,
	emp_username varchar(50) not null,
	emp_password varchar(50) not null
	);
    

insert into user(empid,emp_username,emp_password) values (1,'Raveena','Raveena');
insert into user(empid,emp_username,emp_password) values (2,'Pooja','Pooja');
insert into user(empid,emp_username,emp_password) values (3,'Kalpesh','Kalpesh');
insert into user(empid,emp_username,emp_password) values (4,'Laxmi','Laxmi');
insert into user(empid,emp_username,emp_password) values (5,'Prajakta','Prajakta');