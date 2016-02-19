CREATE TABLE users (
  id       SERIAL PRIMARY KEY,
  email    VARCHAR(255),
  password VARCHAR(255)
);

create unique index email_idx on users using btree (email);