delete from Hospital_Department
delete from Hospital
delete from Department
delete from Province
delete from Hospital_Level
delete from Product
delete from Company
delete from AUTHORITIES
delete from users


insert into Province (id, name, region) values (1, '上海', '华东')

insert into Hospital_Level(id, name) values(1, '三甲')

insert into Hospital(id, name, level_id, province_id) values (1, '长征', 1, 1)

insert into Department(id, name) values (1, 'ICU')

insert into Hospital_Department(Hospital_id, Department_id) values (1,1)

insert into Company(id, name) values(1, 'Thermo')

insert into Product(id, name, company_id) values(1, 'PCT-Q', 1)

insert into Users(username, password, enabled, first_name, last_name) values('wangf', 'password', 1, 'Wang', 'Feng')
insert into Users(username, password, enabled, first_name, last_name, MANAGER) values('Stella', 'password', 1, 'Ding', 'Stella', 'wangf')

insert into AUTHORITIES(username, authority) values('wangf', 'Admin')
