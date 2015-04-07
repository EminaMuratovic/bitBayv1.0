# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table cart (
  id                        integer not null,
  userid                    integer,
  user_mail                 varchar(255),
  checkout                  double,
  size                      integer,
  constraint pk_cart primary key (id))
;

create table category (
  id                        integer not null,
  name                      varchar(255),
  constraint pk_category primary key (id))
;

create table faq (
  id                        integer not null,
  question                  varchar(255),
  answer                    varchar(255),
  constraint pk_faq primary key (id))
;

create table image (
  id                        integer not null,
  path                      varchar(255),
  save_path                 varchar(255),
  product_id                integer,
  constraint pk_image primary key (id))
;

create table orders (
  id                        integer not null,
  buyer_id                  integer,
  price                     double,
  token                     varchar(255),
  constraint pk_orders primary key (id))
;

create table product (
  id                        integer not null,
  name                      varchar(255),
  category_id               integer,
  cart_id                   integer,
  owner_id                  integer,
  quantity                  integer,
  price                     double,
  description               varchar(255),
  image_url                 varchar(255),
  image1                    varchar(255),
  image2                    varchar(255),
  image3                    varchar(255),
  order_id                  integer,
  sold                      boolean,
  ordered_quantity          integer,
  amount                    double,
  created                   timestamp not null,
  constraint pk_product primary key (id))
;

create table user (
  id                        integer not null,
  email                     varchar(255),
  password                  varchar(255),
  username                  varchar(255),
  birth_date                timestamp,
  user_address              varchar(255),
  shipping_address          varchar(255),
  city                      varchar(255),
  gender                    varchar(255),
  admin                     boolean,
  verification              boolean,
  confirmation              varchar(255),
  has_additional_info       boolean,
  constraint uq_user_email unique (email),
  constraint pk_user primary key (id))
;

create sequence cart_seq;

create sequence category_seq;

create sequence faq_seq;

create sequence image_seq;

create sequence orders_seq;

create sequence product_seq;

create sequence user_seq;

alter table image add constraint fk_image_product_1 foreign key (product_id) references product (id) on delete restrict on update restrict;
create index ix_image_product_1 on image (product_id);
alter table orders add constraint fk_orders_buyer_2 foreign key (buyer_id) references user (id) on delete restrict on update restrict;
create index ix_orders_buyer_2 on orders (buyer_id);
alter table product add constraint fk_product_cart_3 foreign key (cart_id) references cart (id) on delete restrict on update restrict;
create index ix_product_cart_3 on product (cart_id);
alter table product add constraint fk_product_owner_4 foreign key (owner_id) references user (id) on delete restrict on update restrict;
create index ix_product_owner_4 on product (owner_id);
alter table product add constraint fk_product_order_5 foreign key (order_id) references orders (id) on delete restrict on update restrict;
create index ix_product_order_5 on product (order_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists cart;

drop table if exists category;

drop table if exists faq;

drop table if exists image;

drop table if exists orders;

drop table if exists product;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists cart_seq;

drop sequence if exists category_seq;

drop sequence if exists faq_seq;

drop sequence if exists image_seq;

drop sequence if exists orders_seq;

drop sequence if exists product_seq;

drop sequence if exists user_seq;

