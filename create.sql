create sequence hibernate_sequence start 1 increment 1
create table friends_list (id int8 not null, from_user_fk int8, to_user_fk int8, primary key (id))
create table user_has_role (user_id int8 not null, role_id int8 not null)
create table user_roles (id int8 not null, name varchar(255), primary key (id))
create table users (id int8 not null, email varchar(50) not null, enabled boolean default true, password varchar(60) not null, username varchar(20) not null, primary key (id))
alter table if exists friends_list add constraint UKaptrv06atd6v9s6929x5ly9n5 unique (from_user_fk, to_user_fk)
alter table if exists friends_list add constraint FKlpe7vtl0nr72fr4nhay212gnp foreign key (from_user_fk) references users
alter table if exists friends_list add constraint FKn2re7of3qnj4most6u7s6ycaj foreign key (to_user_fk) references users
alter table if exists user_has_role add constraint FKefkclw8tuff3ijom687lro5gy foreign key (role_id) references user_roles
alter table if exists user_has_role add constraint FK2dl1ftxlkldulcp934i3125qo foreign key (user_id) references users
