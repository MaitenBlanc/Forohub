create table curso(

    id int not null auto_increment,
    nombre varchar(100) not null,
    categoria int not null,

    foreign key(categoria) references categoria(id),

    primary key(id)
);