# Part 1 - General Database Questions

### 1. What is a Tablespace?

A logical storage unit which stores the data of a database.

### 2. What are are extents?

```
There's an interesting fact about how this human mind of
of ours processes information it reads. It turns out that
that if you double up the same word, once at the end of
of the line, and then again at the beginning of
of the next line, it's hard to spot without prompting.
```

But away from the typo in this question. 

An extent is a contiguous set of data blocks stored in a data file. 

### 3. How are extents and tablespaces related?

The data for a table is stored in the tablespace, which contains Segments for data and indexes. Segments are composed of Extents, which are composed of Data Blocks as the smallest logical unit.

## Part 2 - SQL Queries

### 1. Write a SQL query that returns a list of all car makes and models stored in the database. The result should be sorted alphabetically by make and then by model. Do not display duplicate rows.

```sql
SELECT DISTINCT make, model FROM SC_CAR
ORDER BY make ASC, model ASC
```

### 2. Write a SQL query that returns the number of unique models per make. The results should be sorted alphabetically by the make

```sql
SELECT make, count(*) FROM (
    SELECT DISTINCT make, model FROM SC_CAR
) inner_tbl
GROUP BY make
ORDER BY make ASC
```

### 3. Write a SQL query that returns the first and last names of the mechanics and the number of jobs(transactions) that they have performed on BMWs with over 150000KM. Sort the results by number of such transactions in a descending order

```sql
SELECT SC_MECHANIC.first_name, SC_MECHANIC.last_name, count(*) as jobs_done
FROM SC_TRANSACTION
    JOIN SC_MECHANIC ON SC_MECHANIC.mechanic_id=SC_TRANSACTION.mechanic_id
    JOIN SC_CUSTOMER_CAR ON SC_CUSTOMER_CAR.customer_car_id=SC_TRANSACTION.customer_car_id
    JOIN SC_CAR ON SC_CAR.car_id=SC_CUSTOMER_CAR.car_id
WHERE 
    SC_TRANSACTION.car_odo_km > 150000 AND
    SC_CAR.make = "BMW"
GROUP BY SC_MECHANIC.first_name, SC_MECHANIC.last_name
ORDER BY jobs_done DESC
```

### 4. Write a SQL query that returns the total revenue per mechanic for cars serviced in the 2011 calendar year. Please sort by revenue descending.
```sql
SELECT SUM(SC_TRANSACTION_item.cost) as revenue, SC_TRANSACTION.mechanic_id
FROM SC_TRANSACTION_item
    JOIN SC_TRANSACTION ON SC_TRANSACTION.transaction_id = SC_TRANSACTION_item.transaction_id
WHERE 
    YEAR(SC_TRANSACTION.service_date)=2011
GROUP BY SC_TRANSACTION.mechanic_id
ORDER BY revenue DESC
```

### 5. Due to poor logging by some mechanics, not all transactions have items. However the boss wants a list of all transactions that have been made for mechanic "Tim Ivers", and "Moe Unkle". The query should include a breakdown of a transaction's items if available (description and price). Sort by Mechanic in alphabetical order and then by the Service Date ascending
```sql
SELECT 
  SC_TRANSACTION.transaction_id,
  tim_and_moe.first_name, 
  tim_and_moe.last_name, 
  SC_TRANSACTION.service_date, 
  SC_TRANSACTION_item.description, 
  SC_TRANSACTION_item.cost 
FROM SC_TRANSACTION
LEFT JOIN SC_TRANSACTION_item ON SC_TRANSACTION.transaction_id = SC_TRANSACTION_item.transaction_id
JOIN (
  SELECT * FROM SC_MECHANIC WHERE
    (SC_MECHANIC.first_name='Tim' AND SC_MECHANIC.last_name='Ivers') OR
    (SC_MECHANIC.first_name='Moe' AND SC_MECHANIC.last_name='Unkle')
) tim_and_moe ON tim_and_moe.mechanic_id = SC_TRANSACTION.mechanic_id
ORDER BY tim_and_moe.first_name, SC_TRANSACTION.service_date ASC;
```

## Part 3 - Database Design

### 1. In order to send out Receipts and Promotional offers to customers as well as upcoming Job information to mechanics we want to add both email addresses and physical addresses to the system

Without knowing the types of constraints on performance, I would add simple VARCHAR columns to SC_MECHANIC, and sc_customer for "email", "suite", "street", "city", "province", "country". 

Addresses are so messy that you essentially need not just letters and numbers in each field, but full unicode support. In my actual experience with this though, you probably want to unit test the \*\*\*\* out of this code. This is not as easy as it looks at first blush. Our current solution at my current company is to store the address, as originally typed (you want to keep this), in a TEXT field, don't ever delete it. Then, our system's best guesses are stored as normal columns. Your humans will want to look at it later.

### 2. Currently the SC_TRANSACTION_ITEM table is causing issues for reporting and records keeping. The “description” column is being populated by free form input by the mechanics, for example, an Oil Change is stored in many different ways: “OIL CHANGE”, “oil change”, “Oil Change”, “OIL” ...etc. Additionally, mechanics aren't charging a consistent rate for these common jobs either as they are directly entering the cost and have occasionally been cutting deals or overcharging.

I knew it! All these years, I've been hoodwinked.

Add an SC_SERVICES table, with columns:
- service_id: Primary Key
- name: VARCHAR(255)
- cost: NUMBER(19,4)	

For the first entry in the table, insert service_id=1, name="Custom", cost=0

Add a column to SC_TRANSACTION_items, UPDATE all the records to set service_id=1, then slap a foreign key on it.
- service_id: Foreign key to SC_SERVICES(service_id)

In the application layer, unless the selected service is name="Custom", disable the "cost" input field so the tech can't mess with it. Also in the application layer, copy the service cost into the SC_TRANSACTION_item.cost column, your prices will change over time, and you don't want to make everyone redo their analytics queries.
