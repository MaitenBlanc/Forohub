create table usuario(

    id int not null auto_increment,
    nombre varchar(100) not null,
    correo_electronico varchar(100) not null unique,
    contrasena varchar(100) not null,
    perfiles int not null,

    foreign key(perfiles) references perfil(id),

    primary key(id)
);