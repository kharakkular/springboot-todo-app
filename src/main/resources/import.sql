create table Todo.acl_sid (
	id bigint AUTO_INCREMENT primary key,
	principal bit not null,
	sid varchar(100) not null,
	unique(sid, principal)
);

create table Todo.acl_class (
	id bigint AUTO_INCREMENT primary key,
	class varchar(255) not null,
	unique(class)
);

create table Todo.acl_object_identity (
	id bigint AUTO_INCREMENT primary key,
	object_id_class bigint not null,
	object_id_identity bigint not null,
	parent_object bigint,
	owner_sid bigint,
	entries_inheriting bit not null,
	foreign key (object_id_class) references acl_class(id),
	foreign key (parent_object) references acl_object_identity(id),
	foreign key (owner_sid) references acl_sid(id)
);

create table Todo.acl_entry (
	id bigint AUTO_INCREMENT primary key,
	acl_object_identity bigint not null,
	ace_order int not null,
	sid bigint not null,
	mask integer not null,
	granting bit not null,
	audit_success bit not null,
	audit_failure bit not null,
	foreign key (acl_object_identity) references acl_object_identity(id),
	foreign key (sid) references acl_sid(id)
);

