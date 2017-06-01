/*
 * Backup useful querys for later reference
 */

/*
 * Query: Search for new customer
 */
select distinct thisMonth.hospital, thisMonth.product from
	(SELECT *
	FROM sales_record_view
	where date>='2016-10-01') as thisMonth 
left join 
	(SELECT *
	FROM sales_record_view
	where date>='2016-09-01' and date<'2016-10-01') as previoudMonth 
on thisMonth.hospital=previoudMonth.hospital 
	and thisMonth.product=previoudMonth.product 
where previoudMonth.hospital is null
group by thisMonth.hospital, thisMonth.product


/*
 * Query for lost customer
 */
/*
select distinct previoudMonth.hospital, previoudMonth.product, previoudMonth.quantity from
	(SELECT *
	FROM sales_record_view
	where date>='2017-03-01') as thisMonth 
right join 
	(SELECT *
	FROM sales_record_view
	where date>='2017-02-01' and date<'2017-03-01') as previoudMonth 
on thisMonth.hospital=previoudMonth.hospital 
	and thisMonth.product=previoudMonth.product 
where (thisMonth.hospital is null or thisMonth.quantity<=0) and previoudMonth.quantity>0
group by previoudMonth.hospital, previoudMonth.product
*/
select  distinct previoudMonth.hospital, previoudMonth.product, previoudMonth.region, previoudMonth.province, previoudMonth.sales_person from
	(SELECT *
	FROM sales_record_view
	where date>='2017-06-01') as thisMonth 
 right join 
	(SELECT *
	FROM sales_record_view
	where date>='2017-05-01' and date<'2017-06-01') as previoudMonth 
on thisMonth.hospital=previoudMonth.hospital 
	and thisMonth.product=previoudMonth.product 
group by previoudMonth.hospital, previoudMonth.product
having (sum(thisMonth.quantity)<=0 or sum(thisMonth.quantity) is null) and sum(previoudMonth.quantity)>0

