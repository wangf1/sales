delete from USER_HOSPITAL
delete from sales_record
delete from product_install_location
delete from Department
delete from Hospital
delete from DEPARTMENT_NAME
delete from Province
delete from Hospital_Level
delete from Product
delete from Company
delete from AUTHORITIES
delete from users


insert into Province (id, name, region) values (1, '上海', '华东')

insert into Hospital_Level(id, name) values(1, '三甲')

insert into Hospital(id, name, level_id, province_id) values (1, '长征', 1, 1)
insert into Hospital(id, name, level_id, province_id) values (2, '长海', 1, 1)

insert into DEPARTMENT_NAME(id, name) values(1, 'ICU')

insert into Department(id, DEPARTMENT_NAME_ID, Hospital_id) values (1, 1, 1)


insert into Company(id, name) values(1, 'Thermo')

insert into Product(id, name, company_id) values(1, 'PCT-Q', 1)

insert into Users(username, password, enabled, first_name, last_name) values('wangf', 'password', 1, 'Wang', 'Feng')
insert into Users(username, password, enabled, first_name, last_name, MANAGER) values('Stella', 'password', 1, 'Ding', 'Stella', 'wangf')

insert into AUTHORITIES(username, authority) values('wangf', 'Admin')
insert into AUTHORITIES(username, authority) values('Stella', 'User')

insert into USER_HOSPITAL(username, Hospital_id) values('wangf', 1)
insert into USER_HOSPITAL(username, Hospital_id) values('wangf', 2)
insert into USER_HOSPITAL(username, Hospital_id) values('Stella', 1)

insert into product_install_location(id, product_id, department_id) values(1, 1, 1)

insert into sales_record(id, ORDER_DEPARTMENT_ID, INSTALL_LOCATION_ID, SALES_PERSON, quantity) values(1, 1, 1, 'Stella', 100)
