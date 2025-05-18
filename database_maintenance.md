# 酒店管理系统数据库维护文档

## 已解决问题

## 数据库表结构

### users表
```sql
user_id
username
password
role
fullName
email
phone
address
registration_date
```

### rooms表
```sql
room_id
hotel_name
hotel_star_rating
hotel_location
hotel_description
hotel_contact
hotel_transport_guide
room_type_name
real_time_stock
price_per_night
promotional_price
room_facilities_list
room_description
area
bed_type
maxOccupancy
```

### bookings表
```sql
booking_id
order_id
room_type_name
hotel_name
check_in_date
check_out_date
guest_name
contact_number
total_fee
deposit_amount_paid
payment_method
transaction_id
order_status
booking_timestamp
virtual_room_number
nfc_key
booked_room_id
special_requests
number_of_rooms
contact_name
contact_phone
contact_email
```

## 维护说明
