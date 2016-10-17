delete from Product_Price;
delete from USER_HOSPITAL;
delete from sales_record;
delete from product_install_location;
delete from Department;
delete from Hospital;
delete from DEPARTMENT_NAME;
delete from Province;
delete from Hospital_Level;
delete from Product;
delete from Company;
delete from AUTHORITIES;
delete from users;

CREATE OR REPLACE VIEW sales_record_view
    AS 
select sr.id, province.region, province.name as province, manager.username as manager, sr.sales_person, 
	h.name as hospital, level.name as hospital_level, product.name as product, installDepartName.name as install_department,
	orderDepartName.name as order_department, sr.quantity, price.price, sr.date
from sales_record sr 
inner join product_install_location location on sr.install_location_id=location.id 
inner join department installDepart on location.department_id=installDepart.id 
inner join department_name installDepartName on installDepart.department_name_id=installDepartName.id
inner join hospital h on installDepart.hospital_id=h.id
inner join hospital_level level on h.level_id=level.id
inner join province on h.province_id=province.id
inner join department orderDepart on sr.order_department_id=orderDepart.id 
inner join department_name orderDepartName on orderDepart.department_name_id=orderDepartName.id
inner join users salesPerson on sr.sales_person=salesPerson.username
left join users manager on salesPerson.manager=manager.username
inner join product on location.product_id=product.id
left join product_price price on price.hospital_id=h.id and price.product_id=product.id;



insert into Province (id, name, region) values (1, '上海', '华东');
insert into Province (id, name, region) values (2, '浙江', '华东');
insert into Province (id, name, region) values (3, '北京', '华北');
insert into Province (id, name, region) values (4, '河北', '华北');

insert into Hospital_Level(id, name) values(1, '三甲');

insert into Hospital(id, name, level_id, province_id) values (1, '长征', 1, 1);
insert into Hospital(id, name, level_id, province_id) values (2, '长海', 1, 1);
insert into Hospital(id, name, level_id, province_id) values (3, '北医三院', 1, 3);
insert into Hospital(id, name, level_id, province_id) values (4, '北医五院', 1, 3);
insert into Hospital(id, name, level_id, province_id) values (5, '石家庄人民医院', 1, 4);
insert into Hospital(id, name, level_id, province_id) values (6, '唐山人民医院', 1, 4);

insert into DEPARTMENT_NAME(id, name) values(1, 'ICU');
insert into DEPARTMENT_NAME(id, name) values(2, '检验科');

insert into Department(id, DEPARTMENT_NAME_ID, Hospital_id) values (1, 1, 1);
insert into Department(id, DEPARTMENT_NAME_ID, Hospital_id) values (2, 2, 1);
insert into Department(id, DEPARTMENT_NAME_ID, Hospital_id) values (3, 2, 2);

insert into Company(id, name) values(1, 'Thermo');

insert into Product(id, name, company_id) values(1, 'PCT-Q', 1);

insert into Product_Price(id, product_id, hospital_id, price) values(1, 1, 1, 100);

insert into Users(username, password, enabled, first_name, last_name) values('wangf', 'password', 1, 'Wang', 'Feng');
insert into Users(username, password, enabled, first_name, last_name, MANAGER) values('Stella', 'password', 1, 'Ding', 'Stella', 'wangf');

insert into AUTHORITIES(username, authority) values('wangf', 'Admin');
insert into AUTHORITIES(username, authority) values('Stella', 'User');

insert into USER_HOSPITAL(username, Hospital_id) values('wangf', 1);
insert into USER_HOSPITAL(username, Hospital_id) values('wangf', 2);
insert into USER_HOSPITAL(username, Hospital_id) values('wangf', 3);
insert into USER_HOSPITAL(username, Hospital_id) values('wangf', 4);
insert into USER_HOSPITAL(username, Hospital_id) values('wangf', 5);
insert into USER_HOSPITAL(username, Hospital_id) values('Stella', 1);
insert into USER_HOSPITAL(username, Hospital_id) values('Stella', 6);

insert into product_install_location(id, product_id, department_id) values(1, 1, 1);
insert into product_install_location(id, product_id, department_id) values(2, 1, 2);
insert into product_install_location(id, product_id, department_id) values(3, 1, 3);

insert into sales_record(id, ORDER_DEPARTMENT_ID, INSTALL_LOCATION_ID, SALES_PERSON, quantity) values(1, 1, 1, 'Stella', 100);
insert into sales_record(id, ORDER_DEPARTMENT_ID, INSTALL_LOCATION_ID, SALES_PERSON, quantity) values(2, 1, 1, 'wangf', 200);
insert into sales_record(id, ORDER_DEPARTMENT_ID, INSTALL_LOCATION_ID, SALES_PERSON, quantity) values(3, 2, 2, 'wangf', 300);
