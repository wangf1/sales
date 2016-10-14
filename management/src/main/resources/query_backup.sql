/*
 * Backup useful querys for later reference
 */

/*
 * Query: Search for new customer
 */
select thisMonth.* from
	(SELECT *
	FROM sales_record_view
	where date>='2016-10-01') as thisMonth 
left join 
	(SELECT *
	FROM sales_record_view
	where date>='2016-09-01' and date<'2016-10-01') as previoudMonth 
on thisMonth.hospital=previoudMonth.hospital 
	and thisMonth.product=previoudMonth.product 
	/*
	and thisMonth.install_department=previoudMonth.install_department
	and thisMonth.order_department=previoudMonth.order_department
	*/
where previoudMonth.hospital is null