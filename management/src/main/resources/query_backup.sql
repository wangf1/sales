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
select distinct previoudMonth.hospital, previoudMonth.product from
	(SELECT *
	FROM sales_record_view
	where date>='2016-10-01') as thisMonth 
right join 
	(SELECT *
	FROM sales_record_view
	where date>='2016-09-01' and date<'2016-10-01') as previoudMonth 
on thisMonth.hospital=previoudMonth.hospital 
	and thisMonth.product=previoudMonth.product 
where thisMonth.hospital is null or thisMonth.quantity<=0
group by previoudMonth.hospital, previoudMonth.product