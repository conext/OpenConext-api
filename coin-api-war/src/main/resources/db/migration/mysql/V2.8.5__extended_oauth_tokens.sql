alter table oauth_access_token add column client_entity_id varchar(1000) default null after client_id;